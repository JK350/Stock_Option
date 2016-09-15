package kramer.jeff.stock.option;

import java.util.Date;

public class Transaction {
	public Date transDate;
	public String action;
	public double price;
	public double net;
	public int transactionID;
	
	/**
	 * @param tID
	 * @param tDate
	 * @param action
	 * @param price
	 * @param net
	 */
	public Transaction(int tID, Date tDate, String action, double price, double net) {
		this.transDate = tDate;
		this.action = action;
		this.price = price;
		this.net = net;
		this.transactionID = tID;
	}

	public Date getTransDate() {
		return transDate;
	}
	
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
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
