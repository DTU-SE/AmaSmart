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

   public void putdownCurrentShelf_activity(Shelf shelf){
	   /* put down current shelf */
	
		int eventId = AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "putdown current shelf", 0, "robot",robot.getId(),"shelf",shelf.getId(), null);
		try {
			Thread.sleep(AmaSmart.clock.guessSleepTime(AmaSmart.putDownCurrentSHelf_duration));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AmaSmart.log.logEventDone(eventId);
		//robot.setLocation(null);
	
   }

   
   public void goToAppropriateShelf_activity(Shelf shelf){
		int eventId = AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "go to approriate shelf", 0,"robot",robot.getId(),"shelf",shelf.getId(), null);
		/* Set Robot location to shelf */
		
		/* Go to shelf (activitiy) */
		robot.setStatus(RobotStatus.moving);
		try {
			Thread.sleep(AmaSmart.clock.guessSleepTime(AmaSmart.goToAppropriateShelf_duration));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AmaSmart.log.logEventDone(eventId);
		AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "send shelf being moved to shelf", 2, "robot",robot.getId(),"shelf",shelf.getId(), null);
		
		
   }
   
   
   public void moveshelftoDock_activity(Shelf shelf){
				/* Move Shelf to dock */

				robot.getLocation().moveShelf();
			

				int eventId1 = AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "move shelf to dock", 0,"robot",robot.getId(),"shelf",shelf.getId(), null);
				try {
					Thread.sleep(AmaSmart.clock.guessSleepTime(AmaSmart.moveShelfTodock_duration));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				// if shelf has been shaked
				if (shelf.isShake()) {
					shelf.setStopAccelerometer(true);
					/* Reduce speed (non interrupting event) */
					/// reduce speed -- is not instantaneous !!! -- need
					/// fix for real demo


					robot.setSpeed(robot.getSpeed() / 2); // assume: means double delivery time
					int eventId2 = AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "reduce speed", 0,"robot",robot.getId(),"shelf",shelf.getId(), null);
					try {
						Thread.sleep(AmaSmart.clock.guessSleepTime(AmaSmart.moveShelfTodock_duration));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					AmaSmart.log.logEventDone(eventId2);

				}
				
//				synchronized (RFID.AccAckLock.get(shelf.getId())) {
//				try {
//					while (!RFID.AccAckLockNotify.get(shelf.getId()))
//						RFID.AccAckLock.get(shelf.getId()).wait();
//					RFID.AccAckLockNotify.put(shelf.getId(), false);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				AmaSmart.log.logEventDone(eventId1);
				//}
   }
   
   public boolean moveShelfFromClerkDock_activity(Shelf shelf) {
	  int eventId =  AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "move shelf from dock", 0, "robot",robot.getId(),"shelf",shelf.getId(), null);
		try {
			Thread.sleep(AmaSmart.clock.guessSleepTime(AmaSmart.moveShelfFromClerkDock_duration));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AmaSmart.log.logEventDone(eventId);

		
		return true;
	}
   
	public void run() {


					Shelf shelf = robot.FindAppropriateSHelf(product);
					
					// wait until the shelf free
					
					
					 AmaSmart.log.newOrderLogEvent(product.getId(), "receive product request from clerk", 2,"robot",robot.getId(),"clerk",1, null); // to be changed with more clerks
				
					 System.out.println("1");
						/*
						 * Is robot under the shelf containing the required
						 * product (XOR gateway)
						 */
						boolean robotundershelf = false;

						if (robot.getLocation() == null) {
							robotundershelf = false;
						} else if (robot.getLocation() != null && robot.getLocation().getId() != shelf.getId()) {
							robotundershelf = false;
						} else if (robot.getLocation().getId() == shelf.getId()) {
							robotundershelf = true;
						} else {
							System.err.println("Robot Running: unknown robot position");
						}
						 System.out.println("2");
						robot.setLocation(shelf);
						 System.out.println("3");
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
						
						 System.out.println("4");
						 
						/* XOR gateway: false gate */
						if (!robotundershelf) {
							/* set speed */
							robot.setSpeed(10.00);
							if (robot.getLocation() != null) {
								/* Putdown current shelf (activity)*/
								this.putdownCurrentShelf_activity(shelf);
							}
							
							 System.out.println("5");
								/* Go to appropriate shelf (activity) */
							this.goToAppropriateShelf_activity(shelf);	
							
							 System.out.println("6");
						} 
						/* XOR gateway: true gate */
						else {
							 AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "send shelf being moved to shelf", 2,"robot",robot.getId(),"shelf",shelf.getId(), null);
							/* set speed */
							robot.setSpeed(10.00);
							robot.setStatus(RobotStatus.moving);
							System.out.println("7");
						}

						
						/* Move shelf to dock (activity) (includes reduce speed activity) */
						this.moveshelftoDock_activity(shelf);
						
						
						/* Shelf Delivered (message) */
						/* SHelf delivered (message/send) (going to shelf process) */
						AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "send shelf delivered (to shelf)", 2, "robot",robot.getId(),"shelf",shelf.getId(), null);;
						robot.getLocation().shelfDelivered(shelf.getProduct().getId());
						/* Shelf delivered (message/send) (going to product process) */
						AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "send shelf delivered (to clerk)", 2, "robot",robot.getId(),"clerk",1, null);
						robot.shelfDelivered(shelf.getProduct().getId());

						
						/* Dispose shelf (message/receive) */
						System.err.println("Robot Running: Robot " + robot.getId() + "  controlled by thread: "
								+ robotruningid + " waiting for dispose message"); 
		
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

							AmaSmart.log.newOrderLogEvent(shelf.getProduct().getId(), "receive dispose shelf from clerk", 2, "robot",robot.getId(),"clerk",1, null);
							
							/* Move shelf from clerk */
							moveShelfFromClerkDock_activity(shelf);

							System.err.println("Robot Running: Robot " + robot.getId() + " controlled by thread: "
									+ robotruningid + " Terminated to get product " + product.getId());
							robot.setStatus(RobotStatus.terminated);
							robot.setSpeed(0.00);
							

							// product = get.nextproduct
							
					}
					}
				
			

		

	}

	

}
