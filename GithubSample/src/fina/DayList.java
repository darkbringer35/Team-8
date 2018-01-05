package fina;

public class DayList {
	
	String day;
	String name;
	int sales;
	int price;
	int stock;

	
	
	public DayList () {
		
	}
	
	public String getDay() {return day;}
	public String getName() {return name;}
	public int getSales() {return sales;}
	public int getPrice() {return price;}
	public int getStock() {return stock;}

	
	
	public void setDay(String day) {this.day=day;}
	public void setName(String name) {this.name=name;}
	public void setSales(int sales) {this.sales=sales;}
	public void setPrice(int price) {this.price=price;}
	public void setStock(int stock) {this.stock=stock;}

}
