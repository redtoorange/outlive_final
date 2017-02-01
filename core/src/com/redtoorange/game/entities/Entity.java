package com.redtoorange.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.redtoorange.game.Global;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.components.TransformComponent;
import com.redtoorange.game.engine.Engine;
import com.redtoorange.game.engine.Updateable;

/**
 * Entity.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 14/Jan/2017
 */
public abstract class Entity implements Disposable {
	protected Array<Component> components = new Array<Component>( );
	protected TransformComponent transform;
	protected Engine engine;

	public Entity( Vector2 position, Engine engine ) {
		transform = new TransformComponent( this, position );
		this.engine = engine;
	}

	public Engine getEngine( ) {
		return engine;
	}

	public void update( float deltaTime ) {
		for ( int i = 0; i < components.size; i++ ) {
			if ( components.get( i ) instanceof Updateable )
				( ( Updateable ) components.get( i ) ).update( deltaTime );
		}
	}

	public <T extends Component> T getComponent( Class<? extends Component> classOfInterest ) {
		T obj = null;

		for ( Component c : components ) {
			if ( classOfInterest.isInstance( c ) )
				obj = ( T ) c;
			if ( obj != null )
				return obj;
		}

		return obj;
	}

	public TransformComponent getTransform(){
		return transform;
	}

	protected void addComponent( Component c ) {
		components.add( c );
	}

	protected void removeComponent( Component c ) {
		components.removeValue( c, true );
	}

	public Array<Component> getComponents( ) {
		return components;
	}

	@Override
	public void dispose( ) {
		if(engine != null)
			engine.removeEntity( this );

		for ( Component c : components )
			c.dispose( );

		components.clear( );

		if( Global.DEBUG)
			System.out.println( this.getClass().getSimpleName() + " disposed" );
	}
}
