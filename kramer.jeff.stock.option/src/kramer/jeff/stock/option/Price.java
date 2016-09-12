package kramer.jeff.stock.option;

import java.util.Date;

public class Price {
	public String symbol;
	public Date date;
	public double price;
	
	/**
	 * @param symbol
	 * @param date
	 * @param price
	 */
	public Price(String symbol, Date date, double price) {
		this.symbol = symbol;
		this.date = date;
		this.price = price;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
}
