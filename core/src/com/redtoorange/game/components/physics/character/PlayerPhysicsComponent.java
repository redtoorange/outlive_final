package com.redtoorange.game.components.physics.character;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * PlayerPhysicsComponent.java - A physics component that is specialized to be attached to a player.  This simplifies
 * collision detection between different physics bodies.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class PlayerPhysicsComponent extends CharacterPhysicsComponent {
    /**
     * @param physicsSystem The world's physicsSystem.
     * @param player        The player that this component is attached to.
     */
    public PlayerPhysicsComponent( PhysicsSystem physicsSystem, Player player ) {
        super( physicsSystem, player, 5f, 10f, 10f,
                5f );
    }

    /** @param owner The owning GameObject. */
    @Override
    public void start( GameObject owner ) {
        super.start( owner );

        Filter f = body.getFixtureList().first().getFilterData();
        f.categoryBits = Global.PLAYER;
        f.maskBits = Global.WALL | Global.ENEMY | Global.AMMO;
        body.getFixtureList().first().setFilterData( f );
    }

    /** @return The player's body. */
    public Body getBody() {
        return body;
    }
}