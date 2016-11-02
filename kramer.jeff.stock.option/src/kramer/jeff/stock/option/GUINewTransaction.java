package kramer.jeff.stock.option;

import java.util.HashMap;
import java.util.TreeMap;

import com.jkramer.model.Account;
import com.jkramer.model.Stock;

public interface GUINewTransaction {

	public void createWindow(HashMap<String, Stock> stockMap, TreeMap<String, Account> accountMap);
}
