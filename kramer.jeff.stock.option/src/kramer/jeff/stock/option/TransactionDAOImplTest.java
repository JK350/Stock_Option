package kramer.jeff.stock.option;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.junit.Test;

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
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
			
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1)";
		
		execute(query, conn);
		
		Transaction[] tArray = new Transaction[2]; 
		tArray[0] = new Transaction("NFLX", d1, 1, 95.68, 5.62);
		tArray[1] = new Transaction("NFLX", d1, 2, 95.68, -85.25);
		
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
				calStock.setTime(tArray[i].getTransDate());
				
				assertEquals(tArray[i].getStock(), rs.getString("Symbol"));
				assertEquals(calStock.YEAR, calDB.YEAR);
				assertEquals(calStock.MONTH, calDB.MONTH);
				assertEquals(calStock.DATE, calDB.DATE);
				assertEquals(tArray[i].getAction(), rs.getInt("Action"));
				assertEquals(tArray[i].getPrice(), rs.getDouble("Price"), 0);
				assertEquals(tArray[i].getNet(), rs.getDouble("Net"), 0);
				assertEquals(tArray[i].getTransactionID(), rs.getInt("Transaction_ID"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.TRANSACTIONS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
	}

	@Test
	public void testUpdateTransaction() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		TransactionDAOImpl tImpl = new TransactionDAOImpl();
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
		
		query = "INSERT INTO STOCKOPTIONS.TRANSACTIONS (Symbol, Date, Action, Price, Net) "
				+ "VALUES('NFLX', '" + new java.sql.Date(d1.getTime()) + "', 1, 86.23, 987.25)";
		execute(query, conn);
		
		query = "SELECT Transaction_ID FROM STOCKOPTIONS.TRANSACTIONS";
		
		int transID = -1;
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				transID = rs.getInt("Transaction_ID");
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		Transaction t = new Transaction("NFLX", transID, d1, 2, 94.68, 58.23);
		
		tImpl.updateTransaction(t);
		
		query = "SELECT * FROM STOCKOPTIONS.TRANSACTIONS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				assertEquals(t.getAction(), rs.getInt("Action"));
				assertEquals(t.getPrice(), rs.getDouble("Price"), 0);
				assertEquals(t.getNet(), rs.getDouble("Net"), 0);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.TRANSACTIONS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
	}

	@Test
	public void testDeleteTransaction() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		TransactionDAOImpl tImpl = new TransactionDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		LinkedHashMap<Integer, Transaction> tMap = new LinkedHashMap<Integer, Transaction>();
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
			
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1)";
		execute(query, conn);
		
		Stock s = new Stock("NFLX", "Netflix, Inc.", 0.25, 1);
		
		query = "INSERT INTO STOCKOPTIONS.TRANSACTIONS (Symbol, Date, Action, Price, Net) "
				+ "VALUES('NFLX', '" + new java.sql.Date(d1.getTime()) + "', 1, 86.23, 987.25),"
				+ "('NFLX', '" + new java.sql.Date(d2.getTime()) + "', 1, 58.32, 872.65)";
		execute(query, conn);
		
		Transaction[] tArray = new Transaction[2];
		tArray[0] = new Transaction("NFLX", d1, 1, 86.23, 987.25);
		tArray[1] = new Transaction("NFLX", d2, 1, 58.32, 872.65);
		
		query = "SELECT * FROM STOCKOPTIONS.TRANSACTIONS ORDER BY Date DESC";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				tArray[i].setTransactionID(rs.getInt("Transaction_ID"));
				tMap.put(tArray[i].getTransactionID(), tArray[i]);
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
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
				calTrans.setTime(tArray[i].getTransDate());
				
				assertEquals(calTrans.YEAR, calDB.YEAR);
				assertEquals(calTrans.MONTH, calDB.MONTH);
				assertEquals(calTrans.DATE, calDB.DATE);
				assertEquals(tArray[0].getPrice(), rs.getDouble("Price"), 0);
				assertEquals(tArray[0].getNet(), rs.getDouble("Net"), 0);
				
				i++;
			}
			
			assertEquals(1, i);
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.TRANSACTIONS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.STOCK";
		execute(query, conn);
	}

	@Test
	public void testGetStockTransactionHistory() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		TransactionDAOImpl tImpl = new TransactionDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		LinkedHashMap<Integer, Transaction> tMap = new LinkedHashMap<Integer, Transaction>();
		String query;
		double[] priceArray = new double[2];
		double[] netArray = new double[2];
		
		priceArray[0] = 86.23;
		priceArray[1] = 58.32;
		
		netArray[0] = 987.25;
		netArray[1] = 872.65;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
		
		query = "INSERT INTO STOCKOPTIONS.STOCK VALUES('NFLX', 'Netflix, Inc.', 0.25, 1), "
				+ "('AMZN', 'Amazon.com, Inc.', 0.65, 1)";
		execute(query, conn);
		
		query = "INSERT INTO STOCKOPTIONS.TRANSACTIONS (Symbol, Date, Action, Price, Net) "
				+ "VALUES('NFLX', '" + new java.sql.Date(d1.getTime()) + "', 1, 86.23, 987.25),"
				+ "('NFLX', '" + new java.sql.Date(d2.getTime()) + "', 1, 58.32, 872.65),"
				+ "('AMZN', '" + new java.sql.Date(d1.getTime()) + "', 1, 56.75, -156.23),"
				+ "('AMZN', '" + new java.sql.Date(d2.getTime()) + "', 1, 12.02, 35.26)";
		execute(query, conn);
		
		Stock[] sArray = new Stock[2];
		sArray[0] = new Stock("NFLX", "Netflix, Inc.", 0.25, 1);
		sArray[1] = new Stock("AMZN", "Amazon.com, Inc.", 0.65, 1);
		
		tMap = tImpl.getStockTransactionHistory(sArray[0]);
		
		assertEquals(2, tMap.size());
		
		int i = 0;
		for(Transaction t : tMap.values()){
			assertEquals("NFLX", t.getStock());
			assertEquals(1, t.getAction());
			assertEquals(priceArray[i], t.getPrice(), 0);
			assertEquals(netArray[i], t.getNet(), 0);
			
			i++;
		}
		
		query = "DELETE FROM STOCKOPTIONS.TRANSACTIONS";
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
