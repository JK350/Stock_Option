package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * This class is used to initialize the database connection
 * and to check that the proper schema and tables have been created.
 * @author J Kramer
 * @version 1
 *
*/

public class DatabaseInitializer {
	
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
			Class.forName(Constants.JDBC_DRIVER);
			conn = DriverManager.getConnection(Constants.DB_URL, Constants.USER, Constants.PASSWORD);
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
				if (rs.getString(1).equals(Constants.SCHEMA)){
					System.out.println("Schema " + Constants.SCHEMA + " found.");
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
		System.out.println("Creating schema " + Constants.SCHEMA);
		try{
			stmt = conn.createStatement();
			stmt.execute("CREATE SCHEMA " + Constants.SCHEMA);
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
		for(String table : Constants.TABLES){
			try{
				rs = conn.getMetaData().getTables(null, Constants.SCHEMA, table, null);
				
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
		String query = Constants.TABLE_CREATION_QUERIES.get(table);
		
		try{
			stmt = conn.createStatement();
			
			stmt.execute(query);
			stmt.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
