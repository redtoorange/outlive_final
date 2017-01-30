package com.redtoorange.game.entities.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.engine.Engine;
import com.redtoorange.game.entities.characters.EntityCharacter;
import com.redtoorange.game.entities.characters.Player;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.systems.sound.SoundEffect;
import com.redtoorange.game.systems.sound.SoundManager;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 23/Jan/2017
 */
public class Health extends PowerUp {
	private int amount = 10;
	private SoundManager sm;

	public Health( Vector2 position, Engine engine, PhysicsSystem physicsSystem ) {
		super( engine, position, new Texture( "chilidog.png" ), physicsSystem );

		sm = new SoundManager( );
		sm.addSound( "eating", new SoundEffect( "sounds/eating.wav", 0.10f ) );
	}

	@Override
	public void absorbed( EntityCharacter c ) {
		System.out.println( "Player pickedup " + amount + " health." );
		sm.playSound( "eating" );
		( ( Player ) c ).pickupHealth( amount );
		super.absorbed( c );
	}

	@Override
	public void update( float deltaTime ) {
		super.update( deltaTime );
		sm.update( deltaTime );
	}
}
