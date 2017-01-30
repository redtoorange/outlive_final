package com.redtoorange.game.entities.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.redtoorange.game.engine.Engine;
import com.redtoorange.game.entities.Entity;
import com.redtoorange.game.systems.PhysicsSystem;

public abstract class EntityCharacter extends Entity {
	protected PhysicsSystem physicsSystem;

	protected int maxHealth = 10;
	protected int health = maxHealth;

	public EntityCharacter( Vector2 position, Engine engine, PhysicsSystem physicsSystem ) {
		super( position, engine );
		this.physicsSystem = physicsSystem;
	}

	public Vector3 getPosition3D( ) {
		return new Vector3( transform.getPosition().x, transform.getPosition().y, 0 );
	}

	public void takeDamage( int amount ) {
		health -= amount;
		if ( health <= 0 ) {
			die( );
		}
	}

	public abstract float getRotation( );

	protected abstract void initSpriteComponent( );
	protected abstract void initInputComponent();
	protected abstract void initPhysicsComponent();

	protected abstract void die( );
}