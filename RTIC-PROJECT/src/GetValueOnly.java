import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONObject;


public class GetValueOnly {

	public static void main(String[] args) throws Exception {
		
		HttpsGetDataRTIC http = new HttpsGetDataRTIC();
		
		List<String> carList = http.readCarList("carlist.txt");
		int count = 0;
		
		for (String plate : carList) {
			
			BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("result/" + plate + ".txt"), "UTF-8"));
			String line = br.readLine();
			
			System.out.println("*****" + plate + "*****");
			
			while(line != null) {
				System.out.println(line);
				JSONObject data;
				
				//try{
					data = new JSONObject(line);
				//}catch(Exception e){
					//System.out.println(plate +  ":" + line);
					//break;
				//}
				
				String[] datetime = (data .getString("update_time")).split(" ");
				String lat = data .getDouble("lat") + "";
				String lng = data .getDouble("lng") + "";
				String date = datetime[0];
				String time = datetime[1];
				
				String print = lat + " " + lng + " " + date + " " + time;
				System.out.println(print);
				http. writeDataToFile("Only Value/" + plate + ".txt", print);
				count++;
				line = br.readLine();
				//System.out.println(line);
			}
			
			br.close();
			System.out.println("*****End*****");
		}
		System.out.println("AllPoint = " + count);
	}
}
