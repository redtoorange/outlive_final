package com.redtoorange.game.entities.characters;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.GunType;
import com.redtoorange.game.Inventory;
import com.redtoorange.game.components.PlayerGunComponent;
import com.redtoorange.game.components.input.PlayerInputComponent;
import com.redtoorange.game.components.physics.character.PlayerPhysicsComponent;
import com.redtoorange.game.components.rendering.CrosshairComponent;
import com.redtoorange.game.components.rendering.SpriteComponent;
import com.redtoorange.game.engine.Engine;
import com.redtoorange.game.screens.PlayScreen;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * Player.java - Generic player class.
 *
 * @author - Andrew M.
 * @version - 13/Jan/2017
 */

public class Player extends EntityCharacter {
	private OrthographicCamera camera;
	private Inventory ammo = new Inventory( );
	private PlayScreen playScreen;

	private PlayerInputComponent inputComponent;
	private CrosshairComponent crosshairComponent;
	private SpriteComponent spriteComponent;

	public Player( Engine engine, OrthographicCamera camera, PlayScreen playScreen, PhysicsSystem physicsSystem, Vector2 spawnPoint ) {
		super( spawnPoint, engine, physicsSystem );

		this.camera = camera;
		this.playScreen = playScreen;

		initCrosshair( );
		initSpriteComponent( );
		initInputComponent();
		initPhysicsComponent();
		initGunComponent(playScreen);
	}

	public void update( float deltaTime ) {
		super.update( deltaTime );
	}

	public void pickupAmmo( GunType type, int amount ) {
		ammo.pickup( type, amount );
	}

	public void pickupHealth( int amount) {
		health += amount;

		if(health > maxHealth)
			health = maxHealth;
	}

	public Inventory getInventoy( ) {
		return ammo;
	}

	@Override
	protected void die( ) {
		playScreen.setPlayer( null );
		dispose( );
	}

	@Override
	public float getRotation( ) {
		return spriteComponent.getRotation();
	}

	protected void initCrosshair( ) {
		crosshairComponent = new CrosshairComponent( this );
		addComponent( crosshairComponent );
	}

	protected void initGunComponent( PlayScreen playScreen){
		addComponent( new PlayerGunComponent( physicsSystem, engine, this, playScreen ) );
	}

	@Override
	protected void initSpriteComponent( ) {
		Texture temp = new Texture( "player.png" );

		Sprite sprite = new Sprite( temp );
		sprite.setSize( 1f, 1f );

		spriteComponent = new SpriteComponent( sprite, this );
		addComponent( spriteComponent );
	}

	@Override
	protected void initInputComponent() {
		inputComponent = new PlayerInputComponent( this, camera );
		addComponent( inputComponent );
	}

	@Override
	protected void initPhysicsComponent() {
		addComponent( new PlayerPhysicsComponent( physicsSystem, this ) );
	}
}