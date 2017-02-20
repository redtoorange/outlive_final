package com.redtoorange.game.engine;

import com.redtoorange.game.gameobject.GameObject;

/**
 * EntityListener.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 29/Jan/2017
 */
public interface EntityListener {
	void entityAdded( GameObject e);
	void entityRemoved( GameObject e);
}