package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

public class PriceDAOImpl implements PriceDAO {

	private static Connection conn;
	private String query;
	
	static{
		DatabaseInitializer dbi = new DatabaseInitializer();
		conn = dbi.getConnection();
	}
	
	/**
	 * Method inserts new a new price value for a given stock
	 * 
	 * @author J Kramer
	 * @param p
	 */
	@Override
	public void insertPrice(Price p) {
		Statement stmt = null;
		Date date = p.getDate();
		String symbol = p.getSymbol();
		double price = p.getValue();
		
		query = "INSERT INTO " + Constants.SCHEMA + ".PRICES"
				+ " VALUES(" + symbol + ", " + date + ", " + price + ")";
		
		try{
			stmt = conn.createStatement();
			stmt.executeQuery(query);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(stmt);
		}
	}
	
	/**
	 * Method updates an existing price in the database
	 * 
	 * @author J Kramer
	 * @param p
	 */
	@Override
	public void updatePrice(Price p) {
		Statement stmt = null;
		Date date = p.getDate();
		String symbol = p.getSymbol();
		double price = p.getValue();
		int pID = p.getPriceID();
		
		query = "UPDATE " + Constants.SCHEMA + ".PRICES"
				+ "SET Symbol = " + symbol + ",Date = " + date + ",Price = " + price
				+ "WHERE Price_ID = " + pID;
		
		try{
			stmt = conn.createStatement();
			stmt.executeQuery(query);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * Method to delete entries in the Prices table
	 * 
	 * @author J Kramer
	 * @param priceID
	 * @param stock
	 */
	@Override
	public void deletePrice(int priceID, Stock stock) {
		Statement stmt = null;
		query = "DELETE FROM " + Constants.SCHEMA + ".PRICES "
				+ "WHERE Price_ID = " + priceID;
		try{
			stmt = conn.createStatement();
			stmt.executeQuery(query);
			
			stock.dropPrice(priceID);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * Method that queries for all the entries in the Price table for a given stock
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public void getStockPriceHistory(Stock stock) {
		Statement stmt = null;
		HashMap<Integer, Price> priceMap = new HashMap<Integer, Price>();
		Price p;
		query = "SELECT * "
				+ "FROM " + Constants.SCHEMA + ".PRICES "
				+ "WHERE Symbol = " + stock.getSymbol()
				+ "ORDER BY Date DESC";
		
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				p = new Price(rs.getInt("Price_ID"), rs.getString("Symbol"), rs.getDate("Date"), rs.getDouble("Price"));
				priceMap.put(p.getPriceID(), p);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(stmt);
		}
		
		stock.setPriceHistory(priceMap);
	}

	@Override
	public void getFullPriceHistory() {
		// TODO Auto-generated method stub

	}

	/**
	 * Method used to close a statement.
	 * 
	 * @author J Kramer
	 * @param stmt
	 */
	private void closeStatement(Statement stmt){
		try{
			if(stmt != null){
				stmt.close();
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}	
	
}
