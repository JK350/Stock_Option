package kramer.jeff.stock.option;

import java.sql.ResultSet;

public interface StockDAO {
	public boolean insertStock(Stock stock, int accountID);
	
	public boolean updateStock(Stock stock);
	
	public boolean deleteStock(Stock stock);
	
	public ResultSet getAllStocks();
	
	public ResultSet getActiveStocks();
	
	public boolean deactivateStock(Stock stock);
}
