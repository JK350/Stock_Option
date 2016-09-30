package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

public class StockDAOImpl implements StockDAO{
	
	/**
	 * Method to insert a new stock into the database.
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public void insertStock(Stock stock) {
		Connection conn = getConnection();
		String symbol = stock.getSymbol();
		String companyName = stock.getCompanyName();
		double annualDivRate = stock.getAnnualDivRate();
		PreparedStatement pstmt = null;
		
		String query = "INSERT INTO " + Constants.SCHEMA + ".STOCK (Symbol, Name, Annual_Div_Rate, Active)" + 
				" VALUES(?, ?, ?, 1)";
						
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, symbol);
			pstmt.setString(2, companyName);
			pstmt.setDouble(3, annualDivRate);
			
			pstmt.executeUpdate();
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
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
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String companyName = stock.getCompanyName();
		double annualDivRate = stock.getAnnualDivRate();
		String symbol = stock.getSymbol();
		
		String query = "UPDATE " + Constants.SCHEMA + ".STOCK "
				+ "SET Name = ?, Annual_Div_Rate = ?"
				+ "WHERE Symbol = ?";
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, companyName);
			pstmt.setDouble(2, annualDivRate);
			pstmt.setString(3, symbol);
			
			pstmt.executeUpdate();
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
		
	}

	/**
	 * Method to mark a stock no longer active.
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public void deactivateStock(Stock stock) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String symbol = stock.getSymbol();
		
		String query = "UPDATE " + Constants.SCHEMA + ".STOCK " +
				"SET Active = 0 " +
				"WHERE Symbol = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, symbol);
			pstmt.executeUpdate();
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
		
	}

	/**
	 * Method to pull a HashMap containing all the stocks in the database.  Key is stock symbol, value is Stock object.
	 * 
	 * @author J Kramer
	 * @return HashMap<String, Stock>
	 */
	@Override
	public LinkedHashMap<String, Stock> getAllStocks() {
		Connection conn = getConnection();
		LinkedHashMap<String, Stock> allStocks = new LinkedHashMap<String, Stock>();
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
	
	public void deleteStock(Stock stock){
		Connection conn = getConnection();
		String query = "DELETE FROM " + Constants.SCHEMA + ".STOCK "
				+ "WHERE Symbol = ?";
		PreparedStatement pstmt = null;
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, stock.getSymbol());
			pstmt.executeUpdate();
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
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
	
	/**
	 * Method returns an active connection 
	 * 
	 * @author J Kramer
	 * @return Connection object
	 */
	private Connection getConnection(){
		Connection conn = null;
		
		try{
			conn = DriverManager.getConnection(Constants.DB_URL);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return conn;
	}
}
