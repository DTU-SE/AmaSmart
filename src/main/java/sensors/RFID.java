package sensors;

import java.util.HashMap;
import java.util.LinkedList;

import amazonSmartShelfs.amasmart.AmaSmart;

public class RFID {

	/* Active sensors :
	 *  message events */
	
	/* Simulate the role of the RFID sensor:  Locks used for synchronization using wait/notify  */
	
	public static HashMap<Integer, Object> useRobotLock = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> acessShelfLock = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> reserveShelfLock = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> MovingShelfLock = new HashMap<Integer, Object>();	 
	public static HashMap<Integer, Object> robotInsideLock = new HashMap<Integer, Object>();	
	public static HashMap<Integer, Object> robotInsideLock2 = new HashMap<Integer, Object>();	 
	public static HashMap<Integer, Object> DisposeShelfLock = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> shelfFreelock  = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> accelormeterStop  = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> ExtraChecklock  = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> goTonextLock  = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> AccAckLock  = new HashMap<Integer, Object>();
	public static HashMap<Integer, Object> NRDlock  = new HashMap<Integer, Object>();
	
	public static HashMap<Integer, Boolean> useRobotLockNotify = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> acessShelfLockNotify = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> reserveShelfLockNotify = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> MovingShelfLockNotify = new HashMap<Integer, Boolean>();	 
	public static HashMap<Integer, Boolean> robotInsideLockNotify = new HashMap<Integer, Boolean>();	 
	public static HashMap<Integer, Boolean> robotInsideLock2Notify = new HashMap<Integer, Boolean>();	
	public static HashMap<Integer, Boolean> DisposeShelfLockNotify = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> shelfFreelockNotify  = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> accelormeterStopNotify  = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Integer> ExtraChecklockNotify  = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Boolean> goTonextLockNotify  = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> AccAckLockNotify  = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> NRDlockNotify  = new HashMap<Integer, Boolean>();
	
  /* Active senors : start-time and end-time of manual activities  */
	
	
	public synchronized static int recordBegining(int productId, String eventname,int type,String subjectgroup, int subjectid, String objectgroup, int objectid){
		
		int eventId = AmaSmart.log.newOrderLogEvent(productId, eventname, type, subjectgroup,subjectid,objectgroup,objectid, "manualact");
		
		return eventId ;
		
	}
	
	
	public synchronized static void recordEnd(int eventId){
		AmaSmart.log.logEventDone(eventId);
	}
	
	
	
	
	
	
}
