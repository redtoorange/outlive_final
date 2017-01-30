package com.redtoorange.game.entities.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.input.EnemyInputComponent;
import com.redtoorange.game.components.physics.character.EnemyPhysicsComponent;
import com.redtoorange.game.components.rendering.SpriteComponent;
import com.redtoorange.game.engine.Engine;
import com.redtoorange.game.entities.characters.EntityCharacter;
import com.redtoorange.game.entities.characters.Player;
import com.redtoorange.game.systems.PhysicsSystem;

public class Enemy extends EntityCharacter {
	protected Player player;
	private SpriteComponent spriteComponent;
	private int damage = 5;

	public Enemy( PhysicsSystem physicsSystem, Engine engine, Vector2 spawnPosition, Player player ) {
		this( physicsSystem, engine, spawnPosition );

		this.player = player;
	}

	public Enemy( PhysicsSystem physicsSystem, Engine engine, Vector2 spawnPosition ) {
		super( spawnPosition, engine, physicsSystem );

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
		spriteComponent = new SpriteComponent( loadEnemySprite( transform.getPosition() ), this );
		addComponent( spriteComponent );
	}

	@Override
	protected void initInputComponent() {
		addComponent(  new EnemyInputComponent( this ) );
	}

	@Override
	protected void initPhysicsComponent() {
		addComponent( new EnemyPhysicsComponent( physicsSystem, this ) );
	}
}
