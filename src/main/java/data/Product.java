package data;

import amazonSmartShelfs.amasmart.AmaSmart;
import sensors.RFID;

public class Product {

	private int id ;
	private double price ;
	
	
	
	private boolean requested ;
	private boolean collected ;
	private boolean extraCheckRequired ;
    private boolean checkResult ;
    private boolean packaged ;
    private boolean damaged ;
    
    
    
    
    
	public Product(int id, double price) {
		super();
		this.id = id;
		this.price = price;
		
		this.requested = false ;
		this.collected = false ;
		this.extraCheckRequired = false ;
		this.checkResult = false ;
		this.packaged = false ;
		this.damaged = false ;
	}
	
	public void sendExtraCheckToClerk(int response){
		synchronized(RFID.ExtraChecklock.get(id)){
			
			RFID.ExtraChecklockNotify.put(id,response);
			RFID.ExtraChecklock.get(id).notify(); 
		
	
		
		}
	}
	
	public void freerobot() {
		synchronized (RFID.AccAckLock.get(id)) {
			RFID.AccAckLockNotify.put(id, true);
			RFID.AccAckLock.get(id).notify();
		}
	}
	
	public void goToNext(){
		synchronized(RFID.AccAckLock.get(id)){
			
			RFID.AccAckLockNotify.put(id,true);
			RFID.AccAckLock.get(id).notify(); 		
		}
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isRequested() {
		return requested;
	}
	public void setRequested(boolean requested) {
		this.requested = requested;
	}
	public boolean isCollected() {
		return collected;
	}
	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	public boolean isExtraCheckRequired() {
		return extraCheckRequired;
	}
	public void setExtraCheckRequired(boolean extraCheckRequired) {
		this.extraCheckRequired = extraCheckRequired;
	}
	public boolean isCheckResult() {
		return checkResult;
	}
	public void setCheckResult(boolean checkResult) {
		this.checkResult = checkResult;
	}
	public boolean isPackaged() {
		return packaged;
	}
	public void setPackaged(boolean packaged) {
		this.packaged = packaged;
	}
	public boolean isDamaged() {
		return damaged;
	}
	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}
    
    
	
	
}
