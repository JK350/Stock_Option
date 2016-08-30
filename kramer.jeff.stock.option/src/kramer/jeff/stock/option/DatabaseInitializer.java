package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.*;

/**
 * This class is used to initialize the database connection
 * and to check that the proper schema and tables have been created.
 * @author J Kramer
 * @version 1
 *
*/

public class DatabaseInitializer {
	private static final String DB_URL = "jdbc:derby:StockDB;create=true";
	private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String SCHEMA = "STOCKOPTIONS";
	private static final String USER = "JKramer";
	private static final String PASSWORD = "123abc456";
	private static final String[] TABLES = {"STOCK", "PRICES", "TRANSACTIONS"};
	private static final HashMap<String, String> TABLE_CREATION_QUERIES = new HashMap<String, String>();
	
	//static block to populate the TABLE_CREATION_QUERIES constanct with the queries for each of the tables
	static{
		TABLE_CREATION_QUERIES.put("STOCKS", "CREATE TABLE " + SCHEMA + ".STOCK(Symbol VARCHAR(8) NOT NULL,Name VARCHAR(256) NOT NULL,Annual_Div_Rate FLOAT NOT NULL,Account INTEGER NOT NULL,PRIMARY KEY (Symbol))"
				);
		TABLE_CREATION_QUERIES.put("PRICES", "CREATE TABLE " + SCHEMA + ".PRICES(Symbol VARCHAR(8) NOT NULL,Date DATE NOT NULL,Price DECIMAL(7,2) NOT NULL,PRIMARY KEY (Symbol, Date, Price))");
		TABLE_CREATION_QUERIES.put("TRANSACTIONS", "CREATE TABLE " +  SCHEMA + ".TRANSACTIONS(Symbol VARCHAR(8) NOT NULL,Date DATE NOT NULL,Action INTEGER NOT NULL,Price DECIMAL(7,2),Net DECIMAL(8,2),PRIMARY KEY(Symbol, Date, Action, Price))");	
	}
	
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	
	public void startDB(){
		createConnection();
		
		if (conn != null){
			checkSchema();
			checkTables();
		}
	}
	
	/**
	 * This methods creates the connection to the database
	 */
	private static void createConnection(){
		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}	
	
	/**
	 * This method checks to see if the schema in the SCHEMA constant exists in the database
	 * If the schema does not exist, the method calls createSchema().
	 */
	private static void checkSchema(){
		try{
			boolean schemaFound = false;
			
			rs = conn.getMetaData().getSchemas();

			while(rs.next()){
				if (rs.getString(1).equals(SCHEMA)){
					System.out.println("Schema " + SCHEMA + " found.");
					schemaFound = true;
					break;
				}
			}
			
			rs.close();
			
			if(!schemaFound){
				System.out.println("Schema not found");
				createSchema();
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method creates the schema in the SCHEMA constant.
	 */
	private static void createSchema(){
		System.out.println("Creating schema " + SCHEMA);
		try{
			stmt = conn.createStatement();
			stmt.execute("CREATE SCHEMA " + SCHEMA);
			stmt.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method loops through the tables in the TABLES constant.
	 * For each table the method checks to see if the table exists in the database.
	 * If the table does not exist the createTable() method is called passing the table name.
	 */
	private static void checkTables(){
		for(String table : TABLES){
			try{
				rs = conn.getMetaData().getTables(null, SCHEMA, table, null);
				
				if(!rs.next()){
					System.out.println("Table " + table + " not found");
					createTable(table);
				} else {
					System.out.println("Table " + table + " found.");
				}
				rs.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * This method will create the missing table in the database.
	 * The method will use the table value to pull the query text from TABLE_CREATION_QUERIES HashMap
	 * @param table
	 */
	private static void createTable(String table){
		System.out.println("Creating table " + table);
		String query = TABLE_CREATION_QUERIES.get(table);
		
		try{
			stmt = conn.createStatement();
			
			stmt.execute(query);
			stmt.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
