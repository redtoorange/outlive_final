/*
 * Copyright 2017  Andrew James McGuiness
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated  documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the  rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit   persons to whom the Software is furnished to do
 *   so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
 * MissionState.java - The most fundamental state that the game can be in.  This is the state that allows the player to
 * control a character, updates the UI elements, loads in maps and continues to update/render all the elements in the
 * game.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class MissionState extends ScreenAdapter {
    private boolean running = true;
    private int currentLevel = 0;
    /** A listing of all the game levels that the game can load. */
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

    /** @param core The original game engine core that will be called for screen changes. */
    public MissionState( Core core ) {
        this.core = core;
    }

    /** Called with the screen is first displayed. */
    @Override
    public void show() {
        init();
    }

    /** Initialize all elements of the game state. */
    private void init() {
        VisUI.load();

        //Lock the mouse and capture input
        Gdx.input.setCursorCatched( true );
        Gdx.input.setCursorPosition( Global.WINDOW_WIDTH / 2, Global.WINDOW_HEIGHT / 2 );
        Gdx.input.setInputProcessor( new mouseProcessor() );

        //built the UI elements
        gunui = new GunUI();
        healthui = new HealthUI();

        //set up the rendering elements
        camera = new OrthographicCamera( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT );
        viewport = new ExtendViewport( Global.VIRTUAL_WIDTH, Global.VIRTUAL_HEIGHT, camera );
        batch = new SpriteBatch();

        //load in the level
        loadLevel( currentLevel );
    }

    /**
     * Load the enemies and power-ups into the gameMap.  Their position it randomly generated based on the Map's max
     * height and width.  The number of each it determined by the TMX file's properties.
     *
     * @param exclusionZone The area that nothing should spawn in, usually the player's spawn area.
     */
    private void loadObjects( Rectangle exclusionZone ) {
        remainingEnemies = gameMap.getEnemyCount();

        //Load the enemies.
        for ( int i = 0; i < gameMap.getEnemyCount(); i++ ) {
            Vector2 point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            while ( exclusionZone.contains( point ) )
                point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            sceneRoot.addChild( new Enemy( sceneRoot, physicsSystem, point, player ) );
        }

        //Load ammo power-ups.
        for ( int i = 0; i < gameMap.getAmmoCount(); i++ ) {
            Vector2 point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            while ( exclusionZone.contains( point ) )
                point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            sceneRoot.addChild( new Ammo( sceneRoot, point, physicsSystem ) );
        }

        //Load health power-ups.
        for ( int i = 0; i < gameMap.getHealthCount(); i++ ) {
            Vector2 point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            while ( exclusionZone.contains( point ) )
                point = new Vector2( MathUtils.random( 2, gameMap.getMaxWidth() - 1 ), MathUtils.random( 2, gameMap.getMaxHeight() - 1 ) );

            sceneRoot.addChild( new Health( sceneRoot, point, physicsSystem ) );
        }
    }

    /**
     * Load a new gameMap for the player to play on.
     *
     * @param levelNumber The number of the level to load.
     */
    private void loadLevel( int levelNumber ) {
        resetSystems();
        loadGameMap( levelNumber );
        resetLightingFilter();

        sceneRoot.start( null );
    }

    /** Build a new lighting filter so the rays collide correctly. */
    private void resetLightingFilter() {
        Filter f = new Filter();
        f.categoryBits = Global.LIGHT;
        f.maskBits = Global.ENEMY | Global.WALL;
        Light.setGlobalContactFilter( f );
    }

    /** Reset all the physics systems in the game. */
    private void resetSystems() {
        debugRenderer = new Box2DDebugRenderer();
        contactManager = new ContactManager();
        physicsSystem = new PhysicsSystem();
        physicsSystem.getWorld().setContactListener( contactManager );
        lightingSystem = new LightingSystem( physicsSystem.getWorld() );
    }

    /**
     * Load the map from disc and spawn in the elements, including the player.
     *
     * @param levelNumber The number of the level to load.
     */
    private void loadGameMap( int levelNumber ) {
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
    }

    /**
     * Store the player's current state, then load him into a new map.
     *
     * @param playerSpawn The new location player for the player.
     */
    private void initPlayer( Vector2 playerSpawn ) {
        int currentHealth = 10;
        int maxHealth = 10;
        int rounds = 6;

        if ( player != null ) {
            currentHealth = player.getHealth();
            maxHealth = player.getMaxHealth();
            rounds = player.getInventoy().remaining( GunType.REVOLVER );
        }

        player = new Player( sceneRoot, camera, this, physicsSystem, playerSpawn );
        player.setHealth( currentHealth, maxHealth );
        player.setAmmo( rounds );
    }

    /** Build all the walls for the game map based on the TMX's files objects layer. */
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
     * Update all the systems in the game, including the sceneroot, which will update all the gameObjects in the game.
     *
     * @param deltaTime The time since the last update.
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


    /** Draw all renderable elements in the game during four stages, prelighting, lighting, postlighting and UI. */
    public void draw() {
        if ( !running )
            return;

        camera.update();

        //Prelighting
        gameMap.preLighting( batch );

        //During Lighting
        batch.setProjectionMatrix( camera.combined );
        batch.begin();
        sceneRoot.preLighting( batch );
        batch.end();
        renderLighting();

        //Postlighting
        batch.begin();
        sceneRoot.postLighting( batch );
        batch.end();

        //UI
        gunui.draw( batch );
        healthui.draw( batch );

        if ( Global.DEBUG )
            debugRenderer.render( physicsSystem.getWorld(), camera.combined );
    }

    /** Render the lighting elements in the game, including lights and muzzle flashes. */
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

    /** Update the camera to follow the player. */
    private void updateCameraPosition() {
        if ( player != null )
            camera.position.lerp( player.getPosition3D(), cameraSmoothing );
    }

    /**
     * The play window has been resized, notify all screen elements about the change.
     *
     * @param width  The screen's new width.
     * @param height The screen's new height.
     */
    @Override
    public void resize( int width, int height ) {
        viewport.update( width, height );
        gunui.resize( width, height );
        healthui.resize( width, height );
        camera.update();
    }

    /** Destroy all elements of the missionState.  This destroys everything, including the player, and releases all assets. */
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

    /**
     * Change which Character the Player is controlling.
     *
     * @param player The player GameObject that the Player should be able to control.
     */
    public void setPlayer( Player player ) {
        this.player = player;
        if ( player == null ) {
            running = false;
            core.setPlaying( false );
        }
    }

    /** @return The player's current instance of the GunUI. */
    public GunUI getGunUI() {
        return gunui;
    }

    /** @return The player's current instance of the HealthUI. */
    public HealthUI getHealthUI() {
        return healthui;
    }

    /**
     * The mouse wheel has moved, store the amount.
     *
     * @param amount How much the wheel has moved.
     */
    private void mouseWheelMovement( int amount ) {

        if ( amount != 0 )
            changeZoom( amount );
    }

    /**
     * There should be a zooming effect because of mousewheel movement.
     *
     * @param amount Amount to change the zoom level by.
     */
    private void changeZoom( float amount ) {
        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();

        w -= amount;
        h -= amount;

        viewport.setWorldWidth( w );
        viewport.setWorldHeight( h );
    }

    /** @return Get the current lighting system for the state. */
    public LightingSystem getLightingSystem() {
        return lightingSystem;
    }

    /** The player killed an enemy, update the count to determine when to swap levels. */
    public void enemyKilled() {
        remainingEnemies--;
        if ( remainingEnemies == 0 ) {
            currentLevel++;
            loadLevel( currentLevel );
        }
    }

    /**
     * Processor to handle any movement of the mouse wheel.  This has not been fully implemented, but would allow the
     * player to zoom in and out.
     */
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