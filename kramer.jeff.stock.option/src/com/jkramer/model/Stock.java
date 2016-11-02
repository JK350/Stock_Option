package com.jkramer.model;

import java.util.LinkedHashMap;

public class Stock {
	public String symbol;
	public String companyName;
	public double annualDivRate;
	public String accountNumber;
	public boolean active;
	public LinkedHashMap<Integer, Transaction> transactionHistory;
	public LinkedHashMap<Integer, Price> priceHistory;
	
	//Spring Constructor
	public Stock(){
		
	}
	
	public Stock(String s, String cn, double adr, int act){
		this(s, cn, adr, act, "");
	}
	
	public Stock(String s, String cn, double adr, int act, String acc){
		this.symbol = s;
		this.companyName = cn;
		this.annualDivRate = adr;
		this.accountNumber = acc;
		this.active = (act == 1) ? true : false;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public void setSymbol(String s){
		this.symbol = s;
	}
	
	public String getCompanyName(){
		return companyName;
	}
	
	public void setCompanyName(String cn){
		this.companyName = cn;
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
	
	public String getAccountNumber(){
		return accountNumber;
	}
	
	public void setAccountNumber(String acc){
		this.accountNumber = acc;
	}
	
	public void setTransactionHistory(LinkedHashMap<Integer, Transaction> t){
		this.transactionHistory = t;
	}
	
	public LinkedHashMap<Integer, Transaction> getTransactionHistory(){
		return transactionHistory;
	}
	
	public void setPriceHistory(LinkedHashMap<Integer, Price> p){
		this.priceHistory = p;
	}
	
	public LinkedHashMap<Integer, Price> getPriceHistory(){
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
