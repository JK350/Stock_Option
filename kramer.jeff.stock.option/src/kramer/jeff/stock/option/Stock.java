package kramer.jeff.stock.option;

import java.util.HashMap;

public class Stock {
	public String symbol;
	public String compName;
	public double annualDivRate;
	public String account;
	public boolean active;
	public HashMap<Integer, Transaction> transactionHistory;
	public HashMap<Integer, Price> priceHistory;
	
	public Stock(String s, String cn, double adr, int act){
		this(s, cn, adr, act, "");
	}
	
	public Stock(String s, String cn, double adr, int act, String acc){
		this.symbol = s;
		this.compName = cn;
		this.annualDivRate = adr;
		this.account = acc;
		this.active = (act == 1) ? true : false;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public void setSybmol(String s){
		this.symbol = s;
	}
	
	public String getCompanyName(){
		return compName;
	}
	
	public void setCompanyName(String cn){
		this.compName = cn;
	}
	
	public double getAnnualDivRate(){
		return annualDivRate;
	}
	
	public void setAnnualDivRate(Double adr){
		this.annualDivRate = adr;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void setActive(boolean act){
		this.active = act;
	}
	
	public String getAccount(){
		return account;
	}
	
	public void setAccount(String acc){
		this.account = acc;
	}
	
	public void setTransactionHistory(HashMap<Integer, Transaction> t){
		this.transactionHistory = t;
	}
	
	public HashMap<Integer, Transaction> getTransactionHistory(){
		return transactionHistory;
	}
	
	public void setPriceHistory(HashMap<Integer, Price> p){
		this.priceHistory = p;
	}
	
	public HashMap<Integer, Price> getPriceHistory(){
		return priceHistory;
	}
	
	public void dropPrice(int priceID){
		if(!priceHistory.isEmpty()){
			priceHistory.remove(priceID);
		}
	}
	
	public void dropTransaction(int transactionID){
		if(!transactionHistory.isEmpty()){
			transactionHistory.remove(transactionID);
		}
	}
}
