package com.redtoorange.game.gameobject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.physics.BulletPhysicsComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.systems.sound.SoundEffect;
import com.redtoorange.game.systems.sound.SoundManager;

/**
 * Bullet.java - Represents a bullet fired from the player's gun.  Will handle playing it's own sounds on impact.  Has
 * a specialized physics component to handle collision and it's own sprite.  When the bullet has been fired, it starts
 * a lifetime counter, after the timer runs out, the bullet will be freed to be used by the bullet pool again.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class Bullet extends GameObject {
    /** How long has the bullet been alive. */
    protected float lifeTime = 0.0f;
    /** How long can the bullet live before it dies. */
    protected float maxLife = 5.0f;
    /** Is the bullet currently alive. */
    protected boolean alive = false;
    /** The speed of the bullet when fired. */
    protected float speed;
    /** The attached physics component. */
    protected BulletPhysicsComponent bulletPhysicsComponent;
    /** The attached sprite component. */
    protected SpriteComponent spriteComponent;

    private SoundManager sm = new SoundManager();

    /**
     * @param parent        The owning gameObject.
     * @param sprite        The sprite that represents this bullet.
     * @param physicsSystem The world's physics system.
     * @param position      The initial position of the bullet.
     * @param speed         The base speed of the bullet.
     */
    public Bullet( GameObject parent, Sprite sprite, PhysicsSystem physicsSystem, Vector2 position, float speed ) {
        super( parent, position );

        this.speed = speed;
        spriteComponent = new SpriteComponent( sprite );
        bulletPhysicsComponent = new BulletPhysicsComponent( physicsSystem, this, spriteComponent );

        components.add( bulletPhysicsComponent );
        components.add( spriteComponent );

        sm.addSound( "bullethit", new SoundEffect( "sounds/bullethit.wav", 0.15f ) );
    }

    /**
     * Update the bullet and determine if it has been alive too long to still be seen by the player.
     *
     * @param deltaTime Time since the last update.
     */
    @Override
    public void update( float deltaTime ) {
        if ( alive ) {
            super.update( deltaTime );

            lifeTime += deltaTime;
            if ( lifeTime >= maxLife )
                kill();
        }
        sm.update( deltaTime );
    }

    /**
     * If the bullet is alive, it will be drawn before lighting is calculated.
     *
     * @param batch sprite batch to use for drawing this bullet before the lighting is calculated.
     */
    public void preLighting( SpriteBatch batch ) {
        if ( alive ) {
            spriteComponent.draw( batch );
        }
    }

    /**
     * Called by the gun component when the player clicks the fire button.
     *
     * @param position The position to set the bullet to.
     * @param velocity The velocity of the bullet.
     * @param rotation The rotation of the bullet.
     */
    public void fire( Vector2 position, Vector2 velocity, float rotation ) {
        alive = true;

        spriteComponent.setCenter( position );
        spriteComponent.setRotation( rotation );

        bulletPhysicsComponent.fire( position, velocity, rotation );
    }

    /** Stop this bullet because it hit something. */
    public void kill() {
        sm.playSound( "bullethit" );
        this.alive = false;
        lifeTime = 0.0f;
        bulletPhysicsComponent.kill();
    }

    /** @return is this bullet currently in motion? */
    public boolean isAlive() {
        return alive;
    }

    /** Destroy the bullet and it's components. */
    @Override
    public void dispose() {
        if ( spriteComponent != null )
            spriteComponent.dispose();
        if ( bulletPhysicsComponent != null )
            bulletPhysicsComponent.dispose();
    }

    /** @return Get the speed of the bullet. */
    public float getSpeed() {
        return speed;
    }
}