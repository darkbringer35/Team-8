package jewan;



public class ChickenDao {
	
	String jdbc = "";
	
	
	public ChickenDao() {
		AppManager.getInstance().setChickenDao(this); 
	}
	
	
}
