package com.redtoorange.game.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.redtoorange.game.Global;
import com.redtoorange.game.entities.Entity;
import com.redtoorange.game.systems.RenderingSystem;

public class Engine implements Disposable {
	private RenderingSystem renderingSystem;

	private Array<EntityListener> entityListeners;
	private Array<Entity> entities;

	public Engine( ) {
		renderingSystem = new RenderingSystem();

		entityListeners = new Array<EntityListener>(  );
		entityListeners.add( renderingSystem );

		entities = new Array<Entity>( );
	}

	public void update( float deltaTime ) {
		for ( Entity e : entities ) {
			e.update( deltaTime );
		}
	}

	public void render( SpriteBatch batch ) {
		renderingSystem.render( batch );
	}

	public void postLighting( SpriteBatch batch ) {
		renderingSystem.postLightingRender( batch );
	}

	/*
	 * Adds an Entity to the engine.  All attached Systems will parse the entities components for compatible types.
	 * Comparison for equivalence is always by reference.
	 *
	 * @return		Returns -1 if the Entity is already present and 0 if it was added successfully.
	 * @param	e	A subclass of Entity that needs to be added to the Engine.
	 */
	public int addEntity( Entity e ) {
		int result = Global.PRESENT;

		if ( !entities.contains( e, true ) ) {
			result = Global.SUCCESS;

			entities.add( e );
			entityAdded( e );
		}

		return result;
	}

	/*
	 * Removes an Entity from the engine.  Dispose will NOT be called on the entity.  If this is the only reference, you will
	 * lose access. Comparison for equivalence is always by reference.
	 *
	 * @return		Returns -2 if the Entity is not present and 0 if it was removed successfully.
	 * @param	e	A subclass of Entity that needs to be removed from the engine.
	 */
	public int removeEntity( Entity e ) {
		int result = Global.FAILURE;

		if ( entities.contains( e, true ) ) {
			result = Global.SUCCESS;

			entities.removeValue( e, true );
			entityRemoved( e );
		}

		return result;
	}

	public void dispose( ) {
		renderingSystem.dispose();
		entityListeners.clear();

		for(int i = entities.size-1; i >= 0; i--){
			if(entities.get( i ) != null) {
				entities.get( i ).dispose( );
			}
		}

		entities.clear();

		if(Global.DEBUG)
			System.out.println( "Engine disposed" );
	}

	private void entityAdded( Entity e){
		for(EntityListener el : entityListeners)
			el.entityAdded( e );
	}

	private void entityRemoved(Entity e){
		for(EntityListener el : entityListeners)
			el.entityRemoved( e );
	}

	public void addSystem( EntityListener system ){
		entityListeners.add( system );

		for(Entity e : entities)
			system.entityAdded( e );
	}

}
