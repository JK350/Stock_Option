package kramer.jeff.stock.option;

import java.sql.ResultSet;

public interface AccountDAO {
	public boolean insertAccount(Account a);
	
	public boolean updateAccount(Account a);
	
	public boolean deactivateAccount(Account a);
	
	public boolean deleteAccount(Account a);
	
	public ResultSet getAllAccounts();
	
	public ResultSet getAccountTypes();
	
	public boolean insertAccountType(String s);
}
