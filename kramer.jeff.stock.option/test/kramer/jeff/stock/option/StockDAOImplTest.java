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
		
		stockArray[0].setAccount(a);
		stockArray[1].setAccount(a);
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
			assertTrue(si.insertStock(s));
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
				assertEquals(stockArray[i].getStockID(), rs.getInt("Stock_ID"));
				assertEquals(a.getAccountID(), rs.getInt("Account"));
				assertEquals(stockArray[i].isActive(), rs.getBoolean("Active"));
				
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
		insertStock(stockArray[0]);
		
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
		insertStock(stockArray[0]);
		
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
		insertStock(stockArray[0]);
		insertStock(stockArray[1]);
		
		rs = si.getAllStocks();
		
		int i = 0;
		try{
			while(rs.next()){
				assertEquals(stockArray[i].getSymbol(), rs.getString("Symbol"));
				assertEquals(stockArray[i].getCompanyName(), rs.getString("Name"));
				assertEquals(stockArray[i].getAnnualDivRate(), rs.getDouble("Annual_Div_Rate"), 0);
				assertEquals(stockArray[i].getAccount().getNumber(), rs.getString("Number"));
				assertEquals(stockArray[i].getStockID(), rs.getInt("Stock_ID"));
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
	
		insertStock(stockArray[0]);
		insertStock(stockArray[1]);
		
		rs = si.getActiveStocks();
		
		try{
			while(rs.next()){
				assertEquals(stockArray[1].getSymbol(), rs.getString("Symbol"));
				assertEquals(stockArray[1].getCompanyName(), rs.getString("Name"));
				assertEquals(stockArray[1].getAnnualDivRate(), rs.getDouble("Annual_Div_Rate"), 0);
				assertEquals(stockArray[1].getAccount().getNumber(), rs.getString("Number"));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Test
	public void testDeleteStock(){
		System.out.println("Starting testDeleteStocks() ...");
		insertStock(stockArray[0]);
		insertStock(stockArray[1]);
		
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
	
	private void insertStock(Stock stock){
		PreparedStatement pstmt = null;
		query = "INSERT INTO " + Constants.SCHEMA + ".STOCK (Symbol, Name, Annual_Div_Rate, Active, Account)" + 
				" VALUES(?, ?, ?, ?, ?)";
						
		try{
			pstmt = conn.prepareStatement(query, new String[] {"STOCK_ID"});
			pstmt.setString(1, stock.getSymbol());
			pstmt.setString(2, stock.getCompanyName());
			pstmt.setDouble(3, stock.getAnnualDivRate());
			pstmt.setBoolean(4, stock.isActive());
			pstmt.setInt(5, stock.getAccount().getAccountID());
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			
			if(rs.next()){
				stock.setStockID(rs.getInt(1));
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
