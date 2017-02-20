package com.redtoorange.game.components.rendering.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.redtoorange.game.Global;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.components.input.PlayerInputComponent;
import com.redtoorange.game.gameobject.GameObject;

/**
 * CrosshairComponent.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 29/Jan/2017
 */
public class CrosshairComponent extends Component {
	private Sprite crossHair;
	private PlayerInputComponent inputComponent;
	private GameObject parent;


	public CrosshairComponent( GameObject parent ) {
		this.parent = parent;
	}

	@Override
	public void start( GameObject parent ) {
		Texture cross = new Texture( "crosshair.png" );
		crossHair = new Sprite( cross );
		crossHair.setPosition( 0f, 0f );
		crossHair.setSize( 1f, 1f );
	}

	@Override
	public void update( float deltaTime ) {
		if(inputComponent == null)
			inputComponent = parent.getComponent( PlayerInputComponent.class );

		Vector3 mousePos = inputComponent.getMousePosition();
		crossHair.setCenter( mousePos.x, mousePos.y );
	}

	//TODO: removed
	public void postLightingDraw( SpriteBatch batch ) {
		crossHair.draw( batch );
	}

	@Override
	public void dispose() {
		if( Global.DEBUG)
			System.out.println( this.getClass().getSimpleName() + " disposed" );
	}

	@Override
	public void draw( SpriteBatch batch ) {
		crossHair.draw( batch );
	}
}
