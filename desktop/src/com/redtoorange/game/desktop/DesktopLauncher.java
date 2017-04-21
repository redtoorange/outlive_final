package com.redtoorange.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.redtoorange.game.Core;
import com.redtoorange.game.systems.Global;

/**
 * DesktopLauncher.java - Launch the Game Itself.  This will setup the OpenGL render context and hand it over to the
 * "core" source set.  This source set contains a Core class, that will manage the Game's state up until it exits.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class DesktopLauncher {
    /**
     * Launch the LWJGL Window, the actual game itself.
     *
     * @param fullscreen Should the game be in full screen?
     * @param debug      Should the game be in debug mode?
     * @param width      The width of the window.
     * @param height     The height of the window.
     */
    public DesktopLauncher( boolean fullscreen, boolean debug, double width, double height ) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = Global.WINDOW_TITLE;
        config.fullscreen = fullscreen;

        config.width = ( int ) width;
        config.height = ( int ) height;

        new LwjglApplication( new Core( debug ), config );
    }
}
