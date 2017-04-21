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

    /** Called to get the character's current rotation. */
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