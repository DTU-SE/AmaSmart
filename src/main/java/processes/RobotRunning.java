package processes;

import java.util.HashMap;

import amazonSmartShelfs.amasmart.AmaSmart;
import data.Product;
import data.Robot;
import data.RobotStatus;
import data.Shelf;
import sensors.RFID;

public class RobotRunning  implements Runnable {

    private int robotruningid ;
	private Robot robot ;
	private Product product;
	

	public RobotRunning(int robotruningid, Robot robot,Product product) {

		this.robotruningid = robotruningid;
		this.robot = robot;
		this.product = product ;
		

	}

	public int getRobotruningid() {
		return robotruningid;
	}


	public void setRobotruningid(int robotruningid) {
		this.robotruningid = robotruningid;
	}

   public void putdownCurrentShelf_activity(int productid, int shelfid){
	   /* put down current shelf */
	
		int eventId = AmaSmart.log.newOrderLogEvent(productid, "putdown current shelf", 0, "robot",robot.getId(),"shelf",shelfid, null, productid);
		try {
			Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("putDownCurrentSHelf"), -1));
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		AmaSmart.log.logEventDone(eventId);
		
	
   }

   
   public void goToAppropriateShelf_activity(Shelf shelf, int productid){
		int eventId = AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "go to approriate shelf", 0,"robot",robot.getId(),"shelf",shelf.getId(), null, productid);
		/* Set Robot location to shelf */
		
		/* Go to shelf (activitiy) */
		robot.setStatus(RobotStatus.moving);
		try {
			Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("goToAppropriateShelf"), -1));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AmaSmart.log.logEventDone(eventId);
	    AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "shelfMoved", 2,"shelf",shelf.getId(),"robot",robot.getId(), null, product.getId());
		
   }
   
   
   public void moveshelftoDock_activity(Shelf shelf,int productid){
				/* Move Shelf to dock */

				robot.getLocation().moveShelf();
			

				int eventId1 = AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "move shelf to dock", 0,"robot",robot.getId(),"shelf",shelf.getId(), null, productid);
				try {
					Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("moveShelfTodock"), -1));
				} catch (InterruptedException e) {
				
					e.printStackTrace();
				}
				

				// if shelf has been shacked
				if (shelf.isShake()) {
					shelf.setStopAccelerometer(true);
				


					robot.setSpeed(robot.getSpeed() / 2); 
					int eventId2 = AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "reduce speed", 0,"robot",robot.getId(),"shelf",shelf.getId(), null, product.getId());
					try {
						Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("moveShelfTodock"), -1));
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					
					AmaSmart.log.logEventDone(eventId2);

				}
				

				
				AmaSmart.log.logEventDone(eventId1);
			
   }
   
   public boolean moveShelfFromClerkDock_activity(Shelf shelf) {
	  int eventId =  AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "move shelf from dock", 0, "robot",robot.getId(),"shelf",shelf.getId(), null, product.getId());
		try {
			Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("moveShelfFromClerkDock"), -1));
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
		AmaSmart.log.logEventDone(eventId);

		
		return true;
	}
   
	public void run() {

			System.out.println(" Request product"+product.getId());
		
			int eventId = AmaSmart.log.newOrderLogEvent(product.getId(), "Request product", 0, "clerk", 1, "product",
				product.getId(), null, product.getId());
			try {
				Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("requestProduct"), -1));
			} catch (InterruptedException e1) {
			
			e1.printStackTrace();
			}

			AmaSmart.log.logEventDone(eventId);
		
					Shelf shelf = robot.FindAppropriateSHelf(product);
					
					// wait until the shelf free
					
					 eventId =  AmaSmart.log.newOrderLogEvent(product.getId(), "Assgin robot", 0,"robot",robot.getId(),"shelf",shelf.getId(), null, product.getId());
				
					 
					 AmaSmart.log.logEventDone(eventId);
					
					
						/*
						 * Is robot under the shelf containing the required
						 * product (XOR gateway)
						 */
						boolean robotundershelf = false;
						int oldshelfid = -1 ;

						if (robot.getLocation() == null) {
							robotundershelf = false;
						} else if (robot.getLocation() != null && robot.getLocation().getId() != shelf.getId()) {
							robotundershelf = false;
							oldshelfid = robot.getLocation().getId() ;
						} else if (robot.getLocation().getId() == shelf.getId()) {
							robotundershelf = true;
						} else {
							
						}
						
						 
						
						 
						robot.setLocation(shelf);
						
						/* Reserve shelf */
						AmaSmart.lockShelf(shelf,product,robot);
						
						
						synchronized (RFID.acessShelfLock.get(product.getId())) {
							try {
								while (!RFID.acessShelfLockNotify.get(product.getId()))
									RFID.acessShelfLock.get(product.getId()).wait();
								RFID.acessShelfLockNotify.put(product.getId(), false);

							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						shelf.setProduct(product);
						
						shelf.setStopAccelerometer(false);
						shelf.setShake(false);
						
						 //System.out.println("4");
						 
						/* XOR gateway: false gate */
						if (!robotundershelf) {
							/* set speed */
							robot.setSpeed(10.00);
							if (robot.getLocation() != null) {
								/* Putdown current shelf (activity)*/
								this.putdownCurrentShelf_activity(shelf.getProduct().getId(),oldshelfid);
							}
							
							
								/* Go to appropriate shelf (activity) */
							this.goToAppropriateShelf_activity(shelf,product.getId());	
							
							
						} 
						/* XOR gateway: true gate */
						else {
							 AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "shelfMoved", 2,"robot",robot.getId(),"shelf",shelf.getId(), null, -1);
							/* set speed */
							robot.setSpeed(10.00);
							robot.setStatus(RobotStatus.moving);
							
						}

						
						/* Move shelf to dock (activity) (includes reduce speed activity) */
						this.moveshelftoDock_activity(shelf,product.getId());
						
						
						/* Shelf Delivered (message) */
						/* SHelf delivered (message/send) (going to shelf process) */
						
						AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "shelfDelivered", 2, "shelf",shelf.getId(),"robot",robot.getId(), null, product.getId());
						
												robot.getLocation().shelfDelivered(shelf.getProduct().getId());
												robot.shelfDelivered(shelf.getProduct().getId());


		
						int productid = shelf.getProduct().getId();
						synchronized (RFID.robotInsideLock2.get(productid)) {

							try {
								while (!RFID.robotInsideLock2Notify.get(productid))
									RFID.robotInsideLock2.get(productid).wait();
								RFID.robotInsideLock2Notify.put(productid, false);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							
							/* Move shelf from clerk */
							moveShelfFromClerkDock_activity(shelf);
							
							synchronized (RFID.NRDlock.get(product.getId())) {
								try {
									while (!RFID.NRDlockNotify.get(product.getId()))
										RFID.NRDlock.get(product.getId()).wait();
									RFID.NRDlock.put(product.getId(), false);

								} catch (InterruptedException e) {
									
									e.printStackTrace();
								}
							

						
							robot.setStatus(RobotStatus.terminated);
							robot.setSpeed(0.00);
							
							}

						
							
					}
					}
				
			

		

	}

	

}
