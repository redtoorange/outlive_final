package com.redtoorange.game.desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MainDriver.java - A very bare-bones loader that allows the player to turn on fullscreen and debugging mode.  This
 * should be replaced by something that looks way better.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class MainDriver {
    private JFrame frame;
    private JPanel panel;

    private JCheckBox fullScreen;
    private JCheckBox debugCheck;

    private double width;
    private double height;


    /** Load up the chooser window. */
    public MainDriver() {
        frame = new JFrame( "Option Chooser" );

        frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        panel = new JPanel();
        panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
        frame.add( panel );

        fullscreenPanel();
        debugPanel();

        JButton launchGame = new JButton( "Launch Game" );
        launchGame.addActionListener( new LaunchListener() );
        panel.add( launchGame );

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.getWidth();
        height = screenSize.getHeight();

        frame.pack();
        frame.setLocation(
                ( ( int ) width - frame.getWidth() ) / 2,
                ( ( int ) height - frame.getHeight() ) / 2 );
        frame.setVisible( true );
    }

    public static void main( String[] args ) {
        new MainDriver();
    }

    /** Allow the player to turn on full screen. */
    private void fullscreenPanel() {
        JPanel fullScreenPanel = new JPanel();
        fullScreenPanel.setLayout( new BoxLayout( fullScreenPanel, BoxLayout.X_AXIS ) );
        panel.add( fullScreenPanel );

        JLabel fullScreenLabel = new JLabel( "Full Screen: " );
        fullScreenPanel.add( fullScreenLabel );

        fullScreen = new JCheckBox();
        fullScreenPanel.add( fullScreen );
    }

    /** Allow the user to turn on debugging. */
    private void debugPanel() {
        JPanel debugPanel = new JPanel();
        debugPanel.setLayout( new BoxLayout( debugPanel, BoxLayout.X_AXIS ) );
        panel.add( debugPanel );

        JLabel debugLabel = new JLabel( "Debug: " );
        debugPanel.add( debugLabel );

        debugCheck = new JCheckBox();
        debugPanel.add( debugCheck );
    }

    /** Listen to the launch button. */
    private class LaunchListener implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent e ) {
            frame.setVisible( false );
            new DesktopLauncher( fullScreen.isSelected(), debugCheck.isSelected(), width, height );
        }
    }
}
