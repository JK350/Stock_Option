package kramer.jeff.stock.option;

import java.sql.ResultSet;

public interface TransactionDAO {
	public void insertTransaction(Transaction t);
	
	public void updateTransaction(Transaction t);
	
	public void deleteTransaction(Transaction t, Stock s);
	
	public ResultSet getStockTransactionHistory(Stock stock);
	
	public ResultSet getAccountTransactionHistory(Account account);
}
