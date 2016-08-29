package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseInitializer {
	private static final String DB_URL = "jdbc:derby:StockDB;create=true";
	private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String SCHEMA = "STOCKOPTIONS";
	private static final String USER = "JKramer";
	private static final String PASSWORD = "123abc456";
	
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private final String[] tables = {"STOCK", "PRICE", "TRANSACTIONS"};
	
	public void startDB(){
		createConnection();
		
		if (conn != null){
			checkSchema();
		}
	}
	
	private static void createConnection(){
		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private static void checkSchema(){
		try{
			boolean schemaFound = false;
			
			rs = conn.getMetaData().getSchemas();

			while(rs.next()){
				if (rs.getString(1).equals(SCHEMA)){
					schemaFound = true;
					break;
				}
			}
			
			if(!schemaFound){
				System.out.println("Schema not found");
				System.out.println("Creating Schema");
				createSchema();
			}
			
			//deleteSchema();

		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				rs.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	private static void createSchema(){
		try{
			stmt = conn.createStatement();
			stmt.execute("CREATE SCHEMA " + SCHEMA);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				stmt.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	private static void deleteSchema(){
		try{
			stmt = conn.createStatement();
			stmt.execute("DROP SCHEMA " + SCHEMA + " RESTRICT");
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				stmt.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
