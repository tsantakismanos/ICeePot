package iceepotpc.ui;

import iceepotpc.appication.Context;
import iceepotpc.charteng.ChartCreator;
import iceepotpc.servergw.Meauserement;
import iceepotpc.servergw.Server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

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
	
	Context cntx;
	
	DateFormat df = new SimpleDateFormat();

	
	/**
	 * Create the panel.
	 */
	public PotPanel(final int pin,final JFrame frame) {
		cntx = Context.getInstance();
		this.setToolTipText("");
		
		this.setLayout(null);
		
		final JTextArea txtResults = new JTextArea();
		if(cntx.isDebugMode()){
			
			txtResults.setBounds(47, 58, 601, 400);
			JScrollPane sp = new JScrollPane(txtResults);
			sp.setSize(183, 500);
			sp.setLocation(50, 70);
			sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			this.add(sp);
			txtResults.setColumns(50);
			txtResults.setRows(10);
		}
		
		JButton btnGet = new JButton("Get Info");
		
		btnGet.setBounds(757, 578, 114, 23);
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
		lblDateFrom.setBounds(504, 556, 46, 14);
		this.add(lblDateFrom);
		
		JLabel lblDateTo = new JLabel("To");
		lblDateTo.setBounds(504, 582, 46, 14);
		this.add(lblDateTo);
		
		
		final JComboBox cmbMonthTo = new JComboBox();
		cmbMonthTo.setBounds(568, 579, 72, 20);
		for(int i=0; i<availableMonths.length; i++)
			cmbMonthTo.addItem(availableMonths[i]);
		this.add(cmbMonthTo);
		
		final JComboBox cmbYearTo = new JComboBox();
		cmbYearTo.setBounds(652, 579, 73, 20);
		for(int i=0; i< availableYears.length; i++)
			cmbYearTo.addItem(availableYears[i]);
		this.add(cmbYearTo);
		
		final JComboBox cmbMonthFrom = new JComboBox();
		cmbMonthFrom.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				cmbMonthTo.setSelectedIndex(cmbMonthFrom.getSelectedIndex());				
			}
		});
		cmbMonthFrom.setBounds(569, 547, 71, 20);
		for(int i=0; i<availableMonths.length; i++)
			cmbMonthFrom.addItem(availableMonths[i]);
		this.add(cmbMonthFrom);
		
		final JComboBox cmbYearFrom = new JComboBox();
		cmbYearFrom.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cmbYearTo.setSelectedIndex(cmbYearFrom.getSelectedIndex());
			}
		});
		cmbYearFrom.setBounds(652, 547, 73, 20);
		for(int i=0; i< availableYears.length; i++)
			cmbYearFrom.addItem(availableYears[i]);
		this.add(cmbYearFrom);
				
		
		final ChartPanel pnlChart = new ChartPanel(null);
		add(pnlChart);
						
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
						measurements = Server.GetMeasurements(from, to, pin, cntx);
						if(measurements == null || measurements.size() == 0)
							JOptionPane.showMessageDialog(frame, "Measurements not available yet", "Warning", JOptionPane.WARNING_MESSAGE);
						else{
							if(cntx.isDebugMode()){
							for(int i=0; i<measurements.size(); i++)
								txtResults.setText(txtResults.getText() + "\n" +
													df.format(new Date(measurements.get(i).getMoment())) + "|" + 
													measurements.get(i).getPot() + "|" +
													measurements.get(i).getValue());
							}
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis((long)measurements.get(measurements.size()-1).getMoment());
							
							txtLastTime.setText(df.format(c.getTime()));
							
							txtLastValue.setText(String.valueOf(measurements.get(measurements.size()-1).getValue()));
							
							JFreeChart fc = ChartCreator.createChart(measurements);
							//pnlChart = new ChartPanel(fc, false);
							pnlChart.setChart(fc);
							pnlChart.setBounds(263, 70, 620, 460);
							pnlChart.setVisible(true);
							
							
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
					}
					
				}
				
			}
		});
		
	}
	
	
}
