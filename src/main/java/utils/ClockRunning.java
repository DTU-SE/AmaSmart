package utils;

import java.util.Calendar;

import amazonSmartShelfs.amasmart.AmaSmart;

public class ClockRunning extends Thread {

	Clock clock ;
	Calendar inittimevalue ;

	
	public ClockRunning(Clock clock) {
		this.clock = clock;
		inittimevalue = Calendar.getInstance();
	}
	
	public  void run() {

		
		 synchronized(this)  {
		while (true) {
			
			
			inittimevalue.add(Calendar.SECOND, AmaSmart.clockGranularity);
			clock.setInittimevalue(inittimevalue);
			
			try {
				Thread.sleep(AmaSmart.clockPrecision); // clock delay to align with the artificial clock
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

		}

	}
	}
	
}
