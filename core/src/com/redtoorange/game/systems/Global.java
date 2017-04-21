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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

/**
 * Global.java - Collection of global constants and common functions that don't belong inputComponent a class.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class Global {
    //-------------------------- Global Constants ---------------------------------------
    /** The collision bits to represent the player. */
    public static final short PLAYER = 0x0002;

    /** The world's ambient lighting that is used by the Lighting System. */
    public static final Color AMBIENT_LIGHT = new Color( 0.1f, 0.1f, 0.1f, 0.1f );

    //Collision Constants - These are used when sorting out collision filters.
    /** The collision bits to represent a living bullet. */
    public static final short BULLET_LIVE = 0x0004;
    /** The collision bits to represent a bullet that is not alive. */
    public static final short BULLET_DEAD = 0x0008;
    /** The collision bits to represent all walls. */
    public static final short WALL = 0x0010;
    /** The collision bits to represent an ammo pickup. */
    public static final short AMMO = 0x0020;
    /** The collision bits to represent a light ray. */
    public static final short LIGHT = 0x0040;
    /** The collision bits to represent all enemies. */
    public static final short ENEMY = 0x0001;
    /** Should the game run in Debug mode.  Provides additional output and physics debug information. */
    public static boolean DEBUG = false;


    //Game Window Constants - These are used for setting up the game window.
    /** The default pixel width resolution of the game window. */
    public static int WINDOW_WIDTH = 1920;
    /** The default pixel height resolution of the game window. */
    public static int WINDOW_HEIGHT = 1080;
    /** The game window's title when not in fullscreen. */
    public static String WINDOW_TITLE = "Some cool game thing";
    /** The window's width in world units (meters). */
    public static float VIRTUAL_WIDTH = 16f;
    /** The window's height in world units (meters). */
    public static float VIRTUAL_HEIGHT = 14f;
    //----------------------------------------------------------------------


    /** All the OpenGL JNI code to set the clear color and clear the screen.  Must be called each frame. */
    public static void clearScreen() {
        Gdx.gl20.glClearColor( 0, 0, 0, 1 );
        Gdx.gl20.glClear( GL20.GL_COLOR_BUFFER_BIT );
    }

    /**
     * Translate one Vector2 to look at another Vector2.  The vectors must be calculated from center to center, otherwise
     * there will be an oblique effect.
     *
     * @param source      The starting position of the Vector2, the one that will be rotated.
     * @param destination The point the source should be looking at.
     * @return The angle (in degrees) that the source needs to rotate for it to be pointing at the destination.
     */
    public static float lookAt( Vector2 source, Vector2 destination ) {
        return ( float ) ( Math.toDegrees( Math.atan2( ( destination.y - source.y ), ( destination.x - source.x ) ) ) );
    }
}