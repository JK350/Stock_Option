package kramer.jeff.stock.option.dao;

import java.util.LinkedHashMap;

import kramer.jeff.stock.option.model.Price;
import kramer.jeff.stock.option.model.Stock;

public interface PriceDAO {
	public void insertPrice(Price price);
	
	public void updatePrice(Price price);
	
	public void deletePrice(Price price, Stock stock);
	
	public LinkedHashMap<Integer, Price> getStockPriceHistory(Stock stock);
}
