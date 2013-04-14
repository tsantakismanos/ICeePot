
/*
 * ICeePot client implemented in java swing
 * author: tsantakismanos
 * date: 17/01/2013
 * license: GPL
 ********************************************
 * The window application implemented below is the 
 * client which makes requests to the arduino server
 * and accepts a series of moisture values as well as
 * time & identification for each value 
 *
 */
package iceepotpc.ui;

import iceepotpc.application.Context;

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
import javax.swing.JOptionPane;

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
		try {
			cntx = Context.getInstance();
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(frame,
					e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
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
		frame.setBounds(20, 20, 920, 700);
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
				frame.dispose();
			}
		});
		mnFile.add(mntmClose);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setMinimumSize(new Dimension(900, 780));
		tabbedPane.setPreferredSize(new Dimension(900, 780));
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		if(cntx.getPots() != null)
			for(int i=0; i<cntx.getPots().size(); i++)
				createTab(tabbedPane, cntx.getPots().get(i).getId());
		
		
	}
	
	
	/**
	 *  helper method that is called when it is required
	 *  to create a Tab which represents a pot (input / output)
	 */
	public void createTab(JTabbedPane tabbedPane, int potId){
		PotPanel pnlPot;
		try {
			pnlPot = new PotPanel(potId);
			tabbedPane.addTab(Context.getInstance().getPotById(potId).getDescr(), null, pnlPot, null);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		int potId = Integer.parseInt(arg.toString());

		createTab(tabbedPane, potId);
	}

	
	
}
