package kramer.jeff.stock.option;

public interface PriceDAO {
	public void insertPrice(Price price);
	
	public void updatePrice(Price price);
	
	public void getPriceHistory(Stock stock);
}
