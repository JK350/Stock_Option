package kramer.jeff.stock.option;

import java.util.LinkedHashMap;

public interface PriceDAO {
	public void insertPrice(Price price);
	
	public void updatePrice(Price price);
	
	public void deletePrice(Price price, Stock stock);
	
	public LinkedHashMap<Integer, Price> getStockPriceHistory(Stock stock);
}
