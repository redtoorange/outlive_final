/*
 * Copyright 2017  Andrew James McGuiness
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated  documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the  rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit   persons to whom the Software is furnished to do
 *   so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
