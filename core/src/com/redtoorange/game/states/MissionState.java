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
import com.redtoorange.game.Core;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.gameobject.GameMap;
import com.redtoorange.game.gameobject.SceneRoot;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.gameobject.characters.enemies.Enemy;
import com.redtoorange.game.gameobject.powerups.Ammo;
import com.redtoorange.game.gameobject.powerups.Health;
import com.redtoorange.game.systems.*;
import com.redtoorange.game.ui.missionui.GunUI;
import com.redtoorange.game.ui.missionui.HealthUI;

import java.lang.System;

/**
 * MissionState.java - Primary playing screen that the user will interact with.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class MissionState extends ScreenAdapter {
    //    boolean fading = true;
    boolean running = true;
    private int currentLevel = 0;
    private String[] levelNames = { "level_1.tmx", "level_2.tmx", "level_3.tmx" };
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
    private HealthUI healthui;

    private ConeLight flashLight;
    private PointLight playerLight;

    private SceneRoot sceneRoot;
    private int remainingEnemies = 0;

    public MissionState( Core core ) {
        this.core = core;
    }

    @Override
    public void show() {
        init();
    }

    private void init() {
        VisUI.load();

        Gdx.input.setCursorCatched( true );
        Gdx.input.setCursorPosition( Global.WINDOW_WIDTH / 2, Global.WINDOW_HEIGHT / 2 );
        Gdx.input.setInputProcessor( new mouseProcessor() );

        gunui = new GunUI();
        healthui = new HealthUI();

        camera = new OrthographicCamera( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT );
        viewport = new ExtendViewport( Global.VIRTUAL_WIDTH, Global.VIRTUAL_HEIGHT, camera );
        batch = new SpriteBatch();

        loadLevel( currentLevel );
    }

    private void loadObjects( Rectangle exclusionZone ) {
        remainingEnemies = gameMap.getEnemyCount();

        for ( int i = 0; i < gameMap.getEnemyCount(); i++ ) {
            Vector2 point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            while ( exclusionZone.contains( point ) )
                point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            sceneRoot.addChild( new Enemy( sceneRoot, physicsSystem, point, player ) );
        }

        for ( int i = 0; i < gameMap.getAmmoCount(); i++ ) {
            Vector2 point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            while ( exclusionZone.contains( point ) )
                point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            sceneRoot.addChild( new Ammo( sceneRoot, point, physicsSystem ) );
        }

        for ( int i = 0; i < gameMap.getHealthCount(); i++ ) {
            Vector2 point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            while ( exclusionZone.contains( point ) )
                point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            sceneRoot.addChild( new Health( sceneRoot, point, physicsSystem ) );
        }
    }

    private void loadLevel( int levelNumber ) {
//        if( physicsSystem != null )
//            physicsSystem.dispose();

        debugRenderer = new Box2DDebugRenderer();
        contactManager = new ContactManager();
        physicsSystem = new PhysicsSystem();
        physicsSystem.getWorld().setContactListener( contactManager );
        lightingSystem = new LightingSystem( physicsSystem.getWorld() );

        if ( sceneRoot != null )
            sceneRoot.dispose();

        sceneRoot = new SceneRoot();

        if ( gameMap != null )
            gameMap.dispose();

        gameMap = new GameMap( sceneRoot, "tilemaps/" + levelNames[levelNumber], batch, camera, 1 / 16f );

        Vector2 playerSpawn = new Vector2();
        Rectangle exclusionZone = new Rectangle( playerSpawn.x, playerSpawn.y, 2, 2 );
        gameMap.playerSpawns.first().getCenter( playerSpawn );

        initPlayer( playerSpawn );
        initWalls();
        loadObjects( exclusionZone );

        sceneRoot.addChild( player );
        playerLight = new PointLight( lightingSystem.getRayHandler(), 10, new Color( 1, 1, 1, .75f ), 1f, player.getTransform().getPosition().x, player.getTransform().getPosition().y );
        flashLight = new ConeLight( lightingSystem.getRayHandler(), 100, new Color( 1, 1, 1, .75f ), 10f, player.getTransform().getPosition().x, player.getTransform().getPosition().y, 0, 30f );


        Filter f = new Filter();
        f.categoryBits = Global.LIGHT;
        f.maskBits = Global.ENEMY | Global.WALL;
        Light.setGlobalContactFilter( f );

        sceneRoot.start( null );
    }

    private void initPlayer( Vector2 playerSpawn ) {
        int currentHealth = 10;
        int maxHealth = 10;
        int rounds = 6;

        if(player != null){
            currentHealth = player.getHealth();
            maxHealth = player.getMaxHealth();
            rounds = player.getInventoy().remaining( GunType.REVOLVER );
        }

        player = new Player( sceneRoot, camera, this, physicsSystem, playerSpawn );
        player.setHealth( currentHealth, maxHealth );
        player.setAmmo( rounds );
    }

    private void initWalls() {
        for ( Rectangle r : gameMap.walls ) {
            Filter w = new Filter();
            w.groupIndex = Global.WALL;
            w.maskBits = Global.ENEMY | Global.PLAYER | Global.LIGHT | Global.BULLET_LIVE;

            Body b = Box2DFactory.createStaticBody( physicsSystem, r );
            b.getFixtureList().first().setFilterData( w );
            b.setUserData( r );
        }
    }

    /**
     * @param deltaTime
     */
    public void update( float deltaTime ) {
        if ( !running )
            return;

        physicsSystem.update( deltaTime );

        sceneRoot.update( deltaTime );
        gameMap.update( deltaTime );

        healthui.update( deltaTime );
        gunui.update( deltaTime );

        updateCameraPosition();
    }


    public void draw() {
        if ( !running )
            return;

        camera.update();

        gameMap.preLighting( batch );

        batch.setProjectionMatrix( camera.combined );
        batch.begin();
        sceneRoot.preLighting( batch );
        batch.end();

        renderLighting();

        batch.begin();
        sceneRoot.postLighting( batch );
        batch.end();

        gunui.draw( batch );
        healthui.draw( batch );

        if ( Global.DEBUG )
            debugRenderer.render( physicsSystem.getWorld(), camera.combined );
    }

    private void renderLighting() {
        if ( !running )
            return;

        if ( player != null && flashLight != null && playerLight != null ) {
            Vector2 flashlightPoisition = ( ( SpriteComponent ) player.getComponent( SpriteComponent.class ) ).getCenter();
            flashlightPoisition.add( new Vector2( -0.2f, 0.3f ).rotate( player.getRotation() ) );

            flashLight.setDirection( player.getRotation() );
            flashLight.setPosition( flashlightPoisition );
            playerLight.setPosition( player.getTransform().getPosition() );

            lightingSystem.draw( camera );
        }
    }

    private void updateCameraPosition() {
        if ( player != null )
            camera.position.lerp( player.getPosition3D(), cameraSmoothing );
    }

    @Override
    public void resize( int width, int height ) {
        viewport.update( width, height );
        gunui.resize( width, height );
        healthui.resize( width, height );
        camera.update();
    }

    @Override
    public void dispose() {
        sceneRoot.dispose();

        if ( physicsSystem != null ) {
            flashLight = null;
            playerLight = null;
            physicsSystem.dispose();
            physicsSystem = null;
        }

        if ( gameMap != null ) {
            gameMap.dispose();
            gameMap = null;
        }

        if ( batch != null ) {
            batch.dispose();
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

    public HealthUI getHealthUI() {
        return healthui;
    }

    private void mouseWheelMovement( int amount ) {

        if ( amount != 0 )
            changeZoom( amount );
    }

    private void changeZoom( float amount ) {
        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();

        w -= amount;
        h -= amount;

        viewport.setWorldWidth( w );
        viewport.setWorldHeight( h );
    }

    public LightingSystem getLightingSystem() {
        return lightingSystem;
    }

    public void enemyKilled() {
        remainingEnemies--;
        if ( remainingEnemies == 0 ) {
            currentLevel++;
            loadLevel( currentLevel );
        }
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
}