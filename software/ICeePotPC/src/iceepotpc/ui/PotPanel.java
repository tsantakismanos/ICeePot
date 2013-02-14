package iceepotpc.ui;

import iceepotpc.servergw.Meauserement;
import iceepotpc.servergw.Server;

import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author tsantakis
 * A tab where information and inputs per pot is displayed.
 * 
 */
public class PotPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] availableMonths = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	private String[] availableYears = {"2011","2012","2013","2014"};
	
	private JTextField txtLastValue;
	private JTextField txtLastTime;

	
	/**
	 * Create the panel.
	 */
	public PotPanel(final int pin,final JFrame frame) {
		this.setToolTipText("");
		
		this.setLayout(null);
		
		final JTextArea txtResults = new JTextArea();
		txtResults.setBounds(47, 58, 601, 239);
		JScrollPane sp = new JScrollPane(txtResults);
		sp.setSize(600, 200);
		sp.setLocation(50, 70);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(sp);
		txtResults.setColumns(50);
		txtResults.setRows(10);
		
		JButton btnGet = new JButton("Get Info");
		
		btnGet.setBounds(562, 343, 86, 23);
		this.add(btnGet);
		
		JLabel lblLastMeasruement = new JLabel("Last measurement");
		lblLastMeasruement.setBounds(47, 11, 142, 14);
		this.add(lblLastMeasruement);
		
		txtLastValue = new JTextField();
		txtLastValue.setBounds(206, 9, 71, 20);
		this.add(txtLastValue);
		txtLastValue.setColumns(10);
		txtLastValue.setEditable(false);
		
		JLabel lblAt = new JLabel("at");
		lblAt.setBounds(285, 11, 22, 14);
		this.add(lblAt);
		
		txtLastTime = new JTextField();
		txtLastTime.setBounds(314, 9, 124, 20);
		this.add(txtLastTime);
		txtLastTime.setColumns(10);
		txtLastTime.setEditable(false);
		
		JLabel lblDateFrom = new JLabel("From");
		lblDateFrom.setBounds(321, 322, 46, 14);
		this.add(lblDateFrom);
		
		JLabel lblDateTo = new JLabel("To");
		lblDateTo.setBounds(321, 347, 46, 14);
		this.add(lblDateTo);
		
		final JComboBox cmbMonthFrom = new JComboBox();
		cmbMonthFrom.setBounds(378, 318, 71, 20);
		for(int i=0; i<availableMonths.length; i++)
			cmbMonthFrom.addItem(availableMonths[i]);
		this.add(cmbMonthFrom);
		
		final JComboBox cmbYearFrom = new JComboBox();
		cmbYearFrom.setBounds(459, 318, 73, 20);
		for(int i=0; i< availableYears.length; i++)
			cmbYearFrom.addItem(availableYears[i]);
		this.add(cmbYearFrom);
		
		final JComboBox cmbMonthTo = new JComboBox();
		cmbMonthTo.setBounds(377, 344, 72, 20);
		for(int i=0; i<availableMonths.length; i++)
			cmbMonthTo.addItem(availableMonths[i]);
		this.add(cmbMonthTo);
		
		final JComboBox cmbYearTo = new JComboBox();
		cmbYearTo.setBounds(459, 344, 73, 20);
		for(int i=0; i< availableYears.length; i++)
			cmbYearTo.addItem(availableYears[i]);
		this.add(cmbYearTo);
		
		btnGet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Calendar from = Calendar.getInstance();
				from.set(Calendar.MONTH, cmbMonthFrom.getSelectedIndex()+1);
				from.set(Calendar.DAY_OF_MONTH, 1);
				from.set(Calendar.YEAR, Integer.parseInt((String)cmbYearFrom.getSelectedItem()));
				
				Calendar to = Calendar.getInstance();
				to.set(Calendar.MONTH, cmbMonthTo.getSelectedIndex()+1);
				to.set(Calendar.DAY_OF_MONTH, 1);
				to.set(Calendar.YEAR, Integer.parseInt((String)cmbYearTo.getSelectedItem()));
				
				if(from.getTime().getTime() > to.getTime().getTime()){
					JOptionPane.showMessageDialog(frame, "The \"From\" date is after the \"To\" one", "Warning", JOptionPane.ERROR_MESSAGE);
				}
				else{
					ArrayList<Meauserement> measurements = null;
					try {
						measurements = Server.GetMeasurements(from, pin);
						if(measurements == null || measurements.size() == 0)
							txtResults.setText("No measurements yet");
						else{
							for(int i=0; i<measurements.size(); i++)
								txtResults.setText(txtResults.getText() + "\n" +
													measurements.get(i).getMoment() + "|" + 
													measurements.get(i).getPot() + "|" +
													measurements.get(i).getValue());
							txtLastTime.setText(String.valueOf(measurements.get(measurements.size()-1).getMoment()));
							txtLastValue.setText(String.valueOf(measurements.get(measurements.size()-1).getValue()));
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
					}
					
				}
				
			}
		});
		
		/*btnGet.addMouseListener(new MouseAdapter() {
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
					JOptionPane.showMessageDialog(frame, "The \"From\" date is after the \"To\" one", "Warning", JOptionPane.ERROR_MESSAGE);
				}
				else{
					ArrayList<Meauserement> measurements = null;
					try {
						measurements = Server.GetMeasurements(from, pin);
						if(measurements == null || measurements.size() == 0)
							txtResults.setText("No measurements yet");
						else
							for(int i=0; i<measurements.size(); i++)
								txtResults.setText(txtResults.getText() + "\n" +
													measurements.get(i).getMoment() + "|" + 
													measurements.get(i).getPot() + "|" +
													measurements.get(i).getValue());	
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
					}
					
				}
				//txtResults.setText(from.get(Calendar.MONTH) + "/" + from.get(Calendar.YEAR) + "-" +
				//					to.get(Calendar.MONTH) + "/" + to.get(Calendar.YEAR));
			}
		});*/
	}

}
