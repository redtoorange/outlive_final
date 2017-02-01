package com.redtoorange.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.redtoorange.game.Core;
import com.redtoorange.game.Global;

/**
 * DesktopLauncher.java - Basic Windows window handler.  Launches a new copy of Core. Program entry point.
 *
 * @author - Andrew M.
 * @version - 13/Jan/2017
 */
public class DesktopLauncher {

	public DesktopLauncher( boolean fullscreen, boolean debug){

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration( );

		config.title = Global.WINDOW_TITLE;
		config.fullscreen = fullscreen;

		if(!fullscreen) {
			config.width = Global.WINDOW_WIDTH;
			config.height = Global.WINDOW_HEIGHT;
		}else{
			config.width = 2736;
			config.height = 1824;
		}

		new LwjglApplication( new Core(  debug ), config );
	}
}
