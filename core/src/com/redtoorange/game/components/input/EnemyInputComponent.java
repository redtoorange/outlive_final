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

package com.redtoorange.game.components.input;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.gameobject.characters.GameObjectCharacter;
import com.redtoorange.game.gameobject.characters.enemies.Enemy;
import com.redtoorange.game.systems.Global;

/**
 * EnemyInputComponent.java - The input component that will regulate an Enemy's movement.  This will make them roam around
 * randomly within a small area unless the player is near.  The enemy will begin to persue until the player is outside
 * of the sensor range.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class EnemyInputComponent extends InputComponent {
    private float roamDirection = MathUtils.random( 0f, 359f );
    private float rotation;
    private float sensorRange = 10f;
    private Enemy controlled;
    private float roamingTime = 0;
    private float roamingDelay = 0;
    private State currentState = State.ROAMING;

    /**
     * Create a generic enemy input component.  This provides very basic AI for the enemy.
     *
     * @param owner       The parent GameObject.
     * @param sensorRange How far away the player can be sensed.
     */
    public EnemyInputComponent( GameObjectCharacter owner, float sensorRange ) {
        super( owner );

        this.controlled = ( Enemy ) owner;
        this.sensorRange = sensorRange;
    }

    /**
     * Called each frame, update the current state of the enemy based on the player's distance.
     *
     * @param deltaTime The amount of time since the last update.
     */
    @Override
    public void update( float deltaTime ) {
        switch ( currentState ) {
            case ROAMING:
                romaingAI( deltaTime );
                break;
            case CHASING:
                chasingAI();
                break;
        }
    }

    /** If the enemy is in the CHASE state, the AI will close on the player. */
    private void chasingAI() {
        rotateToFacePlayer();
        calculateDeltaInput();

        if ( !withinRange() ) {
            resetRoamingTimes();
            currentState = State.ROAMING;
        }
    }

    /**
     * If the enemy is in the ROAM state, the AI will roam in a random direction, pause there, then turn in a
     * new direction, and roam there.
     *
     * @param deltaTime Time since the last Roaming update.
     */
    private void romaingAI( float deltaTime ) {
        if ( roamingTime > 0 ) {
            roamingTime -= deltaTime;
            roamDeltaInput();
        } else if ( roamingTime <= 0.0f && roamingDelay > 0 ) {
            roamingDelay -= deltaTime;
            stopMovement();
        } else if ( roamingTime <= 0 && roamingDelay <= 0 ) {
            resetRoamingTimes();
            applyDirection();
        }

        if ( withinRange() )
            currentState = State.CHASING;
    }

    /** Reset the roaming AI's timers. */
    private void resetRoamingTimes() {
        roamingTime = MathUtils.random( 1f, 2f );
        roamingDelay = MathUtils.random( 0.25f, 1f );
        roamDirection = MathUtils.random( 0f, 359f );
        applyDirection();
    }

    /** @return Is the player within the sensor range? */
    private boolean withinRange() {
        Vector2 a = controlled.getTransform().getPosition();
        Vector2 b = controlled.getPlayer().getTransform().getPosition();
        return sensorRange > Vector2.dst( a.x, a.y, b.x, b.y );
    }

    /** Turn to look at the player. */
    protected void rotateToFacePlayer() {
        rotation = Global.lookAt(
                controlled.getTransform().getPosition(),
                controlled.getPlayer().getTransform().getPosition() );
        sc.setRotation( rotation );
    }

    /** Calculate how the enemy should move based on it's speed and then rotating that vector to face the player. */
    protected void calculateDeltaInput() {
        deltaInput.set( 1, 0 );
        deltaInput.rotate( rotation );
    }

    /** Change the AI's facing. */
    protected void applyDirection() {
        sc.setRotation( roamDirection );
    }

    /** Calculate how the enemy should move based on it's speed and then rotating that vector to face the roam direction. */
    protected void roamDeltaInput() {
        deltaInput.set( 1, 0 );
        deltaInput.rotate( roamDirection );
    }

    /** Stop all movement, prevent drift. */
    protected void stopMovement() {
        deltaInput.set( 0, 0 );
    }

    /** Not used, needs to be implemented. */
    @Override
    public void dispose() {
        //Not Used.
    }

    /** The states that the Enemy AI can be in. */
    private enum State {
        ROAMING, CHASING
    }
}
