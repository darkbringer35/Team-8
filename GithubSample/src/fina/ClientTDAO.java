package fina;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ClientTDAO {
	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc����̹��� ��� ����
	Connection conn; // DB�� ������ ���� ����
	PreparedStatement pstmt; // ������ �������� �̸� ������ �Ͽ� ������ ���� ����
	ResultSet rs; // DB�� ������� ������ ���� ����
	String jdbcUrl = "jdbc:mysql://localhost/chicken"; // DB�� ��θ� ������ ���� ����
	String sql; // DB�� ��ɹ��� ������ ���� ����
	ArrayList<ClientTableBtn> tables = null;

	
	public ClientTDAO() {
		ClientAppManager.getInstance().setTableDao(this);
	}
	
	public void connectDB() { // DB�� ������ �Լ� ����
		try {

			Class.forName(jdbcDriver); // jdbc����̹� �ε�
			conn = DriverManager.getConnection(jdbcUrl, "root", "0305"); // DB���, �����id,p/w�� ���Ͽ� �����ϴ� ���� ����
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// Connect DB

	public void closeDB() { // DB ������ ������ �Լ� ����
		try {
			pstmt.close();
			//rs.close();
			conn.close(); // ������ �����Ѵ�
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// closeDB
	
	
	public ArrayList<ClientTableBtn> getTableList(){
		connectDB();
		sql = "select * from tablelist";
		
		tables=new ArrayList<ClientTableBtn>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("tindex");
				ClientTableBtn t = new ClientTableBtn(id);
				t.setLocation(rs.getInt("getx"),rs.getInt("gety"));
				tables.add(t);	
			}
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tables;
	}
	
	
}
