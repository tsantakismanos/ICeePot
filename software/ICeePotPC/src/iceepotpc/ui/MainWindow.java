
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

import java.awt.Window;


import javax.swing.JFrame;
import java.awt.BorderLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



import javax.swing.JTabbedPane;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JWindow;
import java.awt.Dimension;




/**
 * The application's main window where a menu bar is displayed and a set of tabs
 * each one of them representing a pot.
 * @author tsantakis
 * 
 */
public class MainWindow implements Observer{
	
	private JFrame frame;
	private JTabbedPane tabbedPane;
	Context cntx;
	
	/**
	 * Create the application's main window.
	 */
	public MainWindow() {
		cntx = Context.getInstance();
		initialize();
		this.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(920, 700));
		frame.setMinimumSize(new Dimension(920, 700));
		frame.setBounds(50, 50, 920, 700);
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
				NewPotDialog dialog = new NewPotDialog(cntx);
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
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setMinimumSize(new Dimension(900, 780));
		tabbedPane.setPreferredSize(new Dimension(900, 780));
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		if(cntx.getPotDescrs() != null)
			for(int i=0; i<cntx.getPotDescrs().size(); i++)
				createTab(tabbedPane, cntx.getPotDescrs().get(i).getDescr(), cntx.getPotDescrs().get(i).getPin());
		
		
	}
	
	
	/**
	 *  helper method that is called when it is required
	 *  to create a Tab which represents a pot (input / output)
	 */
	public void createTab(JTabbedPane tabbedPane, String descr, final int pin){
		PotPanel pnlPot = new PotPanel(pin, frame);
		tabbedPane.addTab(descr, null, pnlPot, null);
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		int position = Integer.parseInt(arg.toString());

		createTab(tabbedPane, cntx.getPotDescrs().get(position).getDescr(), cntx.getPotDescrs().get(position).getPin());
	}

	
	
}
