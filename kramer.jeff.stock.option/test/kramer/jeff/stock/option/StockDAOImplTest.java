package kramer.jeff.stock.option;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jkramer.dao.StockDAOImpl;
import com.jkramer.model.Account;
import com.jkramer.model.Stock;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class StockDAOImplTest{
	
	@Test
	public void testInsertStock(){
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();

		int accTypeID = setUpAccountType(conn);
		
		Account a = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", "IRA", accTypeID);
		
		setUpAccount(a, conn);		
		
		Stock[] sArray = new Stock[2];
		sArray[0] = new Stock("AMZN", "Amazon.com, Inc.", 0.25, 1, a.getNumber());
		sArray[1] = new Stock("NKE", "Nike Inc", .4, 1, a.getNumber());
		
		for(Stock s : sArray){
			assertTrue(sImpl.insertStock(s, a.getAccountID()));
		}
		
		query = "SELECT * FROM STOCKOPTIONS.STOCK ORDER BY Symbol";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			int i = 0;
			
			while(rs.next()){
				assertEquals(sArray[i].getSymbol(), rs.getString("Symbol"));
				assertEquals(sArray[i].getCompanyName(), rs.getString("Name"));
				assertEquals(sArray[i].getAnnualDivRate(), rs.getDouble("Annual_Div_Rate"), 0);
				assertEquals(a.getAccountID(), rs.getInt("Account"));
				assertEquals(1, rs.getInt("Active"));
				
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				stmt.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
		
		dbi.closeConnection();
	}

	@Test
	public void testUpdateStock() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		int accTypeID = setUpAccountType(conn);
		
		Account a = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", "IRA", accTypeID);
		
		setUpAccount(a, conn);
		
		Stock s = new Stock("NFLX", "Netflix, Inc.", 0.25, 1, a.getNumber());
				
		query = "INSERT INTO STOCKOPTIONS.STOCK (Symbol, Name, Annual_Div_Rate, Active, Account) "
				+ "VALUES('NFLX', 'Netflix, Inc.', 0.25, 1, " + a.getAccountID() + ")";		
		execute(query, conn);
		
		s.setCompanyName("Netflix, Incorporated");
		s.setAnnualDivRate(.30);
		
		assertTrue(sImpl.updateStock(s));
		
		query = "SELECT * FROM STOCKOPTIONS.STOCK";
		
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()){
				assertEquals("Netflix, Incorporated", rs.getString("Name"));
				assertEquals(.30, rs.getDouble("Annual_Div_Rate"), 0);
			}
			
			stmt.close();
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
		
		dbi.closeConnection();
	}

	@Test
	public void testDeactivateStock() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		int accTypeID = setUpAccountType(conn);
		
		Account a = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", "IRA", accTypeID);
		
		setUpAccount(a, conn);
		
		Stock s = new Stock("APPL", "Apple Inc.", 0.25, 1, a.getNumber());
				
		query = "INSERT INTO STOCKOPTIONS.STOCK (Symbol, Name, Annual_Div_Rate, Active, Account)"
				+ " VALUES('APPL', 'Apple Inc.', 0.25, 1, " + a.getAccountID() + ")";		
		execute(query, conn);
		
		assertTrue(sImpl.deactivateStock(s));
		
		query = "SELECT * FROM STOCKOPTIONS.STOCK";
		
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				assertEquals(0, rs.getInt("Active"));
			}
			
			stmt.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
			
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
		
		dbi.closeConnection();
	}

	@Test
	public void testGetAllStocks() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		ResultSet rs = null;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		int accTypeID = setUpAccountType(conn);
		
		Account a1 = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", "IRA", accTypeID);
		Account a2 = new Account("acc2", "Jane Doe", 1, d1, "Cheesy Poofs", "IRA", accTypeID);
		
		setUpAccount(a1, conn);
		setUpAccount(a2, conn);
		
		Stock[] stocks = new Stock[3];
		
		stocks[0] = new Stock("FB", "Facebook, Inc. Common Stock", 0.57, 1, a1.getNumber());
		stocks[1] = new Stock("ORCL", "Oracle Corporation", 0.69, 1, a1.getNumber());
		stocks[2] = new Stock("USD", "Walt Disney Co.", 0.86, 1, a2.getNumber());
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('ORCL', 'Oracle Corporation', 0.69, 1, " + a1.getAccountID() + "),"
					+ "('FB', 'Facebook, Inc. Common Stock', 0.57, 1, " + a1.getAccountID() + "),"
					+ "('USD', 'Walt Disney Co.', 0.86, 1, " + a2.getAccountID() + ")";		
		execute(query, conn);
		
		rs = sImpl.getAllStocks();
		
		int i = 0;
		try{
			while(rs.next()){
				assertEquals(stocks[i].getSymbol(), rs.getString("Symbol"));
				assertEquals(stocks[i].getCompanyName(), rs.getString("Name"));
				assertEquals(stocks[i].getAnnualDivRate(), rs.getDouble("Annual_Div_Rate"), 0);
				assertEquals(stocks[i].getAccountNumber(), rs.getString("Number"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		assertEquals(3, i);
			
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
		
		dbi.closeConnection();
	}
	
	@Test
	public void testGetActiveStocks() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		ResultSet rs = null;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		int accTypeID = setUpAccountType(conn);
		
		Account a1 = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", "IRA", accTypeID);
		Account a2 = new Account("acc2", "Jane Doe", 1, d1, "Cheesy Poofs", "IRA", accTypeID);
		
		setUpAccount(a1, conn);
		setUpAccount(a2, conn);
		
		Stock[] stocks = new Stock[3];
		
		stocks[0] = new Stock("FB", "Facebook, Inc. Common Stock", 0.57, 0, a1.getNumber());
		stocks[1] = new Stock("ORCL", "Oracle Corporation", 0.69, 1, a2.getNumber());
		stocks[2] = new Stock("USD", "Walt Disney Co.", 0.86, 0, a1.getNumber());
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('ORCL', 'Oracle Corporation', 0.69, 1, " + a2.getAccountID() +"),"
					+ "('FB', 'Facebook, Inc. Common Stock', 0.57, 0, " + a1.getAccountID() + "),"
					+ "('USD', 'Walt Disney Co.', 0.86, 0, " + a1.getAccountID() + ")";		
		execute(query, conn);
		
		rs = sImpl.getActiveStocks();
		
		try{
			while(rs.next()){
				assertEquals(stocks[1].getSymbol(), rs.getString("Symbol"));
				assertEquals(stocks[1].getCompanyName(), rs.getString("Name"));
				assertEquals(stocks[1].getAnnualDivRate(), rs.getDouble("Annual_Div_Rate"), 0);
				assertEquals(stocks[1].getAccountNumber(), rs.getString("Number"));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
			
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
		
		dbi.closeConnection();
	}

	@Test
	public void testDeleteStock(){
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		int accTypeID = setUpAccountType(conn);
		
		Account a1 = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", "IRA", accTypeID);
		Account a2 = new Account("acc2", "Jane Doe", 1, d1, "Cheesy Poofs", "IRA", accTypeID);
		
		setUpAccount(a1, conn);
		setUpAccount(a2, conn);
		
		Stock s1 = new Stock("KO", "The Coca-Cola Co.", 0.89, 1, a1.getNumber());
		Stock s2 = new Stock("GOOGL", "Alphabet Inc Class A", 0.58, 1, a2.getNumber());
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES ('GOOGL', 'Alphabet Inc Class A', 0.58, 1, " + a2.getAccountID() + "), "
				+ "('KO', 'The Coca-Cola Co.', 0.89, 1, " + a1.getAccountID() + ")";
		execute(query, conn);
		
		assertTrue(sImpl.deleteStock(s1));
		
		query = "SELECT * FROM STOCKOPTIONS.STOCK";
		
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				assertEquals(s2.getSymbol(), rs.getString("Symbol"));
				i++;
			}
			
			assertEquals(1, i);
			
			stmt.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
		
		dbi.closeConnection();
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
	
	private void setUpAccount(Account a, Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
