package com.redtoorange.game.gameobject.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.physics.PowerUpPhysicsComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.GameObjectCharacter;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 23/Jan/2017
 */
public abstract class PowerUp extends GameObject {
	public PowerUp( GameObject parent, Vector2 position, Texture texture, PhysicsSystem physicsSystem ) {
		super( parent, position );

		initSpriteComponent( position, texture );
		initPhysicsComponent( physicsSystem );
	}

	private void initPhysicsComponent( PhysicsSystem physicsSystem ) {
		addComponent( new PowerUpPhysicsComponent( physicsSystem ) );
	}

	private void initSpriteComponent( Vector2 position, Texture texture ) {
		Sprite sprite = new Sprite( texture );
		sprite.setSize( 0.5f, 0.5f );
		sprite.setCenter( position.x, position.y );
		sprite.setOriginCenter( );

		addComponent( new SpriteComponent( sprite ) );
	}

	public void absorbed( GameObjectCharacter c ) {
		dispose( );
	}
}