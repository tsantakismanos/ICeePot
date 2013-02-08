
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

import iceepotpc.servergw.Meauserement;
import iceepotpc.servergw.Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JTextArea;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;



public class MainWindow {

	private JFrame frame;
	private JTextField txtLastValue;
	private JTextField txtLastTime;
	private JTextField txtDateFrom;
	private JTextField txtDateTo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		frame.setBounds(100, 100, 723, 487);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ICeePot");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Projects\\various\\ICeePot\\software\\ICeePotPC\\icons\\ICeePot_logo_new.png"));
		
		JMenuBar menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnFile.add(mntmSettings);
		
		JMenuItem mntmAddPot = new JMenuItem("Add Pot");
		mnFile.add(mntmAddPot);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel pnlBasil = new JPanel();
		pnlBasil.setToolTipText("");
		tabbedPane.addTab("\u0392\u03B1\u03C3\u03B9\u03BB\u03B9\u03BA\u03CC\u03C2", null, pnlBasil, null);
		pnlBasil.setLayout(null);
		
		final JTextArea txtResults = new JTextArea();
		txtResults.setBounds(47, 58, 601, 239);
		pnlBasil.add(txtResults);
		txtResults.setColumns(50);
		txtResults.setRows(10);
		
		JButton btnGet = new JButton("Get Info");
		btnGet.setBounds(599, 338, 73, 23);
		pnlBasil.add(btnGet);
		
		JLabel lblLastMeasruement = new JLabel("Last measruement: ");
		lblLastMeasruement.setBounds(47, 11, 102, 14);
		pnlBasil.add(lblLastMeasruement);
		
		txtLastValue = new JTextField();
		txtLastValue.setBounds(159, 8, 86, 20);
		pnlBasil.add(txtLastValue);
		txtLastValue.setColumns(10);
		
		JLabel lblAt = new JLabel("at");
		lblAt.setBounds(264, 11, 22, 14);
		pnlBasil.add(lblAt);
		
		txtLastTime = new JTextField();
		txtLastTime.setBounds(285, 8, 86, 20);
		pnlBasil.add(txtLastTime);
		txtLastTime.setColumns(10);
		
		txtDateFrom = new JTextField();
		txtDateFrom.setToolTipText("month/year");
		txtDateFrom.setBounds(472, 308, 86, 20);
		pnlBasil.add(txtDateFrom);
		txtDateFrom.setColumns(10);
		
		JLabel lblDateFrom = new JLabel("date from:");
		lblDateFrom.setBounds(369, 311, 86, 14);
		pnlBasil.add(lblDateFrom);
		
		txtDateTo = new JTextField();
		txtDateTo.setToolTipText("month/year");
		txtDateTo.setBounds(472, 339, 86, 20);
		pnlBasil.add(txtDateTo);
		txtDateTo.setColumns(10);
		
		JLabel lblDateTo = new JLabel("date to:");
		lblDateTo.setBounds(369, 342, 46, 14);
		pnlBasil.add(lblDateTo);
		
		
		btnGet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//initialize the calendar to be given to the server
				Calendar c = Calendar.getInstance();
				Date d ; 
				ArrayList<Meauserement> measurements = Server.GetMeasurements(c, 0);
				
			}
		});
		
	}
}
