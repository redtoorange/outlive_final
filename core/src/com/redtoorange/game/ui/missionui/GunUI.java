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

package com.redtoorange.game.ui.missionui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.System;

/**
 * GunUI.java - A specialized UI setup to display the player's chambered ammo and remaining ammo in their inventory.
 * This is displayed in the lower right corner of the screen.  As the player fires bullets, the UI image can be swapped
 * for a new image to display the remaining rounds, this can also be used when changing guns.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class GunUI extends System {
    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private Table rootTable;
    private Stage uiStage;
    private Image currentImage;
    private Label ammoLabel;
    private TextureRegionDrawable regionDrawable = new TextureRegionDrawable();

    /** Create the UI elements. */
    public GunUI() {
        init();
    }

    /** @param deltaTime Time since the last update. */
    public void update( float deltaTime ) {
        uiStage.act( deltaTime );
    }

    /** @param batch The sprite batch to use for drawing. */
    public void draw( SpriteBatch batch ) {
        uiCamera.update();
        uiStage.draw();
    }

    /** Initialize all of the UI elements. */
    private void init() {
        uiCamera = new OrthographicCamera( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT );
        uiViewport = new ExtendViewport( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, uiCamera );

        uiStage = new Stage( uiViewport );
        rootTable = new Table( VisUI.getSkin() );
        uiStage.addActor( rootTable );
        rootTable.setFillParent( true );

        ammoLabel = new Label( "0", VisUI.getSkin() );
        ammoLabel.setSize( 100f, 100f );
        rootTable.add( ammoLabel ).right().bottom().size( 100f, 100f ).expand();
        currentImage = new Image( regionDrawable );
        rootTable.add( currentImage ).bottom().right().size( 200, 200 );
    }

    /**
     * The play window has been resized, recalculate the position of screen elements based on the new size.
     *
     * @param width  The screen's new width.
     * @param height The screen's new height.
     */
    public void resize( int width, int height ) {
        uiViewport.update( width, height );
        uiCamera.update();
    }

    /**
     * Swap the region displayed by this UI element.
     *
     * @param region The region to swap to.
     */
    public void swapCurrentImage( TextureRegion region ) {
        regionDrawable.setRegion( region );
    }

    /** @param count The number of bullets left in the player's inventory that this UI should display. */
    public void setAmmoCount( int count ) {
        ammoLabel.setText( Integer.toString( count ) );
    }
}
