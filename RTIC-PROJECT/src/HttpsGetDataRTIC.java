import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;

import java.util.ArrayList;
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
	  				
	  				List<String> carList = http.readCarList("carlist.txt");
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
	
}
/*
class Database {
    
    public Database(String url, String user, String pass){
        this.URL = url;
        this.USER = user;
        this.PASS = pass;
        this.conn = connectToDatabase();
    }
    
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = null;
    private String URL = "jdbc:mysql://localhost:3306/mysql";
    private String USER = "root";
    private String PASS = "";
    
    public List<String> getAllValueFromJSON(String JSON) {
		
		List<String> allValue = new ArrayList<String>();
		
		try{
			
        	JSONObject data = new JSONObject(JSON);
        	List<String> att = Arrays.asList(data.getNames(data));
        	
        	for(String tableAtt : att) {
        		allValue.add(data.getString(tableAtt));
        	}
        	
		 }catch(Exception e){
	            //Handle errors for Class.forName
	            e.printStackTrace();
	         }
		
		return allValue;
	}
    
    public Connection connectToDatabase(){
       
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected database successfully...");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    public void insert(String dbName, String tableName, String JSON){
        Connection conn = connectToDatabase();
        Statement stmt = null;
         //STEP 4: Execute a query
//        status = "Grey";
//        description = "Unknown";
        try{
        	
        	JSONObject data = new JSONObject(JSON);
        	List<String> att = Arrays.asList(data.getNames(data));
        	List<String> value = getAllValueFromJSON(JSON);
        	
            System.out.println("Inserting record into the table...");
            stmt = conn.createStatement();

            String sql = "INSERT INTO `" + dbName + "`.`" + tableName + "` (";
            int i = 1;
            int  j = 1;
            
            for(String tableAtt : att) {
            	
            		sql += "`" + tableAtt;
            		
            		if(i < att.size()) {
            			sql += "`, ";
            		}
            		else {
            			sql += "`) VALUES (";
            		}
            		i++;
            }
            
            for(String attValue : value) {
            	
            	sql += "'" + attValue;
            	
        		if(j < value.size()) {
        			sql += "', ";
        		}
        		else {
        			sql += "')";
        		}
        		j++;
            }
            
            System.out.println(sql);

            stmt.executeUpdate(sql);
            //stmt.executeUpdate("INSERT INTO `car_project`.`test_plate` (`lat`, `lng`, `speed`, `direction`, `acc`, `meter`, `ts`, `update_time`) VALUES ('5', '5', '5', '5', '5', '5', '5', '5')");
            System.out.println("Inserted record into the table...");

         }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }
    }//end insert
    
    public void update(String areano,String ipaddress, String status, String description){
        Connection conn = connectToDatabase();
        Statement stmt = null;
         //STEP 4: Execute a query
        try{
            System.out.println("Updating status in the table...");
            stmt = conn.createStatement();

            String sql = "UPDATE `neighborinformation` "
                         + "SET "   
                         + "`areano` ='" + areano + "', "
                         + "`status` ='" + status + "', "
                         + "`description` ='" + description + "' "
                         + "WHERE `ipaddress` ='" + ipaddress + "'";
            stmt.executeUpdate(sql);
            System.out.println("Updated record in the table...");

         }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }
    }//end update
    
    public void updateArea(String ipaddress, String areano){
        Connection conn = connectToDatabase();
        Statement stmt = null;
         //STEP 4: Execute a query
        try{
            System.out.println("Updating areano in the table...");
            stmt = conn.createStatement();
//UPDATE `neighborinformation` SET `areano`='5' WHERE `ipaddress`='2.2.2.2'
            String sql = "UPDATE `neighborinformation` "
                         + "SET "   
                         + "`areano` ='" + areano + "' "
                         + "WHERE `ipaddress`='" + ipaddress + "'";
            stmt.executeUpdate(sql);
            System.out.println("Updated areano in the table...");

         }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }
    }//end update
    
    public void deleteData(String ipaddress){
        Connection conn = connectToDatabase();
        Statement stmt = null;
         //STEP 4: Execute a query
        try{
            System.out.println("Deleting record"+ipaddress+"in the table...");
            stmt = conn.createStatement();
            //DELETE FROM `mysql`.`neighborinformation` WHERE `neighborinformation`.`ipaddress` = '2.2.2.2'
            String sql = "DELETE FROM `mysql`.`neighborinformation` "                 
                         + "WHERE `neighborinformation`.`ipaddress` ='" + ipaddress + "'";
            stmt.executeUpdate(sql);
            System.out.println("Deleted record"+ipaddress+" in the table...");

         }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }
    }//end update
    
    
    
    //if areano = 0 mean select * from table
    public ResultSet select(String areano){
        //Connection conn = connectToDatabase();
        System.out.println("reach select method");
         //STEP 4: Execute a query
        try{
            System.out.println("selecting record in the table...");
            stmt = conn.createStatement();
            sql = "SELECT * FROM `neighborinformation` WHERE `areano` = '" + areano + "'";
            rs = stmt.executeQuery(sql);
            System.out.println("Select record from the table...");
            return rs;

         }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }
        return rs;
    }//end select
    
    public ResultSet selectAll(String ip){
        //Connection conn = connectToDatabase();
        System.out.println("reach select method");
         //STEP 4: Execute a query
        try{
            System.out.println("Selecting All record in the table...");
            stmt = conn.createStatement();
            sql = "SELECT * FROM `neighborinformation` WHERE `ipaddress` = '" + ip + "'";
            rs = stmt.executeQuery(sql);
            System.out.println("Select record from the table...");
            return rs;

         }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }
        return rs;
    }//end select
}
*/