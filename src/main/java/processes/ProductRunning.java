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
		int eventId = RFID.recordBegining(product.getId(), "collect product from shelf", 1, "clerk",AmaSmart.clerkID,"shelf",shelfid);
		
		
	
		
		try {
			Thread.sleep(AmaSmart.clock.guessSleepTime(AmaSmart.collectProductFromShelf_duration));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RFID.recordEnd(eventId);

		

		return true;
	}

	public boolean extraCheck_activity(Product product) {
		/// need RFID sensor here to mark begning and end of this activity
		/* collect product from shelf (activity) */
		
		int eventId = RFID.recordBegining(product.getId(), "do extra check", 1, "clerk",AmaSmart.clerkID, "product",product.getId());
		
		
		try {
			Thread.sleep(AmaSmart.clock.guessSleepTime(AmaSmart.extraCheck_duration));
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
		
		int eventId = RFID.recordBegining(product.getId(), "Package product", 1,"clerk",AmaSmart.clerkID, "product",product.getId());
		
		try {
			Thread.sleep(AmaSmart.clock.guessSleepTime(AmaSmart.packageProduct_duration));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RFID.recordEnd(eventId);

	

		return true;
	}
	
	
	public void run() {

		
		boolean needcheck = false;

		
		

		/* Shelf containing product received (message/receive) */
		robot.shelfContainingProductReceived(product,product.getId());
		AmaSmart.log.newOrderLogEvent(product.getId(), "receive response from robot ", 2, "clerk",AmaSmart.clerkID,"robot",robot.getId(), null);

		int shelfid = robot.getLocation().getId();
		
		/* collect product from shelf (activity) */
		collectProductFromShelf_activity(product,shelfid);
		
		
		
		/* Dispose shelf (message to shelf)(message/send) */
		AmaSmart.log.newOrderLogEvent(product.getId(), "send dispose shelf (to shelf)", 2, "clerk",AmaSmart.clerkID,"shelf",shelfid, null);
		robot.getLocation().DisposeShelf(product.getId());

		/* Dispose shelf (message to robot)(message/send) */
		AmaSmart.log.newOrderLogEvent(product.getId(), "send dispose shelf (to robot)", 2, "clerk",AmaSmart.clerkID,"robot",robot.getId(), null);
		robot.disposeShelf(product.getId());

		
		/* Require extra check condition (event-based gatway) */
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
				needcheck = true;
			}
			
			RFID.ExtraChecklockNotify.put(product.getId(), 0);

			/* extra check required (message received) */
			if (needcheck) {
				/* extra check (activity) */
				AmaSmart.log.newOrderLogEvent(product.getId(), "receive extra check required", 2, "clerk",AmaSmart.clerkID,"shelf",shelfid, null);
				this.extraCheck_activity(product);
			}
			else {
				AmaSmart.log.newOrderLogEvent(product.getId(), "receive no extra check required", 2, "clerk",AmaSmart.clerkID,"shelf",shelfid, null);
			}

			/* package product (activity) */
			this.packageProduct_activity(product);
			
			//robot.goToNext();
			//product.goToNext();
		}
	}

	

}
