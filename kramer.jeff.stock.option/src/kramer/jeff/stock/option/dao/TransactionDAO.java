package kramer.jeff.stock.option.dao;

import java.sql.ResultSet;

import kramer.jeff.stock.option.model.Account;
import kramer.jeff.stock.option.model.Stock;
import kramer.jeff.stock.option.model.Transaction;

public interface TransactionDAO {
	public void insertTransaction(Transaction t);
	
	public void updateTransaction(Transaction t);
	
	public void deleteTransaction(Transaction t, Stock s);
	
	public ResultSet getStockTransactionHistory(Stock stock);
	
	public ResultSet getAccountTransactionHistory(Account account);
}
