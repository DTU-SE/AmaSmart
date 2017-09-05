package processes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import amazonSmartShelfs.amasmart.AmaSmart;
import data.Product;
import data.Robot;
import data.Shelf;
import sensors.Accelerometer;
import sensors.RFID;

import java.sql.Timestamp;
import java.util.Date;

public class ShelfRunning implements Runnable {

	private Shelf shelf;
	private int id;
	private boolean shake;
	private Product product ;
	private Robot robot ;

	public ShelfRunning(Shelf shelf, int id,Product product,Robot robot) {
		// TODO Auto-generated constructor stub
		super();
		this.shelf = shelf;
		this.id = id;
		this.product = product ;
		this.robot = robot ;
	}

	public void run() {
	
			
			/* Shelf scheduling */

				shelf.setShake(false);
			    shelf.proceedRobot(product.getId());
				
				

				// state 1
				
				
				synchronized (RFID.reserveShelfLock.get(id)) {
					try {
						while (!RFID.reserveShelfLockNotify.get(id))
							RFID.reserveShelfLock.get(id).wait();
						RFID.reserveShelfLockNotify.put(id, false);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// state 2
					
					
					
					/* Shelf being moved (message/receive) */
					AmaSmart.log.newOrderLogEvent(product.getId(), "receive shelf being moved from robot", 2,"smart shelf",shelf.getId(),"robot",robot.getId(), null);
				

					Thread acc = new Accelerometer(shelf);
					acc.start();

					/* Shelf delivered to destination (message/receive) */
					synchronized (RFID.MovingShelfLock.get(product.getId())) {
						try {
							while (!RFID.MovingShelfLockNotify.get(product.getId()))
								RFID.MovingShelfLock.get(product.getId()).wait();
								RFID.MovingShelfLockNotify.put(product.getId(), false);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					acc.interrupt();
				
					
						AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "receive shelf delivered from robot", 2,"smart shelf",shelf.getId(),"robot",robot.getId(), null);
						
						/* Shake (signal/incoming) */ 	/* reduce speed (message/send) */
						shake = shelf.isShake();		

						// Interrupt the accelerometer once the moving is over
						

						/* Dispose shelf (message/receive) */
						int productid = shelf.getProduct().getId();
							synchronized (RFID.DisposeShelfLock.get(productid)) {
							try {
								while (!RFID.DisposeShelfLockNotify.get(productid))
									RFID.DisposeShelfLock.get(productid).wait();
								RFID.DisposeShelfLockNotify.put(productid, false);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "receive dispose shelf from clerk", 2,"smart shelf",shelf.getId(),"clerk",1, null);
						
							/*
							 * Send message whether an extra check is required
							 * or not
							 */

							
							// call notify with product id
							int response;
							if (shake) {
								response = 1;
								AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "send extra check required to clerk", 2,"smart shelf",shelf.getId(),"clerk",1, null);
								} else {
									response = 2;
									AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "send no extra check required to clerk", 2,"smart shelf",shelf.getId(),"clerk",1, null);
									}
							
						    /* extra check required or not (message/send)*/
							product.sendExtraCheckToClerk(response);
			
							/* free shelf */
							shelf.freeshelf();
							
							
							
							
					
					
				}
			}

		}

	}

}
