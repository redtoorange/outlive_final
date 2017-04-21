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

import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.systems.Global;

/**
 * TransformComponent.java - Universal component for all GameObjects.  Stores the current location on the gameWorld.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class TransformComponent extends Component {
    private Vector2 position;

    /**
     * @param parent   The owning GameObject.
     * @param position The starting position of this Transform.
     */
    public TransformComponent( GameObject parent, Vector2 position ) {
        this.position = new Vector2( position );
    }

    /** @return The current position of this GameObject. */
    public Vector2 getPosition() {
        return position;
    }

    /** @param position The position to set this GameObject to. */
    public void setPosition( Vector2 position ) {
        this.position.set( position );
    }

    /** Destroy this Transform. */
    @Override
    public void dispose() {
        position = null;

        if ( Global.DEBUG )
            System.out.println( this.getClass().getSimpleName() + " disposed" );
    }
}
