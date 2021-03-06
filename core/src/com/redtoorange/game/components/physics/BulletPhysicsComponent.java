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

package com.redtoorange.game.components.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.gameobject.Bullet;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * BulletPhysicsComponent.java - A physics component that is attached to a bullet.  This allows the bullet to move under
 * realistic physics and collide with walls and enemies.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class BulletPhysicsComponent extends PhysicsComponent {
    private Rectangle bulletBoundingRect;
    private float bulletSpeedMultiplier;
    private Bullet controlledBullet;

    /**
     * Create a new bullet.
     *
     * @param physicsSystem    The world's physicsSystem.
     * @param controlledBullet The Bullet GameObject that this body controls.
     * @param spriteComponent  The SpriteComponent attached to the parent GameObject.
     */
    public BulletPhysicsComponent( PhysicsSystem physicsSystem, Bullet controlledBullet, SpriteComponent spriteComponent ) {
        super( physicsSystem );

        this.controlledBullet = controlledBullet;
        this.bulletSpeedMultiplier = controlledBullet.getSpeed();

        bulletBoundingRect = new Rectangle( spriteComponent.getBoundingBox() );
        bulletBoundingRect.setSize( spriteComponent.getWidth() / 4f, spriteComponent.getHeight() / 4f );
    }

    /**
     * When the gun fires a bullet, these settings will be set based on the player.
     *
     * @param position The position to spawn the bullet.
     * @param velocity The velocity of the bullet (in x, y).
     * @param rotation The rotation of the bullet.
     */
    public void fire( Vector2 position, Vector2 velocity, float rotation ) {
        createBulletBody( position, velocity, rotation );
        Filter f = createFilter();
        body.getFixtureList().first().setFilterData( f );
    }

    /** @return Create the filter that will set the bullet's collision properties. */
    private Filter createFilter() {
        Filter f = body.getFixtureList().first().getFilterData();
        f.categoryBits = Global.BULLET_LIVE;
        f.maskBits = Global.ENEMY | Global.WALL;
        return f;
    }

    /**
     * @param position The position to spawn the bullet.
     * @param velocity The velocity of the bullet (in x, y).
     * @param rotation The rotation of the bullet.
     */
    private void createBulletBody( Vector2 position, Vector2 velocity, float rotation ) {
        body = Box2DFactory.createBody( physicsSystem, bulletBoundingRect, BodyDef.BodyType.DynamicBody, 10f, 0, 0, true, true );

        body.setUserData( controlledBullet );
        body.setTransform( position, ( float ) Math.toRadians( rotation ) );
        body.applyLinearImpulse( velocity.scl( bulletSpeedMultiplier ), body.getWorldCenter(), true );
    }

    /** Kill this bullet's physics from the world. */
    public void kill() {
        physicsSystem.destroyBody( body );
        body = null;
    }

    /**
     * Update the bullet gameObject based on the current position of this body.
     *
     * @param deltaTime The time since the last update.
     */
    @Override
    public void update( float deltaTime ) {
        controlledBullet.getTransform().setPosition( body.getPosition() );
    }
}
