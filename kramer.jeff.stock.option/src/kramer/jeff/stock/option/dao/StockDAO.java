package kramer.jeff.stock.option.dao;

import java.sql.ResultSet;

import kramer.jeff.stock.option.model.Stock;

public interface StockDAO {
	public boolean insertStock(Stock stock);
	
	public boolean updateStock(Stock stock);
	
	public boolean deleteStock(Stock stock);
	
	public ResultSet getAllStocks();
	
	public ResultSet getActiveStocks();
	
	public boolean deactivateStock(Stock stock);
}
