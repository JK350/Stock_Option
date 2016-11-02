package com.jkramer.dao;

import java.util.LinkedHashMap;

import com.jkramer.model.Price;
import com.jkramer.model.Stock;

public interface PriceDAO {
	public void insertPrice(Price price);
	
	public void updatePrice(Price price);
	
	public void deletePrice(Price price, Stock stock);
	
	public LinkedHashMap<Integer, Price> getStockPriceHistory(Stock stock);
}
