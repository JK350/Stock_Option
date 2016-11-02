package com.jkramer.dao;

import java.sql.ResultSet;

import com.jkramer.model.Account;
import com.jkramer.model.Stock;
import com.jkramer.model.Transaction;

public interface TransactionDAO {
	public void insertTransaction(Transaction t);
	
	public void updateTransaction(Transaction t);
	
	public void deleteTransaction(Transaction t, Stock s);
	
	public ResultSet getStockTransactionHistory(Stock stock);
	
	public ResultSet getAccountTransactionHistory(Account account);
}
