package kramer.jeff.stock.option;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.junit.Test;

public class PriceDAOImplTest {

	@Test
	public void testInsertPrice() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		PriceDAOImpl pImpl = new PriceDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
			
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1)";
		
		execute(query, conn);
		
		Price[] pArray = new Price[2];
		pArray[0] = new Price(-1, "NFLX", d1, 95.28);
		pArray[1] = new Price(-1, "NFLX", d2, 98.25);
		
		for(Price p : pArray){
			pImpl.insertPrice(p);
		}
		
		query = "SELECT * FROM STOCKOPTIONS.PRICES ORDER BY Date DESC";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				Calendar calDB = Calendar.getInstance();
				calDB.setTime(rs.getDate("Date"));
				
				Calendar calStock = Calendar.getInstance();
				calStock.setTime(pArray[i].getDate());
				
				assertEquals(pArray[i].getSymbol(), rs.getString("Symbol"));
				assertEquals(calStock.YEAR, calDB.YEAR);
				assertEquals(calStock.MONTH, calDB.MONTH);
				assertEquals(calStock.DATE, calDB.DATE);
				assertEquals(pArray[i].getValue(), rs.getDouble("Price"), 0);
				assertEquals(pArray[i].getPriceID(), rs.getInt("Price_ID"));
				
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.PRICES";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
		
		dbi.closeConnection();
	}

	@Test
	public void testUpdatePrice() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		PriceDAOImpl pImpl = new PriceDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		int priceID = -1;
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1)";
		execute(query, conn);
		
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DAY_OF_MONTH, -6);
		
		query = "INSERT INTO STOCKOPTIONS.PRICES (Symbol, Date, Price)"
				+ "VALUES ('NFLX', '" + new java.sql.Date(cal1.getTime().getTime()) + "', 99.40)";
		execute(query, conn);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.DAY_OF_MONTH, -7);
		
		query = "SELECT * FROM STOCKOPTIONS.PRICES";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				priceID = rs.getInt("Price_ID");
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		Price p = new Price(priceID, "NFLX", cal2.getTime(), 97.34);
		
		pImpl.updatePrice(p);
		
		query = "SELECT * FROM STOCKOPTIONS.PRICES";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				Calendar calDB = Calendar.getInstance();
				calDB.setTime(rs.getDate("Date"));
				
				Calendar calStock = Calendar.getInstance();
				calStock.setTime(p.getDate());
				
				assertEquals(p.getValue(), rs.getDouble("Price"), 0);
				assertEquals(calStock.YEAR, calDB.YEAR);
				assertEquals(calStock.MONTH, calDB.MONTH);
				assertEquals(calStock.DATE, calDB.DATE);
				
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.PRICES";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
	}

	@Test
	public void testDeletePrice() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		PriceDAOImpl pImpl = new PriceDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		Price[] pArray = new Price[2];
		LinkedHashMap<Integer, Price> pMap = new LinkedHashMap<Integer, Price>();
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
		
		Stock s = new Stock("NFLX", "Netflix, Inc.", 0.25, 1);
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1)";
		
		execute(query, conn);		
		
		query = "INSERT INTO STOCKOPTIONS.PRICES (Symbol, Date, Price) "
					+ "VALUES('NFLX', '" + new java.sql.Date(d1.getTime()) + "', 95.28),"
					+ "('NFLX', '" + new java.sql.Date(d2.getTime()) + "', 98.25)";
		execute(query, conn);
		
		query = "SELECT * FROM STOCKOPTIONS.PRICES ORDER BY Date DESC";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				pArray[i] = new Price(rs.getInt("Price_ID"), rs.getString("Symbol"), rs.getDate("Date"), rs.getDouble("Price"));
				pMap.put(pArray[i].getPriceID(), pArray[i]);
				i++;
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		s.setPriceHistory(pMap);
		
		pImpl.deletePrice(pArray[0], s);
		
		query = "SELECT * FROM STOCKOPTIONS.PRICES";
		int priceID = -1;
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				priceID = rs.getInt("Price_ID");
				i++;
			}
			
			assertEquals(1, i);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		pMap = s.getPriceHistory();
		
		assertEquals(1, pMap.size());
		assertTrue(pMap.containsKey(priceID));
		
		query = "DELETE FROM STOCKOPTIONS.PRICES";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
	}

	@Test
	public void testGetStockPriceHistory() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		PriceDAOImpl pImpl = new PriceDAOImpl();
		Connection conn = dbi.getConnection();
		Double[] priceValues = new Double[2];
		String query;
		LinkedHashMap<Integer, Price> pMap = new LinkedHashMap<Integer, Price>();
		
		priceValues[0] = 95.28;
		priceValues[1] = 98.25;
		
		Stock[] sArray = new Stock[2];
		sArray[0] = new Stock("NFLX", "Netflix, Inc.", 0.25, 1);
		sArray[1] = new Stock("AMZN", "Amazon.com, Inc.", 0.95, 1);
		
		query = "INSERT INTO STOCKOPTIONS.STOCK "
				+ "VALUES('NFLX', 'Netflic, Inc.', 0.25, 1),"
				+ "('AMZN', 'Amazon.com, Inc.', 0.95, 1)" ;
		
		execute(query, conn);
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
		
		query = "INSERT INTO STOCKOPTIONS.PRICES (Symbol, Date, Price) "
				+ "VALUES('NFLX', '" + new java.sql.Date(d1.getTime()) + "', 95.28),"
				+ "('NFLX', '" + new java.sql.Date(d2.getTime()) + "', 98.25),"
				+ "('AMZN', '" + new java.sql.Date(d1.getTime()) + "', 805.75),"
				+ "('AMZN', '" + new java.sql.Date(d2.getTime()) + "', 804.70)";
		
		execute(query, conn);
		
		pMap = pImpl.getStockPriceHistory(sArray[0]);
		
		assertEquals(2, pMap.size());
		
		int i = 0;
		for(Price p : pMap.values()){;
			assertEquals("NFLX", p.getSymbol());
			assertEquals(priceValues[i], p.getValue(), 0);
			i++;
		}
		
		query = "DELETE FROM STOCKOPTIONS.PRICES";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
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
