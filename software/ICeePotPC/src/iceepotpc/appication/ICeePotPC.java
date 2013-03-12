package iceepotpc.appication;





import javax.swing.JOptionPane;
import javax.swing.JPanel;

import iceepotpc.ui.MainWindow;



public class ICeePotPC {

	/**
	 * application's entry point
	 * @param args
	 */
	public static void main(String[] args) {
		
		Context c;
		try {
			c = Context.getInstance();
			MainWindow window = new MainWindow();
			
			//add the main window to the observers collection
			c.registerObserverForNewPots(window);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JPanel(),
					e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
	
	


}
