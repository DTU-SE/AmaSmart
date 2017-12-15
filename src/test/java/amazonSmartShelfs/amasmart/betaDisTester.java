package amazonSmartShelfs.amasmart;
import org.apache.commons.math3.distribution.BetaDistribution;


public class betaDisTester {


    public static void main(String[] args) {
    	
    	double min = 5;
		double max = 25;
		double average = 20;
		double mode = 25;
    	
    	 double alpha = ((average - min)*(2*mode - min - max))/((mode - average)*(max-min)) ; 
 	    double beta = alpha*(max-average)/(average-min) ;
    	
    	System.out.println("alpha "+alpha);
    	System.out.println("beta" +beta);
 	    
	    BetaDistribution betadist = new BetaDistribution(alpha, beta);
       
            double x = Math.random();
            double b = min + (max-min)*betadist.inverseCumulativeProbability(x);
        
        System.out.println(b);
        
    }
}
