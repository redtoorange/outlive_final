package com.redtoorange.game.desktop;

import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 01/Feb/2017
 */
public class OptionChooser {
	private JFrame frame;
	private JPanel panel;

	private JButton launchGame;

	private JPanel fullScreenPanel;
	private JLabel fullScreenLabel;
	private JCheckBox fullScreen;

	private JPanel debugPanel;
	private JLabel debugLabel;
	private JCheckBox debugCheck;


	public static void main( String[] args ) {
		new OptionChooser();
	}

	public OptionChooser(){
		frame = new JFrame( "Option Chooser" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		panel = new JPanel(  );
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		frame.add( panel );

		fullScreenPanel = new JPanel(  );
		fullScreenPanel.setLayout( new BoxLayout( fullScreenPanel, BoxLayout.X_AXIS ) );
		panel.add( fullScreenPanel );

		fullScreenLabel = new JLabel( "Full Screen: " );
		fullScreenPanel.add( fullScreenLabel );

		fullScreen = new JCheckBox(  );
		fullScreenPanel.add( fullScreen );

		debugPanel = new JPanel(  );
		debugPanel.setLayout( new BoxLayout( debugPanel, BoxLayout.X_AXIS ) );
		panel.add( debugPanel );

		debugLabel = new JLabel( "Debug: " );
		debugPanel.add( debugLabel );

		debugCheck = new JCheckBox(  );
		debugPanel.add( debugCheck );

		launchGame = new JButton( "Launch Game" );
		launchGame.addActionListener( new LaunchListener() );
		panel.add( launchGame );

		frame.pack();
		frame.setVisible( true );
	}

	private class LaunchListener implements ActionListener{
		@Override
		public void actionPerformed( ActionEvent e ) {
			frame.setVisible( false );
			new DesktopLauncher( fullScreen.isSelected(), debugCheck.isSelected() );
			//frame.dispose();
		}
	}

}
