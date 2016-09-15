package kramer.jeff.stock.option;

import java.util.HashMap;
/**
 * Constant variables for the Stock Option Program
 * @author J Kramer
 * @version 1
 */
public final class Constants {
	public static final String DB_URL = "jdbc:derby:StockDB;create=true";
	public static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String SCHEMA = "STOCKOPTIONS";
	public static final String USER = "JKramer";
	public static final String PASSWORD = "123abc456";
	public static final String[] TABLES = {"STOCK", "PRICES", "TRANSACTIONS"};
	public static final HashMap<String, String> TABLE_CREATION_QUERIES = new HashMap<String, String>();
	
	static{
		TABLE_CREATION_QUERIES.put("STOCKS", "CREATE TABLE " + SCHEMA + ".STOCK("
				+ "Symbol VARCHAR(8) NOT NULL,"
				+ "Name VARCHAR(256) NOT NULL,"
				+ "Annual_Div_Rate FLOAT NOT NULL,"
				+ "Account INTEGER NOT NULL,"
				+ "Active INTEGER NOT NULL,"
				+ "PRIMARY KEY (Symbol))");
		TABLE_CREATION_QUERIES.put("PRICES", "CREATE TABLE " + SCHEMA + ".PRICES("
				+ "Symbol VARCHAR(8) NOT NULL,"
				+ "Date DATE NOT NULL,"
				+ "Price DECIMAL(7,2) NOT NULL,"
				+ "Price_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "PRIMARY KEY (Symbol, Date, Price),"
				+ "FOREIGN KEY (Symbol) REFERENCES STOCKOPTIONS.STOCK (Symbol))");
		TABLE_CREATION_QUERIES.put("TRANSACTIONS", "CREATE TABLE " +  SCHEMA + ".TRANSACTIONS("
				+ "Symbol VARCHAR(8) NOT NULL,"
				+ "Date DATE NOT NULL,"
				+ "Action INTEGER NOT NULL,"
				+ "Price DECIMAL(7,2),"
				+ "Net DECIMAL(8,2),"
				+ "Transaction_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "PRIMARY KEY(Symbol, Date, Action, Price),"
				+ "FOREIGN KEY (Symbol) REFERENCES STOCKOPTIONS.STOCK (Symbol))");	
	}
	
	private Constants(){
		throw new AssertionError();
	}
}
