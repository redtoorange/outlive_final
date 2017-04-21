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

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.redtoorange.game.gameobject.Bullet;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.gameobject.characters.enemies.Enemy;
import com.redtoorange.game.gameobject.powerups.PowerUp;

/**
 * ContactManager.java - Handle the contacts between different game objects.  Special methods need to be implemented
 * if the collision should call a method or event, such as doing damage, etc.  It is impossible to determine the source
 * of the contact without comparisons.  Most of the contact management is instead handled with filters elsewhere, but
 * special cases must be handled here.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class ContactManager implements ContactListener {
    /**
     * There has been a contact between bodies, pull out the actual bodies and pass them to a handler.
     *
     * @param contact The contact event between two bodies, this is passed in by Box2D.
     */
    @Override
    public void beginContact( Contact contact ) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        handleEnemyCollision( bodyA, bodyB );
        handlePowerupCollision( bodyA, bodyB );
        handleBulletCollision( bodyA, bodyB );
        handleWallCollision( bodyA, bodyB );
    }

    /**
     * Handle a collision between a player and a power-up.
     *
     * @param bodyA The body that was moving.
     * @param bodyB The body that was stationary.
     */
    private void handlePowerupCollision( Body bodyA, Body bodyB ) {
        //determine if there is a PowerUp and a Player
        if ( ( bodyA.getUserData() instanceof PowerUp && bodyB.getUserData() instanceof Player ) ||
                ( bodyB.getUserData() instanceof PowerUp && bodyA.getUserData() instanceof Player ) ) {

            //Sort which was which, then apply effects
            if ( bodyA.getUserData() instanceof PowerUp ) {
                ( ( PowerUp ) bodyA.getUserData() ).absorbed( ( ( Player ) bodyB.getUserData() ) );

            } else {
                ( ( PowerUp ) bodyB.getUserData() ).absorbed( ( ( Player ) bodyA.getUserData() ) );
            }
        }
    }

    /**
     * Handle a collision between a bullet and an Enemy.
     *
     * @param bodyA The body that was moving.
     * @param bodyB The body that was stationary.
     */
    private void handleBulletCollision( Body bodyA, Body bodyB ) {
        //determine if there is a Enemy and a Bullet
        if ( ( bodyA.getUserData() instanceof Enemy && bodyB.getUserData() instanceof Bullet ) ||
                ( bodyB.getUserData() instanceof Enemy && bodyA.getUserData() instanceof Bullet ) ) {

            //Sort which was which, then apply effects
            if ( bodyA.getUserData() instanceof Bullet ) {
                Bullet b = ( ( Bullet ) bodyA.getUserData() );
                if ( b.isAlive() ) {
                    b.kill();
                    ( ( Enemy ) bodyB.getUserData() ).takeDamage( 1 );
                }
            } else {
                Bullet b = ( ( Bullet ) bodyB.getUserData() );
                if ( b.isAlive() ) {
                    b.kill();
                    ( ( Enemy ) bodyA.getUserData() ).takeDamage( 1 );
                }
            }
        }
    }

    /**
     * Handle a collision between an enemy and the player.
     *
     * @param bodyA The body that was moving.
     * @param bodyB The body that was stationary.
     */
    private void handleEnemyCollision( Body bodyA, Body bodyB ) {
        //determine if there is a Enemy and a Player
        if ( ( bodyA.getUserData() instanceof Enemy && bodyB.getUserData() instanceof Player ) ||
                ( bodyB.getUserData() instanceof Enemy && bodyA.getUserData() instanceof Player ) ) {

            //Sort which was which, then apply effects
            Enemy enemy;
            Player player;

            if ( bodyA.getUserData() instanceof Enemy ) {
                enemy = ( ( Enemy ) bodyA.getUserData() );
                player = ( ( Player ) bodyB.getUserData() );
            } else {
                player = ( ( Player ) bodyA.getUserData() );
                enemy = ( ( Enemy ) bodyB.getUserData() );
            }

            player.takeDamage( enemy.getDamage() );
        }
    }

    /**
     * Handle a collision between a wall and a bullet.
     *
     * @param bodyA The body that was moving.
     * @param bodyB The body that was stationary.
     */
    private void handleWallCollision( Body bodyA, Body bodyB ) {
        //determine if there is a Bullet and a Wall
        if ( ( bodyA.getUserData() instanceof Bullet && bodyB.getUserData() instanceof Rectangle ) ||
                ( bodyB.getUserData() instanceof Bullet && bodyA.getUserData() instanceof Rectangle ) ) {

            //Sort which was which, then apply effects
            if ( bodyA.getUserData() instanceof Bullet ) {
                Bullet b = ( ( Bullet ) bodyA.getUserData() );
                if ( b.isAlive() ) {
                    b.kill();
                }
            } else {
                Bullet b = ( ( Bullet ) bodyB.getUserData() );
                if ( b.isAlive() ) {
                    b.kill();
                }
            }
        }
    }

    /** @param contact The contact that ended. */
    @Override
    public void endContact( Contact contact ) {
        //  Not Used.
    }

    /**
     * @param contact     The contact event.
     * @param oldManifold The line that connects the two points in the collision.
     */
    @Override
    public void preSolve( Contact contact, Manifold oldManifold ) {
        //  Not Used.
    }

    /**
     * @param contact The contact event.
     * @param impulse The impulse force that will be applied to each body after the collision.
     */
    @Override
    public void postSolve( Contact contact, ContactImpulse impulse ) {
        //  Not Used.
    }
}
