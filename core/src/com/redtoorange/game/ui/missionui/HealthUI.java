package com.redtoorange.game.ui.missionui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.redtoorange.game.Global;
import com.redtoorange.game.systems.System;

/**
 * GunUI.java - User Interface for the Ammunition counter.
 *
 * @author
 * @version 23/Jan/2017
 */
public class HealthUI extends System {
    private enum HealthState {
        HIGH, MEDIUM, LOW
    }

    private HealthState currentState = HealthState.HIGH;
    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private Table rootTable;
    private Stage uiStage;
    private TextureAtlas healthBars;

    private int maxHealth = 0;
    private int currentHealth = 0;

    private Array< Image > healthMeter = new Array< Image >();

    private Image currentImage;
    private Label healthLabel;

    private TextureRegionDrawable regionDrawable = new TextureRegionDrawable();

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
//        healthLabel = new Label( "0", VisUI.getSkin() );
//        healthLabel.setSize( 100f, 100f );
//        rootTable.add( healthLabel ).left().top().size( 100f, 100f ).expandX().row();
//        currentImage = new Image( healthBars.findRegion( "highHealth" ) );

        for ( int i = 0; i < 20; i++ ) {
            healthMeter.add( new Image( healthBars.findRegion( "highHealth" ) ) );
        }
//        rootTable.add( currentImage ).top().left().expand().size( 100f, 100f );
    }

    public void resize( int width, int height ) {
        uiViewport.update( width, height );
        uiCamera.update();
    }

    public void swapCurrentImage( TextureRegion region ) {
        regionDrawable.setRegion( region );
    }

    public void setHealthAmount( int count, int maxHealth ) {
        this.currentHealth = count;
        this.maxHealth = maxHealth;

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

        //clear UI
//        for ( int i = 0; i < healthMeter.size; i++ ) {
//            if ( rootTable.getChildren().contains( healthMeter.get( i ), true ) )
//                rootTable.removeActor( healthMeter.get( i ) );
//        }

        rootTable.clearChildren();

        //Add the bars back
        for ( int i = 0; i < count; i++ ) {
            rootTable.add( healthMeter.get( i ) ).top().left().size( 25f, 100f );
        }

//        healthLabel.setText( Integer.toString( count ) );
    }
}
