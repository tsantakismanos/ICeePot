package iceepotpc.ui;

import java.util.ArrayList;

import iceepotlib.gateway.*;

/** Interface to be implemented 
 * by those UI elements that need to perform Server
 * tasks, they possess a progressbar and they want to 
 * have the server task to update that bar 
 * @author tsantakis
 *
 */
public interface Updatable {
	
	public void updateProgressBar();
	public void updateMeasurementData(ArrayList<Measurement> measurements, Exception exceptionFromServer);

}
