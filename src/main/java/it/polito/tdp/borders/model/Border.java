package it.polito.tdp.borders.model;

public class Border {
	
	private int ccode1;
	private int ccode2;
//	private String StateAbb1;
//	private String StateAbb2;
	private int year;
//	private int conttype;
	public Border(int ccode1, int ccode2, int year) {
		super();
		this.ccode1 = ccode1;
		this.ccode2 = ccode2;
		this.year = year;
	}
	public int getCcode1() {
		return ccode1;
	}
	public void setCcode1(int ccode1) {
		this.ccode1 = ccode1;
	}
	public int getCcode2() {
		return ccode2;
	}
	public void setCcode2(int ccode2) {
		this.ccode2 = ccode2;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "Border [ccode1=" + ccode1 + ", ccode2=" + ccode2 + ", year=" + year + "]";
	}
	
	

}
