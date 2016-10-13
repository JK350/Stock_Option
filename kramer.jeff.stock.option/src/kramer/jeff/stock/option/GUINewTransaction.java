package kramer.jeff.stock.option;

import java.util.HashMap;
import java.util.TreeMap;

public interface GUINewTransaction {

	public void createWindow(HashMap<String, Stock> stockMap, TreeMap<String, Account> accountMap);
}
