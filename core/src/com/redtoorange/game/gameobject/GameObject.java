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

package com.redtoorange.game.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.redtoorange.game.components.Component;
import com.redtoorange.game.components.TransformComponent;
import com.redtoorange.game.engine.PostLightingDraw;
import com.redtoorange.game.systems.Global;

/**
 * GameObject.java -
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public abstract class GameObject implements Disposable {
    protected GameObject parent;

    protected Array< GameObject > children = new Array< GameObject >();
    protected Array< Component > components = new Array< Component >();

    protected TransformComponent transform;

    public GameObject( GameObject parent, Vector2 position ) {
        transform = new TransformComponent( this, position );
        this.parent = parent;
    }

    public void start( GameObject parent ) {
        for ( int i = 0; i < components.size; i++ )
            components.get( i ).start( this );

        for ( int i = 0; i < children.size; i++ )
            children.get( i ).start( this );
    }

    public void update( float deltaTime ) {
        for ( int i = 0; i < components.size; i++ )
            components.get( i ).update( deltaTime );

        for ( int i = 0; i < children.size; i++ )
            children.get( i ).update( deltaTime );
    }

    public void preLighting( SpriteBatch batch ) {
        for ( int i = 0; i < components.size; i++ )
            if ( !( components.get( i ) instanceof PostLightingDraw ) )
                components.get( i ).draw( batch );

        for ( int i = 0; i < children.size; i++ )
            children.get( i ).preLighting( batch );
    }

    public void postLighting( SpriteBatch batch ) {
        for ( int i = 0; i < components.size; i++ )
            if ( components.get( i ) instanceof PostLightingDraw )
                components.get( i ).draw( batch );

        for ( int i = 0; i < children.size; i++ )
            children.get( i ).postLighting( batch );
    }

    public GameObject getParent() {
        return parent;
    }

    public void setParent( GameObject parent ) {
        this.parent = parent;
    }

    public Array< GameObject > getChildren() {
        return children;
    }

    public void addChild( GameObject child ) {
        children.add( child );
    }

    public void removeChild( GameObject child ) {
        children.removeValue( child, true );
    }

    public < T extends Component > T getComponent( Class< ? extends Component > classOfInterest ) {
        T obj = null;

        for ( Component c : components ) {
            if ( classOfInterest.isInstance( c ) )
                obj = ( T ) c;
            if ( obj != null )
                return obj;
        }

        return obj;
    }

    public TransformComponent getTransform() {
        return transform;
    }

    protected void addComponent( Component c ) {
        components.add( c );
    }

    protected void removeComponent( Component c ) {
        components.removeValue( c, true );
    }

    public Array< Component > getComponents() {
        return components;
    }

    @Override
    public void dispose() {
        for ( GameObject go : children )
            go.dispose();

        children.clear();

        for ( Component c : components )
            c.dispose();

        components.clear();

        if ( Global.DEBUG )
            System.out.println( this.getClass().getSimpleName() + " disposed" );
    }
}
