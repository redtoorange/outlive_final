package com.redtoorange.game.gameobject;

import com.badlogic.gdx.math.Vector2;

/**
 * SceneRoot.java - Designed to be the top most node in the scene.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class SceneRoot extends GameObject {
    public SceneRoot() {
        super( null, new Vector2( 0, 0 ) );
    }

    @Override
    public void start( GameObject parent ) {
        super.start( parent );
    }
}
