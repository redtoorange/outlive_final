package com.redtoorange.game.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.redtoorange.game.gameobject.GameObject;

/**
 * Component.java - The generic component template.  Components should contain implmentation and data, while gameObjects
 * should be built out of Components.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public abstract class Component {
    /** The owning GameObject. */
    protected GameObject owner;

    /**
     * Called before the game starts but after the constructor.  Allow components to link into other components
     * regardless of construction order.
     *
     * @param owner The owning GameObject.
     */
    public void start( GameObject owner ) {
        this.owner = owner;
    }

    /**
     * Called each render frame.  Handle updating of things like physics.
     *
     * @param deltaTime The amount of time since the last update.
     */
    public void update( float deltaTime ) {
        //Stubbed: Sub classes should override if they need to update.
    }

    /**
     * Called each render frame.  Handle drawing.
     *
     * @param batch The batch to draw this component to.
     */
    public void draw( SpriteBatch batch ) {
        //Stubbed: Sub classes should override if they need to draw.
    }

    /** Should be implemented by subClasses to handle cleanup. */
    public abstract void dispose();
}
