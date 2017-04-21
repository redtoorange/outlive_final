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

package com.redtoorange.game.systems;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

/**
 * LightingSystem.java - Handle the lighting for the world.  It provides easy access to the world's rayHandler and handles
 * the lighting render step.  It can add and remove lights from the world dynamically and safely, without fear of simulation
 * lockout by the Box2D world.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class LightingSystem {
    private RayHandler rayHandler;

    /** @param world The physics world to draw lighting based on. */
    public LightingSystem( World world ) {
        rayHandler = new RayHandler( world );
        rayHandler.setAmbientLight( Global.AMBIENT_LIGHT );
    }

    /**
     * Draw the lighting in the world based on the camera's location.
     *
     * @param camera The camera to draw the lighting shader onto.
     */
    public void draw( OrthographicCamera camera ) {
        rayHandler.setCombinedMatrix( camera );
        rayHandler.updateAndRender();
    }

    /** @return Get the cached ray handler from the physicSystem's world. */
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    /** Dispose the rayHandler from the physicsSystem world, this should NOT be called during a simulation. */
    public void dispose() {
        if ( rayHandler != null )
            rayHandler.dispose();
    }
}
