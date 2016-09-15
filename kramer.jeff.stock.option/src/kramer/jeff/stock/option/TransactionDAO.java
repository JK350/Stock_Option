package kramer.jeff.stock.option;

public interface TransactionDAO {
	public void insertTransaction(Transaction t);
	
	public void updateTransaction(Transaction t);
	
	public void deleteTransaction(Transaction t, Stock s);
	
	public void getStockTransactionHistory(Stock stock);
}
