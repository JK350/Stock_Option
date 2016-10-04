package kramer.jeff.stock.option;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

public class AccountDAOImpl implements AccountDAO {

	/**
	 * Method for inserting a new account into the database
	 * 
	 * @author J Kramer
	 * @param a - the account to add to the db 
	 */
	@Override
	public final void insertAccount(Account a) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		Date date = a.getDateOpened();
		String number = a.getNumber();
		//String owner = a.getOwner();  Not used at this time.
		String nickname = a.getNickname();
		ResultSet rs = null;
		
		String query = "INSERT INTO " + Constants.SCHEMA + ".ACCOUNTS (Number, Nickname, Date_Opened, Active)"
				+ "VALUES(?, ?, ?, 1)";
		
		try{
			pstmt = conn.prepareStatement(query, new String[] {"ACCOUNT_ID"});
			pstmt.setString(1, number);
			pstmt.setString(2, nickname);
			pstmt.setDate(3, new java.sql.Date(date.getTime()));
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				a.setAccountID(rs.getInt(1));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally{
			closeStatement(pstmt);
		}
	}

	/**
	 * Method for updating an existing account in the database
	 * 
	 * @author J Kramer
	 * @param a - the account to be updated
	 */
	@Override
	public final void updateAccount(Account a) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		Date date = a.getDateOpened();
		String number = a.getNumber();
		String nickname = a.getNickname();
		int id = a.getAccountID();
		
		String query = "UPDATE " + Constants.SCHEMA + ".ACCOUNTS "
				+ "SET Number = ?, Nickname = ?, Date_Opened = ? "
				+ "WHERE Account_ID = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, number);
			pstmt.setString(2, nickname);
			pstmt.setDate(3, new java.sql.Date(date.getTime()));
			pstmt.setInt(4, id);
			pstmt.executeUpdate();
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}

	/**
	 * Method for deactivating accounts in the database
	 * 
	 * @author J Kramer
	 * @param a - account to be deactivated
	 */
	@Override
	public final void deactiveAccount(Account a) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		int id = a.getAccountID();
		
		String query = "UPDATE " + Constants.SCHEMA + ".ACCOUNTS "
				+ "SET Active = 0 "
				+ "WHERE Account_ID = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.execute();
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}

	/**
	 * Method for deleting account in the database
	 * 
	 * @author J Kramer
	 * @param a - account to be deleted
	 */
	@Override
	public final void deleteAccount(Account a) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		int id = a.getAccountID();
		
		String query = "DELETE FROM " + Constants.SCHEMA + ".ACCOUNTS "
				+ "WHERE Account_ID = ?";
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.execute();
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}

	/**
	 * Method to get all of the accounts in the database
	 * 
	 * @author J Kramer
	 * @return result set with all accounts in it
	 */
	@Override
	public final ResultSet getAllAccounts() {
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM " + Constants.SCHEMA + ".ACCOUNTS";
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return rs;
	}

	/**
	 * Method used to close a statement.
	 * 
	 * @author J Kramer
	 * @param stmt - statment to close
	 */
	private void closeStatement(Statement stmt){
		try{
			if(stmt != null){
				stmt.close();
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Method returns an active connection 
	 * 
	 * @author J Kramer
	 * @return Connection object
	 */
	private Connection getConnection(){
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(Constants.DB_URL);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return conn;
	}
}
