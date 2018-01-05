package fina;
import java.util.*;

public class ClientAppManager {
	private static ClientAppManager s_instance;
	private ArrayList<ClientTableBtn> table;
	private int selectedTableIndex;
	private int setTime;
	private ClientTDAO ctDAO;
	private ChickenClient cClient;
	private ClientDialog clientDia;
	private ClientDAOManager dao;
	
	private ClientAppManager() {
		selectedTableIndex=-1;
		setTime=10;
	}
	public static ClientAppManager getInstance() {
		if(s_instance == null) s_instance = new ClientAppManager();
		return s_instance;
	}
	
	public ClientTDAO getTableDao() {
		return ctDAO;
	}
	public void setTableDao(ClientTDAO tdao) {
		ctDAO=tdao;
	}
	public ChickenClient getClient() {
		return cClient;
	}
	public void setClient(ChickenClient client) {
		cClient=client;
	}
	public ArrayList<ClientTableBtn> getTableArray(){
		return table;
	}
	public void setTableArray(ArrayList<ClientTableBtn> t) {
		this.table=t;
	}
	public int getTid() {
		return selectedTableIndex;
	}
	public void setTid(int index) {
		selectedTableIndex=index;
	}
	public int getTimerSet(){
		return setTime;
	}
	public void setTimerSet(int t) {
		setTime=t;
	}
	public ClientDialog getChickenDialog() {
		return clientDia;
	}
	public void setChickenDialog(ClientDialog dia) {
		clientDia=dia;
	}
	public ClientDAOManager getDAOManger() {
		return dao;
	}
	public void setDAOManger(ClientDAOManager dao) {
		this.dao=dao;
	}
	
}
