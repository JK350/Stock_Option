package kramer.jeff.stock.option;

import java.util.TreeMap;
import java.util.Date;
import java.sql.ResultSet;

public class AccountService {
	
	public boolean insertAccount(Account a){
		AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
		return accountDAOImpl.insertAccount(a);
	}
	
	public boolean updateAccount(Account a){
		AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
		return accountDAOImpl.updateAccount(a);
	}
	
	public boolean deleteAccount(Account a){
		AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
		return accountDAOImpl.deleteAccount(a);
	}
	
	public boolean deactivateAccount(Account a){
		AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
		return accountDAOImpl.deactivateAccount(a);
	}
	
	/**
	 * Method puts all accounts into a TreeMap with the account number as a key, Account object as the value
	 * 
	 * @return TreeMap<String, Account> - Key is account number
	 */
	public TreeMap<String, Account> getAllAccounts(){
		ResultSet rs = null;
		TreeMap<String, Account> aMap = new TreeMap<String, Account>();
		AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
		
		rs = accountDAOImpl.getAllAccounts();
		
		try{
			while(rs.next()){
				int id = rs.getInt(1);
				String number = rs.getString(2);
				String name = rs.getString(3);
				Date dateOpened = rs.getDate(4);
				int active = rs.getInt(5);
				String accountType = rs.getString(6);
				Account a = new Account(id, number, "", active, dateOpened, name, accountType);
				aMap.put(number, a);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				rs.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return aMap;
	}
	
	public TreeMap<String, Integer> getAccountTypes(){
		TreeMap<String, Integer> accountTypes = new TreeMap<String, Integer>();
		AccountDAOImpl accountDAO = new AccountDAOImpl();
		ResultSet rs = accountDAO.getAccountTypes();
		
		try{
			while(rs.next()){
				accountTypes.put(rs.getString(1), rs.getInt(2));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{
				rs.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return accountTypes;
	}
}
