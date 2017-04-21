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

package com.redtoorange.game.gameobject.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.physics.PowerUpPhysicsComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.GameObjectCharacter;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * PowerUp.java - Represents a generic power-up that can be picked up by the player.  It needs to have a sensor physics
 * component attached to it to allow everything to pass through it, but still detect an overlap.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public abstract class PowerUp extends GameObject {
    /**
     * @param parent        The owning gameObject, usually the scene root.
     * @param position      The position to spawn the power-up at.
     * @param texture       The texture that that should be applied to this power-up.
     * @param physicsSystem The world's physics system.
     */
    public PowerUp( GameObject parent, Vector2 position, Texture texture, PhysicsSystem physicsSystem ) {
        super( parent, position );

        initSpriteComponent( position, texture );
        initPhysicsComponent( physicsSystem );
    }

    /**
     * Add a specialized physics component.
     *
     * @param physicsSystem The world's physics system.
     */
    private void initPhysicsComponent( PhysicsSystem physicsSystem ) {
        addComponent( new PowerUpPhysicsComponent( physicsSystem ) );
    }

    /**
     * Add a generic sprite component.
     *
     * @param position The position to spawn the power-up at.
     * @param texture  The texture that that should be applied to this power-up.
     */
    private void initSpriteComponent( Vector2 position, Texture texture ) {
        Sprite sprite = new Sprite( texture );
        sprite.setSize( 0.5f, 0.5f );
        sprite.setCenter( position.x, position.y );
        sprite.setOriginCenter();

        addComponent( new SpriteComponent( sprite ) );
    }

    /**
     * Called when the player overlaps with a power-up.
     *
     * @param character The character that the power-up should be applied to.
     */
    public void absorbed( GameObjectCharacter character ) {
        dispose();
    }
}