package iceepotpc.ui;

import iceepotpc.appication.Context;

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


public class SettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtServerHostName;
	private JTextField txtServerPort;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			SettingsDialog dialog = new SettingsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Projects\\various\\ICeePot\\software\\ICeePotPC\\icons\\ICeePot_logo_new.png"));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Settings");
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
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
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
			JLabel lblServerHostName = new JLabel("Server host name (or IP)");
			contentPanel.add(lblServerHostName, "2, 4");
		}
		{
			txtServerHostName = new JTextField();
			contentPanel.add(txtServerHostName, "4, 4, 8, 1");
			txtServerHostName.setColumns(10);
			txtServerHostName.setText(Context.serverHost);
		}
		{
			JLabel lblServerPort = new JLabel("Server Port");
			contentPanel.add(lblServerPort, "2, 8");
		}
		{
			txtServerPort = new JTextField();
			contentPanel.add(txtServerPort, "4, 8, fill, default");
			txtServerPort.setColumns(10);
			txtServerPort.setText(String.valueOf(Context.serverPort));
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(txtServerHostName.getText().equals("") || txtServerPort.getText().equals(""))
							JOptionPane.showMessageDialog((Component) e.getSource(), "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
						else
						{
							Context.serverHost = txtServerHostName.getText();
							Context.serverPort = Integer.parseInt(txtServerPort.getText());
							Window[] w = JWindow.getWindows();
							for(int i=0; i<w.length; i++)
								if(w[i].isVisible() && w[i].getClass() == SettingsDialog.class)
									w[i].dispose();
							Context.updateServerSettings();
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
							if(w[i].isVisible() && w[i].getClass() == SettingsDialog.class)
								w[i].dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
