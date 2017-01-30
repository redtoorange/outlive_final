package com.redtoorange.game.engine;

import com.redtoorange.game.entities.Entity;

/**
 * EntityListener.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 29/Jan/2017
 */
public interface EntityListener {
	void entityAdded( Entity e);
	void entityRemoved( Entity e);
}