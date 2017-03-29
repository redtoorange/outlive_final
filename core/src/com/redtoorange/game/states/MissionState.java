package com.redtoorange.game.states;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.gameobject.GameMap;
import com.redtoorange.game.gameobject.SceneRoot;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.gameobject.characters.enemies.Enemy;
import com.redtoorange.game.gameobject.powerups.Ammo;
import com.redtoorange.game.gameobject.powerups.Health;
import com.redtoorange.game.shaders.SHADER;
import com.redtoorange.game.shaders.ShaderLoader;
import com.redtoorange.game.systems.LightingSystem;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.ui.missionui.GunUI;

import java.util.Arrays;

/**
 * MissionState.java - Primary playing screen that the user will interact with.
 *
 * @author - Andrew M.
 * @version - 13/Jan/2017
 */
public class MissionState extends ScreenAdapter {
	private static final int ENEMY_COUNT = 100;
	private static final int HEALTH_COUNT = 25;
	private static final int AMMO_COUNT = 25;

	boolean fading = true;
	boolean running = true;
	private Core core;
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private GameMap gameMap;
	private Player player;

	private Box2DDebugRenderer debugRenderer;
	private ContactManager contactManager;
	private float cameraSmoothing = 0.1f;

	private PhysicsSystem physicsSystem;
	private LightingSystem lightingSystem;

	private GunUI gunui;
	private ConeLight flashLight;
	private PointLight playerLight;
	private PointLight houseLight;
	private PerformanceCounter updateCounter = new PerformanceCounter( "Update:" );
	private Color minColor = new Color( 1, .5f, .5f, .25f );
	private Color maxColor = new Color( 1, .5f, .5f, .75f );
	private PerformanceCounter drawCounter = new PerformanceCounter( "Draw:" );

	private SceneRoot sceneRoot = new SceneRoot( );

	public MissionState(Core core ) {
		this.core = core;
	}

	@Override
	public void show() {
		init( );
	}

	private void init() {
		VisUI.load( );

		Gdx.input.setCursorCatched( true );
		Gdx.input.setCursorPosition( Global.WINDOW_WIDTH / 2, Global.WINDOW_HEIGHT / 2 );
		Gdx.input.setInputProcessor( new mouseProcessor( ) );

		debugRenderer = new Box2DDebugRenderer( );
		contactManager = new ContactManager( );

		gunui = new GunUI( );
		physicsSystem = new PhysicsSystem( );
		physicsSystem.getWorld( ).setContactListener( contactManager );
		lightingSystem = new LightingSystem( physicsSystem.getWorld() );

		camera = new OrthographicCamera( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT );
		viewport = new ExtendViewport( Global.VIRTUAL_WIDTH, Global.VIRTUAL_HEIGHT, camera );
		batch = new SpriteBatch( );

		gameMap = new GameMap( sceneRoot, "tilemaps/test_map.tmx", batch, camera, 1 / 16f );

		Vector2 playerSpawn = new Vector2( );
		gameMap.playerSpawns.first( ).getCenter( playerSpawn );

		player = new Player( sceneRoot, camera, this, physicsSystem, playerSpawn );
		System.out.println( playerSpawn );

		initWalls( );


		for ( int i = 0; i < ENEMY_COUNT; i++ ) {
			sceneRoot.addChild( new Enemy( sceneRoot, physicsSystem, new Vector2( MathUtils.random( 1, 99 ), MathUtils.random( 1, 99 ) ), player ) );
		}

		for ( int i = 0; i < AMMO_COUNT; i++ ) {
			sceneRoot.addChild( new Ammo( sceneRoot, new Vector2( MathUtils.random( 1, 99 ), MathUtils.random( 1, 99 ) ), physicsSystem ) );
		}

		for ( int i = 0; i < HEALTH_COUNT; i++ ) {
			sceneRoot.addChild( new Health( sceneRoot, new Vector2( MathUtils.random( 1, 99 ), MathUtils.random( 1, 99 ) ), physicsSystem ) );
		}

		sceneRoot.addChild( player );

		playerLight = new PointLight( lightingSystem.getRayHandler( ), 10, new Color( 1, 1, 1, .75f ), 1f, playerSpawn.x, playerSpawn.y );
		houseLight = new PointLight( lightingSystem.getRayHandler( ), 100, new Color( 1, .5f, .5f, .75f ), 10f, 21, 3.5f );
		//houseLight.setSoft( false );

		flashLight = new ConeLight( lightingSystem.getRayHandler( ), 100, new Color( 1, 1, 1, .75f ), 10f, playerSpawn.x, playerSpawn.y, 0, 30f );

		Filter f = new Filter( );
		f.categoryBits = Global.LIGHT;
		f.maskBits = Global.ENEMY | Global.WALL;
		Light.setGlobalContactFilter( f );

		sceneRoot.start( null );

		System.out.println( Arrays.toString( ShaderLoader.S.getShader( SHADER.TEST_SHADER ).getAttributes() ) );
	}

	private void initWalls() {
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

		sceneRoot.update( deltaTime );
		gameMap.update( deltaTime );
		gunui.update( deltaTime );

		updateCameraPosition( );
	}


	public void draw() {
		if ( !running )
			return;

		drawCounter.start( );

		camera.update( );

		gameMap.preLighting( batch );

		batch.setProjectionMatrix( camera.combined );
		batch.begin( );
		sceneRoot.preLighting( batch );
		batch.end( );

		renderLighting( );

		batch.begin( );
		sceneRoot.postLighting( batch );
		batch.end( );

		gunui.draw( batch );
		//System.out.println( drawCounter );

		if ( Global.DEBUG )
			debugRenderer.render( physicsSystem.getWorld( ), camera.combined );

		//System.out.println( drawCounter );
	}

	private void renderLighting() {
		if ( !running )
			return;

		if ( player != null && flashLight != null && playerLight != null ) {
			Vector2 flashlightPoisition = ( ( SpriteComponent ) player.getComponent( SpriteComponent.class ) ).getCenter( );
			flashlightPoisition.add( new Vector2( -0.2f, 0.3f ).rotate( player.getRotation( ) ) );

			flashLight.setDirection( player.getRotation( ) );
			flashLight.setPosition( flashlightPoisition );
			playerLight.setPosition( player.getTransform( ).getPosition( ) );

			lightingSystem.draw( camera );
		}
	}

	private void updateCameraPosition() {
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
	public void dispose() {
		sceneRoot.dispose( );

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
			System.out.println( "MissionState disposed" );
	}

	public void setPlayer( Player player ) {
		this.player = player;
		if ( player == null ) {
			running = false;
			core.setPlaying( false );
		}
	}

	public GunUI getGunUI() {
		return gunui;
	}

	private void mouseWheelMovement( int amount ) {

		if ( amount != 0 )
			changeZoom( amount );
	}

	private void changeZoom( float amount ) {
		float w = viewport.getWorldWidth( );
		float h = viewport.getWorldHeight( );

		w -= amount;
		h -= amount;

		viewport.setWorldWidth( w );
		viewport.setWorldHeight( h );
	}

	private class mouseProcessor implements InputProcessor {
		@Override
		public boolean keyDown( int keycode ) {
			return false;
		}

		@Override
		public boolean keyUp( int keycode ) {
			return false;
		}

		@Override
		public boolean keyTyped( char character ) {
			return false;
		}

		@Override
		public boolean touchDown( int screenX, int screenY, int pointer, int button ) {
			return false;
		}

		@Override
		public boolean touchUp( int screenX, int screenY, int pointer, int button ) {
			return false;
		}

		@Override
		public boolean touchDragged( int screenX, int screenY, int pointer ) {
			return false;
		}

		@Override
		public boolean mouseMoved( int screenX, int screenY ) {
			return false;
		}

		@Override
		public boolean scrolled( int amount ) {
			mouseWheelMovement( amount );
			return true;
		}
	}

	public LightingSystem getLightingSystem(){
		return lightingSystem;
	}
}