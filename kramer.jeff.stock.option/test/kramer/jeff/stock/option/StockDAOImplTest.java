package kramer.jeff.stock.option;

import static org.junit.Assert.*;
import org.junit.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jkramer.dao.StockDAOImpl;
import com.jkramer.model.Account;
import com.jkramer.model.Stock;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StockDAOImplTest{
	
	private DatabaseInitializer dbi = new DatabaseInitializer();
	private ApplicationContext context = null;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private StockDAOImpl si = new StockDAOImpl();
	private Account a = null;
	private Stock[] stockArray = new Stock[2];
	private String query = "";
	
	@Before
	public void initialize(){
		System.out.println("Initializing Test Data ...");
		
		conn = dbi.getConnection();
		context = new ClassPathXmlApplicationContext("TestBeans.xml");
		
		a = (Account) context.getBean("accountOne");
		insertAccount(a, conn);
		
		stockArray[0] = (Stock) context.getBean("stockOne");
		stockArray[1] = (Stock) context.getBean("stockTwo");
		
		stockArray[0].setAccountNumber(a.getNumber());
		stockArray[1].setAccountNumber(a.getNumber());
	}
	
	@After
	public void close(){
		System.out.println("Closing Test ...");

		deleteData(conn);
		dbi.closeConnection();
	}
	
	@Test
	public void testInsertStock(){
		System.out.println("Starting testInsertStock() ...");
		for(Stock s : stockArray){
			assertTrue(si.insertStock(s, a.getAccountID()));
		}
		
		query = "SELECT * FROM STOCKOPTIONS.STOCK ORDER BY Symbol";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			int i = 0;
			
			while(rs.next()){
				assertEquals(stockArray[i].getSymbol(), rs.getString("Symbol"));
				assertEquals(stockArray[i].getCompanyName(), rs.getString("Name"));
				assertEquals(stockArray[i].getAnnualDivRate(), rs.getDouble("Annual_Div_Rate"), 0);
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
	}

	@Test
	public void testUpdateStock() {
		System.out.println("Starting testUpdateStock() ...");
		query = "INSERT INTO STOCKOPTIONS.STOCK (Symbol, Name, Annual_Div_Rate, Active, Account) "
				+ "VALUES('AMZN', 'Amazon.com, Inc.', 0.25, 0, " + a.getAccountID() + ")";		
		execute(query, conn);
		
		stockArray[0].setCompanyName("Netflix, Incorporated");
		stockArray[0].setAnnualDivRate(.30);
		
		assertTrue(si.updateStock(stockArray[0]));
		
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
	}

	@Test
	public void testDeactivateStock() {
		System.out.println("Starting testDeactivateStock() ...");
		query = "INSERT INTO STOCKOPTIONS.STOCK (Symbol, Name, Annual_Div_Rate, Active, Account) "
				+ "VALUES('AMZN', 'Amazon.com, Inc.', 0.25, 0, " + a.getAccountID() + ")";		
		execute(query, conn);
		
		assertTrue(si.deactivateStock(stockArray[0]));
		
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
	}

	@Test
	public void testGetAllStocks() {
		System.out.println("Starting testGetAllStocks() ...");

		query = "INSERT INTO STOCKOPTIONS.STOCK (Symbol, Name, Annual_Div_Rate, Active, Account) "
				+ "VALUES('AMZN', 'Amazon.com, Inc.', 0.25, 0, " + a.getAccountID() + "),"
				+ "('NKE', 'Nike Inc', 0.4, 1, " + a.getAccountID() + ")";	
		execute(query, conn);
		
		rs = si.getAllStocks();
		
		int i = 0;
		try{
			while(rs.next()){
				assertEquals(stockArray[i].getSymbol(), rs.getString("Symbol"));
				assertEquals(stockArray[i].getCompanyName(), rs.getString("Name"));
				assertEquals(stockArray[i].getAnnualDivRate(), rs.getDouble("Annual_Div_Rate"), 0);
				assertEquals(stockArray[i].getAccountNumber(), rs.getString("Number"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		assertEquals(2, i);	
	}
	
	@Test
	public void testGetActiveStocks() {
		System.out.println("Starting testGetAllStocks() ...");

		query = "INSERT INTO STOCKOPTIONS.STOCK (Symbol, Name, Annual_Div_Rate, Active, Account) "
				+ "VALUES('AMZN', 'Amazon.com, Inc.', 0.25, 0, " + a.getAccountID() + "),"
				+ "('NKE', 'Nike Inc', 0.4, 1, " + a.getAccountID() + ")";	
		execute(query, conn);
		
		rs = si.getActiveStocks();
		
		try{
			while(rs.next()){
				assertEquals(stockArray[1].getSymbol(), rs.getString("Symbol"));
				assertEquals(stockArray[1].getCompanyName(), rs.getString("Name"));
				assertEquals(stockArray[1].getAnnualDivRate(), rs.getDouble("Annual_Div_Rate"), 0);
				assertEquals(stockArray[1].getAccountNumber(), rs.getString("Number"));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Test
	public void testDeleteStock(){
		System.out.println("Starting testDeleteStocks() ...");

		query = "INSERT INTO STOCKOPTIONS.STOCK (Symbol, Name, Annual_Div_Rate, Active, Account) "
				+ "VALUES('AMZN', 'Amazon.com, Inc.', 0.25, 0, " + a.getAccountID() + "),"
				+ "('NKE', 'Nike Inc', 0.4, 1, " + a.getAccountID() + ")";	
		execute(query, conn);
		
		assertTrue(si.deleteStock(stockArray[1]));
		
		query = "SELECT * FROM STOCKOPTIONS.STOCK";
		
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				assertEquals(stockArray[0].getSymbol(), rs.getString("Symbol"));
				i++;
			}
			
			assertEquals(1, i);
			
			stmt.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void insertAccount(Account a, Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active, Account_Type)"
				+ "VALUES (?, ?, ?, 1, ?)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
			pstmt.setString(1, a.getNumber());
			pstmt.setString(2, a.getNickname());
			pstmt.setDate(3, new java.sql.Date(a.getDateOpened().getTime()));
			pstmt.setString(4, a.getAccountType());
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
	
	private void deleteData(Connection conn){
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
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
