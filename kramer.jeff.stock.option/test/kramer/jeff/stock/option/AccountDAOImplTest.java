package kramer.jeff.stock.option;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.jkramer.dao.AccountDAOImpl;
import com.jkramer.model.Account;

public class AccountDAOImplTest {

	@Test
	public void testInsertAccount() {
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
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date d2 = cal.getTime();
		
		int[] accTypeID = new int[2];
		String[] accType = new String[2];
		
		accType[0] = "IRA";
		accType[1] = "Joint";
		
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNT_TYPE (Account_Type) "
				+ "VALUES (?)";
		
		int j = 0;
		for(String type : accType){
			try{
				pstmt = conn.prepareStatement(query, new String[]{"ACCOUNT_TYPE_ID"});
				pstmt.setString(1, type);
				pstmt.executeUpdate();
				
				rs = pstmt.getGeneratedKeys();
				
				if(rs.next()){
					accTypeID[j] = rs.getInt(1);
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
			j++;
		}
		
		Account[] aArray = new Account[2];
		
		aArray[0] = new Account("acc1", "Jane Doe", 1, d1, "Cheesy Poofs", Constants.ACCOUNT_TYPE_IRA);
		aArray[1] = new Account("acc2", "John Doe", 1, d2, "Snacky-smores", Constants.ACCOUNT_TYPE_JOINT);
		
		for(Account a : aArray){
			assertTrue(aImpl.insertAccount(a));
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
				assertEquals(aArray[i].getAccountID(), rs.getInt("Account_ID"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
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
		}
		
		Account a = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", Constants.ACCOUNT_TYPE_IRA);
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active, Account_Type)"
				+ "VALUES (?, ?, ?, 1, ?)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
			pstmt.setString(1, "acc1");
			pstmt.setString(2, "Snacky Smores");
			pstmt.setDate(3, new java.sql.Date(d1.getTime()));
			pstmt.setInt(4, accTypeID);
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
		
		assertTrue(aImpl.updateAccount(a));
		
		query = "SELECT * FROM STOCKOPTIONS.ACCOUNTS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				assertEquals(a.getNumber(), rs.getString("Number"));
				assertEquals(a.getNickname(), rs.getString("Nickname"));
				assertEquals(a.getAccountType(), rs.getString("Account_Type"));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
	}

	@Test
	public void testDeactivateAccount() {
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
		}
		
		Account a = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", Constants.ACCOUNT_TYPE_IRA);
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active, Account_Type)"
				+ "VALUES (?, ?, ?, 1, ?)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
			pstmt.setString(1, "acc1");
			pstmt.setString(2, "Snacky Smores");
			pstmt.setDate(3, new java.sql.Date(d1.getTime()));
			pstmt.setInt(4, accTypeID);
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				a.setAccountID(rs.getInt(1));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		assertTrue(aImpl.deactivateAccount(a));
		
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
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
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
		
		int[] accTypeID = new int[2];
		String[] accType = new String[2];
		
		accType[0] = "IRA";
		accType[1] = "Joint";
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNT_TYPE (Account_Type) "
				+ "VALUES (?)";
		
		int j = 0;
		for(String type : accType){
			try{
				pstmt = conn.prepareStatement(query, new String[]{"ACCOUNT_TYPE_ID"});
				pstmt.setString(1, type);
				pstmt.executeUpdate();
				
				rs = pstmt.getGeneratedKeys();
				
				if(rs.next()){
					accTypeID[j] = rs.getInt(1);
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
			j++;
		}
		
		Account[] accounts = new Account[2];
		
		accounts[0] = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", Constants.ACCOUNT_TYPE_JOINT);
		accounts[1] = new Account("acc2", "John Doe", 1, d1, "Cheesy Poofs", Constants.ACCOUNT_TYPE_IRA);
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active, Account_Type)"
				+ "VALUES (?, ?, ?, 1, ?)";
		
		for(Account a : accounts){
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
			}
		}
		
		assertTrue(aImpl.deleteAccount(accounts[1]));
		
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
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
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
		}
		
		Account[] accounts = new Account[2];
		
		accounts[0] = new Account("acc1", "John Doe", 1, d1, "Snacky Smores", Constants.ACCOUNT_TYPE_IRA);
		accounts[1] = new Account("acc2", "John Doe", 1, d1, "Cheesy Poofs", Constants.ACCOUNT_TYPE_IRA);
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active, Account_Type)"
				+ "VALUES (?, ?, ?, 1, ?)";
		
		for(Account a : accounts){
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
			}
		}
		
		rs = aImpl.getAllAccounts();
		
		int i = 0;
		try{
			while(rs.next()){
				Calendar calDB = Calendar.getInstance();
				calDB.setTime(rs.getDate("Date_Opened"));
				
				Calendar calAcc = Calendar.getInstance();
				calAcc.setTime(accounts[i].getDateOpened());
				
				assertEquals(accounts[i].getAccountID(), rs.getInt("Account_ID"));
				assertEquals(calAcc.YEAR, calAcc.YEAR);
				assertEquals(calAcc.MONTH, calAcc.MONTH);
				assertEquals(calAcc.DATE, calAcc.DATE);
				assertEquals(accounts[i].getNickname(), rs.getString("Nickname"));
				assertEquals(accounts[i].getStatus(), rs.getInt("Active"));
				assertEquals(accounts[i].getNumber(), rs.getString("Number"));
				assertEquals(accounts[i].getAccountType(), rs.getString("Account_Type"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		assertEquals(2, i);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNTS";
		execute(query, conn);
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
	}
	
	@Test
	public void testGetAccountTypes(){
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		AccountDAOImpl aImpl = new AccountDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		
		query = "INSERT INTO STOCKOPTIONS.ACCOUNT_TYPE (Account_Type) "
				+ "VALUES ('Joint'),"
				+ "('Personal'),"
				+ "('IRA')";
		
		execute(query, conn);
		
		String[] typeArray = new String[3];
		typeArray[0] = "IRA";
		typeArray[1] = "Joint";
		typeArray[2] = "Personal";
		
		rs = aImpl.getAccountTypes();
		
		try{
			int i = 0;
			while(rs.next()){
				assertEquals(typeArray[i], rs.getString(1));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		query = "DELETE FROM STOCKOPTIONS.ACCOUNT_TYPE";
		execute(query, conn);
	}
	
	@Test
	public void testInsertAccountType(){
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		AccountDAOImpl aImpl = new AccountDAOImpl();
		Connection conn = dbi.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		
		assertTrue(aImpl.insertAccountType("IRA"));
		
		query = "SELECT Account_Type FROM STOCKOPTIONS.ACCOUNT_TYPE";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				assertEquals("IRA", rs.getString(1));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
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
