package com.redtoorange.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.redtoorange.game.gameobject.Bullet;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.gameobject.characters.enemies.Enemy;
import com.redtoorange.game.gameobject.powerups.PowerUp;

/**
 * ContactManager.java - Handle the contacts between different gameobject.
 *
 * @author - Andrew M.
 * @version - 13/Jan/2017
 */
//TODO: Steamline the contacts with presolving.
//TODO: Implement Filters for all major gameobject.
public class ContactManager implements ContactListener {
	@Override
	public void beginContact( Contact contact ) {
		Body bodyA = contact.getFixtureA( ).getBody( );
		Body bodyB = contact.getFixtureB( ).getBody( );

		handleEnemyCollision( bodyA, bodyB );
		handlePowerupCollision( bodyA, bodyB );
		handleBulletCollision( bodyA, bodyB );
		handleWallCollision( bodyA, bodyB );
	}

	private void handlePowerupCollision( Body bodyA, Body bodyB ) {
		if ( ( bodyA.getUserData( ) instanceof PowerUp && bodyB.getUserData( ) instanceof Player ) ||
				( bodyB.getUserData( ) instanceof PowerUp && bodyA.getUserData( ) instanceof Player ) ) {

			if ( bodyA.getUserData( ) instanceof PowerUp ) {
				( ( PowerUp ) bodyA.getUserData( ) ).absorbed( ( ( Player ) bodyB.getUserData( ) ) );

			}
			else {
				( ( PowerUp ) bodyB.getUserData( ) ).absorbed( ( ( Player ) bodyA.getUserData( ) ) );
			}
		}
	}

	private void handleBulletCollision( Body bodyA, Body bodyB ) {
		if ( ( bodyA.getUserData( ) instanceof Enemy && bodyB.getUserData( ) instanceof Bullet ) ||
				( bodyB.getUserData( ) instanceof Enemy && bodyA.getUserData( ) instanceof Bullet ) ) {

			if ( bodyA.getUserData( ) instanceof Bullet ) {
				Bullet b = ( ( Bullet ) bodyA.getUserData( ) );
				if ( b.isAlive( ) ) {
					b.kill( );
					( ( Enemy ) bodyB.getUserData( ) ).takeDamage( 1 );
				}
			}
			else {
				Bullet b = ( ( Bullet ) bodyB.getUserData( ) );
				if ( b.isAlive( ) ) {
					b.kill( );
					( ( Enemy ) bodyA.getUserData( ) ).takeDamage( 1 );
				}
			}
		}
	}

	private void handleEnemyCollision( Body bodyA, Body bodyB ) {
		if ( ( bodyA.getUserData( ) instanceof Enemy && bodyB.getUserData( ) instanceof Player ) ||
				( bodyB.getUserData( ) instanceof Enemy && bodyA.getUserData( ) instanceof Player ) ) {
			Enemy enemy = null;
			Player player = null;

			if ( bodyA.getUserData( ) instanceof Enemy ) {
				enemy = ( ( Enemy ) bodyA.getUserData( ) );
				player = ( ( Player ) bodyB.getUserData( ) );
			}
			else {
				player = ( ( Player ) bodyA.getUserData( ) );
				enemy = ( ( Enemy ) bodyB.getUserData( ) );
			}

			player.takeDamage( enemy.getDamage() );
		}
	}

	private void handleWallCollision( Body bodyA, Body bodyB ) {
		if ( ( bodyA.getUserData( ) instanceof Bullet && bodyB.getUserData( ) instanceof Rectangle ) ||
				( bodyB.getUserData( ) instanceof Bullet && bodyA.getUserData( ) instanceof Rectangle ) ) {
			if ( bodyA.getUserData( ) instanceof Bullet ) {
				Bullet b = ( ( Bullet ) bodyA.getUserData( ) );
				if ( b.isAlive( ) ) {
					b.kill( );
				}
			}
			else {
				Bullet b = ( ( Bullet ) bodyB.getUserData( ) );
				if ( b.isAlive( ) ) {
					b.kill( );
				}
			}
		}
	}

	@Override
	public void endContact( Contact contact ) {

	}

	@Override
	public void preSolve( Contact contact, Manifold oldManifold ) {

	}

	@Override
	public void postSolve( Contact contact, ContactImpulse impulse ) {

	}
}
