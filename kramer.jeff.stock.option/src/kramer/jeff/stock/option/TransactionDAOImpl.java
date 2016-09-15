package kramer.jeff.stock.option;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;

public class TransactionDAOImpl implements TransactionDAO {

	private static Connection conn;

	static{
		DatabaseInitializer dbi = new DatabaseInitializer();
		conn = dbi.getConnection();
	}
	
	/**
	 * Method to insert new transactions into the database
	 * 
	 * @author J Kramer
	 * @param t - The Transaction object to be entered into the databases
	 */
	@Override
	public void insertTransaction(Transaction t) {
		Statement stmt = null;
		String query = "INSERT INTO " + Constants.SCHEMA + ".TRANSACTIONS (Symbol, Date, Action, Price, Net)"
				+ "VALUES(" + t.getStock() + "," + t.getTransDate() + "," + t.getAction() + "," + t.getPrice() + "," + t.getNet() + ")";
		
		try{
			stmt = conn.createStatement();
			stmt.executeQuery(query);
		} catch (Exception ex){
			
		} finally {
			closeStatement(stmt);
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
		Statement stmt = null;
		String query = "UPDATE " + Constants.SCHEMA + ".TRANSACTIONS "
				+ "SET Stock = " + t.getStock() + ", Date =" + t.getTransDate() + ", Action = " + t.getAction() + ", Price = " + t.getPrice() + ", Net = " + t.getNet()
				+ "WHERE Transaction_ID = " + t.getTransactionID();
		
		try{
			stmt = conn.createStatement();
			stmt.executeQuery(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStatement(stmt);
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
		Statement stmt = null;
		String query = "DELETE FROM " + Constants.SCHEMA + ".TRANSACTIONS"
				+ "WHERE Transaction_ID = " + t.getTransactionID();
		
		try{
			stmt = conn.createStatement();
			stmt.executeQuery(query);
			
			s.dropTransaction(t.getTransactionID());
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(stmt);
		}
	}

	@Override
	public void getStockTransactionHistory(Stock stock) {
		// TODO Auto-generated method stub
		Statement stmt = null;
		HashMap<Integer, Transaction> transactionMap = new HashMap<Integer, Transaction>();
		Transaction t;
		String query = "SELECT * "
				+ "FROM " + Constants.SCHEMA +".TRANSACTIONS "
				+ "WHERE Symbol = " + stock.getSymbol();
		
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				t = new Transaction(rs.getString("Symbol"), rs.getInt("Transaction_ID"), rs.getDate("Date"), rs.getInt("Action"), rs.getDouble("Price"), rs.getDouble("Net"));
				transactionMap.put(t.getTransactionID(), t);
			}
			
			stock.setTransactionHistory(transactionMap);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(stmt);
		}
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
