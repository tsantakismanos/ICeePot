package iceepotpc.appication;




import iceepotpc.ui.MainWindow;



public class ICeePotPC {

	/**
	 * application's entry point
	 * @param args
	 */
	public static void main(String[] args) {
		
		Context c = new Context();
		
		MainWindow window = new MainWindow(c);
		
		//add the main window to the observers collection
		c.uiElements.add(window);
	}
	
	


}
