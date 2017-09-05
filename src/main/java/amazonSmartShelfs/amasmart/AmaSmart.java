package amazonSmartShelfs.amasmart;

import java.util.ArrayList;
import java.util.Collections;
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
import processes.ShelfRunning;
import sensors.Accelerometer;
import sensors.RFID;

public class AmaSmart {
	
	/* notes
	 * two additional locks have been added - not useful, might be deleted.
	 * controlling Throughput might be implemented 
	 
	
	/* Config parameters */
	  	static boolean seperateLog = false ;
	  	static boolean logbyprocess = true ;
		static int numberOfproducts = 1000 ; 
		static int numberOfProductsInInventory = 1400 ; 
		static int numberOfShelfs = 20 ; 
		static int NumberofRobots = 8 ;
	  
	
	/* Activities duration (corresponding to thread sleep value) approximation of [+25% -25%] is used for simulation */
	// clerk process activities
		//  requestProduct_duration depends on robot availability
		public static int collectProductFromShelf_duration = 50 ;
		public static int extraCheck_duration = 100 ;
		public static int packageProduct_duration = 70 ;
   /// robot process activities
		public static int putDownCurrentSHelf_duration = 20;
		public static int goToAppropriateShelf_duration = 40;
		public static int moveShelfTodock_duration = 40 ;
		public static int moveShelfFromClerkDock_duration = 10;
		public static int requestProduct_duration = 10 ;
        public static int notify_duration = 4 ;
	    
		/* initiate artificial clock */
		public static utils.Clock clock ;
		
		
	/* catalogue */
	private static HashMap<Integer, Integer[]> catalogue = new HashMap<Integer, Integer[]>();  // key: product-id, value: shelf-id
	
	/* init logger */
	public static utils.Logger log;
	
   public static int clerkID = 0 ;
	
   /* Init RFID sensor for active sensors  */
  
   
    public static void main(String[] args) throws InterruptedException {
    	
    	
    	/* Initiate artificial clock */
    	clock = new utils.Clock();
    	Thread clockrunning = new utils.ClockRunning(clock);
    	clockrunning.start();
    	
    	/* Initiate log */
    	log = new utils.Logger(clock,seperateLog,logbyprocess);
    	
    	

    	/* create Shelfs, fill them with products, start thread for each shelf */
    	
    	 LinkedList<Shelf> shelfs = initshelfs(numberOfShelfs,numberOfProductsInInventory);
//         for(Shelf shelf : shelfs){
//        	 Thread sruning = new ShelfRunning(shelf,shelf.getId());
//        	 sruning.start();
//         }
    	
         /* Generate order from scratch  */	  
      Order order = generateOrderFromScratch(numberOfproducts,numberOfProductsInInventory,shelfs);
     
      
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
    
    public static void requestProduct_activity(Order order,LinkedList<Robot> robots){
    	 int i = 0;
         for(Product product : order.getOrder()){
        	 // dont forget to put(getid,value) when adding a new log
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
      
        	
         	}
         
     	HashMap<Integer, Thread> runningThreads = new HashMap<Integer, Thread>();
     	
		for(int e=0; e<order.getOrder().size() ; e++){
			
			Product product = order.getOrder().get(e);
			
			int eventId = log.newOrderLogEvent(product.getId(), "Request product", 0,"clerk",clerkID,"product",product.getId(), null);
			try {
				Thread.sleep(clock.guessSleepTime(requestProduct_duration));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			log.logEventDone(eventId);
			int min = 0;
			int max = robots.size() - 1;
			int SelectRobot = min + (int) (Math.random() * ((max - min) + 1));
			
			
			/* Send product request$	$	 to robot (message/send) */
			AmaSmart.log.newOrderLogEvent(product.getId(), "send request to robot", 2, "clerk",AmaSmart.clerkID,"robot",SelectRobot, null);
			RobotRunning rr = new RobotRunning(robots.get(SelectRobot).getId(),robots.get(SelectRobot),product);
			robots.get(SelectRobot).execute(rr);
			
			Thread productTr = new ProductRunning(robots.get(SelectRobot), product, order);
			productTr.start();

			
			
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
    	    /// select random shelf where to add the product
    		int selectShelf =   minS + (int)(Math.random() * ((maxS - minS) + 1));
    		// add product
    		
    		shelfs.get(selectShelf).addProduct(p);
    		
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
}
