package kramer.jeff.stock.option;

import java.util.Date;

public class Transaction {
	public Date transDate;
	public int action;
	public double price;
	public double net;
	public int transactionID;
	public String stock;
	
	/**
	 * @param tID
	 * @param tDate
	 * @param action
	 * @param price
	 * @param net
	 */
	public Transaction(String s, int tID, Date tDate, int action, double price, double net) {
		this.stock = s;
		this.transDate = tDate;
		this.action = action;
		this.price = price;
		this.net = net;
		this.transactionID = tID;
	}

	public String getStock(){
		return stock;
	}
	
	public void setStock(String s){
		this.stock = s;
	}
	
	public Date getTransDate() {
		return transDate;
	}
	
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	
	public int getAction() {
		return action;
	}
	
	public void setAction(int action) {
		this.action = action;
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
}
