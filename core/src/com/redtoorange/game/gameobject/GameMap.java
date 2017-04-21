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

package com.redtoorange.game.gameobject;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.redtoorange.game.components.rendering.sprite.MapRenderComponent;


/**
 * GameMap.java - Encapsulated TiledMap renderer. Scaling is handled automatically.  Two Arrays will be
 * pulled for object layers name: "walls", "playerspawn".  Should NOT be added to the engine.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class GameMap extends GameObject {
    /** Parsed from the TMX map and scale by the map scale.  "walls" must be the layer name. */
    public Array< Rectangle > walls = new Array< Rectangle >();
    /** Parsed from the TMX map and scaled by the map scale.  "playerspawns" must be the layer name. */
    public Array< Rectangle > playerSpawns = new Array< Rectangle >();

    private TiledMap map;
    private float mapScale = 1f;
    private MapRenderComponent mapRenderer;
    private int enemyCount = 0;
    private int ammoCount = 0;
    private int healthCount = 0;
    private int maxWidth = 0;
    private int maxHeight = 0;

    /**
     * Build a GameMap that will encapsulate a Tiled TMX Map.  Scaling is handled automatically.  Two Arrays will be
     * pulled from the TMX Map for object layers name: "walls", "playerspawn".
     *
     * @param parent   The gameObject this is placed under, usually the scene root.
     * @param mapPath  Rhe complete path from the Asset folder for the TMX file.
     * @param batch    The SpriteBatch to embed into the MapRenderer.
     * @param camera   The camera that is following the player, this is used for tile occlusion.
     * @param mapScale The amount to resize the entire map by.  1/16f if you want 16 map pixels to equal 1 game unit.
     */
    public GameMap( GameObject parent, String mapPath, SpriteBatch batch, OrthographicCamera camera, float mapScale ) {
        super( parent, new Vector2( 0, 0 ) );
        this.mapScale = mapScale;

        TmxMapLoader mapLoader = new TmxMapLoader( new InternalFileHandleResolver() );
        map = mapLoader.load( mapPath );

        //Pull the properties from the TMX file.
        MapProperties properties = map.getProperties();
        enemyCount = properties.get( "enemy", Integer.class );
        healthCount = properties.get( "health", Integer.class );
        ammoCount = properties.get( "ammo", Integer.class );

        maxWidth = properties.get( "width", Integer.class );
        maxHeight = properties.get( "height", Integer.class );

        mapRenderer = new MapRenderComponent( map, mapScale, batch, camera );

        buildWalls();
        buildPlayerSpawns();
    }

    /**
     * Strip the rectangle objects from the TMX file and resize them to the correct scale for the game world.
     *
     * @param source      The collection of source rectangles from the TMX file.
     * @param destination The collection to place the resized rectangles.
     */
    private void pullAndAddRectangles( Array< RectangleMapObject > source, Array< Rectangle > destination ) {
        for ( RectangleMapObject r : source ) {
            Rectangle rectangle = r.getRectangle();

            Vector2 size = new Vector2();
            Vector2 center = new Vector2();

            rectangle.getSize( size );
            rectangle.getCenter( center );

            size.scl( mapScale );
            center.scl( mapScale );

            rectangle.setSize( size.x, size.y );
            rectangle.setCenter( center.x, center.y );

            destination.add( rectangle );
        }
    }

    /** Construct the walls for the map based on the "walls" layer inside the TMX file. */
    private void buildWalls() {
        Array< RectangleMapObject > wallMapObjects = map.getLayers().get( "walls" ).getObjects().getByType( RectangleMapObject.class );
        pullAndAddRectangles( wallMapObjects, walls );
    }

    /** Locate the player's spawn point on the map based on the "playerspawn" layer inside the TMX file. */
    private void buildPlayerSpawns() {
        Array< RectangleMapObject > playerspawnMapObjects = map.getLayers().get( "playerspawn" ).getObjects().getByType( RectangleMapObject.class );
        pullAndAddRectangles( playerspawnMapObjects, playerSpawns );
    }

    /** @param batch The lighting call that is down before the lighting phase of the draw. */
    public void preLighting( SpriteBatch batch ) {
        mapRenderer.draw( batch );
    }

    /** @return get the number of enemies that are supposed to spawn in the map. */
    public int getEnemyCount() {
        return enemyCount;
    }

    /** @return get the number of ammo pickups that are supposed to spawn in the map. */
    public int getAmmoCount() {
        return ammoCount;
    }

    /** @return get the number of health pickups that are supposed to spawn in the map. */
    public int getHealthCount() {
        return healthCount;
    }

    /** @return get the max width of the map's spawn area */
    public int getMaxWidth() {
        return maxWidth;
    }

    /** @return get the max height of the map's spawn area. */
    public int getMaxHeight() {
        return maxHeight;
    }

    /** Destroy the map and the map renderer. */
    @Override
    public void dispose() {
        if ( map != null )
            map.dispose();

        mapRenderer.dispose();

        super.dispose();
    }
}