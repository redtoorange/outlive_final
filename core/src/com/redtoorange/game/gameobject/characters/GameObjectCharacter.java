package com.redtoorange.game.gameobject.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.systems.PhysicsSystem;

public abstract class GameObjectCharacter extends GameObject {
	protected PhysicsSystem physicsSystem;

	protected int maxHealth = 1;
	protected int health = maxHealth;

	public GameObjectCharacter( GameObject parent, Vector2 position, PhysicsSystem physicsSystem ) {
		super( parent, position );
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