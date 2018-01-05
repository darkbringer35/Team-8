package fina;


public class ClientMain {

	public static void main(String[] args) {
		ClientDAOManager cDao = new ClientDAOManager();
		ClientTDAO tDAO = new ClientTDAO();
		ClientDialog dia = new ClientDialog();
		ChickenClient client = new ChickenClient();
	}
}
