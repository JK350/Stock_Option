package kramer.jeff.stock.option;

import java.util.Date;

public class Price {
	public String symbol;
	public Date date;
	public double value;
	public int priceID;
	
	/**
	 * @param priceID
	 * @param symbol
	 * @param date
	 * @param price
	 */
	public Price(int pID, String s, Date d, double v) {
		this.priceID = pID;
		this.symbol = s;
		this.date = d;
		this.value = v;
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
	
	public double getValue() {
		return value;
	}
	
	public void setPrice(double v) {
		this.value = v;
	}
	
	public int getPriceID(){
		return priceID;
	}
}
