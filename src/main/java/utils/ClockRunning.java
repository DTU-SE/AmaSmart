package utils;

import java.util.Calendar;

public class ClockRunning extends Thread {

	Clock clock ;
	Calendar inittimevalue ;
	int min = 1;
	int max = 60;
	
	public ClockRunning(Clock clock) {
		this.clock = clock;
		inittimevalue = Calendar.getInstance();
	}
	
	public  void run() {

		// 10 microseconds == 1 second
		 synchronized(this)  {
		while (true) {

			
			inittimevalue.add(Calendar.MINUTE, 1);
			inittimevalue.add(Calendar.SECOND, (int) (Math.random() * ((max - min) + 1)) );
			clock.setInittimevalue(inittimevalue);
			
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

		}

	}
	}
	
}
