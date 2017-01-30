package com.redtoorange.game.entities.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.physics.PowerUpPhysicsComponent;
import com.redtoorange.game.components.rendering.SpriteComponent;
import com.redtoorange.game.engine.Engine;
import com.redtoorange.game.entities.Entity;
import com.redtoorange.game.entities.characters.EntityCharacter;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 23/Jan/2017
 */
public abstract class PowerUp extends Entity {
	public PowerUp( Engine engine, Vector2 position, Texture texture, PhysicsSystem physicsSystem ) {
		super( position, engine );

		initSpriteComponent( position, texture );
		initPhysicsComponent( physicsSystem );
	}

	private void initPhysicsComponent( PhysicsSystem physicsSystem ) {
		addComponent( new PowerUpPhysicsComponent( physicsSystem, this, getComponent( SpriteComponent.class ) ) );
	}

	private void initSpriteComponent( Vector2 position, Texture texture ) {
		Sprite sprite = new Sprite( texture );
		sprite.setSize( 0.5f, 0.5f );
		sprite.setCenter( position.x, position.y );
		sprite.setOriginCenter( );

		addComponent( new SpriteComponent( sprite, this ) );
	}

	public void absorbed( EntityCharacter c ) {
		dispose( );
	}
}