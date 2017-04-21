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

package com.redtoorange.game.factories;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * Box2DFactory.java - A special class that simplifies the creation of Box2D bodies.  The process is very redundant, so
 * the code to create them is condensed here.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class Box2DFactory {
    /**
     * Create a new static body based on a rectangle.  Great for walls.
     *
     * @param physicsSystem The world to insert the body into.
     * @param rect          The Rectangle to use for the body's bounds.
     * @return The completed body after it has been inserted into the world.
     */
    public static Body createStaticBody( PhysicsSystem physicsSystem, Rectangle rect ) {
        return createBody( physicsSystem, rect, BodyDef.BodyType.StaticBody,
                1, 0, 0,
                false, false );
    }

    /**
     * Create a generic body in the physics world.
     *
     * @param physicsSystem The world to insert the body into.
     * @param rect          The Rectangle to use for the body's bounds.
     * @param type          The type of the body to create, dynamic, static or kinematic.
     * @param density       The mass of the object in m^2.
     * @param friction      The friction that this object has.
     * @param restitution   The bounciness of this body.
     * @param isSensor      Is this body a sensor for collisions?
     * @param isBullet      Is this body a bullet?
     * @return The completed body after being inserted into the world.
     */
    public static Body createBody( PhysicsSystem physicsSystem, Rectangle rect, BodyDef.BodyType type,
                                   float density, float friction, float restitution,
                                   boolean isSensor, boolean isBullet ) {
        //  Create the bodydef
        BodyDef bDef = new BodyDef();
        bDef.position.set( rect.getX() + ( rect.getWidth() / 2f ), rect.getY() + ( rect.getHeight() / 2f ) );
        bDef.type = type;
        bDef.bullet = isBullet;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( rect.getWidth() / 2f, rect.getHeight() / 2f );

        //  Create the fixture
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.friction = friction;
        fDef.restitution = restitution;
        fDef.density = density;
        fDef.isSensor = isSensor;

        Body body = physicsSystem.createBody( bDef );
        body.createFixture( fDef );

        shape.dispose();
        return body;
    }
}
