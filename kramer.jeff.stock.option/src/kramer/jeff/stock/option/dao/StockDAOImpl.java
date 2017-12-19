package kramer.jeff.stock.option.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import kramer.jeff.stock.option.model.Stock;

import kramer.jeff.stock.option.common.Constants;

public class StockDAOImpl implements StockDAO{
	
	/**
	 * Method to insert a new stock into the database.
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public final boolean insertStock(Stock stock) {
		Connection conn = getConnection();
		String symbol = stock.getSymbol();
		String companyName = stock.getCompanyName();
		double annualDivRate = stock.getAnnualDivRate();
		int accountID = stock.getAccount().getAccountID();
		boolean active = stock.isActive();
		boolean success = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "INSERT INTO " + Constants.SCHEMA + ".STOCK (Symbol, Name, Annual_Div_Rate, Active, Account)" + 
				" VALUES(?, ?, ?, ?, ?)";
						
		try{
			pstmt = conn.prepareStatement(query, new String[] {"STOCK_ID"});
			pstmt.setString(1, symbol);
			pstmt.setString(2, companyName);
			pstmt.setDouble(3, annualDivRate);
			pstmt.setBoolean(4, active);
			pstmt.setInt(5, accountID);
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			
			if(rs.next()){
				stock.setStockID(rs.getInt(1));
			}
			
			success = true;
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
		
		return success;
	}

	/** 
	 * Method to update an existing stock.
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public final boolean updateStock(Stock stock) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		boolean success = false;
		String companyName = stock.getCompanyName();
		double annualDivRate = stock.getAnnualDivRate();
		String symbol = stock.getSymbol();
		int stockID = stock.getStockID();
		int accountID = stock.getAccount().getAccountID();
		
		String query = "UPDATE " + Constants.SCHEMA + ".STOCK "
				+ "SET Symbol = ?, Account = ?, Name = ?, Annual_Div_Rate = ?"
				+ "WHERE Stock_ID = ?";
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, symbol);
			pstmt.setInt(2, accountID);
			pstmt.setString(3, companyName);
			pstmt.setDouble(4, annualDivRate);
			pstmt.setInt(5, stockID);
			
			pstmt.executeUpdate();
			
			success = true;
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
		
		return success;
	}

	/**
	 * Method to mark a stock no longer active.
	 * 
	 * @author J Kramer
	 * @param stock
	 */
	@Override
	public final boolean deactivateStock(Stock stock) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		boolean success = false;
		int stockID = stock.getStockID();
		
		String query = "UPDATE " + Constants.SCHEMA + ".STOCK " +
				"SET Active = false " +
				"WHERE Stock_ID = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, stockID);
			pstmt.executeUpdate();
			
			success = true;
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
		
		return success;
	}

	/**
	 * Method to pull a HashMap containing all the stocks in the database.  Key is stock symbol, value is Stock object.
	 * 
	 * @author J Kramer
	 * @return HashMap<String, Stock>
	 */
	@Override
	public final ResultSet getAllStocks() {
		Connection conn = getConnection();
		
		String query = "SELECT Stock_ID, Symbol, Name, Annual_Div_Rate, " + Constants.SCHEMA + ".ACCOUNTS.Number, " + Constants.SCHEMA + ".STOCK.Active "
				+ "FROM " + Constants.SCHEMA + ".STOCK "
				+ "JOIN " + Constants.SCHEMA + ".ACCOUNTS "
				+ "ON " + Constants.SCHEMA + ".STOCK.Account = " + Constants.SCHEMA + ".ACCOUNTS.Account_ID "
				+ "ORDER BY Symbol";
				
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return rs;
	}
	
	/**
	 * Method to return all the active stocks in the database
	 * 
	 * @author J Kramer
	 */
	public final ResultSet getActiveStocks(){
		Connection conn = getConnection();
		String query = "SELECT Symbol, Name, Annual_Div_Rate, " + Constants.SCHEMA + ".ACCOUNTS.Number, " + Constants.SCHEMA + ".STOCK.Active "
				+ "FROM " + Constants.SCHEMA + ".STOCK "
				+ "JOIN " + Constants.SCHEMA + ".ACCOUNTS "
				+ "ON " + Constants.SCHEMA + ".STOCK.Account = " + Constants.SCHEMA + ".ACCOUNTS.Account_ID "
				+ "WHERE " + Constants.SCHEMA + ".STOCK.Active = true "
				+ "ORDER BY Symbol";;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return rs;
	}
	
	/**
	 * Method to delete a stock from the database permanently
	 * 
	 * @author J Kramer
	 * @param stock - Stock to be deleted
	 */
	public final boolean deleteStock(Stock stock){
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		boolean success = false;
		String query = "DELETE FROM " + Constants.SCHEMA + ".STOCK "
				+ "WHERE Symbol = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, stock.getSymbol());
			pstmt.executeUpdate();
			
			success = true;
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
		
		return success;
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
