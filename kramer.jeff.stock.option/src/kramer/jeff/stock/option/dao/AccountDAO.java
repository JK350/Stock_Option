package kramer.jeff.stock.option.dao;

import java.sql.ResultSet;

import kramer.jeff.stock.option.model.Account;

public interface AccountDAO {
	public boolean insertAccount(Account a);
	
	public boolean updateAccount(Account a);
	
	public boolean deactivateAccount(Account a);
	
	public boolean deleteAccount(Account a);
	
	public ResultSet getAllAccounts();
}
