import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class SimulationData {

public static void main(String[] args) throws Exception {
		
		SimulationData sd = new SimulationData();
		
		//String day = "2015-02-01";		
		String start = "2014-12-17 00:00:00";
		String end = "2014-12-17 23:59:59";
		
		List<String> carList = SharedMethod.readCarList("carlist.txt");
		
		JSONObject test = sd.getAllJSONInRange(carList, new MyDate(start), new MyDate(end));

			
	}

	public JSONObject getAllJSONInRange(List<String> carList, MyDate start, MyDate end) throws Exception {
		
		JSONObject allJSON = new JSONObject();
		
			for (String plate : carList) {
			
				//System.out.println(plate);
			
				BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("data/" + plate + ".txt"), "UTF-8"));
				String line;
				JSONArray temp = new JSONArray();
			
				while((line = br.readLine()) != null) {
				
					line = line.replaceAll("\\p{C}", "");
					//System.out.println(plate + "   " + line);
					JSONArray  data;
				
					try{
					
						data = new JSONArray(line.trim());
					
					}catch(Exception e){
					
						System.out.println(plate +  ":" + line + e);
						break;
					
					}
					
					JSONObject json = data.getJSONObject(0);
					MyDate date = new MyDate(json.getString("update_time"));
					if(date.isBetween(start, end)) temp.put(json);
				
				}  // End while
			
			
				allJSON.put(plate, temp);
		
			}  // End for
		
			System.out.println("Get JSON Complete ^_^");
			return allJSON;
		
	}  // End getAllJSONInRange
	
}
