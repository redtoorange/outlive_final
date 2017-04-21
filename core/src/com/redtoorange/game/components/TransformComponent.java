package com.redtoorange.game.components;

import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.systems.Global;

/**
 * TransformComponent.java - Universal component for all GameObjects.  Stores the current location on the gameWorld.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class TransformComponent extends Component {
    private Vector2 position;

    /**
     * @param parent   The owning GameObject.
     * @param position The starting position of this Transform.
     */
    public TransformComponent( GameObject parent, Vector2 position ) {
        this.position = new Vector2( position );
    }

    /** @return The current position of this GameObject. */
    public Vector2 getPosition() {
        return position;
    }

    /** @param position The position to set this GameObject to. */
    public void setPosition( Vector2 position ) {
        this.position.set( position );
    }

    /** Destroy this Transform. */
    @Override
    public void dispose() {
        position = null;

        if ( Global.DEBUG )
            System.out.println( this.getClass().getSimpleName() + " disposed" );
    }
}
