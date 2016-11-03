package com.jkramer.model;

import java.util.LinkedHashMap;

public class Stock {
	private int stockID;
	private String symbol;
	private String companyName;
	private double annualDivRate;
	private Account account;
	private boolean active;
	private LinkedHashMap<Integer, Transaction> transactionHistory;
	
	//Spring Constructor
	public Stock(){
		
	}
	
	public Stock(String s, String cn, double adr, boolean act){
		this(s, cn, adr, act, null);
	}

	public Stock(String s, String cn, double adr, boolean act, Account acc){
		this.symbol = s;
		this.companyName = cn;
		this.annualDivRate = adr;
		this.account = acc;
		this.active = act;
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
	
	public void setAnnualDivRate(double adr){
		this.annualDivRate = adr;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void setActive(boolean act){
		this.active = act;
	}
	
	public Account getAccount(){
		return account;
	}
	
	public void setAccount(Account acc){
		this.account = acc;
	}
	
	public void setTransactionHistory(LinkedHashMap<Integer, Transaction> t){
		this.transactionHistory = t;
	}
	
	public LinkedHashMap<Integer, Transaction> getTransactionHistory(){
		return transactionHistory;
	}
		
	/**
	 * @return the stockID
	 */
	public int getStockID() {
		return stockID;
	}

	/**
	 * @param stockID the stockID to set
	 */
	public void setStockID(int stockID) {
		this.stockID = stockID;
	}

	public void dropTransaction(int transactionID){
		if(!transactionHistory.isEmpty()){
			transactionHistory.remove(transactionID);
		}
	}
}
