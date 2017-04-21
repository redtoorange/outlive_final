package com.redtoorange.game.components.physics.character;

import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.enemies.Enemy;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.systems.sound.SoundManager;

/**
 * EnemyPhysicsComponent.java - A specialized physics component for enemies.  This handles most of the same work as the
 * other physics components, but it has special filters to allow collision with bullets and the player.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class EnemyPhysicsComponent extends CharacterPhysicsComponent {
    /**
     * @param physicsSystem The world's physics system.
     * @param enemy         The gameObject this is attached to.
     */
    public EnemyPhysicsComponent( PhysicsSystem physicsSystem, Enemy enemy ) {
        super( physicsSystem, enemy, 1f, 10f, 10f, 5f );
    }

    /** @param owner The owning gameObject. */
    @Override
    public void start( GameObject owner ) {
        super.start( owner );

        Filter f = body.getFixtureList().first().getFilterData();
        f.categoryBits = SoundManager.Global.ENEMY;
        f.maskBits = SoundManager.Global.WALL | SoundManager.Global.PLAYER | SoundManager.Global.BULLET_LIVE | SoundManager.Global.LIGHT | SoundManager.Global.ENEMY;
        body.getFixtureList().first().setFilterData( f );
    }
}
