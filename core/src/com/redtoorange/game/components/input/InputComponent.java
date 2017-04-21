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

package com.redtoorange.game.components.input;

import com.badlogic.gdx.math.Vector2;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.GameObject;

/**
 * InputComponent.java - Generic InputComponent that would be used to control a GameObject that can receive input.  This
 * would be the player controlling their character or the enemy's AI.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public abstract class InputComponent extends Component {
    /** The current input this component is receiving. */
    protected Vector2 deltaInput = new Vector2();
    /** The attached sprite component, used by super classes to change facing. */
    protected SpriteComponent sc;


    /** @param owner The GameObject this Component is attached to. */
    public InputComponent( GameObject owner ) {
        sc = owner.getComponent( SpriteComponent.class );
    }

    /** @return The current deltaInput of this component. */
    public Vector2 getDeltaInput() {
        return deltaInput;
    }
}
