package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

public class TransactionDAOImpl implements TransactionDAO {
	
	/**
	 * Method to insert new transactions into the database
	 * 
	 * @author J Kramer
	 * @param t - The Transaction object to be entered into the databases
	 */
	@Override
	public void insertTransaction(Transaction t) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String query = "INSERT INTO " + Constants.SCHEMA + ".TRANSACTIONS (Symbol, Date, Action, Price, Net)"
				+ "VALUES(?, ?, ?, ?, ?)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"TRANSACTION_ID"});
			pstmt.setString(1, t.getStock());
			pstmt.setDate(2, new java.sql.Date(t.getTransDate().getTime()));
			pstmt.setInt(3, t.getAction());
			pstmt.setDouble(4, t.getPrice());
			pstmt.setDouble(5, t.getNet());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			
			if(rs.next()){
				t.setTransactionID(rs.getInt(1));
			}
		} catch (Exception ex){
			
		} finally {
			closeStatement(pstmt);
		}
	}

	/**
	 * Method to update a transaction
	 * 
	 * @author J Kramer
	 * @param t - Transaction to update
	 */
	@Override
	public void updateTransaction(Transaction t) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String query = "UPDATE " + Constants.SCHEMA + ".TRANSACTIONS "
				+ "SET Symbol = ?, Date = ?, Action = ?, Price = ?, Net = ?"
				+ "WHERE Transaction_ID = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, t.getStock());
			pstmt.setDate(2, new java.sql.Date(t.getTransDate().getTime()));
			pstmt.setInt(3, t.getAction());
			pstmt.setDouble(4, t.getPrice());
			pstmt.setDouble(5, t.getNet());
			pstmt.setInt(6, t.getTransactionID());
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}

	/**
	 * Method to delete a transaction
	 * 
	 * @author J Kramer
	 * @param t - Transaction to delete
	 */
	@Override
	public void deleteTransaction(Transaction t, Stock s) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String query = "DELETE FROM " + Constants.SCHEMA + ".TRANSACTIONS "
				+ "WHERE Transaction_ID = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, t.getTransactionID());
			
			pstmt.executeUpdate();			
			s.dropTransaction(t.getTransactionID());
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}

	@Override
	public LinkedHashMap<Integer, Transaction> getStockTransactionHistory(Stock stock) {
		Connection conn = getConnection();
		Transaction t;
		LinkedHashMap<Integer, Transaction> transactionMap = new LinkedHashMap<Integer, Transaction>();
		PreparedStatement pstmt = null;
		
		String query = "SELECT * "
				+ "FROM " + Constants.SCHEMA +".TRANSACTIONS "
				+ "WHERE Symbol = ? "
				+ "ORDER BY Date DESC";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, stock.getSymbol());
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				t = new Transaction(rs.getString("Symbol"), rs.getInt("Transaction_ID"), rs.getDate("Date"), rs.getInt("Action"), rs.getDouble("Price"), rs.getDouble("Net"));
				transactionMap.put(t.getTransactionID(), t);
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
		
		return transactionMap;
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
