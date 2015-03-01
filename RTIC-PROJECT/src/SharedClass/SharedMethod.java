import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


public class SharedMethod {
	
	private final static String FILE_ENCODE = "UTF-8";

	public static double haversinDistance(double lat1, double lng1, double lat2, double lng2) {
		double distance = 0.0;
		double r = 6365.353478035722; // approx at equator 6335.439 km
		
//		radius BKK = 6365.353478035722		
		
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
	
	public static double haversinDistance(Pair<Double, Double> start, Pair<Double, Double> end) {
		
		return SharedMethod.haversinDistance(start.getLeft(), start.getRight(), end.getLeft(), end.getRight());
		
	}
	
	public static boolean checkValidLatLng(double lat0, double lng0, double lat1, double lng1) {
		
		if(lat0 != lat1 || lng0 != lng1) {
			
        	if(lat0 > 13.495060 && lat0 < 13.956429 && lng0 > 100.329752 && lng0 < 100.936369) {
        		
	        	if(lat1 > 13.495060 && lat1 < 13.956429 && lng1 > 100.329752 && lng1 < 100.936369) {
	        		
	        		return true;
	        		
	        	}
	        	
        	}
        	
		}
		
		return false;
		
	}
	
	public static List<String> readCarList(String filename) throws Exception {
		
		List<String> carList = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), FILE_ENCODE));
		String line = br.readLine().substring(1);
		while(line != null) {
			carList.add(line);
			line = br.readLine();
		}
		br.close();
		
		return carList;
	}
	
	public static void writeDataToFile(String filename, String Data) throws Exception {
		
		File file = new File(filename);
	    FileWriter writer = new FileWriter(file, true);
	    PrintWriter printer = new PrintWriter(writer);
        printer.append(Data);
        printer.println("");
        printer.close();
        writer.close();
	}
	
	public static void writeDataToFile2(String filename, String Data) throws Exception {
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), FILE_ENCODE));
	    out.append(Data);
	    out.newLine();
	    out.flush();
        out.close();
        
	}
	
	/**
	 * Seconds Mode = 0, Minutes Mode = 1, Hours Mode = 2, Days Mode = 3, Default Mode = Minutes Mode
	 **/
	public static long dateDiff(MyDate after, MyDate before, int mode) {
		
		long diff = 0;
		
		diff = after.getTime() - before.getTime();         //diff in mS
		
		if(mode == 0)					return diff / 1000;											//Seconds Mode
		else if(mode == 1)		return diff / (60 * 1000);								//Minutes Mode
		else if(mode == 2)		return diff / (60 * 60 * 1000);						//Hours Mode
		else if(mode == 3)		return diff / (24 * 60 * 60 * 1000);				//Days Mode
		else 								return diff / (60 * 1000);								//Default Mode = Minutes Mode
		
	}
	
}
