package iceepotpc.ui;

import iceepotpc.application.Context;

import javax.swing.JDialog;

import javax.swing.JLabel;

import javax.swing.JList;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RemovePotDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JList list;

	private Context cntx;

	public RemovePotDialog() {

		try {
			cntx = Context.getInstance();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		setIconImage(Toolkit.getDefaultToolkit().getImage(
				RemovePotDialog.class
						.getResource("/icons/ICeePot_logo_new.png")));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Remove Pot");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel pnlPots = new JPanel();
		getContentPane().add(pnlPots, BorderLayout.CENTER);
		pnlPots.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("left:default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"), }));

		JLabel lblRemPot = new JLabel(
				"Please select one or more pots to remove");
		pnlPots.add(lblRemPot, "2, 2");

		// get the pots
		DefaultListModel model = new DefaultListModel();

		for (int i = 0; i < cntx.getPots().size(); i++) {

			model.addElement(cntx.getPots().get(i).getId() + " \t "
					+ cntx.getPots().get(i).getDescr());
		}

		// fill the list
		list = new JList(model);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		pnlPots.add(list, "2, 4, 3, 1, fill, center");

		JPanel pnlButtons = new JPanel();
		FlowLayout fl_pnlButtons = (FlowLayout) pnlButtons.getLayout();
		fl_pnlButtons.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(pnlButtons, BorderLayout.SOUTH);

		JButton btnDone = new JButton("OK");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] selected_to_go = list.getSelectedValues();

				for (int i = 0; i < selected_to_go.length; i++) {

					String s = (String) selected_to_go[i];
					s = s.substring(0, s.indexOf(" "));

					try {
						cntx.removePot(Integer.parseInt(s));
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(RemovePotDialog.this,
								e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}

				RemovePotDialog.this.dispose();
			}
		});
		pnlButtons.add(btnDone);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				RemovePotDialog.this.dispose();
			}

		});
		pnlButtons.add(btnCancel);
	}

}
