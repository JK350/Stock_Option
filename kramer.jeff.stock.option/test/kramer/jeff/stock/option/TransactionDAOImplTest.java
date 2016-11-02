package kramer.jeff.stock.option;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.junit.Test;

import com.jkramer.dao.TransactionDAOImpl;
import com.jkramer.model.Account;
import com.jkramer.model.Stock;
import com.jkramer.model.Transaction;

public class TransactionDAOImplTest {

	@Test
	public void testInsertTransaction() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		TransactionDAOImpl tImpl = new TransactionDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		
		//Set up database with proper information
		Account a = setUpAccount(conn);
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1, " + a.getAccountID() + ")";
		execute(query, conn);
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		Transaction[] tArray = new Transaction[2]; 
		tArray[0] = new Transaction("NFLX", a, d1, Constants.STOCK_DIVIDEND, 95.68, 5.62, 2.56);
		tArray[1] = new Transaction("NFLX", a, d1, Constants.OPTION_SALE_CALL, 95.68, -85.5, 0.00);
		
		for(Transaction t : tArray){
			tImpl.insertTransaction(t);
		}
		
		query = "SELECT * FROM STOCKOPTIONS.TRANSACTIONS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				Calendar calDB = Calendar.getInstance();
				calDB.setTime(rs.getDate("Date"));
				
				Calendar calStock = Calendar.getInstance();
				calStock.setTime(tArray[i].getTransactionDate());
				
				assertEquals(tArray[i].getStock(), rs.getString("Symbol"));
				assertEquals(calStock.YEAR, calDB.YEAR);
				assertEquals(calStock.MONTH, calDB.MONTH);
				assertEquals(calStock.DATE, calDB.DATE);
				assertEquals(tArray[i].getTransactionType(), rs.getString("Transaction_Type"));
				assertEquals(tArray[i].getPrice(), rs.getDouble("Price"), 0);
				assertEquals(tArray[i].getNet(), rs.getDouble("Net"), 0);
				assertEquals(tArray[i].getCommission(), rs.getDouble("Commission"), 0);
				assertEquals(tArray[i].getTransactionID(), rs.getInt("Transaction_ID"));
				assertEquals(tArray[i].getAccount().getNumber(), rs.getString("Number"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		deleteData(conn);
	}

	@Test
	public void testUpdateTransaction() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		TransactionDAOImpl tImpl = new TransactionDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query;
		
		//Set up database with proper information
		Account a = null;
		a = setUpAccount(conn);
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1, " + a.getAccountID() + ")";
		execute(query, conn);

		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		Transaction t = new Transaction("NFLX", a, d1, Constants.STOCK_DIVIDEND, 94.68, 58.23, 3.25);
		
		query = "INSERT INTO STOCKOPTIONS.TRANSACTIONS (Symbol, Account, Date, Transaction_Type, Price, Net, Commission) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

		try{
			pstmt = conn.prepareStatement(query, new String[] {"TRANSACTION_ID"});
			pstmt.setString(1, "NFLX");
			pstmt.setInt(2, a.getAccountID());
			pstmt.setDate(3, new java.sql.Date(d1.getTime()));
			pstmt.setString(4, Constants.STOCK_SALE);
			pstmt.setDouble(5, 94.68);
			pstmt.setDouble(6, 58.23);
			pstmt.setDouble(7, 3.25);
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				t.setTransactionID(rs.getInt(1));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		t.setPrice(6.35);
		t.setNet(100.25);
		t.setCommission(0.00);
		
		tImpl.updateTransaction(t);
		
		query = "SELECT * FROM STOCKOPTIONS.TRANSACTIONS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				assertEquals(6.35, rs.getDouble("Price"), 0);
				assertEquals(100.25, rs.getDouble("Net"), 0);
				assertEquals(0.00, rs.getDouble("Commission"), 0);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		deleteData(conn);
	}

	@Test
	public void testDeleteTransaction() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		TransactionDAOImpl tImpl = new TransactionDAOImpl();
		Connection conn = dbi.getConnection();
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		LinkedHashMap<Integer, Transaction> tMap = new LinkedHashMap<Integer, Transaction>();
		String query;
	
		//Set up database with proper information
		Account a = setUpAccount(conn);
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1, " + a.getAccountID() + ")";
		execute(query, conn);
		Stock s = new Stock("NFLX", "Netflix, Inc.", 0.25, 1);

		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
		
		query = "INSERT INTO STOCKOPTIONS.TRANSACTIONS (Symbol, Account, Date, Transaction_Type, Price, Net, Commission) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?)";
		
		Transaction[] tArray = new Transaction[2];
		tArray[0] = new Transaction("NFLX", a, d1, Constants.STOCK_SALE, 86.23, 987.25, 0.00);
		tArray[1] = new Transaction("NFLX", a, d2, Constants.OPTION_SALE_CALL, 58.32, 872.65, 78.35);
		
		for(Transaction t : tArray){
			try{
				pstmt = conn.prepareStatement(query, new String[] {"TRANSACTION_ID"});
				pstmt.setString(1, t.getStock());
				pstmt.setInt(2, t.getAccount().getAccountID());
				pstmt.setDate(3, new java.sql.Date(t.getTransactionDate().getTime()));
				pstmt.setString(4, t.getTransactionType());
				pstmt.setDouble(5, t.getPrice());
				pstmt.setDouble(6, t.getNet());
				pstmt.setDouble(7, t.getCommission());
				
				pstmt.executeUpdate();
				
				rs = pstmt.getGeneratedKeys();
				if(rs.next()){
					t.setTransactionID(rs.getInt(1));
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		s.setTransactionHistory(tMap);
		
		tImpl.deleteTransaction(tArray[1], s);
		
		query = "SELECT * FROM STOCKOPTIONS.TRANSACTIONS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				Calendar calDB = Calendar.getInstance();
				calDB.setTime(rs.getDate("Date"));
				
				Calendar calTrans = Calendar.getInstance();
				calTrans.setTime(tArray[i].getTransactionDate());
				
				assertEquals(calTrans.YEAR, calDB.YEAR);
				assertEquals(calTrans.MONTH, calDB.MONTH);
				assertEquals(calTrans.DATE, calDB.DATE);
				assertEquals(tArray[0].getPrice(), rs.getDouble("Price"), 0);
				assertEquals(tArray[0].getNet(), rs.getDouble("Net"), 0);
				assertEquals(tArray[0].getCommission(), rs.getDouble("Commission"), 0);
				assertEquals(tArray[0].getTransactionType(), rs.getString("Transaction_Type"));
				assertEquals(tArray[0].getAccount().getAccountID(), rs.getInt("Account"));
				i++;
			}
			
			assertEquals(1, i);
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		deleteData(conn);
	}

	@Test
	public void testGetStockTransactionHistory() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		TransactionDAOImpl tImpl = new TransactionDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		double[] priceArray = new double[2];
		double[] netArray = new double[2];
		double[] commissionArray = new double[2];
		
		priceArray[0] = 86.23;
		priceArray[1] = 58.32;
		
		netArray[0] = 987.25;
		netArray[1] = 872.65;
		
		commissionArray[0] = 2.35;
		commissionArray[1] = 0.00;
		
		//Set up Account and Stock in database
		Account a = setUpAccount(conn);
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1, " + a.getAccountID() + "), "
				+ "('AMZN', 'Amazon.com, Inc.', 0.65, 1, " + a.getAccountID() + ")";
		execute(query, conn);
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
		
		query = "INSERT INTO STOCKOPTIONS.TRANSACTIONS (Symbol, Account, Date, Transaction_Type, Price, Net, Commission) "
				+ "VALUES('NFLX', " + a.getAccountID() + ", '" + new java.sql.Date(d1.getTime()) + "', '" + Constants.STOCK_SALE + "', 86.23, 987.25, 2.35),"
				+ "('NFLX', " + a.getAccountID() + ", '" + new java.sql.Date(d2.getTime()) + "', '" + Constants.STOCK_SALE + "', 58.32, 872.65, 0.00),"
				+ "('AMZN', " + a.getAccountID() + ", '" + new java.sql.Date(d1.getTime()) + "', '" + Constants.STOCK_SALE + "', 56.75, -156.23, 8.35),"
				+ "('AMZN', " + a.getAccountID() + ", '" + new java.sql.Date(d2.getTime()) + "', '" + Constants.STOCK_SALE + "', 12.02, 35.26, 9.86)";
		execute(query, conn);
		
		Stock[] sArray = new Stock[2];
		sArray[0] = new Stock("NFLX", "Netflix, Inc.", 0.25, 1);
		sArray[1] = new Stock("AMZN", "Amazon.com, Inc.", 0.65, 1);
		
		ResultSet rs = tImpl.getStockTransactionHistory(sArray[0]);
		
		int i = 0;
		try{
			while(rs.next()){
				assertEquals("NFLX", rs.getString("Symbol"));
				assertEquals(Constants.STOCK_SALE, rs.getString("Transaction_Type"));
				assertEquals(priceArray[i], rs.getDouble("Price"), 0);
				assertEquals(netArray[i], rs.getDouble("Net"), 0);
				assertEquals(commissionArray[i], rs.getDouble("Commission"), 0);
				assertEquals(a.getNumber(), rs.getString("Number"));
				i++;	
			}
			
			rs.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}

		assertEquals(2, i);
		
		deleteData(conn);
	}
	
	@Test
	public void testGetAccountTransactionHistory(){
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		TransactionDAOImpl tImpl = new TransactionDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		double[] priceArray = new double[2];
		double[] netArray = new double[2];
		double[] commissionArray = new double[2];
		
		priceArray[0] = 86.23;
		priceArray[1] = 58.32;
		
		netArray[0] = 987.25;
		netArray[1] = 872.65;
		
		commissionArray[0] = 2.35;
		commissionArray[1] = 0.00;
		
		//Set up Account and Stock in database
		Calendar cal1 = Calendar.getInstance();
		Date d3 = cal1.getTime();
		
		int accTypeID = setUpAccountType(conn);	
		Account[] accountArray = new Account[2];
		accountArray[0] = new Account("acc1", "John Doe", 1, d3, "Snacky Smores", "IRA", accTypeID);
		accountArray[1] = new Account("acc2", "Jane Doe", 1, d3, "Cheesy Poofs", "IRA", accTypeID);
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active, Account_Type)"
				+ "VALUES (?, ?, ?, 1, ?)";
		
		for(Account a : accountArray){
			try{
				pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
				pstmt.setString(1, a.getNumber());
				pstmt.setString(2, a.getNickname());
				pstmt.setDate(3, new java.sql.Date(a.getDateOpened().getTime()));
				pstmt.setInt(4, a.getAccountTypeID());
				pstmt.executeUpdate();
				
				rs = pstmt.getGeneratedKeys();
				if(rs.next()){
					a.setAccountID(rs.getInt(1));
				}
			} catch (Exception ex){
				ex.printStackTrace();
			} finally {
				try{
					rs.close();
					pstmt.close();
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1, " + accountArray[0].getAccountID() + "), "
				+ "('AMZN', 'Amazon.com, Inc.', 0.65, 1, " + accountArray[1].getAccountID() + ")";
		execute(query, conn);
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
		
		query = "INSERT INTO STOCKOPTIONS.TRANSACTIONS (Symbol, Account, Date, Transaction_Type, Price, Net, Commission) "
				+ "VALUES('NFLX', " + accountArray[0].getAccountID() + ", '" + new java.sql.Date(d1.getTime()) + "', '" + Constants.STOCK_SALE + "', 86.23, 987.25, 2.35),"
				+ "('NFLX', " + accountArray[0].getAccountID() + ", '" + new java.sql.Date(d2.getTime()) + "', '" + Constants.STOCK_SALE + "', 58.32, 872.65, 0.00),"
				+ "('AMZN', " + accountArray[1].getAccountID() + ", '" + new java.sql.Date(d1.getTime()) + "', '" + Constants.STOCK_SALE + "', 56.75, -156.23, 8.35),"
				+ "('AMZN', " + accountArray[1].getAccountID() + ", '" + new java.sql.Date(d2.getTime()) + "', '" + Constants.STOCK_SALE + "', 12.02, 35.26, 9.86)";
		execute(query, conn);
		
		rs = tImpl.getAccountTransactionHistory(accountArray[0]);
		
		int i = 0;
		try{
			while(rs.next()){
				assertEquals("NFLX", rs.getString("Symbol"));
				assertEquals(Constants.STOCK_SALE, rs.getString("Transaction_Type"));
				assertEquals(priceArray[i], rs.getDouble("Price"), 0);
				assertEquals(netArray[i], rs.getDouble("Net"), 0);
				assertEquals(commissionArray[i], rs.getDouble("Commission"), 0);
				assertEquals(accountArray[0].getNumber(), rs.getString("Number"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				rs.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		assertEquals(2, i);
		
		deleteData(conn);
 	}
	
	private Account setUpAccount(Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int accTypeID = setUpAccountType(conn);
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		Account a = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", "IRA", accTypeID);
		String query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active, Account_Type)"
				+ "VALUES (?, ?, ?, 1, ?)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
			pstmt.setString(1, a.getNumber());
			pstmt.setString(2, a.getNickname());
			pstmt.setDate(3, new java.sql.Date(a.getDateOpened().getTime()));
			pstmt.setInt(4, a.getAccountTypeID());
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				a.setAccountID(rs.getInt(1));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				rs.close();
				pstmt.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return a;
	}
	
	private int setUpAccountType(Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query;
		
		int accTypeID = 0;
		String accType = "IRA";
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNT_TYPE (Account_Type) "
				+ "VALUES (?)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[]{"ACCOUNT_TYPE_ID"});
			pstmt.setString(1, accType);
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			
			if(rs.next()){
				accTypeID = rs.getInt(1);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				rs.close();
				pstmt.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return accTypeID;
	}
	
	private void deleteData(Connection conn){
		String query = "";
		
		query = "DELETE FROM STOCKOPTIONS.TRANSACTIONS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
	}
	
	private void execute(String query, Connection conn){
		Statement stmt = null;
		
		try{
			stmt = conn.createStatement();
			stmt.execute(query);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				stmt.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
