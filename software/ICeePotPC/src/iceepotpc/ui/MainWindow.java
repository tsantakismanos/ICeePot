
/*
 * ICeePot client implemented in java swing
 * author: tsantakismanos
 * date: 17/01/2013
 * license: GPL
 ********************************************
 * The window application implemented below is the 
 * client which makes requests to the arduino server
 * and accepts a series of moisture values as well as
 * time & analog pin for each value 
 *
 */
package iceepotpc.ui;

import iceepotpc.appication.Context;

import java.awt.EventQueue;
import java.awt.Window;


import javax.swing.JFrame;
import java.awt.BorderLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



import javax.swing.JTabbedPane;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JWindow;




/**
 * The application's main window where a menu bar is displayed and a set of tabs
 * each one of them representing a pot.
 * @author tsantakis
 * 
 */
public class MainWindow{
	
	private JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(50, 50, 922, 709);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ICeePot");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/icons/ICeePot_logo_new.png")));
		
		JMenuBar menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		//File menu-bar item
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		//Settings menu item
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mntmSettings.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				SettingsDialog dialog = new SettingsDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				dialog.pack();
			}
			
		});
		mnFile.add(mntmSettings);

		//Add pot menu item
		JMenuItem mntmAddPot = new JMenuItem("Add Pot");
		mntmAddPot.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				NewPotDialog dialog = new NewPotDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				dialog.pack();
			}
		});
		mnFile.add(mntmAddPot);
		
		//Add close menu item
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				Window[] w = JWindow.getWindows();
				for(int i=0; i<w.length; i++)
					if(w[i].isVisible())
						w[i].dispose();
			}
		});
		mnFile.add(mntmClose);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		if(Context.potDescrs != null)
			for(int i=0; i<Context.potDescrs.size(); i++)
				createTab(tabbedPane, Context.potDescrs.get(i).getDescr(), Context.potDescrs.get(i).getPin());
		
		
	}
	
	
	/**
	 *  helper method that is called when it is required
	 *  to create a Tab which represents a pot (input / output)
	 */
	public void createTab(JTabbedPane tabbedPane, String descr, final int pin){
		PotPanel pnlPot = new PotPanel(pin, frame);
		tabbedPane.addTab(descr, null, pnlPot, null);
	}
	
}
