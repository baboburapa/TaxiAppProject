import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

public class Database {
    
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
            conn = DriverManager.getConnection(URL + "?useUnicode=true&characterEncoding=UTF-8", USER, PASS);
            System.out.println("Connected database successfully...");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    public void insertJSON(String dbName, String tableName, String JSON){
       // Connection conn = connectToDatabase();
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