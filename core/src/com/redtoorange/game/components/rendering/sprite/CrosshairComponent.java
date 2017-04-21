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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.redtoorange.game.components.input.PlayerInputComponent;
import com.redtoorange.game.engine.PostLightingDraw;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.systems.Global;

/**
 * CrosshairComponent.java - A special RenderComponent meant to be attached to the player.  It will track the position
 * of the mouse on the screen, indicating where the player's gun will fire.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class CrosshairComponent extends RenderComponent implements PostLightingDraw {
    private Sprite crossHair;
    private PlayerInputComponent inputComponent;
    private GameObject parent;


    /** @param parent the GameObject this is attached to. */
    public CrosshairComponent( GameObject parent ) {
        this.parent = parent;
    }

    /** @param parent the GameObject this is attached to. */
    @Override
    public void start( GameObject parent ) {
        Texture cross = new Texture( "crosshair.png" );
        crossHair = new Sprite( cross );
        crossHair.setPosition( 0f, 0f );
        crossHair.setSize( 1f, 1f );
    }

    /** @param deltaTime The amount of time since the last update. */
    @Override
    public void update( float deltaTime ) {
        if ( inputComponent == null )
            inputComponent = parent.getComponent( PlayerInputComponent.class );

        Vector3 mousePos = inputComponent.getMousePosition();
        crossHair.setCenter( mousePos.x, mousePos.y );
    }

    /**
     * A special draw call that is handled after the lighting in the game.  Items that should not be shaded should be
     * drawn with it.
     *
     * @param batch The batch to draw this component to.
     */
    public void postLightingDraw( SpriteBatch batch ) {
        crossHair.draw( batch );
    }

    /** Nothing to dispose, the sprite will clean itself. */
    @Override
    public void dispose() {
        if ( Global.DEBUG )
            System.out.println( this.getClass().getSimpleName() + " disposed" );
    }

    /** @param batch The batch to draw this component to. */
    @Override
    public void draw( SpriteBatch batch ) {
        crossHair.draw( batch );
    }
}
