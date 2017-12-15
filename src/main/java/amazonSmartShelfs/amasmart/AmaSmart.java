package amazonSmartShelfs.amasmart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.Random;

import data.Order;
import data.Product;
import data.Robot;
import data.RobotStatus;
import data.Shelf;
import processes.ProductRunning;
import processes.RobotRunning;
import processes.Running;
import processes.ShelfRunning;
import sensors.Accelerometer;
import sensors.RFID;
import threadPool.ThreadPool;

public class AmaSmart {
	
	/*
	 *  Important: Always delete the old logs before generating new ones!
	 */

	/*
	 *  Randomness and Queuing parameters
	 *
	 */
	
	public static int clockGranularity = 60 ; /// 60 second -> 1 second
	public static int serviceRate = 200 ; // the average number of cases to be processed per time unit
	public static double averageTimeBetweenTwoSubsequentCases = 4 ; // the average time (in minute) between two subsequent cases (1/lambda)
	public static double balkingRate = 0.01 ; // the average number of cases that suddenly terminate before being completed
	/* Beta distribution is used to generate activities execution time 
	 * For each activity name, input the min, the max, the average, and the mode*/
    public static final HashMap<String, HashMap<String, Double>> times = new HashMap<String, HashMap<String, Double>>(){{
	    	put("collectProductFromShelf",new HashMap<String, Double>(){{
	    		put("min",1.0);
	    		put("max",15.0);
	    		put("average",2.0);
	    		put("mode",1.5);
	    	}});
	    	
	    	put("extraCheck",new HashMap<String, Double>(){{
	    		put("min",1.0);
	    		put("max",10.0);
	    		put("average",6.0);
	    		put("mode",5.0);
	    	}});
	    	
	    	put("packageProduct",new HashMap<String, Double>(){{
	    		put("min",3.0);
	    		put("max",15.0);
	    		put("average",10.0);
	    		put("mode",8.0);
	    	}});
	    	
	    	put("putDownCurrentSHelf",new HashMap<String, Double>(){{
	    		put("min",0.5);
	    		put("max",10.0);
	    		put("average",4.0);
	    		put("mode",3.0);
	    	}});
	    	
	    	put("goToAppropriateShelf",new HashMap<String, Double>(){{
	    		put("min",2.0);
	    		put("max",15.0);
	    		put("average",10.0);
	    		put("mode",8.0);
	    	}});
	    	
	    	put("moveShelfTodock",new HashMap<String, Double>(){{
	    		put("min",2.0);
	    		put("max",15.0);
	    		put("average",10.0);
	    		put("mode",7.0);
	    	}});
	    	
	    	put("moveShelfFromClerkDock",new HashMap<String, Double>(){{
	    		put("min",2.0);
	    		put("max",15.0);
	    		put("average",10.0);
	    		put("mode",7.0);
	    	}});
	    	
	    	put("requestProduct",new HashMap<String, Double>(){{
	    		put("min",1.0);
	    		put("max",25.0);
	    		put("average",15.0);
	    		put("mode",8.0);
	    	}});
	    	
	    	put("notify",new HashMap<String, Double>(){{
	    		put("min",0.0);
	    		put("max",1.0);
	    		put("average",0.7);
	    		put("mode",0.2);
	    	}});
	    }};
	    public static int clockPrecision = 10 ; // in ms (decrease for faster execution) 
	    
	    
	
	/* 
	 * 
	 * Use case Parameters
	 * 
	 */

		static int numberOfCases = 1500 ; 
		static int numberOfShelfs = 500 ; 
		static int NumberofRobots = 100 ;
		static int productsPerShelf = 20 ; // should match with the number of products in inventory numberOfProductsInInventory such that no product is left without shelf
	    public static double shakeratio = 0.3 ; 

	 /* extra logging parameters */ 
	    
	  	static boolean seperateLog = true ;
	  	static boolean logbyprocess = true ;
	  	static boolean productArtifact = true ;
	  	
	  	
   /* initiate artificial clock */
	public static utils.Clock clock ;
		
		
	/* catalogue */
	private static HashMap<Integer, Integer[]> catalogue = new HashMap<Integer, Integer[]>();  // key: product-id, value: shelf-id
	
	/* init logger */
	public static utils.Logger log;
	
   public static int clerkID = 0 ;
	

  
    public static void main(String[] args) throws InterruptedException {
    	
	
    	/* Initiate artificial clock */
    	clock = new utils.Clock();
    	Thread clockrunning = new utils.ClockRunning(clock);
    	clockrunning.start();
    	
    	/* Initiate log */
    	log = new utils.Logger(clock,seperateLog,logbyprocess,productArtifact);

    	/* create Shelfs, fill them with products, start thread for each shelf */
    	
    	int numberOfProductsInStock = numberOfShelfs*productsPerShelf;
    	
    	 LinkedList<Shelf> shelfs = initshelfs(numberOfShelfs,numberOfProductsInStock);

    	
         /* Generate order from scratch  */	  
      Order order = generateOrderFromScratch(numberOfCases,numberOfProductsInStock,shelfs);
     
      /* Start Robots  */
        LinkedList<Robot> robots = new LinkedList<Robot>();
        for(int i=0;i<NumberofRobots;i++){
        
       
        	RFID.goTonextLock.put(i, new Object());
        	RFID.goTonextLockNotify.put(i, false);

          	RFID.useRobotLock.put(i, new Object());
        	RFID.useRobotLockNotify.put(i, false);
         
        	
        	Robot r = new Robot(i,catalogue,shelfs); 
        	robots.add(r);
        
     
        }

        /* set clerk id */
        clerkID = 1 ;
        
        /* Request product (activity) */
        
        requestProduct_activity(order,robots);
       
       /* Find next activities/messages in productRunning.java class */
        
        
    }
    
    
    public static  void lockShelf(Shelf shelf,Product product,Robot robot) {
  		/* Shelf being moved (message) */
      	shelf.setState(1);
  		ShelfRunning ss = new ShelfRunning(shelf,shelf.getId(),product,robot);
  		shelf.execute(ss);
  	}
    
    public static void requestProduct_activity(Order order,LinkedList<Robot> robots) throws InterruptedException{
    	 int i = 0;
         for(Product product : order.getOrder()){
        	
        	 RFID.ExtraChecklock.put(product.getId(), false); 
        	 RFID.ExtraChecklockNotify.put(product.getId(), 0);
        	
        	 
        	 RFID.MovingShelfLock.put(product.getId(), new Object());
             RFID.MovingShelfLockNotify.put(product.getId(),  false);
             
             RFID.robotInsideLock.put(product.getId(), new Object());
           	RFID.robotInsideLockNotify.put(product.getId(), false);
           	
            RFID.DisposeShelfLock.put(product.getId(), new Object());
            RFID.DisposeShelfLockNotify.put(product.getId(),  false);
        	 
        	RFID.robotInsideLock2.put(product.getId(), new Object());
        	RFID.robotInsideLock2Notify.put(product.getId(), false);
        	
        	 RFID.acessShelfLock.put(product.getId(), new Object());
             RFID.acessShelfLockNotify.put(product.getId(), false);
             
          	 RFID.NRDlock.put(product.getId(), new Object());
             RFID.NRDlockNotify.put(product.getId(), false);
             
            
         	}
         
     	HashMap<Integer, Thread> runningThreads = new HashMap<Integer, Thread>();
     	
     	/* Pool threads according to serviceRate */
     	ThreadPool tp = new ThreadPool(serviceRate);
     	
		for(int e=0; e<=order.getOrder().size() ; e++){
			
			if(e==order.getOrder().size()){				
			    // last iteration : wait for the robots to finish their last job 
				System.out.println("----");
				for(int r=0; r<robots.size(); r++){		
					while(robots.get(r).getQueue().size()!=0){
						Thread.sleep(100);
					}
				}
				
			 System.exit(0);
			}
			
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = clock.getInittimevalue().getTime();
			String timenow = dateFormat.format(date); 
		
			
			try {
				Thread.sleep(getPoissonRandom(averageTimeBetweenTwoSubsequentCases*AmaSmart.clockPrecision)); 
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			date = clock.getInittimevalue().getTime();
			timenow = dateFormat.format(date); 
			
			Product product = order.getOrder().get(e);
				
			int min = 0;
			int max = robots.size() - 1;
			int SelectRobot = min + (int) (Math.random() * ((max - min) + 1));
			
			/// find appropriate shelf
			Robot selectedR = robots.get(SelectRobot);
			Shelf shelf = selectedR.FindAppropriateSHelf(product);
			
			// check if both robot and shelf are occupied
			if (!selectedR.getStatus().equals(RobotStatus.terminated) && shelf.getState()!=0 ){
				e--;
				// skip case
			}
			
			else {

				Running rr = new Running(robots,SelectRobot,product,order);
				tp.execute(rr);
				
				
			}
		    
			
		

			
			
			}

    }
    
    public static LinkedList<Shelf> initshelfs(int numberOfShelfs,int numberOfProducts){
    	
       
    	LinkedList<Shelf> shelfs = new LinkedList<Shelf>();
  
    	/* create shelfs */ 
        for(int i=0;i<numberOfShelfs;i++){
            Shelf s = new Shelf(i);
            shelfs.add(s);
            // dont forget these while adding new locks
          
            RFID.reserveShelfLock.put(i, new Object());
         
  
            RFID.shelfFreelock.put(i, new Object());
            RFID.accelormeterStop.put(i, new Object());
           
            RFID.reserveShelfLockNotify.put(i, false);
          
          
            RFID.shelfFreelockNotify.put(i,  false);
            RFID.accelormeterStopNotify.put(i,  false);
            
            
            RFID.AccAckLock.put(i, new Object());
       	 	RFID.AccAckLockNotify.put(i, false);
   
           
        }	
        
        /* add products to random shelfs */ 
        
        double minP = 20; // minimum price
    	double maxP = 100; // maximum price 
    	Random randomPrice = new Random();
    	Product p ;
    	int minS = 0 ;
    	int maxS = numberOfShelfs-1 ;
    	for(int i=0;i<numberOfProducts;i++){		
    		/// create product with corresponding price
    		p = new Product(i, minP + (maxP - minP) * randomPrice.nextDouble());
    		
    		boolean findemptyshelf = false ;
    		
    		int selectShelf =   minS + (int)(Math.random() * ((maxS - minS) + 1));
    		while(findemptyshelf==false){
    		// add product
    		 // check if shelf has space
    		if(shelfs.get(selectShelf).getContent().size()<productsPerShelf){
    			shelfs.get(selectShelf).addProduct(p);
    			findemptyshelf = true ;
    		}
    		else {
        	    /// select another random shelf 
    	    	selectShelf =   minS + (int)(Math.random() * ((maxS - minS) + 1));
    		}
    		}
    		
    		Integer[] productShelfAndIndex = new Integer[2];
    		productShelfAndIndex[0] = selectShelf ;
    		productShelfAndIndex[1] = shelfs.get(selectShelf).getContent().indexOf(p) ;
    		
    		catalogue.put(i, productShelfAndIndex);
    	}
        
    
    	
        return shelfs ;
    }
    
    
    public static Order generateOrderFromScratch(int numberOfItems,int numberOfProducts, LinkedList<Shelf> shelfs){
    	
    	// create order
    	Order o1 = new Order(1);
    	// create a list with random products ids and shuffle it
    	ArrayList<Integer> rp = new ArrayList<Integer>();
    	for(int i=0;i<numberOfProducts;i++){
    		rp.add(i);
    	}
    	Collections.shuffle(rp);
    	// populate order with random products
    	Product p ;
    	LinkedList<Product> products = new LinkedList<Product>();
    	for(int i=0;i<numberOfItems;i++){
    		int productid = rp.get(i);
    		Integer[] productShelfAndIndex = catalogue.get(productid);

    		p = shelfs.get(productShelfAndIndex[0]).getContent().get(productShelfAndIndex[1]);
    		products.add(p);
    	}
    	o1.setOrder(products);
    	
    	return o1 ;
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
