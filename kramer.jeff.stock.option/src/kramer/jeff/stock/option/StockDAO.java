package kramer.jeff.stock.option;

public interface StockDAO {
	public void insertStock(Stock stock);
	
	public void updateStock(Stock stock);
	
	public void deleteStock(Stock stock);
	
	public void getAllStocks();
	
	public void getStock(Stock stock);
}
