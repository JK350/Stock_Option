package kramer.jeff.stock.option;

import java.util.Date;

public class Transaction {
	public Date transDate;
	public String action;
	public double price;
	public double net;
	
	/**
	 * @param transDate
	 * @param action
	 * @param price
	 * @param net
	 */
	public Transaction(Date transDate, String action, double price, double net) {
		this.transDate = transDate;
		this.action = action;
		this.price = price;
		this.net = net;
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
}
