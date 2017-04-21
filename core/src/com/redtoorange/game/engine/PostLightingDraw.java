package com.redtoorange.game.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * PostLightingDraw.java - Any class that implements this will have it's postLightingDraw called after the lighting
 * for the scene has been calculated.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public interface PostLightingDraw {
    /**
     * Draw call that draws the sprites on top of the lighting layer.
     *
     * @param batch The spriteBatch to use to draw the renderComponent.
     */
    void postLightingDraw( SpriteBatch batch );
}
