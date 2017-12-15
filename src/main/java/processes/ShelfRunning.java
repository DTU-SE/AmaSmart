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
				
				
					
					
					
						
						/* Shake (signal/incoming) */ 	/* reduce speed (message/send) */
						shake = shelf.isShake();		

						
						if(shake){
							/*  record shake for affected products */
							int eventId = 	AmaSmart.log.newOrderLogEvent(product.getId(), "record shake for affected products", 0,"smart shelf",shelf.getId(),"products",-1, null);		
							AmaSmart.log.logEventDone(eventId);
							
							
								for(int i=0;  i<shelf.getContent().size(); i++){
									shelf.getContent().get(i).setExtraCheckRequired(true);
									AmaSmart.log.updateArtifact(shelf.getContent().get(i).getId(), shelf.getContent().get(i).getPrice(), shelf.getId(), "true");
								}
							}
						else {
							AmaSmart.log.updateArtifact(product.getId(), product.getPrice(), shelf.getId(), "false");
						}
						
						
						
									
						/* send last check */
				
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

							
							
							int response;
							if (shake) {
								response = 1;
								} else {
									response = 2;
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
