package kramer.jeff.stock.option;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
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
		
		Stock[] sArray = new Stock[2];
		sArray[0] = new Stock("AMZN", "Amazon.com, Inc.", 0.25, 1, "Account 1");
		sArray[1] = new Stock("NKE", "Nike Inc", .4, 1, "");
		
		for(Stock s : sArray){
			sImpl.insertStock(s);
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
		
		dbi.closeConnection();
	}

	@Test
	public void testUpdateStock() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		
		Stock s = new Stock("NFLX", "Netflix, Inc.", 0.25, 1, "Account 1");
				
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1)";		
		execute(query, conn);
		
		s.setCompanyName("Netflix, Incorporated");
		s.setAnnualDivRate(.30);
		
		sImpl.updateStock(s);
		
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
		
		dbi.closeConnection();
	}

	@Test
	public void testDeactivateStock() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		
		Stock s = new Stock("APPL", "Apple Inc.", 0.25, 1, "Account 1");
				
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('APPL', 'Apple Inc.', 0.25, 1)";		
		execute(query, conn);
		
		sImpl.deactivateStock(s);
		
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
		
		dbi.closeConnection();
	}

	@Test
	public void testGetAllStocks() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockDAOImpl sImpl = new StockDAOImpl();
		Connection conn = dbi.getConnection();
		String query;
		LinkedHashMap<String, Stock> allStocks = new LinkedHashMap<String, Stock>();
		
		Stock[] stocks = new Stock[3];
		
		stocks[0] = new Stock("FB", "Facebook, Inc. Common Stock", 0.57, 1);
		stocks[1] = new Stock("ORCL", "Oracle Corporation", 0.69, 1);
		stocks[2] = new Stock("USD", "Walt Disney Co.", 0.86, 1);
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('ORCL', 'Oracle Corporation', 0.69, 1),"
					+ "('FB', 'Facebook, Inc. Common Stock', 0.57, 1),"
					+ "('USD', 'Walt Disney Co.', 0.86, 1)";		
		execute(query, conn);
		
		allStocks = sImpl.getAllStocks();
		
		int i = 0;
		for(Map.Entry<String, Stock> stock : allStocks.entrySet()){
			assertEquals(stocks[i].getSymbol(), stock.getKey());
			assertEquals(stocks[i].getCompanyName(), stock.getValue().getCompanyName());
			assertEquals(stocks[i].getAnnualDivRate(), stock.getValue().getAnnualDivRate(), 0);
			i++;
		}
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		
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
		
		Stock s1 = new Stock("KO", "The Coca-Cola Co.", 0.89, 1);
		Stock s2 = new Stock("GOOGL", "Alphabet Inc Class A", 0.58, 1);
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES ('GOOGL', 'Alphabet Inc Class A', 0.58, 1), "
				+ "('KO', 'The Coca-Cola Co.', 0.89, 1)";
		
		execute(query, conn);
		
		sImpl.deleteStock(s1);
		
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
		
		dbi.closeConnection();
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
