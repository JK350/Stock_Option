package kramer.jeff.stock.option;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class AccountDAOImplTest {

	@Test
	public void testInsertAccount() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		AccountDAOImpl aImpl = new AccountDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
		
		Account[] aArray = new Account[2];
		
		aArray[0] = new Account("acc1", "Jane Doe", 1, d1);
		aArray[1] = new Account("acc2", "John Doe", 1, d2, "Snacky-smores");
		
		for(Account a : aArray){
			aImpl.insertAccount(a);
		}
		
		query = "SELECT * FROM STOCKOPTIONS.ACCOUNTS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				Calendar calDB = Calendar.getInstance();
				calDB.setTime(rs.getDate("DATE_OPENED"));
				
				Calendar calAcc = Calendar.getInstance();
				calAcc.setTime(aArray[i].getDateOpened());
				
				assertEquals(aArray[i].getAccountID(), rs.getInt("Account_ID"));
				assertEquals(calAcc.YEAR, calAcc.YEAR);
				assertEquals(calAcc.MONTH, calAcc.MONTH);
				assertEquals(calAcc.DATE, calAcc.DATE);
				assertEquals(aArray[i].getNickname(), rs.getString("Nickname"));
				assertEquals(aArray[i].getStatus(), rs.getInt("Active"));
				assertEquals(aArray[i].getNumber(), rs.getString("Number"));
				
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
	}

	@Test
	public void testUpdateAccount() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		AccountDAOImpl aImpl = new AccountDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		Account a = new Account("acc1", "John Doe", 1, d1, "Snacky Smores");
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active)"
				+ "VALUES (?, ?, ?, 1)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
			pstmt.setString(1, "acc1");
			pstmt.setString(2, "Snacky Smores");
			pstmt.setDate(3, new java.sql.Date(d1.getTime()));
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				a.setAccountID(rs.getInt(1));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		a.setNickname("Cheesy Poofs");
		a.setNumber("acc2");
		
		aImpl.updateAccount(a);
		
		query = "SELECT * FROM STOCKOPTIONS.ACCOUNTS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				assertEquals(a.getNumber(), rs.getString("Number"));
				assertEquals(a.getNickname(), rs.getString("Nickname"));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
	}

	@Test
	public void testDeactiveAccount() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		AccountDAOImpl aImpl = new AccountDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		Account a = new Account("acc1", "John Doe", 1, d1, "Snacky Smores");
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active)"
				+ "VALUES (?, ?, ?, 1)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
			pstmt.setString(1, "acc1");
			pstmt.setString(2, "Snacky Smores");
			pstmt.setDate(3, new java.sql.Date(d1.getTime()));
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				a.setAccountID(rs.getInt(1));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		aImpl.deactiveAccount(a);
		
		query = "SELECT * FROM STOCKOPTIONS.ACCOUNTS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				assertEquals(0, rs.getInt("Active"));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
	}

	@Test
	public void testDeleteAccount() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		AccountDAOImpl aImpl = new AccountDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		Account[] accounts = new Account[2];
		
		accounts[0] = new Account("acc1", "John Doe", 1, d1, "Snacky Smores");
		accounts[1] = new Account("acc2", "John Doe", 1, d1, "Cheesy Poofs");
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active)"
				+ "VALUES (?, ?, ?, 1)";
		
		for(Account a : accounts){
			try{
				pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
				pstmt.setString(1, a.getNumber());
				pstmt.setString(2, a.getNickname());
				pstmt.setDate(3, new java.sql.Date(a.getDateOpened().getTime()));
				pstmt.executeUpdate();
				
				rs = pstmt.getGeneratedKeys();
				if(rs.next()){
					a.setAccountID(rs.getInt(1));
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		aImpl.deleteAccount(accounts[1]);
		
		query = "SELECT * FROM STOCKOPTIONS.ACCOUNTS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				assertEquals(accounts[i].getAccountID(), rs.getInt("Account_ID"));
				i++;
			}
			
			assertEquals(1, i);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
	}

	@Test
	public void testGetAllAccounts() {
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		AccountDAOImpl aImpl = new AccountDAOImpl();
		Connection conn = dbi.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query;
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		
		Account[] accounts = new Account[2];
		
		accounts[0] = new Account("acc1", "John Doe", 1, d1, "Snacky Smores");
		accounts[1] = new Account("acc2", "John Doe", 1, d1, "Cheesy Poofs");
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active)"
				+ "VALUES (?, ?, ?, 1)";
		
		for(Account a : accounts){
			try{
				pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
				pstmt.setString(1, a.getNumber());
				pstmt.setString(2, a.getNickname());
				pstmt.setDate(3, new java.sql.Date(a.getDateOpened().getTime()));
				pstmt.executeUpdate();
				
				rs = pstmt.getGeneratedKeys();
				if(rs.next()){
					a.setAccountID(rs.getInt(1));
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		rs = aImpl.getAllAccounts();
		
		int i = 0;
		try{
			while(rs.next()){
				Calendar calDB = Calendar.getInstance();
				calDB.setTime(rs.getDate("DATE_OPENED"));
				
				Calendar calAcc = Calendar.getInstance();
				calAcc.setTime(accounts[i].getDateOpened());
				
				assertEquals(accounts[i].getAccountID(), rs.getInt("Account_ID"));
				assertEquals(calAcc.YEAR, calAcc.YEAR);
				assertEquals(calAcc.MONTH, calAcc.MONTH);
				assertEquals(calAcc.DATE, calAcc.DATE);
				assertEquals(accounts[i].getNickname(), rs.getString("Nickname"));
				assertEquals(accounts[i].getStatus(), rs.getInt("Active"));
				assertEquals(accounts[i].getNumber(), rs.getString("Number"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		assertEquals(2, i);
		
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
