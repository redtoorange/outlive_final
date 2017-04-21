package com.redtoorange.game.systems.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * SoundEffect.java -
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class SoundEffect {
	private Sound sound;
	private float length;
	private float currentTime = 0;
	private boolean playing = false;

	public SoundEffect( String fileName, float length ) {
		this.length = length;

		sound = Gdx.audio.newSound( Gdx.files.internal( fileName ) );
	}

	public void update( float deltaTime ) {
		if ( playing ) {
			currentTime += deltaTime;
			if ( currentTime >= length ) {
				playing = false;
				currentTime = 0;
			}
		}
	}

	public boolean isPlaying( ) {
		return playing;
	}

	public void play( ) {
		playing = true;
		sound.play( );
	}
}
