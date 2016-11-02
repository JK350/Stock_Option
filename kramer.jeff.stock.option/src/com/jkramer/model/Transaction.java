package com.jkramer.model;

import java.util.Date;

public class Transaction {
	private Date transDate;
	private String transType;
	private double price;
	private double net;
	private double commission;
	private int transactionID;
	private String stock;
	private Account account;
	
	/**
	 * Constructor for Transaction information coming out of the database
	 * 
	 * @param tID
	 * @param tDate
	 * @param action
	 * @param price
	 * @param net
	 * @param commission
	 */
	public Transaction(String s, Account a, int tID, Date tDate, String tType, double price, double net, double commission) {
		this.stock = s;
		this.account = a;
		this.transDate = tDate;
		this.transType = tType;
		this.price = price;
		this.net = net;
		this.transactionID = tID;
		this.commission = commission;
	}
	
	/**
	 * Constructor for Transaction information going into the database
	 * 
	 * @param tID
	 * @param tDate
	 * @param action
	 * @param price
	 * @param net
	 * @param commission
	 */
	public Transaction(String s, Account a, Date tDate, String tType, double price, double net, double commission) {
		this.stock = s;
		this.account = a;
		this.transDate = tDate;
		this.transType = tType;
		this.price = price;
		this.net = net;
		this.commission = commission;
	}

	public String getStock(){
		return stock;
	}
	
	public void setStock(String s){
		this.stock = s;
	}
	
	public Date getTransactionDate() {
		return transDate;
	}
	
	public void setTransactionDate(Date transDate) {
		this.transDate = transDate;
	}
	
	public String getTransactionType() {
		return transType;
	}
	
	public void setTransactionType(String tType) {
		this.transType = tType;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getNet() {
		return net;
	}
	
	public void setNet(double net) {
		this.net = net;
	}
	
	public int getTransactionID(){
		return transactionID;
	}
	
	public void setTransactionID(int tID){
		this.transactionID = tID;
	}

	/**
	 * @return the commission
	 */
	public double getCommission() {
		return commission;
	}

	/**
	 * @param commission the commission to set
	 */
	public void setCommission(double commission) {
		this.commission = commission;
	}

	/**
	 * @return the transDate
	 */
	public Date getTransDate() {
		return transDate;
	}

	/**
	 * @param transDate the transDate to set
	 */
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	/**
	 * @return the transType
	 */
	public String getTransType() {
		return transType;
	}

	/**
	 * @param transType the transType to set
	 */
	public void setTransType(String transType) {
		this.transType = transType;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
}
