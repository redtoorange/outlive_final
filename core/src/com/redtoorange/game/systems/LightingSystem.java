package com.redtoorange.game.systems;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

/**
 * LightingSystem.java -
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class LightingSystem {
	private RayHandler rayHandler;
	private Color ambientLight = new Color( 0.1f, 0.1f, 0.1f, 0.1f );

	public LightingSystem( World world ){
		rayHandler = new RayHandler( world );
		rayHandler.setAmbientLight( ambientLight );
	}

	public void draw( OrthographicCamera camera ) {
		rayHandler.setCombinedMatrix( camera );
		rayHandler.updateAndRender( );
	}

	public RayHandler getRayHandler( ) {
		return rayHandler;
	}

	public void dispose(){
		if ( rayHandler != null )
			rayHandler.dispose( );
	}
}
