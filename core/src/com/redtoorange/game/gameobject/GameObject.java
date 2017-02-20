package com.redtoorange.game.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.redtoorange.game.Global;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.components.TransformComponent;

/**
 * GameObject.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 14/Jan/2017
 */
public abstract class GameObject implements Disposable {
	protected GameObject parent;

	protected  Array<GameObject > children = new Array<GameObject >(  );
	protected Array<Component> components = new Array<Component>( );

	protected TransformComponent transform;

	public GameObject( GameObject parent, Vector2 position ) {
		transform = new TransformComponent( this, position );
		this.parent = parent;
	}

	public void start( GameObject parent ){
		for( Component c : components)
			c.start( this );

		for( GameObject go : children )
			go.start( this );
	}

	public void update( float deltaTime ) {
		for( Component c : components)
			c.update( deltaTime );

		for( GameObject go : children )
			go.update( deltaTime );
	}

	public void draw( SpriteBatch batch ){
		for( Component c : components)
			c.draw( batch );

		for( GameObject go : children )
			go.draw( batch );
	}

	public GameObject getParent(){
		return parent;
	}

	public void setParent( GameObject parent ) {
		this.parent = parent;
	}

	public Array< GameObject > getChildren() {
		return children;
	}

	public void addChild( GameObject child ){
		children.add( child );
	}

	public void removeChild( GameObject child ){
		children.removeValue( child, true );
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
		for( GameObject go : children )
			go.dispose();

		children.clear();

		for( Component c : components )
			c.dispose();

		components.clear( );

		if( Global.DEBUG)
			System.out.println( this.getClass().getSimpleName() + " disposed" );
	}
}
