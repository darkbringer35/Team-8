package fina;
import java.sql.*;
import java.util.*;

public class ClientDAOManager {
	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc����̹��� ��� ����
	Connection conn; // DB�� ������ ���� ����
	PreparedStatement pstmt; // ������ �������� �̸� ������ �Ͽ� ������ ���� ����
	ResultSet rs; // DB�� ������� ������ ���� ����
	String jdbcUrl = "jdbc:mysql://localhost/chicken"; // DB�� ��θ� ������ ���� ����
	String sql; // DB�� ��ɹ��� ������ ���� ����
	Vector<String> items = null;
	
	public ClientDAOManager() {
		ClientAppManager.getInstance().setDAOManger(this);
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

	public int getPrice(int index) { // ���̺��� ������ �ε����� ������ ��Ÿ���ش�
		int result = 0;
		sql = "select price from chicken" + index + " order by day desc limit 1"; 

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
			result = rs.getInt("price");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�
		return result;
	}
	
	
	public Vector<String> getComboIndex() {		//�޺��ڽ��� �ε�����
		
		items = new Vector<String>(); 
		sql = "select cname from menulist"; 
		try {
			connectDB(); // DB�� �����Ѵ�.
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) { // ���� ������� ������ ������ ���� �ݺ��� ����
				items.add(String.valueOf(rs.getString("cname"))); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�
		return items;
	}
}
