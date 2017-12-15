package utils;

import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.math3.distribution.BetaDistribution;

import amazonSmartShelfs.amasmart.AmaSmart;

public class Clock extends Thread {

	Calendar inittimevalue ;
	


	public  synchronized  Calendar getInittimevalue() {
		return inittimevalue;
	}

	public synchronized void setInittimevalue(Calendar inittimevalue) {
		this.inittimevalue = inittimevalue;
	}
	
	
	public synchronized  int sleepTimeFromBetaDistribution(HashMap<String, Double> timedata, int fraction){

		double min = timedata.get("min");
		double max = timedata.get("max");
		double average = timedata.get("average");
		double mode = timedata.get("mode");
		
	
		
	    double alpha = ((average - min)*(2*mode - min - max))/((mode - average)*(max-min)) ; 
	    double beta = alpha*(max-average)/(average-min) ;
	    
	    BetaDistribution betadist = new BetaDistribution(alpha, beta);
       
            double x = Math.random();
            double b = min + (max-min)*betadist.inverseCumulativeProbability(x);
            
		
		int ret = (int) Math.round(b) ; 
		if(fraction!=-1){
			ret =  (int) Math.round(b/fraction) ;
		}
		
		return  ret*AmaSmart.clockPrecision;  
	}

	
}
