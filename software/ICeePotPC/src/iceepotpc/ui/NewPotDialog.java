package iceepotpc.ui;

import iceepotpc.application.Context;
import iceepotpc.application.Pot;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JSlider;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class NewPotDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtPotDescr;
	private JTextField txtPotId;
	private JSlider sldMinMoist;
	private JSlider sldMaxMoist;

	private Context cntx;
	private JDialog me = null;
	private JTextField txtMinMoistDispl;
	private JTextField txtMaxMoistDispl;

	/**
	 * Create the dialog.
	 */
	public NewPotDialog() {
		me = this;
		try {
			cntx = Context.getInstance();
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(me,
					e2.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				NewPotDialog.class.getResource("/icons/ICeePot_logo_new.png")));
		setTitle("Add New Pot");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblPotDescr = new JLabel("Pot Description");
			contentPanel.add(lblPotDescr, "2, 2");
		}
		{
			txtPotDescr = new JTextField();
			contentPanel.add(txtPotDescr, "4, 2, 3, 1, fill, default");
			txtPotDescr.setColumns(10);
		}
		{
			JLabel lblPotId = new JLabel("Id of the Pot");
			contentPanel.add(lblPotId, "2, 4");
		}
		{
			txtPotId = new JTextField();
			contentPanel.add(txtPotId, "4, 4, fill, default");
			txtPotId.setColumns(10);
		}
		{
			JLabel lblMinimumMoistureValue = new JLabel(
					"Minimum Moisture Value");
			contentPanel.add(lblMinimumMoistureValue, "2, 6");
		}
		{
			sldMinMoist = new JSlider();
			
			sldMinMoist.setMinimum(0);
			sldMinMoist.setMaximum(950);
			sldMinMoist.setValue(500);
			contentPanel.add(sldMinMoist, "6, 6");
		}
		{
			txtMinMoistDispl = new JTextField();
			txtMinMoistDispl.setText(String.valueOf(sldMinMoist.getValue()));
			txtMinMoistDispl.setEditable(false);
			contentPanel.add(txtMinMoistDispl, "4, 6, fill, default");
			txtMinMoistDispl.setColumns(10);
		}
		
		{
			JLabel lblMaximumMoistureValue = new JLabel(
					"Maximum Moisture Value");
			contentPanel.add(lblMaximumMoistureValue, "2, 8");
		}
		{
			sldMaxMoist = new JSlider();
			sldMaxMoist.setMinimum(0);
			sldMaxMoist.setMaximum(950);
			sldMaxMoist.setValue(500);
			contentPanel.add(sldMaxMoist, "6, 8");
		}
		{
			txtMaxMoistDispl = new JTextField();
			txtMaxMoistDispl.setText(String.valueOf(sldMaxMoist.getValue()));
			txtMaxMoistDispl.setEditable(false);
			contentPanel.add(txtMaxMoistDispl, "4, 8, fill, default");
			txtMaxMoistDispl.setColumns(10);
		}
		
		sldMinMoist.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(sldMinMoist.getValue() > sldMaxMoist.getValue()){
					sldMinMoist.setValue(sldMaxMoist.getValue());
					sldMinMoist.updateUI();
				}
				txtMinMoistDispl.setText(String.valueOf(sldMinMoist.getValue()));
			}
		});
		sldMaxMoist.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(sldMaxMoist.getValue()<sldMinMoist.getValue()){
					sldMaxMoist.setValue(sldMinMoist.getValue());
					sldMaxMoist.updateUI();
				}
				txtMaxMoistDispl.setText(String.valueOf(sldMaxMoist.getValue()));
			}
		});
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(ValidateDescr() && 
								ValidateId()) {

							Pot p = new Pot(txtPotDescr.getText(), Integer
									.parseInt(txtPotId.getText()), sldMinMoist
									.getValue(), sldMaxMoist.getValue());

							try {
								cntx.addPot(p);
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(
										(Component) e.getSource(),
										"Problem in adding port: "
												+ e1.getMessage(), "Error",
										JOptionPane.ERROR_MESSAGE);
							} finally {
								me.dispose();
							}
						}

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						me.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	/** Validateion if no description has been given
	 * @return
	 */
	private boolean ValidateDescr(){
		if(txtPotDescr.getText().equals("")){
			JOptionPane.showMessageDialog(
					me,
					"Pot Description cannot be empty", "Error",
					JOptionPane.ERROR_MESSAGE);
		
			return false;
		}
		else
			return true;
	}
	
	/**
	 * Id validation
	 */
	private boolean ValidateId(){
		
		try{
			int i = Integer.parseInt(txtPotId.getText());
			if(i<0 || i>5){
				JOptionPane.showMessageDialog(
						me,
						"Id should be between 0 and 5", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}else{
				
				return true;
			}
		}catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(
					me,
					"Id should be in number format", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	/**
	 * validation to see if max > min
	 */
	/*private boolean ValidateLimits(){
		
		if(sldMaxMoist.getValue() <= sldMinMoist.getValue()){
			JOptionPane.showMessageDialog(
					me,
					"Max moisture value should be greater than Min one", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
			return true;
			
	}*/

}
