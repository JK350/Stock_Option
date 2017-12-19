package kramer.jeff.stock.option.common;

import kramer.jeff.stock.option.common.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to initialize the database connection
 * and to check that the proper schema and tables have been created.
 * @author J Kramer
 * @version 1
 *
*/

public final class DatabaseInitializer {
	
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public DatabaseInitializer(){
		createConnection();
	}
	
	/**
	 * Method runs at start up to ensure all the proper tables are created.
	 */
	public final void startUp(){
		if(conn == null){
			createConnection();
			
			if (conn != null){
				checkSchema();
				checkTables();
			}
		}
	}
	
	/**
	 * This methods creates the connection to the database
	 */
	private void createConnection(){
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
	private final void checkSchema(){
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
	private final void createSchema(){
		System.out.println("Creating schema " + Constants.SCHEMA);
		try{
			stmt = conn.createStatement();
			stmt.execute(Constants.SCHEMA_CREATION_QUERY);
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
	private final void checkTables(){
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
	private final void createTable(String table){
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
	
	public Connection getConnection(){
		return conn;
	}
	
	public final void closeConnection(){
		try{
			if(stmt != null){
				stmt.close();
			}
			
			if(conn != null){
				DriverManager.getConnection(Constants.DB_URL + ";shutdown=true");
				conn.close();
			}
		} catch (SQLException SQLex){
			System.out.println("Database successfully closed.");
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
