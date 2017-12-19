package kramer.jeff.stock.option;

import static org.junit.Assert.*;
import org.junit.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Calendar;

import kramer.jeff.stock.option.dao.AccountDAOImpl;
import kramer.jeff.stock.option.model.Account;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountDAOImplTest {

	private DatabaseInitializer dbi = new DatabaseInitializer();
	private ApplicationContext context = null;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String query = "";
	private AccountDAOImpl adi = new AccountDAOImpl();
	private Account[] accountArray = new Account[2];
	
	@Before
	public void initializer(){
		System.out.println("Initializing Test Data ...");
		
		conn = dbi.getConnection();
		context = new ClassPathXmlApplicationContext("TestBeans.xml");
		
		accountArray[0] = (Account) context.getBean("accountOne");
		accountArray[1] = (Account) context.getBean("accountTwo");
	}
	
	@After
	public void close(){
		System.out.println("Closing Test ...");

		deleteData(conn);
		dbi.closeConnection();
	}
	
	
	@Test
	public void testInsertAccount() {
		System.out.println("Starting testInsertAccount() ...");
		for(Account a : accountArray){
			assertTrue(adi.insertAccount(a));
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
				calAcc.setTime(accountArray[i].getDateOpened());
				
				assertEquals(accountArray[i].getAccountID(), rs.getInt("Account_ID"));
				assertEquals(calAcc.YEAR, calAcc.YEAR);
				assertEquals(calAcc.MONTH, calAcc.MONTH);
				assertEquals(calAcc.DATE, calAcc.DATE);
				assertEquals(accountArray[i].getNickname(), rs.getString("Nickname"));
				assertEquals(accountArray[i].getStatus(), rs.getInt("Active"));
				assertEquals(accountArray[i].getNumber(), rs.getString("Number"));
				assertEquals(accountArray[i].getAccountID(), rs.getInt("Account_ID"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Test
	public void testUpdateAccount() {
		insertAccount(accountArray[0]);
		
		accountArray[0].setNickname("Cheesy Poofs");
		accountArray[0].setNumber("acc2");
		
		assertTrue(adi.updateAccount(accountArray[0]));
		
		query = "SELECT * FROM STOCKOPTIONS.ACCOUNTS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				assertEquals(accountArray[0].getNumber(), rs.getString("Number"));
				assertEquals(accountArray[0].getNickname(), rs.getString("Nickname"));
				assertEquals(accountArray[0].getAccountType(), rs.getString("Account_Type"));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Test
	public void testDeactivateAccount() {
		System.out.println("Starting testDeactivateAccount() ...");
		insertAccount(accountArray[0]);
		
		assertTrue(adi.deactivateAccount(accountArray[0]));
		
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
	}

	@Test
	public void testDeleteAccount() {		
		System.out.println("Starting testDeleteAccount()");
		insertAccount(accountArray[0]);
		insertAccount(accountArray[1]);
		
		assertTrue(adi.deleteAccount(accountArray[1]));
		
		query = "SELECT * FROM STOCKOPTIONS.ACCOUNTS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				assertEquals(accountArray[i].getAccountID(), rs.getInt("Account_ID"));
				i++;
			}
			
			assertEquals(1, i);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Test
	public void testGetAllAccounts() {
		System.out.println("Starting testGetAllAccount() ...");
		insertAccount(accountArray[0]);
		insertAccount(accountArray[1]);
		
		
		rs = adi.getAllAccounts();
		
		int i = 0;
		try{
			while(rs.next()){
				Calendar calDB = Calendar.getInstance();
				calDB.setTime(rs.getDate("Date_Opened"));
				
				Calendar calAcc = Calendar.getInstance();
				calAcc.setTime(accountArray[i].getDateOpened());
				
				assertEquals(accountArray[i].getAccountID(), rs.getInt("Account_ID"));
				assertEquals(calAcc.YEAR, calAcc.YEAR);
				assertEquals(calAcc.MONTH, calAcc.MONTH);
				assertEquals(calAcc.DATE, calAcc.DATE);
				assertEquals(accountArray[i].getNickname(), rs.getString("Nickname"));
				assertEquals(accountArray[i].getStatus(), rs.getInt("Active"));
				assertEquals(accountArray[i].getNumber(), rs.getString("Number"));
				assertEquals(accountArray[i].getAccountType(), rs.getString("Account_Type"));
				i++;
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		assertEquals(2, i);
	}
	
	private void insertAccount(Account a){
		query = "INSERT INTO STOCKOPTIONS.ACCOUNTS (Number, Nickname, Date_Opened, Active, Account_Type)"
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
		}
	}
	
	private void deleteData(Connection conn){		
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
