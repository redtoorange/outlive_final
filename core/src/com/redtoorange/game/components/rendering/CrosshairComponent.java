package com.redtoorange.game.components.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.redtoorange.game.Global;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.components.input.PlayerInputComponent;
import com.redtoorange.game.engine.PostLightingDraw;
import com.redtoorange.game.engine.Updateable;
import com.redtoorange.game.entities.Entity;

/**
 * CrosshairComponent.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 29/Jan/2017
 */
public class CrosshairComponent extends Component implements Updateable, PostLightingDraw{
	private Sprite crossHair;
	private PlayerInputComponent inputComponent;

	public CrosshairComponent( Entity parent ) {
		super( parent );

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

	@Override
	public void postLightingDraw( SpriteBatch batch ) {
		crossHair.draw( batch );
	}

	@Override
	public void dispose() {
		if( Global.DEBUG)
			System.out.println( this.getClass().getSimpleName() + " disposed" );
	}
}
