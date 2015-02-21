import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProcessData {
	
	private final static String DBIP = "localhost";
	//private final static String DBName = "taxi_project";
	//private final static String DBPass = "123457890";
	private final static String DBName = "root";
	private final static String DBPass = "";
	private final static String DateFormat = "yyyy-MM-dd HH:mm:ss";
	
	HttpsGetDataRTIC http = new HttpsGetDataRTIC();
	//Database DB = new Database("jdbc:mysql://" + DBIP + ":3306/", DBName, DBPass);
	GoogleDirections gd = new GoogleDirections();
	SimulationTaxi st = new SimulationTaxi();

	public static void main(String[] args) throws Exception {
		
		ProcessData pd = new ProcessData();
		
		List<GeoRouteList> allRoute = pd.getAllRoute();
		
		//List<Pair<String, Double>> totalDis = pd.calTotalDis(allRoute);
		//List<Pair<String, Double>> occDis = pd.calOccDis(allRoute);
		
		for(GeoRouteList grl : allRoute) {
			
			grl.print();
			
		}
		
		/*
		for(Pair<String, Double> dp : totalDis) {
			
			System.out.print("[Total] ");
			dp.print();
			
		}
		
		for(Pair<String, Double> dp : occDis) {
			
			System.out.print("[Occ] ");
			dp.print();
			
		}
		*/
		//System.out.println(st.calTotalDistance());
		
	}
	/*
	public List<Pair<String, Double>> calOccDis(List<GeoRouteList> allRoute) {
		
		String plate = "";
		List<Pair<String, Double>> allDis = new ArrayList<Pair<String,Double>>();
		
		for(GeoRouteList grList : allRoute) {
			
			int i = 0;
			double dis = 0.0;
			
			for(GeoRoute gr : grList) {
				
				plate = gr.getPlate();
				
				if(gr.getType() == 1) {
					
					dis += st.haversinDistance(gr.getLat(), gr.getLng(), gr.getNlat(), gr.getNlng());
					i++;
					
				}
				//gr.printGeoRoute();
				//System.out.println("dis = " + st.haversinDistance(gr.getLat(), gr.getLng(), gr.getNlat(), gr.getNlng()));
				
			}
			
			allDis.add(new Pair<String, Double>(plate, dis));
			//System.out.println(plate + " : " + dis + " Times: " + i);
			
		}
		
		return allDis;
		
	}
	
	public List<Pair<String, Double>> calTotalDis(List<GeoRouteList> allRoute) {
		
		String plate = "";
		 List<Pair<String, Double>> allDis = new ArrayList<Pair<String,Double>>();
		
		for(GeoRouteList grList : allRoute) {
			
			int i = 0;
			double dis = 0.0;
			
			for(GeoRoute gr : grList) {
				
				plate = gr.getPlate();
				dis += st.haversinDistance(gr.getLat(), gr.getLng(), gr.getNlat(), gr.getNlng());
				i++;
				//gr.printGeoRoute();
				//System.out.println("dis = " + st.haversinDistance(gr.getLat(), gr.getLng(), gr.getNlat(), gr.getNlng()));
				
			}
			
			allDis.add(new Pair<String, Double>(plate, dis));
			//System.out.println(plate + " : " + dis + " Times: " + i);
			
		}
		
		return allDis;
		
	}
	*/
	public List<GeoRouteList> getAllRoute() throws Exception {
		
		List<GeoRouteList> allRoute = new ArrayList<GeoRouteList>();
		
		List<String> carList = http.readCarList("carlist.txt");
		
		JSONObject allJSON = getAllJSON(carList);
		
		double lat = -1;
		double lng = -1;
		String before = "";
		//String after = "";
		int meter = -1;
		
		for(String plate : carList) {
			
			JSONArray ja = allJSON.getJSONArray(plate);
			JSONObject jo;
			GeoRouteList allRouteOfPlate = new GeoRouteList(plate);
			
			for(int i = 0 ; i < ja.length() ; i++) {
				
				jo = ja.getJSONObject(i);
				
				if(before != "" && dateDiff(DateFormat, before, jo.getString("update_time")) < 5) {
					
					if(checkValidLatLng(lat, lng, jo.getDouble("lat"), jo.getDouble("lng"))) {
						
						GeoRoute gr = new GeoRoute(meter, lat, lng, jo.getDouble("lat"), jo.getDouble("lng"));
						allRouteOfPlate.add(gr);
						//gr.printGeoRoute();
						
					}
					
				}
					
				before = jo.getString("update_time");
				meter = jo.getInt("meter");
				lat = jo.getDouble("lat");
				lng = jo.getDouble("lng");
				
			}  // End for i
			
			allRoute.add(allRouteOfPlate);
			
		}  // End for plate
		
		System.out.println("Get Route Complete :D");
		return allRoute;

	}
	
	public boolean checkValidLatLng(double lat0, double lng0, double lat1, double lng1) {
		
		if(lat0 != lat1 || lng0 != lng1) {
			
        	if(lat0 > 13.495060 && lat0 < 13.956429 && lng0 > 100.329752 && lng0 < 100.936369) {
        		
	        	if(lat1 > 13.495060 && lat1 < 13.956429 && lng1 > 100.329752 && lng1 < 100.936369) {
	        		
	        		return true;
	        		
	        	}
	        	
        	}
        	
		}
		
		return false;
		
	}
	
	/*
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

				line = br.readLine();
			}
			br.close();
		}
		
	}
	*/
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
	
	public void getOccupyPoint() throws Exception {
		
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
	
	public JSONObject getAllJSON(List<String> carList) throws Exception {
		
		JSONObject allJSON = new JSONObject();
		
			for (String plate : carList) {
			
				//System.out.println(plate);
			
				BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("data/" + plate + ".txt"), "UTF-8"));
				String line;
				JSONArray temp = new JSONArray();
			
				while((line = br.readLine()) != null) {
				
					//String infor = (line.substring(1, line.length() - 1)).trim();
					//System.out.println(infor);
					line = line.replaceAll("\\p{C}", "");
					JSONArray  data;
				
					try{
					
						data = new JSONArray(line.trim());
						//data = new JSONObject(infor);
					
					}catch(Exception e){
					
						System.out.println(plate +  ":" + line + e);
						line = line.substring(1);
						data = new JSONArray(line.trim());
						//break;
					
					}
				
					//data.accumulate("plate", plate);
					temp.put(data.getJSONObject(0));
				
				}  // End while
			
			
				allJSON.put(plate, temp);
		
			}  // End for
		
			System.out.println("Get JSON Complete ^_^");
			return allJSON;
		
	}
	
}  // End class
