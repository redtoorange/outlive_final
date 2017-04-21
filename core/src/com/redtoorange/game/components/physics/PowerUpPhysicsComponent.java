package com.redtoorange.game.components.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * PowerUpPhysicsComponent.java - Generic physics component that is attached to Ammo and Health power-ups.  Allows the
 * player collide with and detect overlaps.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class PowerUpPhysicsComponent extends PhysicsComponent {
    private GameObject powerUpGO;

    /** @param physicsSystem The physics system that is currently being used. */
    public PowerUpPhysicsComponent( PhysicsSystem physicsSystem ) {
        super( physicsSystem );
    }

    /**
     * Called before the game starts but after the constructor.
     *
     * @param parent The parent GameObject
     */
    @Override
    public void start( GameObject parent ) {
        powerUpGO = parent;
        initPhysics( parent.getComponent( SpriteComponent.class ) );
    }

    /**
     * Initialize the physics body based on the size, shape and position of the parent's sprite.
     *
     * @param parentSpriteComponent The spriteComponent that will be used to build the body.
     */
    private void initPhysics( SpriteComponent parentSpriteComponent ) {
        body = Box2DFactory.createBody( physicsSystem, parentSpriteComponent.getBoundingBox(), BodyDef.BodyType.DynamicBody, 10f, 0, 0, true, false );

        body.setUserData( powerUpGO );

        Filter f = body.getFixtureList().first().getFilterData();
        f.categoryBits = Global.AMMO;
        body.getFixtureList().first().setFilterData( f );

        body.setTransform( parentSpriteComponent.getCenter(), ( float ) Math.toRadians( parentSpriteComponent.getRotation() ) );
    }
}
