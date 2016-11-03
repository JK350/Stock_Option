package com.jkramer.service;

import java.util.HashMap;
import java.util.TreeMap;

import com.jkramer.dao.StockDAOImpl;
import com.jkramer.model.Account;
import com.jkramer.model.Stock;

import java.sql.ResultSet;

public class StockService {
	
	public HashMap<String, Stock> getAllStockHashMap(TreeMap<String, Account> accountMap){
		HashMap<String, Stock> sMap = new HashMap<String, Stock>();
		StockDAOImpl stockDAOImpl = new StockDAOImpl();
		ResultSet rs = stockDAOImpl.getAllStocks();
		
		try{
			while(rs.next()){
				String symbol = rs.getString("SYMBOL");
				double annDivRate = rs.getDouble("ANNUAL_DIV_RATE");
				String compName = rs.getString("NAME");
				boolean active = rs.getBoolean("ACTIVE");
				
				Stock stock = new Stock(symbol, compName, annDivRate, active, accountMap.get(rs.getString("NUMBER")));
				
				sMap.put(symbol, stock);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return sMap;
	}
		
	public boolean deleteStock(Stock s){
		StockDAOImpl stockDAOImpl = new StockDAOImpl();
		boolean success = stockDAOImpl.deleteStock(s);
		
		return success;
	}
	
	public boolean insertStock(Stock s){
		StockDAOImpl stockDAOImpl = new StockDAOImpl();
		boolean success = stockDAOImpl.insertStock(s);
		
		return success;
	}
	
	public boolean updateStock(Stock s){
		StockDAOImpl stockDAOImpl = new StockDAOImpl();
		boolean success = stockDAOImpl.updateStock(s);
		
		return success;
	}
}
