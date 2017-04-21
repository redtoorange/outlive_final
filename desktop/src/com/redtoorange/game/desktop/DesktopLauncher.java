package com.redtoorange.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.redtoorange.game.Core;
import com.redtoorange.game.systems.Global;

/**
 * DesktopLauncher.java - Basic Windows window handler.  Launches a new copy of Core. Program entry point.
 *
 * @author - Andrew M.
 * @version - 13/Jan/2017
 */
public class DesktopLauncher {

	public DesktopLauncher( boolean fullscreen, boolean debug, double width, double height){

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration( );

		config.title = Global.WINDOW_TITLE;
		config.fullscreen = fullscreen;

		config.width = (int)width;
		config.height = (int)height;

		new LwjglApplication( new Core(  debug ), config );
	}
}
