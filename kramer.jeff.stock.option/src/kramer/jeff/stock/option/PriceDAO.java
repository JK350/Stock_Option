package kramer.jeff.stock.option;

public interface PriceDAO {
	public void insertPrice(Price price);
	
	public void updatePrice(Price price);
	
	public void deletePrice(int priceID, Stock stock);
	
	public void getStockPriceHistory(Stock stock);
	
	public void getFullPriceHistory();
}
