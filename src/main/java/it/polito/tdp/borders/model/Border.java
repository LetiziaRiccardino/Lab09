package it.polito.tdp.borders.model;

public class Border {
	
	private Country c1;
	private Country c2;
//	private String StateAbb1;
//	private String StateAbb2;
	private int year;
//	private int conttype;
	public Border(Country c1, Country c2, int year) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		this.year = year;
	}
	public Country getC1() {
		return c1;
	}
	public void setC1(Country c1) {
		this.c1 = c1;
	}
	public Country getC2() {
		return c2;
	}
	public void setC2(Country c2) {
		this.c2 = c2;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "Border [c1=" + c1.getCcode() + ", c2=" + c2.getCcode() + ", year=" + year + "]";
	}
	
	
	

}
