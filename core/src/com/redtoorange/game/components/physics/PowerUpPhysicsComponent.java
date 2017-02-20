package com.redtoorange.game.components.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.Global;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 23/Jan/2017
 */
public class PowerUpPhysicsComponent extends PhysicsComponent {
	private GameObject powerUpGO;

	public PowerUpPhysicsComponent( PhysicsSystem physicsSystem ) {
		super( physicsSystem );
	}

	@Override
	public void start( GameObject parent ) {
		powerUpGO = parent;
		initPhysics( (SpriteComponent ) parent.getComponent( SpriteComponent.class ) );
	}

	private void initPhysics( SpriteComponent parentSpriteComponent ) {
		body = Box2DFactory.createBody( physicsSystem, parentSpriteComponent.getBoundingBox( ), BodyDef.BodyType.DynamicBody, 10f, 0, 0, true, false );

		body.setUserData( powerUpGO );

		Filter f = body.getFixtureList( ).first( ).getFilterData( );
		f.categoryBits = Global.AMMO;
		body.getFixtureList( ).first( ).setFilterData( f );

		body.setTransform( parentSpriteComponent.getCenter( ), ( float ) Math.toRadians( parentSpriteComponent.getRotation( ) ) );
	}
}
