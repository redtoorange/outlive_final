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

package com.redtoorange.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.redtoorange.game.states.MissionState;
import com.redtoorange.game.systems.Global;

/**
 * Core.java - Core game class that handles the different states.  Can be given a debugging flag that will cause the
 * debug renderer to be used and verbose logging to the console.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class Core extends Game {
    private MissionState missionState;
    private boolean playing = true;

    /** @param debugging Should the debug renderer and verbose logging be used? */
    public Core( boolean debugging ) {
        super();
        Global.DEBUG = debugging;
    }

    /** Called after construction by the Game. */
    @Override
    public void create() {
        missionState = new MissionState( this );
        setScreen( missionState );
    }

    /**
     * Set the game to a new state based on a screen, the old screen will be disposed.
     *
     * @param screen The new state to put the game into.
     */
    @Override
    public void setScreen( Screen screen ) {
        if ( this.screen != null )
            this.screen.dispose();

        this.screen = screen;

        if ( this.screen != null )
            this.screen.show();
    }

    /**
     * Anytime there is a sizing event on the game window, this method is called by the system.  It notifies the current
     * state so it can adjust the camera and viewport.
     *
     * @param width  The screen's new width.
     * @param height The screen's new height.
     */
    @Override
    public void resize( int width, int height ) {
        screen.resize( width, height );
        super.resize( width, height );
    }

    /**
     * If the game is playing, update and render, otherwise, dispose.
     */
    @Override
    public void render() {
        if ( playing )
            update();

        if ( playing )
            draw();

        if ( !playing )
            dispose();
    }

    /** Call the state's update method. */
    public void update() {
        missionState.update( Gdx.graphics.getDeltaTime() );
    }

    /** Call the state's render method. */
    public void draw() {
        Global.clearScreen();
        missionState.draw();
    }

    /** Dispose the current screen and release the mouse. */
    @Override
    public void dispose() {
        if ( screen != null ) {
            screen.dispose();
            screen = null;
        }

        if ( Global.DEBUG )
            System.out.println( "Core disposed" );

        Gdx.input.setCursorCatched( false );
        Gdx.app.exit();
    }

    /** @param playing Set whether the game is playing or not. */
    public void setPlaying( boolean playing ) {
        this.playing = playing;
    }
}