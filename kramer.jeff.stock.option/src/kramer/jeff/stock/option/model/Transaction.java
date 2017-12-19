package kramer.jeff.stock.option.model;

import java.util.Date;

public class Transaction {
	private Stock stock;
	private String transactionType;
	private Date transactionDate;
	private int shares;
	private double price;
	private double total;
	private double commission;
	private double net;
	private int transactionID;
	private double strikePrice;
	private Date expirationDate;
	
	/**
	 * Constructor for Transaction information coming out of the database
	 * Transaction ID IS present in the constructor
	 * 
	 * @param stock
	 * @param tID
	 * @param tDate
	 * @param action
	 * @param price
	 * @param net
	 * @param commission
	 */
	public Transaction(Stock stock, int tID, Date tDate, String tType, double price, double net, double commission) {
		this.stock = stock;
		this.transactionDate = tDate;
		this.transactionType = tType;
		this.price = price;
		this.net = net;
		this.transactionID = tID;
		this.commission = commission;
	}
	
	/**
	 * Constructor for Transaction information going into the database
	 * Transaction ID IS NOT present in the constructor
	 * 
	 * @param stock
	 * @param tDate
	 * @param tType
	 * @param price
	 * @param net
	 * @param commission
	 */
	public Transaction(Stock stock, Date tDate, String tType, double price, double net, double commission) {
		this.stock = stock;
		this.transactionDate = tDate;
		this.transactionType = tType;
		this.price = price;
		this.net = net;
		this.commission = commission;
	}

	public Stock getStock(){
		return stock;
	}
	
	public void setStock(Stock s){
		this.stock = s;
	}
	
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(Date transDate) {
		this.transactionDate = transDate;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String tType) {
		this.transactionType = tType;
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
}
