package com.redtoorange.game.gameobject.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.PlayerGunComponent;
import com.redtoorange.game.components.input.PlayerInputComponent;
import com.redtoorange.game.components.physics.character.PlayerPhysicsComponent;
import com.redtoorange.game.components.rendering.sprite.CrosshairComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.states.MissionState;
import com.redtoorange.game.systems.GunType;
import com.redtoorange.game.systems.Inventory;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * Player.java - A GameObject that represents the player in the gameWorld.  He has a collection of components that allow
 * him to interact with power-up, fire bullets and take damage.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class Player extends GameObjectCharacter {
    private OrthographicCamera camera;
    private Inventory ammo = new Inventory();
    private MissionState missionState;
    private SpriteComponent spriteComponent;

    /**
     * @param parent        The player's parent, usually the sceneRoot.
     * @param camera        The OrthoCamera that should follow the player.
     * @param missionState  The mission state that controls the game.
     * @param physicsSystem The world's physics system.
     * @param position      The starting position of the player.
     */
    public Player( GameObject parent, OrthographicCamera camera, MissionState missionState, PhysicsSystem physicsSystem, Vector2 position ) {
        super( parent, position, physicsSystem );

        this.camera = camera;
        this.missionState = missionState;

        maxHealth = 10;
        health = 10;
        missionState.getHealthUI().setHealthAmount( health, maxHealth );
    }

    /**
     * Initialize all the components.
     *
     * @param parent The player's parent, usually the sceneRoot.
     */
    @Override
    public void start( GameObject parent ) {
        initCrosshair();
        initSpriteComponent();
        initInputComponent();
        initPhysicsComponent();
        initGunComponent( missionState );

        super.start( parent );
    }


    /**
     * @param type   Type of ammunition picked up.
     * @param amount Amount of ammo picked up.
     */
    public void pickupAmmo( GunType type, int amount ) {
        ammo.pickup( type, amount );
        missionState.getGunUI().setAmmoCount( ammo.remaining( type ) );
    }

    /** @param amount The amount of health the player picked up. */
    public void pickupHealth( int amount ) {
        health += amount;

        if ( health > maxHealth )
            health = maxHealth;

        missionState.getHealthUI().setHealthAmount( health, maxHealth );
    }

    /** @return get the player's current inventory. */
    public Inventory getInventoy() {
        return ammo;
    }

    /** The player has died, gameover. */
    @Override
    protected void die() {
        Gdx.app.exit();
        missionState.setPlayer( null );
    }

    /** @return get the sprite and player's current rotation. */
    @Override
    public float getRotation() {
        return spriteComponent.getRotation();
    }

    /**
     * The player has taken damage from some source.  Update the UI.
     *
     * @param amount Amount of damage taken.
     */
    @Override
    public void takeDamage( int amount ) {
        super.takeDamage( amount );
        missionState.getHealthUI().setHealthAmount( health, maxHealth );
    }

    /** Initialize the crossHair component. */
    protected void initCrosshair() {
        addComponent( new CrosshairComponent( this ) );
    }

    /** Initialize the gun component. */
    protected void initGunComponent( MissionState missionState ) {
        addComponent( new PlayerGunComponent( physicsSystem, missionState ) );
    }

    /** Initialize the sprite component. */
    @Override
    protected void initSpriteComponent() {
        Texture temp = new Texture( "player.png" );

        Sprite sprite = new Sprite( temp );
        sprite.setSize( 1f, 1f );

        spriteComponent = new SpriteComponent( sprite );
        addComponent( spriteComponent );
    }

    /** Initialize the input component. */
    @Override
    protected void initInputComponent() {
        addComponent( new PlayerInputComponent( this, camera ) );
    }

    /** Initialize the physics component. */
    @Override
    protected void initPhysicsComponent() {
        addComponent( new PlayerPhysicsComponent( physicsSystem, this ) );
    }

    /** The player killed someone, notify the gameState. */
    public void killedEnemy() {
        missionState.enemyKilled();
    }

    /**
     * Set the player's health.
     *
     * @param current How much health he has now.
     * @param max     His max health.
     */
    public void setHealth( int current, int max ) {
        health = current;
        maxHealth = max;

        missionState.getHealthUI().setHealthAmount( health, maxHealth );
    }

    /** @param amount Set the amount of ammo the player has. */
    public void setAmmo( int amount ) {
        ammo.setAmount( GunType.REVOLVER, amount );
        missionState.getGunUI().setAmmoCount( ammo.remaining( GunType.REVOLVER ) );
    }

    /** @return Get how much health the player has. */
    public int getHealth() {
        return health;
    }

    /** @return Get the player's max health. */
    public int getMaxHealth() {
        return maxHealth;
    }
}