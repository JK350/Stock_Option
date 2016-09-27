package kramer.jeff.stock.option;

import java.util.LinkedHashMap;

public interface TransactionDAO {
	public void insertTransaction(Transaction t);
	
	public void updateTransaction(Transaction t);
	
	public void deleteTransaction(Transaction t, Stock s);
	
	public LinkedHashMap<Integer, Transaction> getStockTransactionHistory(Stock stock);
}
