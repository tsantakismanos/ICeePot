package iceepotpc.appication;

import iceepotpc.ui.MainWindow;



public class ICeePotPC {

	/**
	 * application's entry point
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		configureApplication();
		
		MainWindow.launch();

	}
	
	
	private static void configureApplication(){
		
		//initialize the pots 
		Context.potDescrs.add(new Pot("Basil", 0));
		Context.potDescrs.add(new Pot("Mint", 1));
		Context.potDescrs.add(new Pot("Benjamin", 2));
		Context.potDescrs.add(new Pot("Gold Crest", 2));
		
	}

}
