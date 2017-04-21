package com.redtoorange.game.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * PhysicsComponent.java - Generic Physics component that is used by all GameObjects that require physical interaction
 * with the world or each-other.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public abstract class PhysicsComponent extends Component {
    /** The body of this gameObject. */
    protected Body body;
    /** The state's physicsSystem. */
    protected PhysicsSystem physicsSystem;
    /** How fast will this body move. */
    protected float speed;


    /** @param physicsSystem The current state's physicsSystem. */
    public PhysicsComponent( PhysicsSystem physicsSystem ) {
        this.physicsSystem = physicsSystem;
    }

    /** @return The center position of the Box2D Body attached to this Component. */
    public Vector2 getBodyPosition() {
        Vector2 position = new Vector2();

        if ( body != null )
            position.set( body.getPosition() );

        return position;
    }

    /** Destroys this body from from the physicsSystem. */
    public void destroy() {
        if ( body != null && physicsSystem != null ) {
            physicsSystem.destroyBody( body );
            body = null;
        }
    }

    /** Destroy this body. */
    @Override
    public void dispose() {
        destroy();

        if ( Global.DEBUG )
            System.out.println( this.getClass().getSimpleName() + " disposed" );
    }
}
