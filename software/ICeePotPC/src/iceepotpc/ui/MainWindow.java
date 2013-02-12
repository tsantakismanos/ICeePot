
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


import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JComboBox;





public class MainWindow {
	
	private String[] availableMonths = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	private String[] availableYears = {"2011","2012","2013","2014"};
	

	private JFrame frame;
	private JTextField txtLastValue;
	private JTextField txtLastTime;

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
		mntmClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
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
		btnGet.setBounds(562, 343, 86, 23);
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
		
		JLabel lblDateFrom = new JLabel("From");
		lblDateFrom.setBounds(321, 322, 46, 14);
		pnlBasil.add(lblDateFrom);
		
		JLabel lblDateTo = new JLabel("To");
		lblDateTo.setBounds(321, 347, 46, 14);
		pnlBasil.add(lblDateTo);
		
		final JComboBox cmbMonthFrom = new JComboBox();
		cmbMonthFrom.setBounds(378, 318, 71, 20);
		for(int i=0; i<availableMonths.length; i++)
			cmbMonthFrom.addItem(availableMonths[i]);
		pnlBasil.add(cmbMonthFrom);
		
		final JComboBox cmbYearFrom = new JComboBox();
		cmbYearFrom.setBounds(459, 318, 73, 20);
		for(int i=0; i< availableYears.length; i++)
			cmbYearFrom.addItem(availableYears[i]);
		pnlBasil.add(cmbYearFrom);
		
		final JComboBox cmbMonthTo = new JComboBox();
		cmbMonthTo.setBounds(377, 344, 72, 20);
		for(int i=0; i<availableMonths.length; i++)
			cmbMonthTo.addItem(availableMonths[i]);
		pnlBasil.add(cmbMonthTo);
		
		final JComboBox cmbYearTo = new JComboBox();
		cmbYearTo.setBounds(459, 344, 73, 20);
		for(int i=0; i< availableYears.length; i++)
			cmbYearTo.addItem(availableYears[i]);
		pnlBasil.add(cmbYearTo);
		
		
		btnGet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				Calendar from = Calendar.getInstance();
				from.set(Calendar.MONTH, cmbMonthFrom.getSelectedIndex()+1);
				from.set(Calendar.DAY_OF_MONTH, 1);
				from.set(Calendar.YEAR, Integer.parseInt((String)cmbYearFrom.getSelectedItem()));
				
				Calendar to = Calendar.getInstance();
				to.set(Calendar.MONTH, cmbMonthTo.getSelectedIndex()+1);
				to.set(Calendar.DAY_OF_MONTH, 1);
				to.set(Calendar.YEAR, Integer.parseInt((String)cmbYearTo.getSelectedItem()));
				
				if(from.getTime().getTime() > to.getTime().getTime()){
					AlertDialog ad = new AlertDialog();
					ad.setVisible(true);
				}
				else{
					ArrayList<Meauserement> measurements = Server.GetMeasurements(from, 0);
					for(int i=0; i<measurements.size(); i++)
						txtResults.setText(measurements.get(i).getMoment() + "|" +
											measurements.get(i).getPot() + "|" +
											measurements.get(i).getValue());	
				//	txtResults.setText(from.get(Calendar.MONTH) + "/" + from.get(Calendar.YEAR) + "-" +
				//					to.get(Calendar.MONTH) + "/" + to.get(Calendar.YEAR));
				}
			}
		});
		
	}
}
