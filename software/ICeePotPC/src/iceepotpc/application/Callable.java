package iceepotpc.application;

import iceepotpc.servergw.Meauserement;

import java.util.ArrayList;

/** Interface to be implemented 
 * by those UI elements that need to perform Server
 * tasks, they possess a progressbar and they want to 
 * have the server task to update that bar 
 * @author tsantakis
 *
 */
public interface Callable {
	
	public void updateProgressBar();
	public void updateMeasurementData(ArrayList<Meauserement> measurements);

}
