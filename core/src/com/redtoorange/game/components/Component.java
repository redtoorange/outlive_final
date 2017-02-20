package com.redtoorange.game.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.redtoorange.game.gameobject.GameObject;


public abstract class Component {
	protected GameObject owner;

	public void start( GameObject owner ){
		this.owner = owner;
	}

	public GameObject getOwner(){
		return owner;
	}

	public void setOwner( GameObject owner ){
		this.owner = owner;
	}

	public void update( float deltaTime ){

	}

	public void draw( SpriteBatch batch ){

	}

	public abstract void dispose();
}
