import org.json.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;


public class SimulationTaxi {
	
private final static String GoogleKey = "AIzaSyAJz2OzvJA1meJzOd9Z3XCFmK-7h2nEXeg";


public double calTotalDistance(){
	
	
	double totalDistance = 0.0;
	try {
		BufferedReader br0 = new BufferedReader(new InputStreamReader(new FileInputStream("data/Á©-2148.txt")));
	    try {
	    	
	    	
	    	
	        String line0 = br0.readLine();
	        line0 = line0.substring(1, line0.length()-1); // cut "[" , "]"
	        JSONObject obj = new JSONObject(line0);
	        String latS = (String) obj.get("lat");
            String lngS = (String) obj.get("lng");
            double lat0 = Double.parseDouble(latS);
	        double lng0 = Double.parseDouble(lngS);  // already get lat0 lng0 
	        System.out.println("lat0 = " + lat0 + "\t lng0 = " + lng0);
	        int i =  0;
	        
//		    for(int i=1 ; i<=1000 ; i++){
	        while (line0 != null) {
	        	
	        	i++;
	        	
	            line0 = br0.readLine(); // read next line0
	            line0 = line0.substring(1, line0.length()-1); // cut "[" and "]" 
	            
	            obj = new JSONObject(line0);

		        latS = (String) obj.get("lat");
	            lngS = (String) obj.get("lng");
	            double lat1 = Double.parseDouble(latS);
	            double lng1 = Double.parseDouble(lngS);
	            
	            System.out.println("i = " + i + "\t lat0" + lat0 + "\t lng0" + lng0 +"\t lat1" + lat1 +"\t lng1" + lng1 );
	            
	            // have to filter time when server down and start on different point
		        if(lat0 > 13.495060 && lat0 < 13.956429 && lng0 > 100.329752 && lng0 < 100.936369){
		        	if(lat1 > 13.495060 && lat1 < 13.956429 && lng1 > 100.329752 && lng1 < 100.936369){
		        		totalDistance = totalDistance + haversinDistance(lat0, lng0, lat1, lng1);
			            System.out.println("total distance = " + totalDistance);
		        	}
		        }
	            
	            lat0 = lat1;
	            lng0 = lng1;
	            
	         }// end while
	        System.out.println("final total distance = " + totalDistance);

	    } finally {
	        br0.close();
	    }
	} catch (Exception e) {
		// TODO: handle exception
	}
	System.out.println("done");
	return totalDistance;
	
       
        
}

public double calOccupiedDistance(){
	double occupiedDistance = 0.0;
	try {
		BufferedReader br0 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/Beejkap^^/4_term_2/(PreProject)/data/Á©-2148.txt")));
		BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/Beejkap^^/4_term_2/(PreProject)/data/Á©-2148.txt")));
	    try {
	    	
	    	
	        String line0 = br0.readLine();
	        String line1 = br1.readLine(); // read 1st line
	        line1 = br1.readLine(); // read 2nd line 
	        line0 = line0.substring(1, line0.length()-1); // cut "[" , "]"
	        line1 = line1.substring(1, line1.length()-1); // cut "[" , "]"
	        System.out.println("line0 98 = " + line0);
	        JSONObject obj = new JSONObject(line0);
	        JSONObject tempObj = new JSONObject(line1);
	       
	        String latS;
            String lngS;
            String meterS;
            double lat0 = 0;
	        double lng0 = 0;
	        double lat1;
            double lng1;
	        double tempLat = 0;
	        double tempLng = 0;
	        int meter0;
	        int meter1;
	        
//	        System.out.println("lat0 = " + lat0 + "\t lng0 = " + lng0);
	        int enable = 0;
	    	int i = 0;
	        
//		    for(int i=0 ; i< 100 ; i++){
	        while (line0 != null ) {
	        	i = i+1;
	        	System.out.print(" i = " + i + "\t");

		        meterS = (String) obj.get("meter");
		        meter0 = Integer.parseInt(meterS);
		        
//		        System.out.println("line1" + line0);
		        
		        if(meter0 == 1){
//		        	System.out.println("line0 144");
		        	latS = (String) obj.get("lat");
		            lngS = (String) obj.get("lng");
		            lat0 = Double.parseDouble(latS);
			        lng0 = Double.parseDouble(lngS);  // already get lat0 lng0
			        
			        if(lat0 != 0 && lng0 != 0){
				        latS = (String) tempObj.get("lat");
			            lngS = (String) tempObj.get("lng");
			            lat1 = Double.parseDouble(latS);
			            lng1 = Double.parseDouble(lngS);
			            
			            meterS = (String) tempObj.get("meter");
			            meter1 = Integer.parseInt(meterS);
			            
			            // ignore same lat lng case 
			           	if(lat0 != lat1 || lng0 != lng1){
			            	if(lat0 > 13.495060 && lat0 < 13.956429 && lng0 > 100.329752 && lng0 < 100.936369){
					        	if(lat1 > 13.495060 && lat1 < 13.956429 && lng1 > 100.329752 && lng1 < 100.936369){
					        		
					        		occupiedDistance = occupiedDistance + haversinDistance(lat0, lng0, lat1, lng1);
					        		System.out.println("meter0 = " + meter0  + "\t" + "lat0 = " + lat0 + "\t" + "lng0 = " + lng0 + "\t" +"lat1 = " + lat1 + "\t"+ "lng1 = " + lng1 );
//							        System.out.println("distance with passengers = " + occupiedDistance);
					        	}
					        }
						}
			        }
		        }// end if meter = 1

		        System.out.println("distance with passengers = " + occupiedDistance);
		        
		        line0 = br0.readLine();
		        line1 = br1.readLine();
		        line0 = line0.substring(1, line0.length()-1); // cut "[" , "]"
		        line1 = line1.substring(1, line1.length()-1); // cut "[" , "]"
		        obj = new JSONObject(line0);
		        tempObj = new JSONObject(line1);
	         } // end while
	        System.out.println("occupied distance = " + occupiedDistance);

	    } finally {
	        br0.close();
	    }
	} catch (Exception e) {
		// TODO: handle exception
	}
	System.out.println("done");
	return occupiedDistance;
}

public void calTotalDistanceEveryFile(String plateNameFile){
	
	double totalDistance = 0.0;
	try {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(plateNameFile)));
		String plateName = "";
		try {
			plateName = br.readLine();
			while(plateName != null){
				try {
					BufferedReader br0 = new BufferedReader(new InputStreamReader(new FileInputStream(plateNameFile+"/"+plateName)));
				    try {
			 	
				        String line0 = br0.readLine();
				        line0 = line0.substring(1, line0.length()-1); // cut "[" , "]"
				        JSONObject obj = new JSONObject(line0);
				        String latS = (String) obj.get("lat");
			            String lngS = (String) obj.get("lng");
			            double lat0 = Double.parseDouble(latS);
				        double lng0 = Double.parseDouble(lngS);  // already get lat0 lng0 
				        System.out.println("lat0 = " + lat0 + "\t lng0 = " + lng0);
				        int i =  0;
				        
//					    for(int i=1 ; i<=1000 ; i++){
				        while (line0 != null) {
				        	
				        	i++;
				        	
				            line0 = br0.readLine(); // read next line0
				            line0 = line0.substring(1, line0.length()-1); // cut "[" and "]" 
				            
				            obj = new JSONObject(line0);

					        latS = (String) obj.get("lat");
				            lngS = (String) obj.get("lng");
				            double lat1 = Double.parseDouble(latS);
				            double lng1 = Double.parseDouble(lngS);
				            
				            System.out.println("i = " + i + "\t lat0" + lat0 + "\t lng0" + lng0 +"\t lat1" + lat1 +"\t lng1" + lng1 );
				            
				            // have to filter time when server down and start on different point
					        if(lat0 > 13.495060 && lat0 < 13.956429 && lng0 > 100.329752 && lng0 < 100.936369){
					        	if(lat1 > 13.495060 && lat1 < 13.956429 && lng1 > 100.329752 && lng1 < 100.936369){
					        		totalDistance = totalDistance + haversinDistance(lat0, lng0, lat1, lng1);
						            System.out.println("total distance = " + totalDistance);
					        	}
					        }
				            
				            lat0 = lat1;
				            lng0 = lng1;
				            
				         }// end while
				        System.out.println("final total distance = " + totalDistance);

				    } finally {
				        br0.close();
				    }
				} catch (Exception e) {
					// TODO: handle exception
				}

			}// end while
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}// try catch plateNameFile
	
	System.out.println("done");
//	return totalDistance;
	
       
        
}

public void calTotalTime(){

	try {
		BufferedReader br0 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/Beejkap^^/4_term_2/(PreProject)/data/Á©-2148.txt")));
	    try {
	    	
	    	int totalHour = 0;
	    	int totalMinute = 0;
	    	
	        String line0 = br0.readLine();
	        line0 = line0.substring(1, line0.length()-1); // cut "[" , "]"
	        JSONObject obj = new JSONObject(line0);
	        String timeS = (String) obj.get("update_time");
	        
            double time0 = Double.parseDouble(timeS);
	         
//	        System.out.println("lat0 = " + time0 + "\t lng0 = " + lng0);
	        int i =  0;
	        
//		    for(int i=1 ; i<=1000 ; i++){
	        while (line0 != null) {
	        	
	        	i++;
	        	
	            line0 = br0.readLine(); // read next line0
	            line0 = line0.substring(1, line0.length()-1); // cut "[" and "]" 
	            
	            obj = new JSONObject(line0);

//		        latS = (String) obj.get("lat");
//	            lngS = (String) obj.get("lng");
//	            double lat1 = Double.parseDouble(latS);
//	            double lng1 = Double.parseDouble(lngS);
//	            
//	            System.out.println("i = " + i + "\t lat0" + lat0 + "\t lng0" + lng0 +"\t lat1" + lat1 +"\t lng1" + lng1 );
	            
	            // have to filter time when server down and start on different point
//		        if(lat0 > 13.495060 && lat0 < 13.956429 && lng0 > 100.329752 && lng0 < 100.936369){
//		        	if(lat1 > 13.495060 && lat1 < 13.956429 && lng1 > 100.329752 && lng1 < 100.936369){
//		        		totalDistance = totalDistance + haversinDistance(lat0, lng0, lat1, lng1);
//			            System.out.println("total distance = " + totalDistance);
//		        	}
//		        }
//	            
//	            lat0 = lat1;
//	            lng0 = lng1;
	            
	         }// end while
//	        System.out.println("final total distance = " + totalDistance);

	    } finally {
	        br0.close();
	    }
	} catch (Exception e) {
		// TODO: handle exception
	}

        System.out.println("done");
}

public void calOccupiedTime(){

	try {
		BufferedReader br0 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/Beejkap^^/4_term_2/(PreProject)/data/Á©-2148.txt")));
		BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/Beejkap^^/4_term_2/(PreProject)/data/Á©-2148.txt")));
	    try {
	    	
	    	double occupiedDistance = 0.0;
	        String line0 = br0.readLine();
	        String line1 = br1.readLine(); // read 1st line
	        line1 = br1.readLine(); // read 2nd line 
	        line0 = line0.substring(1, line0.length()-1); // cut "[" , "]"
	        line1 = line1.substring(1, line1.length()-1); // cut "[" , "]"
	        System.out.println("line0 98 = " + line0);
	        JSONObject obj = new JSONObject(line0);
	        JSONObject tempObj = new JSONObject(line1);
	       
	        String latS;
            String lngS;
            String meterS;
            double lat0 = 0;
	        double lng0 = 0;
	        double lat1;
            double lng1;
	        double tempLat = 0;
	        double tempLng = 0;
	        int meter0;
	        int meter1;
	        
//	        System.out.println("lat0 = " + lat0 + "\t lng0 = " + lng0);
	        int enable = 0;
	    	int i = 0;
	        
//		    for(int i=0 ; i< 100 ; i++){
	        while (line0 != null ) {
	        	i = i+1;
	        	System.out.print(" i = " + i + "\t");

		        meterS = (String) obj.get("meter");
		        meter0 = Integer.parseInt(meterS);
		        
//		        System.out.println("line1" + line0);
		        
		        if(meter0 == 1){
//		        	System.out.println("line0 144");
		        	latS = (String) obj.get("lat");
		            lngS = (String) obj.get("lng");
		            lat0 = Double.parseDouble(latS);
			        lng0 = Double.parseDouble(lngS);  // already get lat0 lng0
			        
			        if(lat0 != 0 && lng0 != 0){
				        latS = (String) tempObj.get("lat");
			            lngS = (String) tempObj.get("lng");
			            lat1 = Double.parseDouble(latS);
			            lng1 = Double.parseDouble(lngS);
			            
			            meterS = (String) tempObj.get("meter");
			            meter1 = Integer.parseInt(meterS);
			            
			            // ignore same lat lng case 
			           	if(lat0 != lat1 || lng0 != lng1){
			            	if(lat0 > 13.495060 && lat0 < 13.956429 && lng0 > 100.329752 && lng0 < 100.936369){
					        	if(lat1 > 13.495060 && lat1 < 13.956429 && lng1 > 100.329752 && lng1 < 100.936369){
					        		
					        		occupiedDistance = occupiedDistance + haversinDistance(lat0, lng0, lat1, lng1);
					        		System.out.println("meter0 = " + meter0  + "\t" + "lat0 = " + lat0 + "\t" + "lng0 = " + lng0 + "\t" +"lat1 = " + lat1 + "\t"+ "lng1 = " + lng1 );
//							        System.out.println("distance with passengers = " + occupiedDistance);
					        	}
					        }
						}
			        }
		        }// end if meter = 1

		        System.out.println("distance with passengers = " + occupiedDistance);
		        
		        line0 = br0.readLine();
		        line1 = br1.readLine();
		        line0 = line0.substring(1, line0.length()-1); // cut "[" , "]"
		        line1 = line1.substring(1, line1.length()-1); // cut "[" , "]"
		        obj = new JSONObject(line0);
		        tempObj = new JSONObject(line1);
	         } // end while
	        System.out.println("occupied distance = " + occupiedDistance);

	    } finally {
	        br0.close();
	    }
	} catch (Exception e) {
		// TODO: handle exception
	}
        System.out.println("done");
}
public double sumDistancefromLatLng(double lat1, double lng1, double lat2, double lng2){
	double dis = 0.0;
	dis = haversinDistance(lat1, lng1, lat2, lng2);
	return dis;

}

public double haversinDistance(double lat1, double lng1, double lat2, double lng2){
	double distance = 0.0;
	double r = 6365.353478035722; // approx at equator 6335.439 km
	
//	radius BKK = 6365.353478035722		
	
	lat1 = Math.toRadians(lat1);
	lat2 = Math.toRadians(lat2);
	lng1 = Math.toRadians(lng1);
	lng2 = Math.toRadians(lng2);
	double sinLat = (lat2-lat1)/2;
	double sinLong = (lng2-lng1)/2;
	double powLat = Math.pow(sinLat, 2);
	double powLong = Math.pow(sinLong, 2);
	double cos1 = Math.cos(lat1);
	double cos2 = Math.cos(lat2);
	double root = Math.sqrt(powLat+cos1*cos2*powLong);
	
	distance = 2*r*Math.asin(root);
	
	return distance;
}

public double haversinRadius(double lat1, double lng1, double lat2, double lng2, double distance){
//	distance = 0.0;
	double r = 0.0;
//	double r = 6335.439; // approx 6335.439 km
	lat1 = Math.toRadians(lat1);
	lat2 = Math.toRadians(lat2);
	lng1 = Math.toRadians(lng1);
	lng2 = Math.toRadians(lng2);
	double sinLat = (lat2-lat1)/2;
	double sinLong = (lng2-lng1)/2;
	double powLat = Math.pow(sinLat, 2);
	double powLong = Math.pow(sinLong, 2);
	double cos1 = Math.cos(lat1);
	double cos2 = Math.cos(lat2);
	double root = Math.sqrt(powLat+cos1*cos2*powLong);
	
	r = distance/(2*Math.asin(root));
//	distance = 2*r*Math.asin(root);
	return r;
}

public static void main(String[]args) throws IOException{
	SimulationTaxi testsim = new SimulationTaxi();
	double totalDistance = testsim.calTotalDistance();
	double occupiedDistance = testsim.calOccupiedDistance();
	System.out.println("total = " + totalDistance);
	System.out.println("occupied = " + occupiedDistance);
	System.out.println("occupied / total = " + occupiedDistance/totalDistance);
	System.out.println("occupied / total (%) = " + (occupiedDistance/totalDistance)*100 + " % ");
	double lat1 = 13.78215;
	double lng1 = 100.47343;
	double lat2 = 13.78028;
	double lng2 = 100.47518;
	double dis = testsim.haversinDistance(lat1,lng1,lat2,lng2);
	double radius = testsim.haversinRadius(13.720686, 100.457720,13.757683, 100.519475,7.8);
	
//	URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?sensor=false&origin=(13.730780,100.540878)&destination=(13.755291,100.492984)&language=en,OK&key=" + GoogleKey);
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		
//		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
// 
//		while ((inputLine = in.readLine()) != null) {
//			System.out.println(inputLine);
//			response.append(inputLine);
	}
	
//		System.out.println(response);
//		in.close();
		
//	}
  
}
