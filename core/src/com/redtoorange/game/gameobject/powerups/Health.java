package com.redtoorange.game.gameobject.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.GameObjectCharacter;
import com.redtoorange.game.gameobject.characters.Player;
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

	public Health( GameObject parent, Vector2 position, PhysicsSystem physicsSystem ) {
		super( parent, position, new Texture( "chilidog.png" ), physicsSystem );

		sm = new SoundManager( );
		sm.addSound( "eating", new SoundEffect( "sounds/eating.wav", 0.10f ) );
	}

	@Override
	public void absorbed( GameObjectCharacter c ) {
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
