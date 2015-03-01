import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class ProcessData {
	
	//private final static String DBIP = "localhost";
	//private final static String DBName = "taxi_project";
	//private final static String DBPass = "123457890";
	//private final static String DBName = "root";
	//private final static String DBPass = "";
	//private final static String DateFormat = "yyyy-MM-dd HH:mm:ss";
	
	//HttpsGetDataRTIC http = new HttpsGetDataRTIC();
	//Database DB = new Database("jdbc:mysql://" + DBIP + ":3306/", DBName, DBPass);
	//GoogleDirections gd = new GoogleDirections();
	//SimulationTaxi st = new SimulationTaxi();

	public static void main(String[] args) throws Exception {
		
		ProcessData pd = new ProcessData();
		
		String day = "2015-01-31";		
		//String start = "2014-12-17 00:00:00";
		//String end = "2014-12-17 23:59:59";
		
		AllRoute allRoute = pd.getAllRouteInDay(day);
		
		int passengerCount = 0;
		for(GeoRouteList grl : allRoute) passengerCount += grl.getPassengerIndex().size();
		System.out.println("Raw: " + passengerCount + "Passengers");
		SharedMethod.writeDataToFile("Sim Result/" + day + ".txt", "");
		for(GeoRouteList grl : allRoute) {

			SharedMethod.writeDataToFile("Sim Result/" + day + ".txt", grl.toString());
			grl.print();
			
		}
		
		AllRoute result = pd.simulation(allRoute);
		
		int passengerCount2 = 0;
		for(GeoRouteList grl : result) passengerCount2 += grl.getPassengerIndex().size();
		System.out.println("Result: " + passengerCount2 + "Passengers");
		SharedMethod.writeDataToFile("Sim Result/" + day + ".txt", "");
		for(GeoRouteList grl : result) {

			SharedMethod.writeDataToFile("Sim Result/" + day + ".txt", grl.toString());
			grl.print();
			
		}
		
//		int passengerCount2 = 0;
//		for(GeoRouteList grl : result) passengerCount2 += grl.getPassengerIndex().size();
//		System.out.println(passengerCount);
//		System.out.println(passengerCount2);
		
		System.out.println("Compare Result: ");
		SharedMethod.writeDataToFile("Sim Result/" + day + ".txt", "");
		
		double active = 0;
		double increase = 0;
		double decrease = 0;
		double increaseRate = 0;
		double decreaseRate = 0;
		
		for(int i = 0 ; i < allRoute.size() ; i++) {
			
			GeoRouteList a = allRoute.get(i);
			GeoRouteList r = result.get(i);
			double real = a.getVacDis()/a.getTotalDis();
			double sim = r.getVacDis()/r.getTotalDis();
			double rate = 0.0;
			
			String status = "equal";
			if(real > sim) {
				
				status = "increase";
				increase++;
				rate = ((real - sim) / real) * 100;
				increaseRate += rate;
				
			}
			else if(real < sim) {
				
				status = "decrease";
				decrease++;
				rate = ((sim - real) / real) * 100;
				decreaseRate += rate;
				
			}
			
			if(r.getTotalDis() != 0) active++;
			
			String print = "Plate: " + a.getPlate() + "\tReal: " + real + "\tSim: " + sim + "\tStatus: " + status + "\tRate: " + rate + "%";
			SharedMethod.writeDataToFile("Sim Result/" + day + ".txt", print);
			System.out.println(print);
			
		} //End for
		
		String con = day + " --> " + "Taxi That Increase Ratio: " + (increase/active)*100 + "%" + "\tIncrease Average: " + (increaseRate/increase) + "%" + "\tDecrease Average: " + (decreaseRate/decrease) + "%";
		SharedMethod.writeDataToFile("Sim Result/Conclusion.txt", con);
		System.out.println(con);
			
	}
	
	public AllRoute simulation(AllRoute allRoute) throws Exception {
		
		List<String> carList = SharedMethod.readCarList("carlist.txt");
		AllRoute result = new AllRoute();
		
		int passengerCount = 0;
		for(GeoRouteList grl : allRoute) passengerCount += grl.getPassengerIndex().size();
		
		//Prepare result
		for(int i = 0 ; i < carList.size() ; i++) {
			
			String plate = carList.get(i);
			GeoRouteList grl = new GeoRouteList(plate);
			if(allRoute.get(i).size() != 0) grl.add(allRoute.get(i).get(0));
			result.add(grl);
			
		}
		
		Pair<String, GeoRoute> FPassGR;
		
		for(int i = 0 ; i < (passengerCount - 1) ; i++) {

			FPassGR = allRoute.findFirstPassenger();
			//System.out.println(FPassGR.toString());
			result.findTaxi(FPassGR);
			
		}
		
		return result;
		
	}
	
	public AllRoute getAllRouteInDay(String day) throws Exception {
		
		String start = day + " 00:00:00";
		String end = day + " 23:59:59";
		
		return getAllRoute(start, end);
		
	}

	public AllRoute getAllRoute(String start, String end) throws Exception {
		
		AllRoute allRoute = new AllRoute();
		
		List<String> carList = SharedMethod.readCarList("carlist.txt");
		
		JSONObject allJSON = getAllJSONInRange(carList, new MyDate(start), new MyDate(end));
		
		double lat = -1;
		double lng = -1;
		MyDate before = null;
		int meter = -1;
		
		for(String plate : carList) {
			
			JSONArray ja = allJSON.getJSONArray(plate);
			JSONObject jo;
			GeoRouteList allRouteOfPlate = new GeoRouteList(plate);
			
			for(int i = 0 ; i < ja.length() ; i++) {
				
				jo = ja.getJSONObject(i);
				
				if(before != null && SharedMethod.dateDiff(before, new MyDate(jo.getString("update_time")), 1) < 5) {
					
					if(SharedMethod.checkValidLatLng(lat, lng, jo.getDouble("lat"), jo.getDouble("lng"))) {
						
						GeoRoute gr = new GeoRoute(meter, lat, lng, jo.getDouble("lat"), jo.getDouble("lng"), before.toString(), jo.getString("update_time"));
						allRouteOfPlate.add(gr);
						//gr.printGeoRoute();
						
					}
					
				}
					
				before = new MyDate(jo.getString("update_time"));
				meter = jo.getInt("meter");
				lat = jo.getDouble("lat");
				lng = jo.getDouble("lng");
				
			}  // End for i
			
			allRoute.add(allRouteOfPlate);
			
		}  // End for plate
		
		System.out.println("Get Route Complete : D");
		return allRoute;

	}

	public JSONObject getAllJSONInRange(List<String> carList, MyDate start, MyDate end) throws Exception {
		
//		File folder = new File("data/");
//		File[] listOfFiles = folder.listFiles();
		
		JSONObject allJSON = new JSONObject();
		
			for (String plate : carList) {
			//for(File plate : listOfFiles){
				//System.out.println(plate);
				
				FileInputStream fis = new FileInputStream("data/" + plate + ".txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line;
				JSONArray temp = new JSONArray();
			
				while((line = br.readLine()) != null) {

					line = line.replaceAll("\\p{C}", "");
					//System.out.println(plate + "   " + line);
					JSONArray  data;
				
					try{
					
						data = new JSONArray(line.trim());
					
					}catch(Exception e){
					
						System.out.println(plate +  ": " + line + e);
						break;
					
					}
					
					JSONObject json = data.getJSONObject(0);
					MyDate date = new MyDate(json.getString("update_time"));
					if(date.isBetween(start, end)) temp.put(json);
				
				}  // End while
				
				br.close();
				//String t = plate.getName().substring(0, plate.getName().indexOf("."));
				allJSON.put(plate, temp);
				
			}  // End for
		
			System.out.println("Get JSON Complete ^_^");
			return allJSON;
		
	}  // End getAllJSONInRange
	/*
	public void getOccupyPoint() throws Exception {
		
		List<String> carList = SharedMethod.readCarList("carlist.txt");
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
					SharedMethod.writeDataToFile("result/" + plate + ".txt", infor);
					count++;
					System.out.println(infor);
				}
				else if(state.equals("occupy") && meter.equals("0") && acc.equals("0")) {
					state = "vacant";
				}
				line = br.readLine();
			}
			br.close();
			SharedMethod.writeDataToFile("result/" + "Conclusion.txt", plate +  ": " + count);
			allCount += count;
			System.out.println("*****END*****");
		}
		
		SharedMethod.writeDataToFile("result/" + "Conclusion.txt", "AllCount: " + allCount);
		SharedMethod.writeDataToFile("result/" + "Conclusion.txt", "AllData: " + allData);
		System.out.println("All Count = " + allCount);
		System.out.println("All Data = " + allData);
		
	}
	*/
}  // End class
