package sensors;

import java.util.Calendar;
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
				Thread.sleep(AmaSmart.moveShelfTodock_duration/4);
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
			
				/*
				synchronized(RFID.accelormeterStop.get(shelf.getId())){
					RFID.accelormeterStopNotify.put(shelf.getId(), true);
					RFID.robotInsideLock.get(shelf.getId()).notify(); 
					
				}
				*/
			
			
			shelf.accAck(shelf.getId());
			
		
		
	}
	
	
	public boolean detectedShake(){
		
		/* 25% chance for shake */
		
		int min = 1; 
    	int max = 4;
    	
    	int randomvalue = min + (int)(Math.random() * ((max - min) + 1));
    	
    	if(randomvalue==1){
    		return true ;
    	}
		
		return false ;
	}
	
}
