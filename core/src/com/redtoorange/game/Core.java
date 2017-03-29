package com.redtoorange.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.redtoorange.game.states.MissionState;

/**
 * Core.java - Core game class that handles the different states.
 *
 * @author - Andrew M.
 * @version - 13/Jan/2017
 */
public class Core extends Game {
	private FPSLogger logger = new FPSLogger( );
	private MissionState missionState;
	private boolean playing = true;

	public Core( boolean debugging ) {
		super( );
		Global.DEBUG = debugging;

	}

	@Override
	public void create( ) {

		missionState = new MissionState( this );
		setScreen(missionState);
	}

	@Override
	public void setScreen( Screen screen ) {
		if ( this.screen != null )
			this.screen.dispose( );

		this.screen = screen;

		if ( this.screen != null )
			this.screen.show( );
	}

	@Override
	public void resize( int width, int height ) {
		screen.resize( width, height );
		super.resize( width, height );
	}

	@Override
	public void render( ) {
		if ( Global.DEBUG )
			logger.log( );

		if(playing)
			update( );
		if(playing)
			draw( );

		if(!playing)
			dispose();
	}

	public void update( ) {
		missionState.update( Gdx.graphics.getDeltaTime( ) );
	}

	public void draw( ) {
		Global.clearScreen( );
		missionState.draw( );
	}

	@Override
	public void dispose( ) {
		if ( screen != null ) {
			screen.dispose( );
			screen = null;
		}

		if ( Global.DEBUG )
			System.out.println( "Core disposed" );

		Gdx.input.setCursorCatched( false );
		Gdx.app.exit();
	}

	public void setPlaying(boolean playing){
		this.playing = playing;
	}
}