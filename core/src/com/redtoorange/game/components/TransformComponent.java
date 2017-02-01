package com.redtoorange.game.components;

import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.Global;
import com.redtoorange.game.entities.Entity;

/**
 * TransformComponent.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 29/Jan/2017
 */
public class TransformComponent extends Component {
	private Vector2 position;

	public TransformComponent( Entity parent ) {
		super( parent );
		position = new Vector2( 0,0 );
	}

	public TransformComponent( Entity parent, Vector2 position ) {
		super( parent );
		this.position = new Vector2( position );
	}

	public void setPosition( Vector2 position ){
		this.position.set( position );
	}

	public Vector2 getPosition(){
		return position;
	}

	public void translatePosition( Vector2 translation ){
		position.add( translation );
	}

	@Override
	public void dispose() {
		position = null;

		if( Global.DEBUG)
			System.out.println( this.getClass().getSimpleName() + " disposed" );
	}
}
