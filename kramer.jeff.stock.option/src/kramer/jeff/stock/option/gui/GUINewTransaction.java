package kramer.jeff.stock.option.gui;

import java.util.HashMap;
import java.util.TreeMap;

import kramer.jeff.stock.option.model.Account;
import kramer.jeff.stock.option.model.Stock;

public interface GUINewTransaction {

	public void createWindow(HashMap<String, Stock> stockMap, TreeMap<String, Account> accountMap);
}
