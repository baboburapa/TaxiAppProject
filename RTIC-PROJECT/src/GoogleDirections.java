import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleDirections {
	
	private final static String GoogleKey = "AIzaSyAJz2OzvJA1meJzOd9Z3XCFmK-7h2nEXeg";
	private final static String GoogleMainURL = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&units=metric&key=" + GoogleKey;
	//private final static String GoogleMainURL = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&units=metric";
	
	public static void main(String[] args) throws Exception {
		
		GoogleDirections gd = new GoogleDirections();
		
		//System.out.println(gd.getDistance());
		
	}
	
	public double getDistance(String lat, String lng, String Nlat, String Nlng)  throws Exception {
		
		//System.out.println(lat);
		//System.out.println(lng);
		//System.out.println(Nlat);
		//System.out.println(Nlng);
		
		URL url = new URL(GoogleMainURL + "&origin=(" + lat + "," + lng + ")&destination=(" + Nlat + "," + Nlng + ")");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		//StringBuffer response = new StringBuffer();
		StringBuilder sb = new StringBuilder();
 
		while ((inputLine = in.readLine()) != null) {
			
			//System.out.println(inputLine);
			sb.append(inputLine);
			
		}
		
		String response = sb.toString();
		System.out.println(response);
		in.close();
		
		JSONObject json = new JSONObject(response);
		
		JSONObject route = json.getJSONArray("routes").getJSONObject(0);
		JSONObject legs = route.getJSONArray("legs").getJSONObject(0);
		JSONObject distance = legs.getJSONObject("distance");
		String text = distance.getString("text");
		String[] Atext = text.split(" ");
		//System.out.println(text);
		
		return Double.parseDouble(Atext[0]);
		
	}
	
}
