package com.redtoorange.game.systems.sound;

import com.badlogic.gdx.utils.ArrayMap;

/**
 * SoundManager.java -
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class SoundManager {
	protected ArrayMap<String, SoundEffect> sounds;

	public SoundManager( ) {
		sounds = new ArrayMap<String, SoundEffect>( );
	}

	public void playSound( String name ) {
		if ( sounds.containsKey( name ) )
			sounds.get( name ).play( );
	}

	public void update( float deltaTime ) {
		for ( SoundEffect se : sounds.values( ) ) {
			se.update( deltaTime );
		}
	}

	public void addSound( String name, SoundEffect soundEffect ) {
		sounds.put( name, soundEffect );
	}

	public SoundEffect getSoundEffect( String name ) {
		return sounds.get( name );
	}



}