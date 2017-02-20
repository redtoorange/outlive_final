package com.redtoorange.game.components.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.Global;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.gameobject.Bullet;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * BulletPhysicsComponent.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 14/Jan/2017
 */
public class BulletPhysicsComponent extends PhysicsComponent {
	private Rectangle bulletBoundingRect;
	private float bulletSpeedMultiplier;
	private Bullet controlledBullet;

	public BulletPhysicsComponent( PhysicsSystem physicsSystem, Bullet controlledBullet, SpriteComponent spriteComponent ) {
		super( physicsSystem );

		this.controlledBullet = controlledBullet;
		this.bulletSpeedMultiplier = controlledBullet.getSpeed( );

		bulletBoundingRect = new Rectangle( spriteComponent.getBoundingBox( ) );
		bulletBoundingRect.setSize( spriteComponent.getWidth( ) / 4f, spriteComponent.getHeight( ) / 4f );
	}

	public void fire( Vector2 position, Vector2 velocity, float rotation ) {
		createBulletBody( position, velocity, rotation );
		Filter f = createFilter( );
		body.getFixtureList( ).first( ).setFilterData( f );
	}

	private Filter createFilter() {
		Filter f = body.getFixtureList( ).first( ).getFilterData( );
		f.categoryBits = Global.BULLET_LIVE;
		f.maskBits = Global.ENEMY | Global.WALL;
		return f;
	}

	private void createBulletBody( Vector2 position, Vector2 velocity, float rotation ) {
		body = Box2DFactory.createBody( physicsSystem, bulletBoundingRect, BodyDef.BodyType.DynamicBody, 10f, 0, 0, true, true );

		body.setUserData( controlledBullet );
		body.setTransform( position, ( float ) Math.toRadians( rotation ) );
		body.applyLinearImpulse( velocity.scl( bulletSpeedMultiplier ), body.getWorldCenter( ), true );
	}

	public void kill( ) {
		physicsSystem.destroyBody( body );
		body = null;
	}

	@Override
	public void update( float deltaTime ) {
		controlledBullet.getTransform().setPosition( body.getPosition( ) );
	}
}
