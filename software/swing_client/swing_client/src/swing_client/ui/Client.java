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
package swing_client.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.ScrollPaneLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class Client {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
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
	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		
		JScrollPane panel = new JScrollPane();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new ScrollPaneLayout());
		
		final JTextArea txtResults = new JTextArea();
		panel.setViewportView(txtResults);
		
		JButton btnGet = new JButton("Get Info");
		frame.getContentPane().add(btnGet, BorderLayout.NORTH);
		btnGet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				InputStream is = null;
				OutputStream os = null;
				Socket s = null;
				try 
				{
					byte[] request = {'g'};
					int response = 0;
					String response_str = "";
					s = new Socket("192.168.1.20", 3629);
					
					
					os = s.getOutputStream();
			
					
					//send request
					os.write(request);
					os.flush();
					
					
					is = s.getInputStream();
					
					//get & print response
					do
					{
						response = is.read();
						response_str = response_str + (char)response;
						
					}while(response != -1);
					
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
