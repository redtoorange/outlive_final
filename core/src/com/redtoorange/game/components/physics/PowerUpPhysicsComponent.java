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

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * PowerUpPhysicsComponent.java - Generic physics component that is attached to Ammo and Health power-ups.  Allows the
 * player collide with and detect overlaps.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class PowerUpPhysicsComponent extends PhysicsComponent {
    private GameObject powerUpGO;

    /** @param physicsSystem The physics system that is currently being used. */
    public PowerUpPhysicsComponent( PhysicsSystem physicsSystem ) {
        super( physicsSystem );
    }

    /**
     * Called before the game starts but after the constructor.
     *
     * @param parent The parent GameObject
     */
    @Override
    public void start( GameObject parent ) {
        powerUpGO = parent;
        initPhysics( (SpriteComponent ) parent.getComponent( SpriteComponent.class ) );
    }

    /**
     * Initialize the physics body based on the size, shape and position of the parent's sprite.
     *
     * @param parentSpriteComponent The spriteComponent that will be used to build the body.
     */
    private void initPhysics( SpriteComponent parentSpriteComponent ) {
        body = Box2DFactory.createBody( physicsSystem, parentSpriteComponent.getBoundingBox(), BodyDef.BodyType.DynamicBody, 10f, 0, 0, true, false );

        body.setUserData( powerUpGO );

        Filter f = body.getFixtureList().first().getFilterData();
        f.categoryBits = Global.AMMO;
        body.getFixtureList().first().setFilterData( f );

        body.setTransform( parentSpriteComponent.getCenter(), ( float ) Math.toRadians( parentSpriteComponent.getRotation() ) );
    }
}
