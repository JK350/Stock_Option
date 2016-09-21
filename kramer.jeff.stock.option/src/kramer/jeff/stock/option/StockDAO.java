package kramer.jeff.stock.option;

import java.util.LinkedHashMap;

public interface StockDAO {
	public void insertStock(Stock stock);
	
	public void updateStock(Stock stock);
	
	public void deleteStock(Stock stock);
	
	public LinkedHashMap<String, Stock> getAllStocks();
	
	public void deactivateStock(Stock stock);
}
