package com.redtoorange.game.gameobject.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.input.EnemyInputComponent;
import com.redtoorange.game.components.physics.character.EnemyPhysicsComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.GameObjectCharacter;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.systems.PhysicsSystem;

public class Enemy extends GameObjectCharacter {
	protected Player player;
	private SpriteComponent spriteComponent;
	private int damage = 5;

	public Enemy( GameObject parent, PhysicsSystem physicsSystem, Vector2 spawnPosition, Player player ) {
		this( parent, physicsSystem, spawnPosition );
		this.player = player;
	}

	public Enemy( GameObject parent, PhysicsSystem physicsSystem, Vector2 spawnPosition ) {
		super( parent, spawnPosition, physicsSystem );
		initSpriteComponent( );
		initInputComponent();
		initPhysicsComponent();
	}


	protected Sprite loadEnemySprite( Vector2 spawnPoint ) {
		Texture temp = new Texture( "zombie.png" );

		Sprite sprite = new Sprite( temp );
		sprite.setSize( 1f, 1f );

		return sprite;
	}

	public void setPlayer( Player player ) {
		this.player = player;
	}

	public Player getPlayer(){
		return player;
	}

	@Override
	public void takeDamage( int amount ) {
		super.takeDamage( amount );
		spriteComponent.setColor( ( ( float ) health / ( float ) maxHealth ), 0, 0, 1 );
	}

	public int getDamage( ){
		return damage;
	}

	@Override
	protected void die( ) {
		dispose( );
	}

	@Override
	public float getRotation( ) {
		return spriteComponent.getRotation();
	}

	@Override
	protected void initSpriteComponent( ) {
		spriteComponent = new SpriteComponent( loadEnemySprite( transform.getPosition() ) );
		addComponent( spriteComponent );
	}

	@Override
	protected void initInputComponent() {
		addComponent(  new EnemyInputComponent( this, 5f ) );
	}

	@Override
	protected void initPhysicsComponent() {
		addComponent( new EnemyPhysicsComponent( physicsSystem, this ) );
	}
}
