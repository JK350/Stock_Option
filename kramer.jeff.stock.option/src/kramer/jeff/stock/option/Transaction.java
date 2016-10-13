package kramer.jeff.stock.option;

import java.util.Date;

public class Transaction {
	public Date transDate;
	public String transType;
	public double price;
	public double net;
	public double commission;
	public int transactionID;
	public String stock;
	
	/**
	 * @param tID
	 * @param tDate
	 * @param action
	 * @param price
	 * @param net
	 * @param commission
	 */
	public Transaction(String s, int tID, Date tDate, String tType, double price, double net, double commission) {
		this.stock = s;
		this.transDate = tDate;
		this.transType = tType;
		this.price = price;
		this.net = net;
		this.transactionID = tID;
		this.commission = commission;
	}
	
	/**
	 * @param tID
	 * @param tDate
	 * @param action
	 * @param price
	 * @param net
	 * @param commission
	 */
	public Transaction(String s, Date tDate, String tType, double price, double net, double commission) {
		this.stock = s;
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
}
