package kramer.jeff.stock.option;

import java.sql.ResultSet;

public interface AccountDAO {
	public void insertAccount(Account a);
	
	public void updateAccount(Account a);
	
	public void deactiveAccount(Account a);
	
	public void deleteAccount(Account a);
	
	public ResultSet getAllAccounts();
}
