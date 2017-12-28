package processes;

import java.util.HashMap;
import java.util.LinkedList;

import amazonSmartShelfs.amasmart.AmaSmart;
import data.Robot;
import sensors.RFID;
import data.Product;
import data.Order;

public class ProductRunning extends Thread {

	private Robot robot;
	private Product product;
	private Order order;

	public ProductRunning(Robot robot, Product product, Order order) {
		this.robot = robot;
		this.product = product;
		this.order = order;

	}

	public boolean needcheck() {
		return false;
	}

	public boolean collectProductFromShelf_activity(Product product, int shelfid) {
	
		/* collect product from shelf (activity) */

		
		// mark begining of activity using RFID sensor
		int eventId = RFID.recordBegining(product.getId(), "collector", 1, "clerk",AmaSmart.clerkID,"product",product.getId());
		
		
	
		
		try {
			Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("collectProductFromShelf"), -1));
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		RFID.recordEnd(eventId);

		

		return true;
	}

	public boolean extraCheck_activity(Product product) {
		/// need RFID sensor here to mark begning and end of this activity
		/* collect product from shelf (activity) */
		
		int eventId = RFID.recordBegining(product.getId(), "extraCheck", 1, "clerk",AmaSmart.clerkID, "product",product.getId());
		
		
		try {
			Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("extraCheck"), -1));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RFID.recordEnd(eventId);
		

	

		return true;
	}

	public boolean packageProduct_activity(Product product) {
		/// need RFID sensor here to mark begining and end of this activity
		/* collect product from shelf (activity) */
		
		int eventId = RFID.recordBegining(product.getId(), "packageDone", 1,"clerk",AmaSmart.clerkID, "product",product.getId());
		
		try {
			Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("packageProduct"), -1));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RFID.recordEnd(eventId);

		
	   eventId = AmaSmart.log.newOrderLogEvent(product.getId(), "Product Delivered", 0,"clerk",1,"product",product.getId(), null, product.getId());
		AmaSmart.log.logEventDone(eventId);
	

		return true;
	}
	
	
	public void run() {

		
		boolean needcheck = false;


		/* Shelf containing product received (message/receive) */
		robot.shelfContainingProductReceived(product,product.getId());
		//AmaSmart.log.newOrderLogEvent(product.getId(), "receive response from robot ", 2, "clerk",AmaSmart.clerkID,"robot",robot.getId(), null);

		int shelfid = robot.getLocation().getId();
		
		/* collect product from shelf (activity) */
		collectProductFromShelf_activity(product,shelfid);
		
		
		
		/* Dispose shelf (message to shelf)(message/send)  (ignore) */
		robot.getLocation().DisposeShelf(product.getId());

		/* Dispose shelf (message to robot)(message/send) */
		AmaSmart.log.newOrderLogEvent(product.getId(), "disposeShelf", 2, "clerk",AmaSmart.clerkID,"robot",robot.getId(), null, product.getId());
		robot.disposeShelf(product.getId());

		
		/* wait for the last check */
		synchronized (RFID.ExtraChecklock.get(product.getId())) {
			try {
				while (RFID.ExtraChecklockNotify.get(product.getId()) == 0) {
					RFID.ExtraChecklock.get(product.getId()).wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (RFID.ExtraChecklockNotify.get(product.getId()) == 1) {
			//	needcheck = true;
			}
			
			RFID.ExtraChecklockNotify.put(product.getId(), 0);

		
		 /* check the product artificat */
			int eventId = 	AmaSmart.log.newOrderLogEvent(product.getId(), "check if the product was shaked", 0,"clerk",1,"product",product.getId(), null, product.getId());		
			AmaSmart.log.logEventDone(eventId);
		    if(product.isExtraCheckRequired()) needcheck = true ; else needcheck = false ;
		
			if (needcheck) {
				/* extra check (activity) */
				this.extraCheck_activity(product);
			}

			/* package product (activity) */
			this.packageProduct_activity(product);
			
			robot.instanceDone(product.getId());
			
		
		}
	}

	

}
