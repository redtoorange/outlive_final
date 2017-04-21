package com.redtoorange.game.gameobject.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.GameObjectCharacter;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.systems.sound.SoundEffect;
import com.redtoorange.game.systems.sound.SoundManager;

/**
 * Health.java - A health pack that will heal a small portion of the player's health.  If the player is already full,
 * then the power-up is wasted.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class Health extends PowerUp {
    /** How much health to restore. */
    private int amount = 1;
    private SoundManager sm;

    /**
     * @param parent        The owning gameObject, usually the scene root.
     * @param position      The position to spawn the power-up at.
     * @param physicsSystem The world's physics system.
     */
    public Health( GameObject parent, Vector2 position, PhysicsSystem physicsSystem ) {
        super( parent, position, new Texture( "chilidog.png" ), physicsSystem );

        sm = new SoundManager();
        sm.addSound( "eating", new SoundEffect( "sounds/eating.wav", 0.10f ) );
    }

    /**
     * Apply the health to the player's health bar and play a sound to signal the food was consumed.
     *
     * @param character The character that the power-up should be applied to.
     */
    @Override
    public void absorbed( GameObjectCharacter character ) {
        sm.playSound( "eating" );
        ( ( Player ) character ).pickupHealth( amount );
        super.absorbed( character );
    }

    /** @param deltaTime The time since the last update. */
    @Override
    public void update( float deltaTime ) {
        super.update( deltaTime );
        sm.update( deltaTime );
    }
}
