package amazonSmartShelfs.amasmart;

import java.util.Random;

public class poissonProcessTest {

	  public static void main(String[] args) {
		  
		  for(int i=0;i<1000;i++)
		  System.out.println(getPoissonRandom(5.0));
		  
	  }
	  
	  private static int getPoissonRandom(double mean) {
	        Random r = new Random();
	        double L = Math.exp(-mean);
	        int k = 0;
	        double p = 1.0;
	        do {
	            p = p * r.nextDouble();
	            k++;
	        } while (p > L);
	        return k - 1;
	    }
	
}
