package com.redtoorange.game.desktop;

import javax.swing.*;
import java.awt.*;
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



	private double width;
	private double height;


	public static void main( String[] args ) {
		new OptionChooser();
	}

	public OptionChooser(){
		frame = new JFrame( "Option Chooser" );

		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		panel = new JPanel(  );
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		frame.add( panel );

		fullscreenPanel( );
		debugPanel( );

		launchGame = new JButton( "Launch Game" );
		launchGame.addActionListener( new LaunchListener() );
		panel.add( launchGame );

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();

		frame.pack();
		frame.setLocation(
				((int)width - frame.getWidth()) / 2,
				((int)height - frame.getHeight()) / 2 );
		frame.setVisible( true );
	}

	private void fullscreenPanel() {
		fullScreenPanel = new JPanel(  );
		fullScreenPanel.setLayout( new BoxLayout( fullScreenPanel, BoxLayout.X_AXIS ) );
		panel.add( fullScreenPanel );

		fullScreenLabel = new JLabel( "Full Screen: " );
		fullScreenPanel.add( fullScreenLabel );

		fullScreen = new JCheckBox(  );
		fullScreenPanel.add( fullScreen );
	}

	private void debugPanel() {
		debugPanel = new JPanel(  );
		debugPanel.setLayout( new BoxLayout( debugPanel, BoxLayout.X_AXIS ) );
		panel.add( debugPanel );

		debugLabel = new JLabel( "Debug: " );
		debugPanel.add( debugLabel );

		debugCheck = new JCheckBox(  );
		debugPanel.add( debugCheck );
	}

	private class LaunchListener implements ActionListener{
		@Override
		public void actionPerformed( ActionEvent e ) {
			frame.setVisible( false );
			new DesktopLauncher( fullScreen.isSelected(), debugCheck.isSelected(), width, height );
			//frame.dispose();
		}
	}

}
