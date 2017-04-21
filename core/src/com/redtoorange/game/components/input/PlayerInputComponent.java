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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.systems.Global;

/**
 * PlayerInputComponent.java - Control all the input from the player.  Keyboard and mouse input is processed here and
 * boiled down into it's deltas.  The SpriteComponent and the PhysicsComponents of the player will used these deltas
 * when they update.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class PlayerInputComponent extends InputComponent {
    private Vector3 mousePosition = new Vector3();
    private OrthographicCamera camera;
    private GameObject player;

    /**
     * @param player The player this is attached to.
     * @param camera The camera that is following the player.
     */
    public PlayerInputComponent( Player player, OrthographicCamera camera ) {
        super( player );
        this.camera = camera;
        this.player = player;
    }

    /** Handle keyboard input. */
    protected void updateDeltaInput() {
        //cancel out input to avoid drift.
        deltaInput.set( 0, 0 );

        //Close the Application
        if ( Gdx.input.isKeyPressed( Input.Keys.ESCAPE ) )
            Gdx.app.exit();

        //read inputs
        if ( Gdx.input.isKeyPressed( Input.Keys.W ) )
            deltaInput.y = 1;
        if ( Gdx.input.isKeyPressed( Input.Keys.S ) )
            deltaInput.y = -1;
        if ( Gdx.input.isKeyPressed( Input.Keys.A ) )
            deltaInput.x = -1;
        if ( Gdx.input.isKeyPressed( Input.Keys.D ) )
            deltaInput.x = 1;
    }

    /** Update the player's rotation based on the Mouse Position. */
    public void updateRotation() {
        float rotation = Global.lookAt( player.getTransform().getPosition(), new Vector2( mousePosition.x, mousePosition.y ) );
        sc.setRotation( rotation );
    }

    /** Handle mouse input. */
    private void updateMousePosition() {
        mousePosition = camera.unproject( new Vector3( Gdx.input.getX(), Gdx.input.getY(), 0f ) );
    }

    /**
     * Called each frame, update all the player's inputs and store them in the deltas.
     *
     * @param deltaTime The time since the last update.
     */
    public void update( float deltaTime ) {
        updateDeltaInput();
        updateMousePosition();
        updateRotation();
    }

    /** @return The current mouse Position. */
    public Vector3 getMousePosition() {
        return mousePosition;
    }

    /** @return The current keyboard input. */
    public Vector2 getDeltaInput() {
        return deltaInput;
    }

    /** Not used, implemented as required. */
    @Override
    public void dispose() {
        //  There is nothing to dispose
    }
}
