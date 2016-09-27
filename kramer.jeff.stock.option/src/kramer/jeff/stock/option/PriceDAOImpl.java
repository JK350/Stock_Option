package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

public class PriceDAOImpl implements PriceDAO {
	
	/**
	 * Method inserts new a new price value for a given stock
	 * 
	 * @author J Kramer
	 * @param p
	 */
	@Override
	public void insertPrice(Price p) {
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(Constants.DB_URL);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		PreparedStatement pstmt = null;
		Date date = p.getDate();
		String symbol = p.getSymbol();
		double price = p.getValue();
		
		String query = "INSERT INTO " + Constants.SCHEMA + ".PRICES (Symbol, Date, Price)"
				+ " VALUES(?, ?, ?)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"PRICE_ID"});
			pstmt.setString(1, symbol);
			pstmt.setDate(2, new java.sql.Date(date.getTime()));
			pstmt.setDouble(3, price);
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				p.setPriceID(rs.getInt(1));
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
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
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(Constants.DB_URL);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		PreparedStatement pstmt = null;
		Date date = p.getDate();
		String symbol = p.getSymbol();
		double price = p.getValue();
		int pID = p.getPriceID();
		
		String query = "UPDATE " + Constants.SCHEMA + ".PRICES "
				+ "SET Symbol = ?, Date = ?, Price = ? "
				+ "WHERE Price_ID = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, symbol);
			pstmt.setDate(2, new java.sql.Date(date.getTime()));
			pstmt.setDouble(3, price);
			pstmt.setInt(4, pID);
			pstmt.executeUpdate();
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
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
	public void deletePrice(Price price, Stock stock) {
		Connection conn = null;
		int priceID = price.getPriceID();
		
		try{
			conn = DriverManager.getConnection(Constants.DB_URL);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		PreparedStatement pstmt = null;
		String query = "DELETE FROM " + Constants.SCHEMA + ".PRICES "
				+ "WHERE Price_ID = ?";
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, priceID);
			pstmt.executeUpdate();
			
			stock.dropPrice(priceID);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}

	/**
	 * Method that queries for all the entries in the Price table for a given stock
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public LinkedHashMap<Integer, Price> getStockPriceHistory(Stock stock) {
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(Constants.DB_URL);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		PreparedStatement pstmt = null;
		LinkedHashMap<Integer, Price> priceMap = new LinkedHashMap<Integer, Price>();
		Price p;
		String query = "SELECT * "
				+ "FROM " + Constants.SCHEMA + ".PRICES "
				+ "WHERE Symbol = ? "
				+ "ORDER BY Date DESC";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, stock.getSymbol());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				p = new Price(rs.getInt("Price_ID"), rs.getString("Symbol"), rs.getDate("Date"), rs.getDouble("Price"));
				priceMap.put(p.getPriceID(), p);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
		
		return priceMap;
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
