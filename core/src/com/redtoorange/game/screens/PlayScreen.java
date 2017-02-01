package com.redtoorange.game.screens;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.redtoorange.game.ContactManager;
import com.redtoorange.game.Core;
import com.redtoorange.game.Global;
import com.redtoorange.game.PerformanceCounter;
import com.redtoorange.game.components.rendering.SpriteComponent;
import com.redtoorange.game.engine.Engine;
import com.redtoorange.game.entities.GameMap;
import com.redtoorange.game.entities.characters.Player;
import com.redtoorange.game.entities.characters.enemies.Enemy;
import com.redtoorange.game.entities.powerups.Ammo;
import com.redtoorange.game.entities.powerups.Health;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.ui.GunUI;

/**
 * PlayScreen.java - Primary playing screen that the user will interact with.
 *
 * @author - Andrew M.
 * @version - 13/Jan/2017
 */
public class PlayScreen extends ScreenAdapter {
	private static final int ENEMY_COUNT = 100;
	private static final int HEALTH_COUNT = 25;
	private static final int AMMO_COUNT = 25;

	boolean fading = true;
	private Core core;
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private GameMap gameMap;
	private Player player;
	private Box2DDebugRenderer debugRenderer;
	private ContactManager contactManager;
	private float cameraSmoothing = 0.1f;
	private Engine engine;
	private PhysicsSystem physicsSystem;
	private GunUI gunui;

	private ConeLight flashLight;
	private PointLight playerLight;
	private PointLight houseLight;

	private PerformanceCounter updateCounter = new PerformanceCounter( "Update:" );
	private Color minColor = new Color( 1, .5f, .5f, .25f );
	private Color maxColor = new Color( 1, .5f, .5f, .75f );
	private PerformanceCounter drawCounter = new PerformanceCounter( "Draw:" );
	boolean running = true;

	public PlayScreen( Core core ) {
		this.core = core;
	}

	@Override
	public void show( ) {
		init( );
	}

	private void init( ) {
		VisUI.load( );

		Gdx.input.setCursorCatched( true );
		Gdx.input.setCursorPosition( Global.WINDOW_WIDTH / 2, Global.WINDOW_HEIGHT / 2 );

		debugRenderer = new Box2DDebugRenderer( );
		contactManager = new ContactManager( );
		engine = new Engine( );

		gunui = new GunUI( );
		physicsSystem = new PhysicsSystem( );
		physicsSystem.getWorld( ).setContactListener( contactManager );

		camera = new OrthographicCamera( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT );
		viewport = new ExtendViewport( Global.VIRTUAL_WIDTH, Global.VIRTUAL_HEIGHT, camera );
		batch = new SpriteBatch( );

		gameMap = new GameMap( "tilemaps/test_map.tmx", null, batch, camera, 1 / 16f );

		Vector2 playerSpawn = new Vector2( );
		gameMap.playerSpawns.first( ).getCenter( playerSpawn );

		player = new Player( engine, camera, this, physicsSystem, playerSpawn );
		System.out.println( playerSpawn );

		initWalls( );


		for ( int i = 0; i < ENEMY_COUNT; i++ ) {
			engine.addEntity( new Enemy( physicsSystem, engine, new Vector2( MathUtils.random( 1, 99 ), MathUtils.random( 1, 99 ) ), player ) );
		}

		for ( int i = 0; i < AMMO_COUNT; i++ ) {
			engine.addEntity( new Ammo( new Vector2( MathUtils.random( 1, 99 ), MathUtils.random( 1, 99 ) ), engine, physicsSystem ) );
		}

		for ( int i = 0; i < HEALTH_COUNT; i++ ) {
			engine.addEntity( new Health( new Vector2( MathUtils.random( 1, 99 ), MathUtils.random( 1, 99 ) ), engine, physicsSystem ) );
		}


		engine.addEntity( player );

		playerLight = new PointLight( physicsSystem.getRayHandler( ), 10, new Color( 1, 1, 1, .75f ), 1f, playerSpawn.x, playerSpawn.y );
		houseLight = new PointLight( physicsSystem.getRayHandler( ), 100, new Color( 1, .5f, .5f, .75f ), 10f, 21, 3.5f );
		//houseLight.setSoft( false );

		flashLight = new ConeLight( physicsSystem.getRayHandler( ), 100, new Color( 1, 1, 1, .75f ), 10f, playerSpawn.x, playerSpawn.y, 0, 30f );

		Filter f = new Filter( );
		f.categoryBits = Global.LIGHT;
		f.maskBits = Global.ENEMY | Global.WALL;
		Light.setGlobalContactFilter( f );
	}

	private void initWalls( ) {
		for ( Rectangle r : gameMap.walls ) {
			Filter w = new Filter( );
			w.groupIndex = Global.WALL;
			w.maskBits = Global.ENEMY | Global.PLAYER | Global.LIGHT | Global.BULLET_LIVE;
			Body b = Box2DFactory.createStaticBody( physicsSystem, r );
			b.getFixtureList( ).first( ).setFilterData( w );
			b.setUserData( r );
		}
	}

	/**
	 * @param deltaTime
	 */
	public void update( float deltaTime ) {
		if ( !running )
			return;

		updateCounter.start( );
		physicsSystem.update( deltaTime );

		engine.update( deltaTime );
		gameMap.update( deltaTime );
		gunui.update( deltaTime );

		updateCameraPosition( );

//		Color c = houseLight.getColor( );
//		if ( fading ) {
//			c.lerp( minColor, deltaTime * ( MathUtils.random( 2, 7 ) ) );
//			if ( c.a <= 0.3f )
//				fading = false;
//		} else {
//			c.lerp( maxColor, deltaTime * ( MathUtils.random( 2, 7 ) ) );
//			if ( c.a >= .7f )
//				fading = true;
//		}
//		houseLight.setColor( c );
		//System.out.println( updateCounter );


	}

	/**
	 *
	 */
	public void draw( ) {
		if ( !running )
			return;

		drawCounter.start( );

		camera.update( );

		gameMap.draw( batch );

		batch.setProjectionMatrix( camera.combined );
		batch.begin( );
		engine.render( batch );
		batch.end( );

		renderLighting( );

		batch.begin( );
		engine.postLighting( batch );
		batch.end( );

		gunui.draw( batch );
		//System.out.println( drawCounter );

		if ( Global.DEBUG )
			debugRenderer.render( physicsSystem.getWorld( ), camera.combined );

		//System.out.println( drawCounter );
	}

	private void renderLighting( ) {
		if ( !running )
			return;

		if ( player != null && flashLight != null && playerLight != null ) {
			Vector2 flashlightPoisition = ( ( SpriteComponent ) player.getComponent( SpriteComponent.class ) ).getCenter( );
			flashlightPoisition.add( new Vector2( -0.2f, 0.3f ).rotate( player.getRotation( ) ) );

			flashLight.setDirection( player.getRotation( ) );
			flashLight.setPosition( flashlightPoisition );
			playerLight.setPosition( player.getTransform( ).getPosition( ) );

			physicsSystem.render( camera );
		}
	}

	private void updateCameraPosition( ) {
		if ( player != null )
			camera.position.lerp( player.getPosition3D( ), cameraSmoothing );
	}

	@Override
	public void resize( int width, int height ) {
		viewport.update( width, height );
		gunui.resize( width, height );
		camera.update( );
	}

	@Override
	public void dispose( ) {
		if ( engine != null ) {
			engine.dispose( );
			engine = null;
		}

		if ( physicsSystem != null ) {
			flashLight = null;
			houseLight = null;
			playerLight = null;
			physicsSystem.dispose( );
			physicsSystem = null;
		}

		if ( gameMap != null ) {
			gameMap.dispose( );
			gameMap = null;
		}

		if ( batch != null ) {
			batch.dispose( );
			batch = null;
		}

		if ( Global.DEBUG )
			System.out.println( "PlayScreen disposed" );
	}

	public void setPlayer( Player player ) {
		this.player = player;
		if ( player == null ) {
			running = false;
			core.setPlaying( false );
		}
	}

	public GunUI getGunUI( ) {
		return gunui;
	}
}