package data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import amazonSmartShelfs.amasmart.AmaSmart;
import processes.RobotRunning;
import processes.ShelfRunning;
import sensors.RFID;
import threadPool.PoolWorker;

public class Robot {

	private int id;
	private RobotStatus status;
	private Shelf location;
	private double speed;
	private HashMap<Integer, Integer[]> catalogue;
	private LinkedList<Shelf> shelfs;
	private  Queue<Product> ProductRequests;
	
	
	  private   PoolWorker thread;
	  private  LinkedBlockingQueue queue;

	public Robot(int id, HashMap<Integer, Integer[]> catalogue, LinkedList<Shelf> shelfs) {
		this.id = id;
		this.status = RobotStatus.iddle;
		this.location = null;
		this.speed = 0.00;
		this.ProductRequests = new LinkedList<Product>();

		this.catalogue = catalogue;
		this.shelfs = shelfs;
		this.queue = new LinkedBlockingQueue();
		
		this.status = RobotStatus.terminated; // added recently 
		
		
		  thread = new PoolWorker(queue);
          thread.start();

	}
	
    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
}
	
  
	public LinkedBlockingQueue getQueue(){
		return this.queue;
	}

	public Shelf FindAppropriateSHelf(Product p) {
		Integer[] productShelfAndIndex = catalogue.get(p.getId());
		return shelfs.get(productShelfAndIndex[0]);
	}

	

	public void shelfContainingProductReceived(Product product, int id) {

		

		synchronized (RFID.robotInsideLock.get(id)) {

			try {
				int i = 0;
				while (!RFID.robotInsideLockNotify.get(id)) {
					
					RFID.robotInsideLock.get(id).wait();
				
					i++;
				}

				RFID.robotInsideLockNotify.put(id, false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			

		}

	}

	public synchronized  Product getNextProduct() {
		return ProductRequests.poll();
	}

	public void shelfDelivered(int id) {
		synchronized (RFID.robotInsideLock.get(id)) {

			RFID.robotInsideLockNotify.put(id, true);
			RFID.robotInsideLock.get(id).notify(); 

		}
	}

	public void disposeShelf(int id) {
		
		synchronized (RFID.robotInsideLock2.get(id)) {
			RFID.robotInsideLock2Notify.put(id, true);
			RFID.robotInsideLock2.get(id).notify();
		}
	}

	public void instanceDone(int id) {
		
		synchronized (RFID.NRDlock.get(id)) {
			RFID.NRDlockNotify.put(id, true);
			RFID.NRDlock.get(id).notify();
		}
	}
	
	
	/* getters and setters */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RobotStatus getStatus() {
		return status;
	}

	public void setStatus(RobotStatus status) {
		this.status = status;
	}

	public Shelf getLocation() {
		return location;
	}

	public void setLocation(Shelf location) {
		this.location = location;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
