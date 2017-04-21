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
 * HealthUI.java -
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class HealthUI extends System {
    private HealthState currentState = HealthState.HIGH;
    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private Table rootTable;
    private Stage uiStage;
    private TextureAtlas healthBars;
    private int currentHealth = 0;
    private Array< Image > healthMeter = new Array< Image >();

    public HealthUI() {
        init();
    }

    public void update( float deltaTime ) {
        uiStage.act( deltaTime );
    }

    public void draw( SpriteBatch batch ) {
        uiCamera.update();
        uiStage.draw();
    }

    private void init() {
        healthBars = new TextureAtlas( Gdx.files.internal( "health/health.pack" ) );

        uiCamera = new OrthographicCamera( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT );
        uiViewport = new ExtendViewport( Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, uiCamera );

        uiStage = new Stage( uiViewport );
        rootTable = new Table( VisUI.getSkin() );
        uiStage.addActor( rootTable );
        rootTable.setFillParent( true );
        rootTable.left().top();

        for ( int i = 0; i < 20; i++ ) {
            healthMeter.add( new Image( healthBars.findRegion( "highHealth" ) ) );
        }
    }

    public void resize( int width, int height ) {
        uiViewport.update( width, height );
        uiCamera.update();
    }

    public void setHealthAmount( int count, int maxHealth ) {
        this.currentHealth = count;

        if ( .66 <= ( ( float ) currentHealth / ( float ) maxHealth ) ) {
            if ( currentState != HealthState.HIGH ) {
                currentState = HealthState.HIGH;
                for ( Image i : healthMeter ) {
                    i.setDrawable( new TextureRegionDrawable( healthBars.findRegion( "highHealth" ) ) );
                }
            }
        } else if ( .33 <= ( ( float ) currentHealth / ( float ) maxHealth ) ) {
            if ( currentState != HealthState.MEDIUM ) {
                currentState = HealthState.MEDIUM;
                for ( Image i : healthMeter ) {
                    i.setDrawable( new TextureRegionDrawable( healthBars.findRegion( "mediumHealth" ) ) );
                }
            }
        } else if ( .0 <= ( ( float ) currentHealth / ( float ) maxHealth ) ) {
            if ( currentState != HealthState.LOW ) {
                currentState = HealthState.LOW;
                for ( Image i : healthMeter ) {
                    i.setDrawable( new TextureRegionDrawable( healthBars.findRegion( "lowHealth" ) ) );
                }
            }
        }

        rootTable.clearChildren();

        //Add the bars back
        for ( int i = 0; i < count; i++ ) {
            rootTable.add( healthMeter.get( i ) ).top().left().size( 25f, 100f );
        }
    }


    private enum HealthState {
        HIGH, MEDIUM, LOW
    }
}
