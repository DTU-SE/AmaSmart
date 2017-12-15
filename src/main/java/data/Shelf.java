package data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import amazonSmartShelfs.amasmart.AmaSmart;
import sensors.RFID;
import threadPool.PoolWorker;

public class Shelf {

	private int id;
	private boolean shake;
	private boolean stopAccelerometer ;

	private int state; // 0:free, 1:locked 2:moving 3:waiting for pickup

	private ArrayList<Product> content;
	private HashMap<Integer, Object> ShelfLock;

	private Product product;
	
	  private   PoolWorker thread;
	   private  LinkedBlockingQueue queue;

	public Shelf(int id) {
		super();
		this.id = id;
		this.content = new ArrayList<Product>();
		this.ShelfLock = RFID.acessShelfLock;
		this.stopAccelerometer = false;
		this.state = 0 ;
		this.queue = new LinkedBlockingQueue();
		
		  thread = new PoolWorker(queue);
          thread.start();

	}
	
	   public void execute(Runnable task) {
	        synchronized (queue) {
	            queue.add(task);
	            queue.notify();
	        }
	}
	
	

	public void setState(int state) {
		this.state = state;
	}




	public boolean isStopAccelerometer() {
		return stopAccelerometer;
	}



	public void setStopAccelerometer(boolean stopAccelerometer) {
		this.stopAccelerometer = stopAccelerometer;
	}



	public void addProduct(Product product) {
		content.add(product);
	}

	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}

	

	public void moveShelf() {
		synchronized (RFID.reserveShelfLock.get(id)) {
			this.state = 2;
			RFID.reserveShelfLockNotify.put(id, true);
			RFID.reserveShelfLock.get(id).notify();
		}
	}
	
	
	public void proceedRobot(int id) {
		synchronized (RFID.acessShelfLock.get(id)) {
			RFID.acessShelfLockNotify.put(id, true);
			RFID.acessShelfLock.get(id).notify();
		}
	}

	public void shelfDelivered(int id) {
		synchronized (RFID.MovingShelfLock.get(id)) {
			this.state = 3;
			RFID.MovingShelfLockNotify.put(id, true);
			RFID.MovingShelfLock.get(id).notify();
		}
	}
	
	public void accAck(int id) {
		synchronized (RFID.AccAckLock.get(id)) {
			RFID.AccAckLockNotify.put(id, true);
			RFID.AccAckLock.get(id).notify();
		}
	}

	public void freeshelf() {
		synchronized (RFID.shelfFreelock.get(id)) {
			this.state = 0;
			RFID.shelfFreelockNotify.put(id, true);
			RFID.shelfFreelock.get(id).notify();
		}
	}

	public void DisposeShelf(int id) {

		
		synchronized (RFID.DisposeShelfLock.get(id)) {
			RFID.DisposeShelfLockNotify.put(id, true);
			RFID.DisposeShelfLock.get(id).notify();
		}
	}
	
	

	public int getState() {
		return state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isShake() {
	
		return shake ;
		
	}

	public synchronized void setShake(boolean shake) {
		if(this.stopAccelerometer==false)
		this.shake = shake;
	}

	public ArrayList<Product> getContent() {
		return content;
	}

}
