package com.redtoorange.game.gameobject.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.GunType;
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
public class Ammo extends PowerUp {
	private GunType type = GunType.REVOLVER;
	private int amount;
	private SoundManager sm;

	public Ammo( GameObject parent, Vector2 position, PhysicsSystem physicsSystem ) {
		super( parent, position, new Texture( "weapons/revolver/bullets.png" ), physicsSystem );

		sm = new SoundManager( );
		sm.addSound( "ammopickup", new SoundEffect( "sounds/ammopickup.wav", 0.10f ) );
		amount = MathUtils.random( 1, 3 );
	}

	@Override
	public void absorbed( GameObjectCharacter c ) {
		System.out.println( "Player pickedup " + amount + " " + type + " bullets." );
		sm.playSound( "ammopickup" );
		( ( Player ) c ).pickupAmmo( type, amount );
		super.absorbed( c );
	}

	@Override
	public void update( float deltaTime ) {
		super.update( deltaTime );
		sm.update( deltaTime );
	}
}
