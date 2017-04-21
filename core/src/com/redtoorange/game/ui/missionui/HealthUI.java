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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.System;

/**
 * HealthUI.java - A specialized UI setup to display the player's current health in the upper left of the screen.
 * The color of the bars is determined by the state of the player's health, low, medium or high.  This is calculated as
 * a percentage based on his health and his maxHealth.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class HealthUI extends System {
    private HealthState currentState = HealthState.HIGH;
    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private Table rootTable;
    private Stage uiStage;
    private TextureAtlas healthBars;
    private int currentHealth = 0;
    private int maxHealth = 0;
    private Array< Image > healthMeter = new Array< Image >();

    /** Create the UI elements. */
    public HealthUI() {
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

    /** Load the images from disc and init the UI. */
    private void init() {
        healthBars = new TextureAtlas( Gdx.files.internal( "health/health.pack" ) );
        buildUIElements();
    }

    /** Build all the UI elements. */
    private void buildUIElements() {
        uiCamera = new OrthographicCamera( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT );
        uiViewport = new ExtendViewport( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, uiCamera );

        //Build the table.
        uiStage = new Stage( uiViewport );
        rootTable = new Table( VisUI.getSkin() );
        uiStage.addActor( rootTable );
        rootTable.setFillParent( true );
        rootTable.left().top();

        //Fill up the healthMeter is images to be used as healthBars.
        for ( int i = 0; i < 20; i++ ) {
            healthMeter.add( new Image( healthBars.findRegion( "highHealth" ) ) );
        }
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
     * Set the number of health bars that should be displayed on the screen.
     *
     * @param count     The player's current health.
     * @param maxHealth The player's max health.
     */
    public void setHealthAmount( int count, int maxHealth ) {
        this.currentHealth = count;
        this.maxHealth = maxHealth;

        determineState();

        //put the bars on the screen
        rootTable.clearChildren();
        for ( int i = 0; i < count; i++ ) {
            rootTable.add( healthMeter.get( i ) ).top().left().size( 25f, 100f );
        }
    }

    /** Determine the current state of the health, low, medium or high. */
    private void determineState() {
        if ( .66 <= ( ( float ) currentHealth / maxHealth ) ) {
            if ( currentState != HealthState.HIGH ) {
                currentState = HealthState.HIGH;
                for ( Image i : healthMeter ) {
                    i.setDrawable( new TextureRegionDrawable( healthBars.findRegion( "highHealth" ) ) );
                }
            }
        } else if ( .33 <= ( ( float ) currentHealth / maxHealth ) ) {
            if ( currentState != HealthState.MEDIUM ) {
                currentState = HealthState.MEDIUM;
                for ( Image i : healthMeter ) {
                    i.setDrawable( new TextureRegionDrawable( healthBars.findRegion( "mediumHealth" ) ) );
                }
            }
        } else if ( .0 <= ( ( float ) currentHealth / maxHealth ) ) {
            if ( currentState != HealthState.LOW ) {
                currentState = HealthState.LOW;
                for ( Image i : healthMeter ) {
                    i.setDrawable( new TextureRegionDrawable( healthBars.findRegion( "lowHealth" ) ) );
                }
            }
        }
    }


    /** The three possible states the player's health could be in. */
    private enum HealthState {
        HIGH, MEDIUM, LOW
    }
}
