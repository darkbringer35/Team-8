package jewan;

import java.util.*;

public class AppManager {
	private static AppManager s_instance;
	private ChickenMain cMain;
	private ChickenDao cDao;
	private ChickenDialog cDia;
	private Vector<TableBtn> table;
	private int selectedTableIndex;
	private int frameMode;
	private int setTime;
	private boolean mouseOut;
	private DAOManager dao;
	
	private AppManager() {
		frameMode=0;
		selectedTableIndex=-1;
		setTime=10;
		mouseOut=false;
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
	public Vector<TableBtn> getTableArray(){
		return table;
	}
	public void setTableArray(Vector<TableBtn> t) {
		table = t;
	}
	public int getTid() {
		return selectedTableIndex;
	}
	public void setTid(int index) {
		selectedTableIndex=index;
	}
	public int getFrameMode() {
		return frameMode;
	}
	public void setFrameMode(int mode) {
		frameMode=mode;
	}
	public int getTimerSet(){
		return setTime;
	}
	public void setTimerSet(int t) {
		setTime=t;
	}
	public boolean getMouseOut() {
		return mouseOut;
	}
	public void setMouseOut(boolean b) {
		mouseOut=b;
	}
	public DAOManager getDAOManger()	{
		return dao;
	}
	public void setDAOManger(DAOManager dao) {
		this.dao = dao;
	}
}
