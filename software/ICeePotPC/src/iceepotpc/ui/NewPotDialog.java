package iceepotpc.ui;

import iceepotpc.appication.Context;
import iceepotpc.appication.Pot;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
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

public class NewPotDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtPotDescr;
	private JTextField txtPotPin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewPotDialog dialog = new NewPotDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewPotDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Projects\\various\\ICeePot\\software\\ICeePotPC\\icons\\ICeePot_logo_new.png"));
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
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
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
			contentPanel.add(lblPotDescr, "2, 4");
		}
		{
			txtPotDescr = new JTextField();
			contentPanel.add(txtPotDescr, "4, 4, 5, 1, fill, default");
			txtPotDescr.setColumns(10);
		}
		{
			JLabel lblPotPin = new JLabel("Pin Connected to (0..5)");
			contentPanel.add(lblPotPin, "2, 8");
		}
		{
			txtPotPin = new JTextField();
			contentPanel.add(txtPotPin, "4, 8, fill, default");
			txtPotPin.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(txtPotDescr.getText().equals("") || txtPotPin.getText().equals(""))
							JOptionPane.showMessageDialog((Component) e.getSource(), "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
						else{
							
							Pot p = new Pot(txtPotDescr.getText(), Integer.parseInt(txtPotPin.getText()));
							Context.potDescrs.add(p);
							
							Window[] w = JWindow.getWindows();
							for(int i=0; i<w.length; i++)
								if(w[i].isVisible() && w[i].getClass() == NewPotDialog.class)
									w[i].dispose();
							
							Context.addPot(p);
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
						Window[] w = JWindow.getWindows();
						for(int i=0; i<w.length; i++)
							if(w[i].isVisible() && w[i].getClass() == NewPotDialog.class)
								w[i].dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
