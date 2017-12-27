package jewan;

public class AppManager {
	private static AppManager s_instance;
	private ChickenMain cMain;
	private ChickenDao cDao;
	private ChickenDialog cDia;
	
	private AppManager() {
		
	}
	public static AppManager getInstance() {
		if(s_instance == null) s_instance = new AppManager();
		return s_instance;
	}
	
	public ChickenMain getChickenMain() {
		return cMain;
	}
	public void setChickenMain(ChickenMain main){
		cMain=main;
	}
	public ChickenDao getChickenDao() {
		return cDao;
	}
	public void setChickenDao(ChickenDao dao) {
		cDao = dao;
	}
	public ChickenDialog getChickenDialog() {
		return cDia;
	}
	public void setChickenDialog(ChickenDialog dia) {
		cDia=dia;
	}
}
