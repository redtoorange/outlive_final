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

package com.redtoorange.game.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * PhysicsSystem.java - Box2D Physics World encapsulation.  Will handle adding and removal of bodies gracefully while
 * still allowing the World to step.  All physicsComponents in the engine must register with this system inorder to be
 * inside of the world.  Bodies that are created outside of the physicsSystem can be added to the world one at a time.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class PhysicsSystem extends System {
    private World world;

    private boolean cullBodies = false;
    private boolean createBodies = false;

    private Array< BodyDef > newBodies = new Array< BodyDef >();
    private Array< Body > deadBodies = new Array< Body >();

    /** Create a new Box2D World with no gravity. */
    public PhysicsSystem() {
        super();
        world = new World( new Vector2( 0f, 0f ), true );
    }

    /**
     * @param gravity       How much x,y gravity to apply per time step.
     * @param allowSleeping Should the World allow bodies to sleep.
     */
    public PhysicsSystem( Vector2 gravity, boolean allowSleeping ) {
        world = new World( new Vector2( gravity ), allowSleeping );
    }

    /**
     * Step the world based on the deltaTime.  All bodies will be stepped.  If there are any bodies to be destroyed,
     * it will happen after the world is stepped.
     *
     * @param deltaTime The amount of time to step the world by.
     */
    public void update( float deltaTime ) {
        world.step( deltaTime, 6, 2 );

        if ( cullBodies )
            destroyBodies();
        if ( createBodies )
            spawnBodies();
    }

    /** Safely destroy bodies from the world. */
    private void destroyBodies() {
        Array< Body > bodies = new Array< Body >();
        world.getBodies( bodies );

        cullBodies = false;
        for ( int i = deadBodies.size - 1; i >= 0; i-- ) {
            if ( deadBodies.get( i ) != null && bodies.contains( deadBodies.get( i ), true ) )
                world.destroyBody( deadBodies.get( i ) );

            deadBodies.removeIndex( i );
        }
    }

    /**
     * Register a body for destruction.  It will be culled after the next time step.
     *
     * @param body The body to destroy.
     */
    public void destroyBody( Body body ) {
        cullBodies = true;
        deadBodies.add( body );
    }

    /**
     * Safely create a new body in the world.
     *
     * @param bDef Box2D Body Definition to used when creating the body.
     * @return A reference to the create body.
     */
    public Body createBody( BodyDef bDef ) {
        return world.createBody( bDef );
    }

    /** Create all bodies that are waiting to be added to the world.  This allows for bodies to be added during simulation. */
    private void spawnBodies() {
        createBodies = false;
        for ( int i = newBodies.size - 1; i >= 0; i-- ) {
            if ( newBodies.get( i ) != null )
                world.createBody( newBodies.get( i ) );

            newBodies.removeIndex( i );
        }
    }

    /** @return Get a reference to the World. */
    public World getWorld() {
        return world;
    }

    /** Destroy the world and all bodies in it.  Any bodies awaiting destruction or creation will be released. */
    public void dispose() {
        if ( world != null )
            world.dispose();

        deadBodies.clear();
        newBodies.clear();

        createBodies = false;
        cullBodies = false;

        if ( Global.DEBUG )
            java.lang.System.out.println( "Physics system disposed" );
    }
}
