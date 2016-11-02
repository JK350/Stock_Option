package kramer.jeff.stock.option;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jkramer.model.Price;

import junit.framework.*;

import java.util.Calendar;
import java.util.Date;

public class PriceTest extends TestCase{

	protected String symbol;
	protected double value;
	protected int priceID;
	protected Date date;
	protected Price p;
	
	protected void setUp(){
		symbol = "TSLA";
		value = 200.42;
		priceID = 1;
		
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 9, 15, 0, 0, 0);
		date = cal.getTime();
		
		p = new Price(priceID, symbol, date, value);
	}
	
	@Test
	public void testPrice() {
		assertNotNull(p);
	}

	@Test
	public void testGetSymbol() {
		assertEquals("TSLA", p.getSymbol());
	}
	
	@Test
	public void testSetSymbol(){
		String s1 = "AMZN";
		p.setSymbol(s1);
		assertEquals(s1, p.getSymbol());
	}

	@Test
	public void testGetDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 9, 15, 0, 0, 0);
		Date d1 = cal.getTime();
		
		assertEquals(d1, p.getDate());
		
		
	}
	
	@Test
	public void testSetDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 9, 15, 0, 0, 0);
		Date d2 = cal.getTime();
		p.setDate(d2);
		
		assertEquals(d2, p.getDate());
	}

	@Test
	public void testGetValue() {
		assertEquals(200.42, p.getValue(), 0);
	}
	
	@Test
	public void testSetValue(){
		double v2 = 769.69;
		p.setPrice(v2);
		
		assertEquals(v2, p.getValue(), 0);
	}

	@Test
	public void testGetPriceID() {
		assertEquals(1, p.getPriceID());
	}
}
