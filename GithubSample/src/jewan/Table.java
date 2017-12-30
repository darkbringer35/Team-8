package jewan;

public class Table {
	int itemIndex;
	int itemAmount;
	
	public Table() {
		
	}
	public Table(int index, int amount) {
		itemIndex=index;
		itemAmount=amount;
	}
	public int getItemIndex(){
		return itemIndex;
	}
	public int getItemAmount() {
		return itemAmount;
	}
	public void setItemIndex(int itemIndex){
		this.itemIndex=itemIndex;
	}
	public void setItemAmount(int itemAmount) {
		this.itemAmount=itemAmount;
	}
}
