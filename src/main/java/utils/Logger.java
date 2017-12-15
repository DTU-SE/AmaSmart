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
import java.util.Random;

import amazonSmartShelfs.amasmart.AmaSmart;

public class Logger {
	
	/* notes */
	// activities and messages following a specific formating
	// 1 log file or 3 log files
	// split by case
	
	
	HashMap<Integer, LinkedList<String>> tempLog = new HashMap<Integer, LinkedList<String>>();
	
	ArrayList<String> balkedArtificats ;
	
	String filename = "log/AmaSmartLog";
	ArrayList<String> OrderLog ;
	int eventIdFirstIndex = 1310 ;
	
	
	Clock clock ;
	boolean separateLog ;
	boolean logbyprocess ;
	boolean productArtifact ;
	
	ArrayList<Integer> productartificatlogged ;
	
	
	public Logger(Clock clock, boolean separateLog, boolean logbyprocess,boolean productArtifact){
		this.clock = clock ;
		this.separateLog = separateLog ;
		this.logbyprocess = logbyprocess ;
		this.productArtifact = productArtifact ;
		this.productartificatlogged = new ArrayList<Integer>();
	    this.balkedArtificats =new ArrayList<String>();
		
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
			        sb.append("Event Name");
			        sb.append(',');
			        sb.append("Event Type");
			        sb.append(',');
			        sb.append("Subject Group");
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
				        sb.append("Event Name");
				        sb.append(',');
				        sb.append("Event Type");
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
					        sb.append("Event Name");
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
				        sb.append("Event Name");
				        sb.append(',');
				        sb.append("Event Type");
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
					        sb.append("Event Name");
					        sb.append(',');
					        sb.append("Event Type");
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
						        sb.append("Shelf Id");
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
						        
						        
						    	fw = new FileWriter(filename+"_CLERK_RFID.csv", true);
								
								  sb = new StringBuilder();
							        sb.append("Event Id");
							        sb.append(',');
							        sb.append("Chip");
							        sb.append(',');
							        sb.append("Timestamp");
							        sb.append(',');
							        sb.append("Product Id");


							        sb.append('\n');

							        fw.write(sb.toString());
							        fw.close();
							        
							        
							        fw = new FileWriter(filename+"_ROBOT_RFID.csv", true);
									
									  sb = new StringBuilder();
								        sb.append("Event Id");
								        sb.append(',');
								        sb.append("Chip Name");
								        sb.append(',');
								        sb.append("Timestamp");
								        sb.append(',');
								        sb.append("Item Id");


								        sb.append('\n');

								        fw.write(sb.toString());
								        fw.close();
								        
								        fw = new FileWriter(filename+"_SHELF_RFID.csv", true);
										
										  sb = new StringBuilder();
									        sb.append("Event Id");
									        sb.append(',');
									        sb.append("Chip Name");
									        sb.append(',');
									        sb.append("Timestamp");
									        sb.append(',');
									        sb.append("Item Id");


									        sb.append('\n');

									        fw.write(sb.toString());
									        fw.close();
						      
					
				}
				  if(productArtifact){
					  fw = new FileWriter(filename+"_productArtifact.csv", true);
						
					  StringBuilder sb = new StringBuilder();
				        sb.append("Product");
				        sb.append(',');
				        sb.append("Price");
				        sb.append(',');
				        sb.append("Shelf");
				        sb.append(',');
				        sb.append("ExtraCheck");


				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
				        
				        
				        fw = new FileWriter(filename+"_productArtifactUpdates.csv", true);
						
						  sb = new StringBuilder();
					        sb.append("Product Id");
					        sb.append(',');
					        sb.append("Timestamp");
					        sb.append(',');
					        sb.append("ExtraCheck");


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
	
	public synchronized void updateArtifact(int productid, double price, int shelf, String extraCheck){
		
		  if(productArtifact){
			  if(!this.productartificatlogged.contains(productid)){
				  
			  
			  FileWriter fw;
			try {
				fw = new FileWriter(filename+"_productArtifact.csv", true);
		
				
			  StringBuilder sb = new StringBuilder();
		        sb.append(productid);
		        sb.append(',');
		        sb.append(round(price,2));
		        sb.append(',');
		        sb.append(shelf);
		        sb.append(',');
		        sb.append(extraCheck);


		        sb.append('\n');

		        fw.write(sb.toString());
		        fw.close();
		        
		        
		        if(extraCheck.equals("true")){
		        	fw = new FileWriter(filename+"_productArtifactUpdates.csv", true);
					
					
					   sb = new StringBuilder();
				        sb.append(productid);
				        sb.append(',');
				        sb.append(getTimeStamp());
				        sb.append(',');
				        sb.append(extraCheck);
				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
		        }
		    	
		        
		        
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.productartificatlogged.add(productid);
			
		  }
			  }
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public synchronized int newOrderLogEvent(int productId, String eventname,int type,String subjectgroup, int subjectid, String objectgroup, int objectid, String extrainfo){
	
		   /* balking probability */
		
		    Random rn = new Random();
		    double d = rn.nextDouble();     // random value in range 0.0 - 1.0
		    if(d<=AmaSmart.balkingRate){
		  
		    	this.balkedArtificats.add(productId+"");
		    	return -1 ;
		    	
		    }
		
		    else if(this.balkedArtificats.contains(productId+"")){
		    	return -1 ; // already balked ;
		    }
		    
		    
		
		if(!subjectgroup.equals("Accelerometer")){
		try {
			Thread.sleep(4);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		
		if(extrainfo==null) extrainfo = "";
		
			
		int eventId = getEventIdIndex();
		LinkedList<String> logevent = new LinkedList<String>();
		
		logevent.add(Integer.toString(productId));
		logevent.add(Integer.toString(eventId));
		logevent.add(extrainfo.equals("manualact") ? "" : getTimeStamp());
		logevent.add(eventname);
		logevent.add(((type==0 ? "process activity" : type==1 ? "manual activity" : type==2 ? "message event" : "accelerometer")));

		logevent.add(subjectgroup);
		logevent.add(Integer.toString(subjectid)+""+Character.toString(subjectgroup.charAt(0)));
		if(!subjectgroup.equals("Accelerometer")){
			logevent.add(objectgroup);
			logevent.add(Integer.toString(objectid)+""+Character.toString(objectgroup.charAt(0)));
		}
		
		
	
		
		tempLog.put(eventId, logevent);
		
		if(type==2 || type==3){
			if(type==2){
				try {
					Thread.sleep(AmaSmart.clock.sleepTimeFromBetaDistribution(AmaSmart.times.get("notify"), -1));
				} catch (InterruptedException e) {
				
					e.printStackTrace();
				}
			}
			
			logEventDone(eventId);
		}
		
		
		return eventId ;
	      
	}
	
	public synchronized void logEventDone(int eventId){
		
		if(eventId==-1){
			return ;
		}
		
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
				
				if(logevent.get(4).equals("message event")  || logevent.get(4).equals("manual activity") ){ 
				    
				StringBuilder sb = new StringBuilder();
	
				//  || logevent.get(3).equals("collector")
					if(logevent.get(7).equals("product")){
					  	fw = new FileWriter(filename+"_CLERK_RFID.csv", true);
						
				    	 sb = new StringBuilder();
					        sb.append(logevent.get(1)+"CR"); 
					        sb.append(',');
					        sb.append(logevent.get(3)); 
					        sb.append(',');
					        sb.append(getTimeStamp()); 
					        sb.append(',');
					        sb.append(logevent.get(8)); 


					        sb.append('\n');

					        fw.write(sb.toString());
					        fw.close();
					}
				        
				  
					else if(logevent.get(7).equals("robot")){
					        
					        fw = new FileWriter(filename+"_ROBOT_RFID.csv", true);
							
							  sb = new StringBuilder();
						        sb.append(logevent.get(1)+"RR"); 
						        sb.append(',');
						        sb.append(logevent.get(3)); 
						        sb.append(',');
						        sb.append(getTimeStamp()); 
						        sb.append(',');
						        sb.append(logevent.get(8)); 


						        sb.append('\n');

						        fw.write(sb.toString());
						        fw.close();
						        
					}
					
				
				
					else if(logevent.get(7).equals("shelf") ){
						        fw = new FileWriter(filename+"_SHELF_RFID.csv", true);
								
								  sb = new StringBuilder();
							        sb.append(logevent.get(1)+"SR"); 
							        sb.append(',');
							        sb.append(logevent.get(3)); 
							        sb.append(',');
							        sb.append(getTimeStamp()); 
							        sb.append(',');
							        sb.append(logevent.get(8)); 


							        sb.append('\n');

							        fw.write(sb.toString());
							        fw.close();
					}
					
				}
				
				else if(logevent.get(4).equals("accelerometer")){
					  
					fw = new FileWriter(filename+"_accelerometer.csv", true);
					
					  StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0)+"AC");
				        sb.append(',');
				        sb.append(logevent.get(1));
				        sb.append(',');
				        sb.append(logevent.get(2));
				        sb.append(',');




				        sb.append('\n');

				        fw.write(sb.toString());
				        fw.close();
			  }
				else if(logevent.get(5).equals("clerk") || logevent.get(3).equals("Assgin robot")){
					
				
					fw = new FileWriter(filename+"_clerkProcess.csv", true);
					
					
					 StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0)+"CP");
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
				else if(logevent.get(5).equals("robot") && !logevent.get(3).equals("Assgin robot")){
					fw = new FileWriter(filename+"_robotProcess.csv", true);
					
					
					 StringBuilder sb = new StringBuilder();
				        sb.append(logevent.get(0)+"RP");
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
				        sb.append(logevent.get(0)+"SSP");
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
				        sb.append(logevent.get(6));


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
	
	
	
	public synchronized  int getEventIdIndex(){
		eventIdFirstIndex++;
		return eventIdFirstIndex;
	}
	
	
}
