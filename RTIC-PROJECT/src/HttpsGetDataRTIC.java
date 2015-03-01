import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
 
public class HttpsGetDataRTIC {
	
	private HttpsURLConnection conn;
 
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";
	private final static String FILE_ENCODE = "UTF-8";
	private final static String DBIP = "localhost";
	private final static String DBName = "taxi_project";
	private final static String DBPass = "123457890";
	
	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("data/Á©-2148.txt"), "UTF-8"));
		String line;
		JSONArray temp = new JSONArray();
		
		while((line = br.readLine()) != null) {
			line = line.replaceAll("\\p{C}", "");
			//System.out.println(plate + "   " + line);
			JSONArray  data = null;
		
			try{
			
				data = new JSONArray(line.trim());
			
			}catch(Exception e){
			
				System.out.println(line + e);
			
			}
		
			temp.put(data);
		}
		
		System.out.println("Fin" + temp);
	  /*
	  		HttpsGetDataRTIC http = new HttpsGetDataRTIC();
	  		CookieHandler.setDefault(new CookieManager());
	  
	  		String main = "https://app.rtic-thai.info/tms/main/php/monitor.php";
	  
	  		String cookieValue = "qutl55d8s4itesruing4tm67o1";
	  		String rticCookie = http.createRTICCookie(cookieValue);

	  		Database DB = new Database("jdbc:mysql://" + DBIP + ":3306/" + DBName, DBName, DBPass);
	  		System.out.println("DB Success");
	  		
	  		while(true) {
	  			
	  			try {
	  				
	  				List<String> carList = SharedMethod.readCarList("carlist.txt");
		  			System.out.println("*****Start*****");
		  			
		  			for (String plate : carList) {
		  			
		  				String car = URLEncoder.encode(plate, FILE_ENCODE);
		  				String data = http.requestData(main + "?plate_no=" + car, rticCookie);
		  			
		  				String infor = (data.substring(1, data.length() - 1)).trim();
		  				//System.out.println(infor);
		  				DB.insertJSON(DBName, "test_plate", infor);
		  				//http. writeDataToFile("data/" + plate + ".txt", data);
		  			
		  				System.out.println(plate + ": " + data);
		  			}
		  			System.out.println("*****End*****");
		  			Thread.sleep(30000);
		  			
				} catch (Exception e) {
					// TODO: handle exception
				}
	  			
	  		}
	  */
	}
 
	private String requestData(String url, String cookie) throws Exception {
 
		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();
 
		// default is GET
		conn.setRequestMethod("GET");
 
		conn.setUseCaches(false);
	
		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Accept-Language", "n-US,en;q=0.8");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Host", "app.rtic-thai.info");
		conn.setRequestProperty("Referer", "https://app.rtic-thai.info/tms/main/");
		//conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

		conn.addRequestProperty("Cookie", cookie);
	
		conn.setDoOutput(true);
		conn.setDoInput(true);

		//int responseCode = conn.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);
	
		// Send post request
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
  
	private String createRTICCookie(String value) {
	  
	  	String cookieName = "PHPSESSID";
	  	String cookiePath = "/";
	  	
	  	return cookieName + "=" + value + "; Path=" + cookiePath;
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
}