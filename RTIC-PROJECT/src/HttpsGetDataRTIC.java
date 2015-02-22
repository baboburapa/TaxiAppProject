import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;
 
public class HttpsGetDataRTIC {
	
	private HttpsURLConnection conn;
 
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";
	private final static String FILE_ENCODE = "UTF-8";
	private final static String DBIP = "localhost";
	private final static String DBName = "taxi_project";
	private final static String DBPass = "123457890";
	
	public static void main(String[] args) throws Exception {
	  
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
	public List<String> readCarList(String filename) throws Exception {
		
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
	
	public void writeDataToFile(String filename, String Data) throws Exception {
		
		File file = new File(filename);
	    FileWriter writer = new FileWriter(file, true);
	    PrintWriter printer = new PrintWriter(writer);
        printer.append(Data);
        printer.println("");
        printer.close();
        writer.close();
	}
	*/
}