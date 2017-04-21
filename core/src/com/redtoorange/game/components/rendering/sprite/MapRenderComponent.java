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

package com.redtoorange.game.components.rendering.sprite;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.redtoorange.game.systems.Global;

/**
 * MapRenderComponent.java - This component should be attached to a GameMap GameObject in order for the GameMap to be
 * rendered correctly to the screen.  The map will be scaled proportionally to the game world.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class MapRenderComponent extends RenderComponent {
    private TiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    /**
     * @param map      TiledMap loaded from a file on disk and rendered by this component.
     * @param mapScale Amount to scale the map to fit correctly with the world units.
     * @param batch    The batch that this map will used to render.
     * @param camera   The camera that is following the player.
     */
    public MapRenderComponent( TiledMap map, float mapScale, SpriteBatch batch, OrthographicCamera camera ) {
        this.camera = camera;
        mapRenderer = new OrthogonalTiledMapRenderer( map, mapScale, batch );
    }

    /** @param batch The batch to draw this component to. */
    @Override
    public void draw( SpriteBatch batch ) {
        mapRenderer.setView( camera );
        mapRenderer.render();
    }

    /** The cleanup is handled by the map GameObject. */
    @Override
    public void dispose() {
        if ( Global.DEBUG )
            System.out.println( this.getClass().getSimpleName() + " disposed" );
    }
}