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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.systems.Global;

/**
 * SpriteComponent.java - The primary visual component of GameObjects in the engine.  Anything with a SpriteComponent
 * will be drawn on the screen.  The sprite's size, position and rotation are set by other components, including the
 * input and physics component, depending on what the SpriteComponent is attached to.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class SpriteComponent extends RenderComponent {
    private Sprite sprite;

    /** @param sprite Sprite that should be cloned into this component. */
    public SpriteComponent( Sprite sprite ) {
        this.sprite = new Sprite( sprite );
    }

    /** @param owner The owner of this spriteComponent. */
    @Override
    public void start( GameObject owner ) {
        super.start( owner );

        Vector2 pos = owner.getTransform().getPosition();
        this.sprite.setCenter( pos.x, pos.y );
        this.sprite.setOriginCenter();
    }

    /**
     * Set the tint of the Sprite.
     *
     * @param red   0-1 * 255 red value.
     * @param green 0-1 * 255 green value.
     * @param blue  0-1 * 255 blue value.
     * @param alpha 0-1% alpha value.
     */
    public void setColor( float red, float green, float blue, float alpha ) {
        sprite.setColor( new Color( red, green, blue, alpha ) );
    }

    /** @param batch The batch to draw this component to. */
    @Override
    public void draw( SpriteBatch batch ) {
        setCenter( owner.getTransform().getPosition() );
        sprite.draw( batch );
    }

    /** @return the sprite stored inside of this component. */
    public Sprite getSprite() {
        return sprite;
    }

    /** @return A float representing the rotation of the sprite inputComponent degrees. */
    public float getRotation() {
        return sprite.getRotation();
    }

    /** @param rotation the degree representation the Sprite should be set to. */
    public void setRotation( float rotation ) {
        sprite.setRotation( rotation );
    }

    /** @return The Center of the Sprite. */
    public Vector2 getCenter() {
        return new Vector2( getCenterX(), getCenterY() );
    }

    /** @param center Where the center of the sprite should be preLighting. */
    public void setCenter( Vector2 center ) {
        sprite.setCenter( center.x, center.y );
    }

    /** @return the sprite's center x position. */
    public float getCenterX() {
        return ( sprite.getX() + ( sprite.getWidth() / 2f ) );
    }

    /** @return the sprite's center y position. */
    public float getCenterY() {
        return ( sprite.getY() + ( sprite.getHeight() / 2f ) );
    }


    /** @return The smallest possible rectangle that will fit around the Sprite Image. */
    public Rectangle getBoundingBox() {
        return sprite.getBoundingRectangle();
    }

    /** @return the sprite's width in world units. */
    public float getWidth() {
        return sprite.getWidth();
    }

    /** @return the sprite's height in world units. */
    public float getHeight() {
        return sprite.getHeight();
    }

    /** Sprites are automatically disposed when the go out of scope. */
    @Override
    public void dispose() {
        if ( Global.DEBUG )
            System.out.println( this.getClass().getSimpleName() + " disposed" );
    }
}