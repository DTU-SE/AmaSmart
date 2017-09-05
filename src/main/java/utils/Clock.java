package utils;

import java.util.Calendar;

public class Clock extends Thread {

	Calendar inittimevalue ;
	


	public  synchronized  Calendar getInittimevalue() {
		return inittimevalue;
	}

	public synchronized void setInittimevalue(Calendar inittimevalue) {
		this.inittimevalue = inittimevalue;
	}
	
	
	public synchronized  int guessSleepTime(int time){
		
		int range = (int) time/4 ;
	    int min = time - range ;
	    int max = time + range ;
	    
	    int result = (int) (Math.random() * ((max - min) + 1)) ;
		
		return result ;
	}

	
}
