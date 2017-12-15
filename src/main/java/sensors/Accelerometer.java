package sensors;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import amazonSmartShelfs.amasmart.AmaSmart;
import data.Shelf;

public class Accelerometer  extends Thread  implements Runnable  {

	private Shelf shelf ;

	
	
	public Accelerometer(Shelf shelf) {
		super();
		this.shelf = shelf;
	}



	public void run(){
		

			while(!Thread.currentThread().isInterrupted()){			
				/// listen for shakes 
					
			
			
				try {
					Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("moveShelfTodock"), 4));
				
				if (detectedShake()) {
					/* Shake (signal event) */
					/* Reduce speed (message) */
					AmaSmart.log.newOrderLogEvent(shelf.getId(), "Shake", 3, "Accelerometer", -1, "", -1, null); 
					shelf.setShake(true);
					break;
				}
				} catch (InterruptedException e) {
					shelf.accAck(shelf.getId());
					  Thread.currentThread().interrupt();
					break;
				}
				
				}
			
				
			
			
			shelf.accAck(shelf.getId());
			
		
		
	}
	
	
	public boolean detectedShake(){
		if( new Random().nextDouble() <= AmaSmart.shakeratio) {  
			return true ;
			}
			return false ;
	}
	
}
