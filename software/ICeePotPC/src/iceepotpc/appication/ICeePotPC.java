package iceepotpc.appication;




import iceepotpc.ui.MainWindow;



public class ICeePotPC {

	/**
	 * application's entry point
	 * @param args
	 */
	public static void main(String[] args) {
		
		Context c = Context.getInstance();
		
		MainWindow window = new MainWindow();
		
		//add the main window to the observers collection
		c.registerObserver(window);
	}
	
	


}
