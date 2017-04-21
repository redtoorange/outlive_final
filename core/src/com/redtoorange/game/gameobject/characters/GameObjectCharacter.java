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

package com.redtoorange.game.gameobject.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * GameObjectCharacter.java - A generic GameObject that represents a character in-game.  They have several components in
 * common, physics, input and sprites.  Because of this, any character must implement a method that will create these to
 * be considered a character.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public abstract class GameObjectCharacter extends GameObject {
    /** The world's physics system. */
    protected PhysicsSystem physicsSystem;
    /** The character's current health. */
    protected int maxHealth = 1;
    /** The character's health cap. */
    protected int health = maxHealth;

    /**
     * @param parent        The character's parent, usually the sceneRoot.
     * @param physicsSystem The world's physics system.
     * @param position      The starting position of the character.
     */
    public GameObjectCharacter( GameObject parent, Vector2 position, PhysicsSystem physicsSystem ) {
        super( parent, position );
        this.physicsSystem = physicsSystem;
    }

    /** @return get the character's position in 3D space. Z is always 0. */
    public Vector3 getPosition3D() {
        return new Vector3( transform.getPosition().x, transform.getPosition().y, 0 );
    }

    /**
     * Deal damage to the character, if it's health reaches 0, they will die.
     *
     * @param amount Amount of damage the character has received.
     */
    public void takeDamage( int amount ) {
        health -= amount;
        if ( health <= 0 ) {
            die();
        }
    }

    /** @return The character's current rotation. */
    public abstract float getRotation();

    /** Called to initialize the sprite component. */
    protected abstract void initSpriteComponent();

    /** Called to initialize the input component. */
    protected abstract void initInputComponent();

    /** Called to initialize the physics component. */
    protected abstract void initPhysicsComponent();

    /** Should be called when the character dies. */
    protected abstract void die();
}