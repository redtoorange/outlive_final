package com.redtoorange.game.components.input;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.Global;
import com.redtoorange.game.entities.characters.EntityCharacter;
import com.redtoorange.game.entities.characters.enemies.Enemy;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 27/Jan/2017
 */
public class EnemyInputComponent extends InputComponent{
	private float rotation;
	private float sensorRange = 10f;
	private Enemy controlled;

	private float roamingTime = 1.0f;
	private float roamingDelay = 0.25f;

	private State currentState = State.ROAMING;
	private enum State{
		ROAMING, CHASING
	}

	public EnemyInputComponent( EntityCharacter parent, float sensorRange  ) {
		super( parent );
		this.controlled = (Enemy)parent;
		this.sensorRange = sensorRange;
	}

	@Override
	public void update( float deltaTime ) {
		switch(currentState){
			case ROAMING:
				romaingAI( deltaTime );
				break;
			case CHASING:
				chasingAI();
				break;
		}
	}

	private void chasingAI(){
		rotateToFacePlayer();
		calculateDeltaInput();


		if( !withinRange() ) {
			resetRoamingTimes();
			currentState = State.ROAMING;
		}
	}

	float roamDirection = MathUtils.random( 0f, 359f );

	private void romaingAI( float deltaTime ){
		if( roamingTime > 0 ){
			roamingTime -= deltaTime;
			roamDeltaInput();
		}
		else if( roamingTime <= 0.0f && roamingDelay > 0){
			roamingDelay -= deltaTime;
			stopMovement();
		}
		else if(roamingTime <= 0 && roamingDelay <= 0){
			resetRoamingTimes( );
			applyDirection();
		}

		if( withinRange() )
			currentState = State.CHASING;
	}

	private void resetRoamingTimes( ) {
		roamingTime = MathUtils.random( 1f, 2f );
		roamingDelay = MathUtils.random( 0.25f, 1f );
		roamDirection = MathUtils.random( 0f, 359f );
	}

	private boolean withinRange( ) {
		Vector2 a = parent.getTransform().getPosition();
		Vector2 b = controlled.getPlayer().getTransform().getPosition();
		return sensorRange > Vector2.dst( a.x, a.y, b.x, b.y );
	}

	protected void rotateToFacePlayer( ) {
		rotation = Global.lookAt(
				parent.getTransform().getPosition(),
				(( Enemy)parent).getPlayer().getTransform().getPosition( ) );
		sc.setRotation( rotation );
	}

	protected void calculateDeltaInput( ) {
		deltaInput.set( 1, 0 );
		deltaInput.rotate( rotation );
	}

	protected void applyDirection(){
		sc.setRotation( roamDirection );
	}

	protected void roamDeltaInput( ) {
		deltaInput.set( 1, 0 );
		deltaInput.rotate( roamDirection );
	}

	protected void stopMovement( ) {
		deltaInput.set( 0, 0 );
	}

	@Override
	public void dispose( ) {

	}
}
