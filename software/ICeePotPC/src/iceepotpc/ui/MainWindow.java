
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

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Toolkit;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;




public class MainWindow {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

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
		
		JPanel panel1 = new JPanel();
		panel1.setToolTipText("");
		tabbedPane.addTab("\u0392\u03B1\u03C3\u03B9\u03BB\u03B9\u03BA\u03CC\u03C2", null, panel1, null);
		panel1.setLayout(null);
		
		final JTextArea txtResults = new JTextArea();
		txtResults.setBounds(47, 58, 283, 239);
		panel1.add(txtResults);
		txtResults.setColumns(50);
		txtResults.setRows(10);
		
		JButton btnGet = new JButton("Get Info");
		btnGet.setBounds(599, 338, 73, 23);
		panel1.add(btnGet);
		
		JLabel lblLastMeasruement = new JLabel("Last measruement: ");
		lblLastMeasruement.setBounds(47, 11, 102, 14);
		panel1.add(lblLastMeasruement);
		
		textField = new JTextField();
		textField.setBounds(159, 8, 86, 20);
		panel1.add(textField);
		textField.setColumns(10);
		
		JLabel lblAt = new JLabel("at");
		lblAt.setBounds(264, 11, 22, 14);
		panel1.add(lblAt);
		
		textField_1 = new JTextField();
		textField_1.setBounds(285, 8, 86, 20);
		panel1.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setToolTipText("month/year");
		textField_2.setBounds(472, 308, 86, 20);
		panel1.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblDateFrom = new JLabel("date from:");
		lblDateFrom.setBounds(369, 311, 86, 14);
		panel1.add(lblDateFrom);
		
		textField_3 = new JTextField();
		textField_3.setToolTipText("month/year");
		textField_3.setBounds(472, 339, 86, 20);
		panel1.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblDateTo = new JLabel("date to:");
		lblDateTo.setBounds(369, 342, 46, 14);
		panel1.add(lblDateTo);
		btnGet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				InputStream is = null;
				OutputStream os = null;
				Socket s = null;
				try 
				{
					byte[] request = {'2','2','0','1','3','.','t','x','t','\0'};
					int response = 0;
					String response_str = "";
					s = new Socket("homeplants.dyndns.org", 3629);
					
					
					os = s.getOutputStream();
			
					
					//send request
					os.write(request);
					os.flush();
					
					
					is = s.getInputStream();
					
					//get & print response
					response = is.read();
					
					while(response != -1)
					{
						response_str = response_str + (char)response;
						response = is.read();
					}
					
					txtResults.setText(response_str);
					
					
					
				} catch (UnknownHostException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				finally{
					try {
						is.close();
						s.close();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					
				}
				
			}
		});
		
	}
}
