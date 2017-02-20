package com.redtoorange.game.components.input;

import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;

/**
 * InputComponent.java - Description
 *
 * @author
 * @version 27/Jan/2017
 */
public abstract class InputComponent extends Component {
	protected Vector2 deltaInput = new Vector2(  );
	protected SpriteComponent sc;


	public InputComponent( GameObject owner ) {
		sc = owner.getComponent( SpriteComponent.class );
	}

	public Vector2 getDeltaInput(){
		return deltaInput;
	}
}
