package com.redtoorange.game.gameobject.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.GunType;
import com.redtoorange.game.Inventory;
import com.redtoorange.game.components.PlayerGunComponent;
import com.redtoorange.game.components.input.PlayerInputComponent;
import com.redtoorange.game.components.physics.character.PlayerPhysicsComponent;
import com.redtoorange.game.components.rendering.sprite.CrosshairComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.states.MissionState;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * Player.java - Generic player class.
 *
 * @author - Andrew M.
 * @version - 13/Jan/2017
 */

public class Player extends GameObjectCharacter {
	private OrthographicCamera camera;
	private Inventory ammo = new Inventory( );

	private MissionState missionState;
	private SpriteComponent spriteComponent;

	public Player(GameObject parent, OrthographicCamera camera, MissionState missionState, PhysicsSystem physicsSystem, Vector2 position ) {
		super( parent, position, physicsSystem );

		this.camera = camera;
		this.missionState = missionState;

		maxHealth  = 10;
		health = 10;
	}

	@Override
	public void start( GameObject parent ) {
		initCrosshair( );
		initSpriteComponent( );
		initInputComponent( );
		initPhysicsComponent( );
		initGunComponent(missionState);

		super.start( parent );
	}

	public void update( float deltaTime ) {
		super.update( deltaTime );
	}

	public void pickupAmmo( GunType type, int amount ) {
		ammo.pickup( type, amount );
		missionState.getGunUI().setAmmoCount( ammo.remaining( type ) );
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
		Gdx.app.exit();
		missionState.setPlayer( null );
	}

	@Override
	public float getRotation( ) {
		return spriteComponent.getRotation();
	}

	protected void initCrosshair( ) {
		addComponent( new CrosshairComponent( this ) );
	}

	protected void initGunComponent( MissionState missionState){
		addComponent( new PlayerGunComponent( physicsSystem, missionState) );
	}

	@Override
	protected void initSpriteComponent( ) {
		Texture temp = new Texture( "player.png" );

		Sprite sprite = new Sprite( temp );
		sprite.setSize( 1f, 1f );

		spriteComponent = new SpriteComponent( sprite );
		addComponent( spriteComponent );
	}

	@Override
	protected void initInputComponent() {
		addComponent( new PlayerInputComponent( this, camera ) );
	}

	@Override
	protected void initPhysicsComponent() {
		addComponent( new PlayerPhysicsComponent( physicsSystem, this ) );
	}
}