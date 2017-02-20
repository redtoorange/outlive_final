package com.redtoorange.game.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.redtoorange.game.Global;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * PhysicsComponent.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 20/Jan/2017
 */
public abstract class PhysicsComponent extends Component {
	protected Body body;
	protected PhysicsSystem physicsSystem;
	protected float speed;


	public PhysicsComponent( PhysicsSystem physicsSystem ) {
		this.physicsSystem = physicsSystem;
	}

	/**
	 * Retrieve the Body position, assuming it is not-null.
	 *
	 * @return The center position of the Box2D Body attached to this Component.
	 */
	public Vector2 getBodyPosition() {
		Vector2 position = new Vector2( );

		if ( body != null )
			position.set( body.getPosition( ) );

		return position;
	}

	public void destroy() {
		if ( body != null && physicsSystem != null ) {
			physicsSystem.destroyBody( body );
			body = null;
		}
	}

	@Override
	public void dispose() {
		destroy( );

		if ( Global.DEBUG )
			System.out.println( this.getClass( ).getSimpleName( ) + " disposed" );
	}
}
