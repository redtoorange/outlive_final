package com.redtoorange.game.gameobject.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.GameObjectCharacter;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.systems.GunType;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.systems.sound.SoundEffect;
import com.redtoorange.game.systems.sound.SoundManager;

/**
 * Ammo.java - An ammunition pack that will add a random amount of ammunition to the player's inventory.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class Ammo extends PowerUp {
    /** Which gun the ammunition can be used in. */
    private GunType type = GunType.REVOLVER;
    /** How much ammunition does this pack restore. */
    private int amount;
    private SoundManager sm;

    /**
     * @param parent        The owning gameObject, usually the scene root.
     * @param position      The position to spawn the power-up at.
     * @param physicsSystem The world's physics system.
     */
    public Ammo( GameObject parent, Vector2 position, PhysicsSystem physicsSystem ) {
        super( parent, position, new Texture( "weapons/revolver/bullets.png" ), physicsSystem );

        sm = new SoundManager();
        sm.addSound( "ammopickup", new SoundEffect( "sounds/ammopickup.wav", 0.10f ) );
        amount = MathUtils.random( 1, 3 );
    }

    /**
     * Apply the ammo to the player's inventory and play a sound to signal the food was consumed.
     *
     * @param character The character that the power-up should be applied to.
     */
    @Override
    public void absorbed( GameObjectCharacter character ) {
        System.out.println( "Player pickedup " + amount + " " + type + " bullets." );
        sm.playSound( "ammopickup" );
        ( ( Player ) character ).pickupAmmo( type, amount );
        super.absorbed( character );
    }

    /** @param deltaTime The time since the last update. */
    @Override
    public void update( float deltaTime ) {
        super.update( deltaTime );
        sm.update( deltaTime );
    }
}
