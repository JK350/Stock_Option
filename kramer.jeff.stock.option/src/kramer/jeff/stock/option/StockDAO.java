package kramer.jeff.stock.option;

import java.util.HashMap;

public interface StockDAO {
	public void insertStock(Stock stock);
	
	public void updateStock(Stock stock);
	
	public void deleteStock(Stock stock);
	
	public HashMap<String, Stock> getAllStocks();
}
