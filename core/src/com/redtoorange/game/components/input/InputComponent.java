package com.redtoorange.game.components.input;

import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;

/**
 * InputComponent.java - Generic InputComponent that would be used to control a GameObject that can receive input.  This
 * would be the player controlling their character or the enemy's AI.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public abstract class InputComponent extends Component {
    /** The current input this component is receiving. */
    protected Vector2 deltaInput = new Vector2();
    /** The attached sprite component, used by super classes to change facing. */
    protected SpriteComponent sc;


    /** @param owner The GameObject this Component is attached to. */
    public InputComponent( GameObject owner ) {
        sc = owner.getComponent( SpriteComponent.class );
    }

    /** @return The current deltaInput of this component. */
    public Vector2 getDeltaInput() {
        return deltaInput;
    }
}
