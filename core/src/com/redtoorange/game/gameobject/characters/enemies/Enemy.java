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

/**
 * Enemy.java - The primary antagonist in the game are the enemies.  Right now, this class is implemented as more of a
 * prototype to represent an enemy.  It can be modified to load in other enemy textures with different stats with only
 * minor edits.  The enemy will attached to a player which will be tracked by the enemy's input component.  If the player
 * gets too closer, the enemy will attempt to chase.  The enemy will deal damage to the player on contact and will take
 * damage from bullets.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class Enemy extends GameObjectCharacter {
    /** The player that this enemy should chase or attack. */
    protected Player player;
    private SpriteComponent spriteComponent;
    private int damage = 1;

    /**
     * @param parent        The player's parent, usually the sceneRoot.
     * @param physicsSystem The world's physics system.
     * @param spawnPosition The starting position of the enemy.
     * @param player        The player that this enemy should be concerned it.
     */
    public Enemy( GameObject parent, PhysicsSystem physicsSystem, Vector2 spawnPosition, Player player ) {
        this( parent, physicsSystem, spawnPosition );
        this.player = player;
    }

    /**
     * @param parent        The player's parent, usually the sceneRoot.
     * @param physicsSystem The world's physics system.
     * @param spawnPosition The starting position of the enemy.
     */
    public Enemy( GameObject parent, PhysicsSystem physicsSystem, Vector2 spawnPosition ) {
        super( parent, spawnPosition, physicsSystem );
        initSpriteComponent();
        initInputComponent();
        initPhysicsComponent();
    }

    /**
     * Load the enemy sprite from disk into a texture.  This is to make it so all the enemies will just use a single texture
     * in VRAM.
     *
     * @param spawnPoint The starting position of the enemy.
     * @return The finished Sprite.
     */
    protected Sprite loadEnemySprite( Vector2 spawnPoint ) {
        Texture temp = new Texture( "zombie.png" );

        Sprite sprite = new Sprite( temp );
        sprite.setSize( 1f, 1f );

        return sprite;
    }

    /** @return Get the player this enemy is v with. */
    public Player getPlayer() {
        return player;
    }

    /** @param player Set the player this enemy should be concerned with. */
    public void setPlayer( Player player ) {
        this.player = player;
    }

    /**
     * Deal damage to the character, if it's health reaches 0, they will die.  The enemy's tint will change to represent
     * taking damage.
     *
     * @param amount Amount of damage the character has received.
     */
    @Override
    public void takeDamage( int amount ) {
        super.takeDamage( amount );
        spriteComponent.setColor( ( ( float ) health / ( float ) maxHealth ), 0, 0, 1 );
    }

    /** @return Get the amount of damage this enemy deals. */
    public int getDamage() {
        return damage;
    }

    /** Called when the enemy reaches health 0.  Notifies the player that he has killed an enemy. */
    @Override
    protected void die() {
        player.killedEnemy();
        dispose();
    }

    /** @return get the sprite's current rotation. */
    @Override
    public float getRotation() {
        return spriteComponent.getRotation();
    }

    /** Initialize the sprite component. */
    @Override
    protected void initSpriteComponent() {
        spriteComponent = new SpriteComponent( loadEnemySprite( transform.getPosition() ) );
        addComponent( spriteComponent );
    }

    /** Initialize the input component. */
    @Override
    protected void initInputComponent() {
        addComponent( new EnemyInputComponent( this, 5f ) );
    }

    /** Initialize the physics component. */
    @Override
    protected void initPhysicsComponent() {
        addComponent( new EnemyPhysicsComponent( physicsSystem, this ) );
    }
}
