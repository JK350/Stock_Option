package kramer.jeff.stock.option;

import java.util.HashMap;

public final class Constants {
	public static final String DB_URL = "jdbc:derby:StockDB;create=true";
	public static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String SCHEMA = "STOCKOPTIONS";
	public static final String USER = "JKramer";
	public static final String PASSWORD = "123abc456";
	public static final String[] TABLES = {"STOCK", "PRICES", "TRANSACTIONS"};
	public static final HashMap<String, String> TABLE_CREATION_QUERIES = new HashMap<String, String>();
	
	static{
		TABLE_CREATION_QUERIES.put("STOCKS", "CREATE TABLE " + SCHEMA + ".STOCK(Symbol VARCHAR(8) NOT NULL,Name VARCHAR(256) NOT NULL,Annual_Div_Rate FLOAT NOT NULL,Account INTEGER NOT NULL,PRIMARY KEY (Symbol))"
				);
		TABLE_CREATION_QUERIES.put("PRICES", "CREATE TABLE " + SCHEMA + ".PRICES(Symbol VARCHAR(8) NOT NULL,Date DATE NOT NULL,Price DECIMAL(7,2) NOT NULL,PRIMARY KEY (Symbol, Date, Price))");
		TABLE_CREATION_QUERIES.put("TRANSACTIONS", "CREATE TABLE " +  SCHEMA + ".TRANSACTIONS(Symbol VARCHAR(8) NOT NULL,Date DATE NOT NULL,Action INTEGER NOT NULL,Price DECIMAL(7,2),Net DECIMAL(8,2),PRIMARY KEY(Symbol, Date, Action, Price))");	
	}
	
	private Constants(){
		
		throw new AssertionError();
	}
}
