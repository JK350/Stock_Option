package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class StockDAOImpl implements StockDAO{
	
	private static Connection conn;
	
	static{
		DatabaseInitializer dbi = new DatabaseInitializer();
		conn = dbi.getConnection();
	}
	
	/**
	 * Method to insert a new stock into the database.
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public void insertStock(Stock stock) {
		String query = "INSERT INTO " + Constants.SCHEMA + ".STOCK" + 
				" VALUES(?, ?, ?, 1)";
		try{
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, stock.getSymbol());
			pstmt.setString(2, stock.getCompanyName());
			pstmt.setDouble(3, stock.getAnnualDivRate());
			pstmt.executeUpdate();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	/** 
	 * Method to update an existing stock.
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public void updateStock(Stock stock) {
		String query = "UPDATE " + Constants.SCHEMA + ".STOCK" +
				"SET Name = ?, Annual_Div_Rate = ?" + 
				"WHERE Symbol = ?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, stock.getCompanyName());
			pstmt.setDouble(2, stock.getAnnualDivRate());
			pstmt.setString(3, stock.getSymbol());
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}

	/**
	 * Method to mark a stock no longer active.
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public void deleteStock(Stock stock) {
		String query = "UPDATE " + Constants.SCHEMA + ".STOCK" +
				"SET Active = 0 " +
				"WHERE Symbol = " + stock.getSymbol();
		Statement stmt = null;
		
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
	 * Method to pull a HashMap containing all the stocks in the database.  Key is stock symbol, value is Stock object.
	 * 
	 * @author J Kramer
	 * @return HashMap<String, Stock>
	 */
	@Override
	public HashMap<String, Stock> getAllStocks() {
		HashMap<String, Stock> allStocks = new HashMap<String, Stock>();
		String query = "SELECT * FROM " + Constants.SCHEMA + ".STOCK " +
				"ORDER BY Symbol";
		
		Statement stmt = null;
		
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				String symbol = rs.getString("SYMBOL");
				double annDivRate = rs.getDouble("ANNUAL_DIV_RATE");
				String compName = rs.getString("NAME");
				int active = rs.getInt("ACTIVE");
				
				Stock stock = new Stock(symbol, compName, annDivRate, active);
				
				allStocks.put(symbol, stock);
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(stmt);
		}
		
		return allStocks;
	}

	/**
	 * Private method used to close a statement.
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
