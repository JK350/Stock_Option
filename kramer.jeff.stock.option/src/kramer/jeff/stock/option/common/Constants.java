package kramer.jeff.stock.option.common;

import java.util.HashMap;

import javafx.scene.text.*;
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
	public static final String[] TABLES = {"ACCOUNTS", "STOCKS", "PRICES", "TRANSACTIONS"};
	public static final HashMap<String, String> TABLE_CREATION_QUERIES = new HashMap<String, String>();
	
	/**
	 * Available Transaction Types
	 */
	public static final String TRANSACTION_TYPE_STOCK_PURCHASE = "Stock Purchase";
	public static final String TRANSACTION_TYPE_STOCK_SALE = "Stock Sale";
	public static final String TRANSACTION_TYPE_OPTION_SALE_CALL = "Option Sales Call";
	public static final String TRANSACTION_TYPE_STOCK_DIVIDEND = "Stock Dividend";
	
	/**
	 * Available Account Types
	 */
	public static final String ACCOUNT_TYPE_IRA = "IRA";
	public static final String ACCOUNT_TYPE_JOINT = "Joint";
	public static final String ACCOUNT_TYPE_PERSONAL = "Personal";
	
	public static final Font FONT_ARIAL_BOLD_16 = Font.font("Arial", FontWeight.BOLD, 16);	
	
	static{
		TABLE_CREATION_QUERIES.put("STOCKS", "CREATE TABLE " + SCHEMA + ".STOCK("
				+ "Stock_ID Integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "Symbol VARCHAR(8) NOT NULL,"
				+ "Name VARCHAR(256) NOT NULL,"
				+ "Annual_Div_Rate FLOAT NOT NULL,"	
				+ "Active INTEGER NOT NULL,"
				+ "Account INTEGER NOT NULL,"
				+ "PRIMARY KEY (Symbol, Account),"
				+ "FOREIGN KEY (Account) REFERENCES STOCKOPTIONS.ACCOUNTS (Account_ID))");
		TABLE_CREATION_QUERIES.put("PRICES", "CREATE TABLE " + SCHEMA + ".PRICES("
				+ "Symbol VARCHAR(8) NOT NULL,"
				+ "Date DATE NOT NULL,"
				+ "Price DECIMAL(7,2),"
				+ "Price_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "PRIMARY KEY (Symbol, Date, Price),"
				+ "FOREIGN KEY (Symbol) REFERENCES STOCKOPTIONS.STOCK (Symbol))");
		TABLE_CREATION_QUERIES.put("TRANSACTIONS", "CREATE TABLE " +  SCHEMA + ".TRANSACTIONS("
				+ "Transaction_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "Account_ID INTEGER NOT NULL,"
				+ "Stock_ID INTEGER NOT NULL,"
				+ "Transaction_Type VARCHAR(75) NOT NULL,"
				+ "Date DATE NOT NULL,"
				+ "Shares INTEGER,"
				+ "Price DECIMAL(7,2),"
				+ "Total DECIMAL(11,2),"
				+ "Net DECIMAL(11,2),"
				+ "Commission DECIMAL(7,2),"
				+ "Expiration_Date DATE,"
				+ "Strike_Price DECIMAL(7,2),"
				+ "FOREIGN KEY (STOCK_ID) REFERENCES STOCKOPTIONS.STOCKS (STOCK_ID),"
				+ "FOREIGN KEY (ACCOUNT_ID) REFERENCES STOCKOPTIONS.ACCOUNTS (ACCOUNT_ID))");
		TABLE_CREATION_QUERIES.put("ACCOUNTS", "CREATE TABLE STOCKOPTIONS.ACCOUNTS( "
				+ "Account_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "Number VARCHAR(75) NOT NULL,"
				+ "Active INTEGER NOT NULL,"
				+ "Date_Opened DATE NOT NULL,"
				+ "Nickname VARCHAR(256),"
				+ "Account_Type VARCHAR(256),"
				+ "PRIMARY KEY(Number))");
	}
	
	private Constants(){
		throw new AssertionError();
	}
}
