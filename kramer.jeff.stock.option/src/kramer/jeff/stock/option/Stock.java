package kramer.jeff.stock.option;

public class Stock {
	public String symbol;
	public String compName;
	public double annualDivRate;
	public String account;
	
	Stock(String s, String cn, Double adr){
		this(s, cn, adr, "");
	}
	
	Stock(String s, String cn, Double adr, String acc){
		this.symbol = s;
		this.compName = cn;
		this.annualDivRate = adr;
		this.account = acc;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public void setSybmol(String s){
		this.symbol = s;
	}
	
	public String getCompanyName(){
		return compName;
	}
	
	public void setCompanyName(String cn){
		this.compName = cn;
	}
	
	public Double getAnnualDivRate(){
		return annualDivRate;
	}
	
	public void setAnnualDivRate(Double adr){
		this.annualDivRate = adr;
	}
	
	public String getAccount(){
		return account;
	}
	
	public void setAccount(String acc){
		this.account = acc;
	}
}
