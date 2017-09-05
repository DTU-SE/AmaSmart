package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import amazonSmartShelfs.amasmart.AmaSmart;

public class Logger {
	
	/* notes */
	// activities and messages following a specific formating
	// 1 log file or 3 log files
	// split by case
	
	
	HashMap<Integer, LinkedList<String>> tempLog = new HashMap<Integer, LinkedList<String>>();
	
	
	
	String filename = "log/AmaSmartLog";
	ArrayList<String> OrderLog ;
	int eventIdFirstIndex = 1310 ;
	
	
	Clock clock ;
	boolean separateLog ;
	boolean logbyprocess ;
	
	
	public Logger(Clock clock, boolean separateLog, boolean logbyprocess){
		this.clock = clock ;
		this.separateLog = separateLog ;
		this.logbyprocess = logbyprocess ;
		
		FileWriter fw = null;
		
		
			try {
				if(separateLog==false){
					fw = new FileWriter(filename+"_compact.csv", true);
				
				 StringBuilder sb = new StringBuilder();
			        sb.append("Case Id");
			        sb.append(',');
			        sb.append("Event Id");
			        sb.append(',');
			        sb.append("Start Timestamp");
			        sb.append(',');
			        sb.append("End Timesstamp");
			        sb.append(',');
			        sb.append("Event name");
			        sb.append(',');
			        sb.append("Event type");
			        sb.append(',');
			        sb.append("Subjec Group");
			        sb.append(',');
			        sb.append("Subject Id");
			        sb.append(',');
			        sb.append("Object Group");
			        sb.append(',');
			        sb.append("Object Id");
			        sb.append('\n');

			        fw.write(sb.toString());
			        fw.close();
				}
				else if(separateLog==true && logbyprocess==false) {
					
					fw = new FileWriter(filename+"_activities.csv", true);
					
					 StringBuilder sb = new StringBuilder();
				        sb.append("Case Id");
				        sb.append(',');
				        sb.append("Event Id");
				        sb.append(',');
				        sb.append("Start Timestamp");
				        sb.append(',');
				        sb.append("End Timesstamp");
				        sb.append(',');
				        sb.append("Event name");
				        sb.append(',');
				        sb.append("Event type");
				        sb.append(',');
				        sb.append("Subjec Group");
				        sb.append(',');
				        sb.append("Subject Id");
				        sb.append(',');
				        sb.append("Object Group");
				        sb.append(',');
				        sb.append("Object Id");

				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
				        
				    	fw = new FileWriter(filename+"_msgs.csv", true);
						
						 sb = new StringBuilder();
					        sb.append("Case Id");
					        sb.append(',');
					        sb.append("Event Id");
					        sb.append(',');
					        sb.append("Start Timestamp");
					        sb.append(',');
					        sb.append("End Timesstamp");
					        sb.append(',');
					        sb.append("Event name");
					        sb.append(',');
					        sb.append("Subjec Group");
					        sb.append(',');
					        sb.append("Subject Id");
					        sb.append(',');
					        sb.append("Object Group");
					        sb.append(',');
					        sb.append("Object Id");

					        sb.append('\n');

					        fw.write(sb.toString());
					        fw.close();
					        
					    	fw = new FileWriter(filename+"_accelerometer.csv", true);
							
							  sb = new StringBuilder();
						        sb.append("Shelf Id");
						        sb.append(',');
						        sb.append("Event Id");
						        sb.append(',');
						        sb.append("Timestamp");
						        sb.append(',');
						        sb.append(" ");


						        sb.append('\n');

						        fw.write(sb.toString());
						        fw.close();
				        
				} 
				else if(separateLog==true && logbyprocess==true) {
					
					fw = new FileWriter(filename+"_clerkProcess.csv", true);
					
					 StringBuilder sb = new StringBuilder();
					   sb.append("Case Id");
				        sb.append(',');
				        sb.append("Event Id");
				        sb.append(',');
				        sb.append("Start Timestamp");
				        sb.append(',');
				        sb.append("End Timesstamp");
				        sb.append(',');
				        sb.append("Event name");
				        sb.append(',');
				        sb.append("Event type");
				        sb.append(',');
				        sb.append("Subjec Group");
				        sb.append(',');
				        sb.append("Subject Id");
				        sb.append(',');
				        sb.append("Object Group");
				        sb.append(',');
				        sb.append("Object Id");
				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
				        
				    	fw = new FileWriter(filename+"_robotProcess.csv", true);
						
				    	sb = new StringBuilder();
				    	   sb.append("Case Id");
					        sb.append(',');
					        sb.append("Event Id");
					        sb.append(',');
					        sb.append("Start Timestamp");
					        sb.append(',');
					        sb.append("End Timesstamp");
					        sb.append(',');
					        sb.append("Event name");
					        sb.append(',');
					        sb.append("Event type");
					        sb.append(',');
					        sb.append("Subjec Group");
					        sb.append(',');
					        sb.append("Subject Id");
					        sb.append(',');
					        sb.append("Object Group");
					        sb.append(',');
					        sb.append("Object Id");
					        sb.append('\n');

					        fw.write(sb.toString());
					        fw.close();
					        
					        
					        fw = new FileWriter(filename+"_smartShelfProcess.csv", true);
							
					    	sb = new StringBuilder();
					    	   sb.append("Case Id");
						        sb.append(',');
						        sb.append("Event Id");
						        sb.append(',');
						        sb.append("Start Timestamp");
						        sb.append(',');
						        sb.append("End Timesstamp");
						        sb.append(',');
						        sb.append("Event name");
						        sb.append(',');
						        sb.append("Event type");
						        sb.append(',');
						        sb.append("Subjec Group");
						        sb.append(',');
						        sb.append("Subject Id");
						        sb.append(',');
						        sb.append("Object Group");
						        sb.append(',');
						        sb.append("Object Id");
						        sb.append('\n');

						        fw.write(sb.toString());
						        fw.close();
					        
					    	fw = new FileWriter(filename+"_accelerometer.csv", true);
							
							  sb = new StringBuilder();
						        sb.append("Shelf Id");
						        sb.append(',');
						        sb.append("Event Id");
						        sb.append(',');
						        sb.append("Timestamp");
						        sb.append(',');
						        sb.append(" ");


						        sb.append('\n');

						        fw.write(sb.toString());
						        fw.close();
					
				}
					
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			} 
		}
		
		
	
	/*
	 * Log new event. an event can be of the following types: 0 normal activity, 1: manual acitivity (starttime-endtime) (active sensor)
	 * 2: message events (active sensor), 3: accelerometer (decoractive sensor)
	 */
	public synchronized int newOrderLogEvent(int productId, String eventname,int type,String subjectgroup, int subjectid, String objectgroup, int objectid, Calendar tstamp){
		
		
		System.err.println("productid: "+productId+". eventname: "+eventname+". type: "+((type==0 ? "process activity" : type==1 ? "manual activity " : type==2 ? "message event" : "accelerometer"))+" process: "+subjectgroup+". processid: "+subjectid);
		
		int eventId = getEventIdIndex();
		LinkedList<String> logevent = new LinkedList<String>();
		
		logevent.add(Integer.toString(productId));
		logevent.add(Integer.toString(eventId));
		logevent.add(getTimeStamp());
		logevent.add(eventname);
		logevent.add(((type==0 ? "process activity" : type==1 ? "manual activity " : type==2 ? "message event" : "accelerometer")));
		logevent.add(subjectgroup);
		logevent.add(Integer.toString(subjectid));
		logevent.add(objectgroup);
		logevent.add(Integer.toString(objectid));
		
	
		
		tempLog.put(eventId, logevent);
		
		if(type==2 || type==3){
			if(type==2){
				try {
					Thread.sleep(AmaSmart.notify_duration);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			logEventDone(eventId);
		}
		
		
		return eventId ;
	      
	}
	
	public synchronized void logEventDone(int eventId){
		
		LinkedList<String> logevent = tempLog.get(eventId);
		
		FileWriter fw = null;
		try {
		  if(separateLog==false){
				fw = new FileWriter(filename+"_compact.csv", true);
				
			
			 StringBuilder sb = new StringBuilder();
		        sb.append(logevent.get(0));
		        sb.append(',');
		        sb.append(logevent.get(1));
		        sb.append(',');
		        sb.append(logevent.get(2));
		        sb.append(',');
		        sb.append(getTimeStamp());
		        sb.append(',');
		        sb.append(logevent.get(3));
		        sb.append(',');
		        sb.append(logevent.get(4));
		        sb.append(',');
		        sb.append(logevent.get(5));
		        sb.append(',');
		        sb.append(logevent.get(6));
		        sb.append(',');
		        sb.append(logevent.get(7));
		        sb.append(',');
		        sb.append(logevent.get(8));

		        sb.append('\n');

		        fw.write(sb.toString());
		        fw.close();
		  }
			else if(separateLog==true && logbyprocess==false) {
			  if(logevent.get(4).equals("message event")){
				  fw = new FileWriter(filename+"_msgs.csv", true);
					
					 StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0));
				        sb.append(',');
				        sb.append(logevent.get(1));
				        sb.append(',');
				        sb.append(logevent.get(2));
				        sb.append(',');
				        sb.append(getTimeStamp());
				        sb.append(',');
				        sb.append(logevent.get(3));
				        sb.append(',');
				        sb.append(logevent.get(5));
				        sb.append(',');
				        sb.append(logevent.get(6));
				        sb.append(',');
				        sb.append(logevent.get(7));
				        sb.append(',');
				        sb.append(logevent.get(8));

				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
			  }
			  else if(logevent.get(4).equals("accelerometer")){
				  
					fw = new FileWriter(filename+"_accelerometer.csv", true);
					
					  StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0));
				        sb.append(',');
				        sb.append(logevent.get(1));
				        sb.append(',');
				        sb.append(logevent.get(2));
				        sb.append(',');




				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
			  }
			  else {
				  fw = new FileWriter(filename+"_activities.csv", true);
					
					
					 StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0));
				        sb.append(',');
				        sb.append(logevent.get(1));
				        sb.append(',');
				        sb.append(logevent.get(2));
				        sb.append(',');
				        sb.append(getTimeStamp());
				        sb.append(',');
				        sb.append(logevent.get(3));
				        sb.append(',');
				        sb.append(logevent.get(4));
				        sb.append(',');
				        sb.append(logevent.get(5));
				        sb.append(',');
				        sb.append(logevent.get(6));
				        sb.append(',');
				        sb.append(logevent.get(7));
				        sb.append(',');
				        sb.append(logevent.get(8));

				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
			  }
		  }
			else if(separateLog==true && logbyprocess==true) {
				
				if(logevent.get(4).equals("accelerometer")){
					  
					fw = new FileWriter(filename+"_accelerometer.csv", true);
					
					  StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0));
				        sb.append(',');
				        sb.append(logevent.get(1));
				        sb.append(',');
				        sb.append(logevent.get(2));
				        sb.append(',');




				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
			  }
				else if(logevent.get(5).equals("clerk")){
					fw = new FileWriter(filename+"_clerkProcess.csv", true);
					
					
					 StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0));
				        sb.append(',');
				        sb.append(logevent.get(1));
				        sb.append(',');
				        sb.append(logevent.get(2));
				        sb.append(',');
				        sb.append(getTimeStamp());
				        sb.append(',');
				        sb.append(logevent.get(3));
				        sb.append(',');
				        sb.append(logevent.get(4));
				        sb.append(',');
				        sb.append(logevent.get(5));
				        sb.append(',');
				        sb.append(logevent.get(6));
				        sb.append(',');
				        sb.append(logevent.get(7));
				        sb.append(',');
				        sb.append(logevent.get(8));

				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
					
				}
				else if(logevent.get(5).equals("robot")){
					fw = new FileWriter(filename+"_robotProcess.csv", true);
					
					
					 StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0));
				        sb.append(',');
				        sb.append(logevent.get(1));
				        sb.append(',');
				        sb.append(logevent.get(2));
				        sb.append(',');
				        sb.append(getTimeStamp());
				        sb.append(',');
				        sb.append(logevent.get(3));
				        sb.append(',');
				        sb.append(logevent.get(4));
				        sb.append(',');
				        sb.append(logevent.get(5));
				        sb.append(',');
				        sb.append(logevent.get(6));
				        sb.append(',');
				        sb.append(logevent.get(7));
				        sb.append(',');
				        sb.append(logevent.get(8));

				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
					
				}
				else if(logevent.get(5).equals("smart shelf")){
					
					fw = new FileWriter(filename+"_smartShelfProcess.csv", true);
					
					
					 StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0));
				        sb.append(',');
				        sb.append(logevent.get(1));
				        sb.append(',');
				        sb.append(logevent.get(2));
				        sb.append(',');
				        sb.append(getTimeStamp());
				        sb.append(',');
				        sb.append(logevent.get(3));
				        sb.append(',');
				        sb.append(logevent.get(4));
				        sb.append(',');
				        sb.append(logevent.get(5));
				        sb.append(',');
				        sb.append(logevent.get(6));
				        sb.append(',');
				        sb.append(logevent.get(7));
				        sb.append(',');
				        sb.append(logevent.get(8));

				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
					
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} 
	}
	
	public  synchronized String getTimeStamp(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = clock.getInittimevalue().getTime();
		return dateFormat.format(date); 
	}
	
	/// to be removed
	public  synchronized String getEndTimeStamp(int duration){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			cal.setTime(sdf.parse(getTimeStamp()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// all done
		
		// 10 millisecond = 1 second
		cal.add(Calendar.SECOND, duration*10);
		
		Date date = cal.getTime();
		return dateFormat.format(date); 

	}
	
	
	
	
	public synchronized  int getEventIdIndex(){
		eventIdFirstIndex++;
		return eventIdFirstIndex;
	}
	
	
}
