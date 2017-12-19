package com.jkramer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.jkramer.model.Account;
import com.jkramer.model.Stock;
import com.jkramer.model.Transaction;

import kramer.jeff.stock.option.Constants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
		Account a = t.getStock().getAccount();
		String query = "INSERT INTO " + Constants.SCHEMA + ".TRANSACTIONS (Stock_ID, Account_ID, Date, Transaction_Type, Price, Net, Commission)"
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"TRANSACTION_ID"});
			pstmt.setInt(1, t.getStock().getStockID());
			pstmt.setInt(2, a.getAccountID());
			pstmt.setDate(3, new java.sql.Date(t.getTransactionDate().getTime()));
			pstmt.setString(4, t.getTransactionType());
			pstmt.setDouble(5, t.getPrice());
			pstmt.setDouble(6, t.getNet());
			
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
		Account a = t.getStock().getAccount();
		String query = "UPDATE " + Constants.SCHEMA + ".TRANSACTIONS "
				+ "SET Stock_ID = ?, Account_ID = ?, Date = ?, Transaction_Type = ?, Price = ?, Net = ?, Commission = ?"
				+ "WHERE Transaction_ID = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, t.getStock().getStockID());
			pstmt.setInt(2, a.getAccountID());
			pstmt.setDate(3, new java.sql.Date(t.getTransactionDate().getTime()));
			pstmt.setString(4, t.getTransactionType());
			pstmt.setDouble(5, t.getPrice());
			pstmt.setDouble(6, t.getNet());
			pstmt.setDouble(7, t.getCommission());
			pstmt.setInt(8, t.getTransactionID());
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
	public ResultSet getStockTransactionHistory(Stock stock) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "SELECT Symbol, " + Constants.SCHEMA + ".ACCOUNTS.Number, Transaction_ID, Date, Transaction_Type, Price, Net, Commission "
				+ "FROM " + Constants.SCHEMA + ".TRANSACTIONS "
				+ "JOIN " + Constants.SCHEMA + ".ACCOUNTS "
				+ "ON " + Constants.SCHEMA + ".TRANSACTIONS.Account = " + Constants.SCHEMA + ".ACCOUNTS.Account_ID "
				+ "WHERE Symbol = ? "
				+ "ORDER BY Date DESC";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, stock.getSymbol());
			
			rs = pstmt.executeQuery();			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return rs;
	}
	
	@Override
	public ResultSet getAccountTransactionHistory(Account account){
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "SELECT Symbol, " + Constants.SCHEMA + ".ACCOUNTS.Number, Transaction_ID, Date, Transaction_Type, Price, Net, Commission "
				+ "FROM " + Constants.SCHEMA + ".TRANSACTIONS "
				+ "JOIN " + Constants.SCHEMA + ".ACCOUNTS "
				+ "ON " + Constants.SCHEMA + ".TRANSACTIONS.Account = " + Constants.SCHEMA + ".ACCOUNTS.Account_ID "
				+ "WHERE Account = ? "
				+ "ORDER BY Date DESC";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, account.getAccountID());
			
			rs = pstmt.executeQuery();			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return rs;
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
