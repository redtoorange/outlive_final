package com.redtoorange.game.components.rendering.sprite;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.redtoorange.game.Global;

/**
 * MapRenderComponent.java - Description
 *
 * @version 20/Jan/2017
 * @author Andrew J. McGuiness
 */
public class MapRenderComponent extends RenderComponent {
	private TiledMapRenderer mapRenderer;
	private OrthographicCamera camera;

	public MapRenderComponent( TiledMap map, float mapScale, SpriteBatch batch, OrthographicCamera camera ) {
		this.camera = camera;
		mapRenderer = new OrthogonalTiledMapRenderer( map, mapScale, batch );
	}

	@Override
	public void draw( SpriteBatch batch ) {
		mapRenderer.setView( camera );
		mapRenderer.render( );
	}

	@Override
	public void dispose( ) {
		if( Global.DEBUG)
			System.out.println( this.getClass().getSimpleName() + " disposed" );
	}
}