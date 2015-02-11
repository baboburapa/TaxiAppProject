import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class ProcessData {
	
	private final static String DBIP = "localhost";
	//private final static String DBName = "taxi_project";
	//private final static String DBPass = "123457890";
	private final static String DBName = "root";
	private final static String DBPass = "";
	private final static String DateFormat = "yyyy-MM-dd HH:mm:ss";
	
	HttpsGetDataRTIC http = new HttpsGetDataRTIC();
	Database DB = new Database("jdbc:mysql://" + DBIP + ":3306/", DBName, DBPass);
	GoogleDirections gd = new GoogleDirections();

	public static void main(String[] args) throws Exception {
		
		ProcessData pd = new ProcessData();
		
		List<String> carList = pd.http.readCarList("carlist.txt");
		
		for (String plate : carList) {
			
			System.out.println(plate);
			BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("data/" + plate + ".txt"), "UTF-8"));
//			String line = br.readLine();
			
			String before = null;
			String after = null;
			String lat = null;
			String lng = null;
			String Nlat = null;
			String Nlng = null;
			long diff;
			double allDis = 0.0;
			double allOccDis = 0.0;
			String line;
			int i =0;
			
			while((line = br.readLine()) != null) {
				
				String infor = (line.substring(1, line.length() - 1)).trim();
				System.out.println(infor);
				JSONObject data;
				
				try{
					
					data = new JSONObject(infor);
					
				}catch(Exception e){
					
					System.out.println(plate +  ":" + infor + e);
					break;
					
				}

				if(!((data.getDouble("lat") > 13.495060) && (data.getDouble("lat") < 13.956429) && (data.getDouble("lng") > 100.329752) && (data.getDouble("lng") < 100.936369))) {
					System.out.println("GG");
					continue;
				}
				
				if(before == null) {
					
					before = data.getString("update_time");
					lat = data.getDouble("lat") + "";
					lng = data.getDouble("lng") + "";
					
				}
				else {
					
					after = data.getString("update_time");
					Nlat = data.getDouble("lat") + "";
					Nlng = data.getDouble("lng") + "";
					
					diff = pd.dateDiff(DateFormat, before, after);
					
					if(diff < 5 && !(lat.equals(Nlat)) && !lng.equals(Nlng)) {
						
						allDis += pd.gd.getDistance(lat, lng, Nlat, Nlng);
						System.out.println(plate + (++i));
						System.out.println(++i);
					}
					
					before = after;
					lat = Nlat;
					lng = Nlng;

				}
				
				System.out.println(allDis);
			}
			br.close();
		}
		
	}
	
	public void sendOldDataToDB(String dbName) throws Exception {
		
		List<String> carList = http.readCarList("carlist.txt");
		//SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		
		for (String plate : carList) {
			BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("data/" + plate + ".txt"), "UTF-8"));
			String line = br.readLine();
			
			while(line != null) {
				String infor = (line.substring(1, line.length() - 1)).trim();
				try {
					DB.insertJSON(dbName, plate, infor);
				}catch(Exception e) {
					DB.insertJSON(dbName, plate, infor);
				}
				/*
				JSONObject data;
				try{
					data = new JSONObject(infor);
				}catch(Exception e){
					System.out.println(plate +  ":" + infor + e);
					break;
				}
				
				//String meter = data .getInt("meter") + "";
				//String speed = data .getString("speed");
				//String acc = data .getInt("acc") + "";
				String datetime = data .getString("update_time");
				//String lat = data .getDouble("lat") + "";
				//String lng = data .getDouble("lng") + "";
				
				if(before == null)		before = datetime;
				else {
					
					diff = dateDiff(DateFormat, before, datetime);
					if(diff <= 3) {
						DB.insertJSON(dbName, plate, infor);
						before = datetime;
					}
					else {
						DB.insertJSON(dbName, plate,"{\"lat\":\"a\",\"lng\":\"a\",\"speed\":\"a\",\"direction\":\"a\",\"acc\":\"a\",\"meter\":\"a\",\"ts\":\"a\"}");
						DB.insertJSON(dbName, plate, infor);
						before = datetime;
					}
					
				}
				*/
				line = br.readLine();
			}
			br.close();
		}
		
	}
	
	public long dateDiff(String dateFormat, String date1, String date2) {
		
		long diff = 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		
		Date before = null;
		Date after = null;
		
		try {
		    before = sdf.parse(date1);
		    after = sdf.parse(date2);
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		diff = after.getTime() - before.getTime();         //diff in mS
		
		//long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		//long diffHours = diff / (60 * 60 * 1000);
		//long diffDays = diff / (24 * 60 * 60 * 1000);		
		
		return diffMinutes;
		
	}
	
	public void getOccupyPoint() throws Exception{
		
		List<String> carList = http.readCarList("carlist.txt");
		int allCount = 0;
		long allData = 0;
		
		for (String plate : carList) {
			BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("data/" + plate + ".txt"), "UTF-8"));
			String line = br.readLine();
			
			int count = 0;
			String state = "int";
			
			System.out.println("*****" + plate + "*****");
			
			while(line != null) {
				String infor = (line.substring(1, line.length() - 1)).trim();
				//System.out.println(infor);
				JSONObject data;
				try{
					data = new JSONObject(infor);
				}catch(Exception e){
					System.out.println(plate +  ":" + infor);
					break;
				}
				
				allData++;
				
				String meter = data .getInt("meter") + "";
				//String speed = data .getString("speed");
				String acc = data .getInt("acc") + "";
				//String time = data .getString("update_time");
				//String lat = data .getDouble("lat") + "";
				//String lng = data .getDouble("lng") + "";
				
				if(state.equals("int") && meter.equals("0") && acc.equals("0")) {
					state = "vacant";
				}
				else if(state.equals("vacant") && meter.equals("1") && acc.equals("0")) {
					state = "occupy";
					http. writeDataToFile("result/" + plate + ".txt", infor);
					count++;
					System.out.println(infor);
				}
				else if(state.equals("occupy") && meter.equals("0") && acc.equals("0")) {
					state = "vacant";
				}
				line = br.readLine();
			}
			br.close();
			http. writeDataToFile("result/" + "Conclusion.txt", plate +  ": " + count);
			allCount += count;
			System.out.println("*****END*****");
		}
		
		http. writeDataToFile("result/" + "Conclusion.txt", "AllCount: " + allCount);
		http. writeDataToFile("result/" + "Conclusion.txt", "AllData: " + allData);
		System.out.println("All Count = " + allCount);
		System.out.println("All Data = " + allData);
		
	}
	
}
