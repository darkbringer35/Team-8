package changgyu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAOManager {

	private static DAOManager daoManager;

	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc����̹��� ��� ����
	Connection conn; // DB�� ������ ���� ����
	PreparedStatement pstmt; // ������ �������� �̸� ������ �Ͽ� ������ ���� ����
	ResultSet rs; // DB�� ������� ������ ���� ����
	String jdbcUrl = "jdbc:mysql://localhost/example"; // DB�� ��θ� ������ ���� ����
	String sql; // DB�� ��ɹ��� ������ ���� ����
	int lastIndex = 0; // �޴�����Ʈ ���̺��� �����ִ� ������ �ε��� ����

	public DAOManager() {

	}

	public static DAOManager getInstance() {
		if (daoManager == null)
			daoManager = new DAOManager();
		return daoManager;
	}

	public void connectDB() { // DB�� ������ �Լ� ����
		try {

			Class.forName(jdbcDriver); // jdbc����̹� �ε�
			conn = DriverManager.getConnection(jdbcUrl, "root", "ghktks12"); // DB���, �����id,p/w�� ���Ͽ� �����ϴ� ���� ����
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// Connect DB

	public void closeDB() { // DB ������ ������ �Լ� ����
		try {
			pstmt.close();
			rs.close();
			conn.close(); // ������ �����Ѵ�
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// closeDB

	public boolean addMenu(String menuname) {// �޺��ڽ��� �ε�������, �޴��� �߰��� �̸�, �޴��� ���̵��� �Ű��������Ѵ�
		int result = 0;
		lastIndex = getKey();
		String chicken = "chicken";
		sql = "insert into menulist(mainkey,cname,id) values(?,?,?)"; // int,String,String���� ���̺� �����ϴ� ������

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lastIndex + 1);
			pstmt.setString(2, menuname);
			pstmt.setString(3, chicken + (lastIndex + 1)); //
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean delMenu(int selected) {
		sql = "delete from menulist where mainkey=?";
		int result = 0;
		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, selected);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ ���´�
		if (result > 0) {
			return true;
		} // ������� 0���� ũ�� �� ������ �Ǿ��ٴ� �ǹ��� ���ǹ�
		else {
			return false;
		}
	}// delProduct

	public boolean addMenuTable() {
		int result = 0;
		lastIndex = getKey();
		sql = "create table chicken" + lastIndex + "(day varchar(30) not null primary key, sales int(30), stock int(30), price int(30));";
		try {
			connectDB();
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		closeDB(); // DB������ ���´�
		if (result > 0) {
			return true;
		} // ������� 0���� ũ�� �� ������ �Ǿ��ٴ� �ǹ��� ���ǹ�
		else {
			return false;
		}
	}

	public DayList getOneDay(int index, String day) {
		connectDB();
		// ArrayList<DayList> days= new ArrayList<DayList>();
		sql = "select * from chicken" + index + "where day = ?";
		DayList d = new DayList();

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();
			while (rs.next()) {
				d.setDay(rs.getString("day"));
				d.setSales(rs.getInt("sales"));
				d.setStock(rs.getInt("stock"));
				d.setPrice(rs.getInt("price"));
			}
		} catch (Exception e) {
		}
		closeDB();
		return d;
	}

	public ArrayList<DayList> getAll(int index) {
		connectDB();
		ArrayList<DayList> days = new ArrayList<DayList>();
		sql = "select * from chicken" + index + "where day = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();
			while (rs.next()) {
				DayList d = new DayList();
				d.setDay(rs.getString("day"));
				d.setSales(rs.getInt("sales"));
				d.setStock(rs.getInt("stock"));
				d.setPrice(rs.getInt("price"));
				days.add(d);
			}
		} catch (Exception e) {
		}
		closeDB();
		return days;
	}

	public boolean insertList(int index, String day, int sales, int stock, int price) { // insert�޼ҵ�� ���update�޼ҵ� ���� ����
		int result = 0;
		connectDB();
		try {
			sql = "select sales,stock,price from chicken" + index + " where day=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, day);
			rs = pstmt.executeQuery();
			//System.out.println("rs >> "+rs.next());
			boolean flag = rs.next();

			if (flag == false) {
				sql = "insert into chicken" + index + "(day,sales,stock,price) values(?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, day);
				pstmt.setInt(2, sales);
				pstmt.setInt(3, stock);
				pstmt.setInt(4, price);
				result = pstmt.executeUpdate();
			}

			else if (flag == true) {				
				sql = "update chicken" + index + " set sales = ?, stock = ?, price = ? where day = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, rs.getInt("sales") + sales);
				pstmt.setInt(2, rs.getInt("stock") - sales);
				pstmt.setInt(3, rs.getInt("price"));
				pstmt.setString(4, day);
				result = pstmt.executeUpdate();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		closeDB(); // DB������ ���´�

		if (result > 0) {
			return true;
		} // ������� 0���� ũ�� �� ������ �Ǿ��ٴ� �ǹ��� ���ǹ�
		else {
			return false;
		}
	}

	public boolean stockUpdate(int index, int addedStock, int price) {
		int result = 0;
		int stock=0;
		String day="";
		connectDB();
		try {
			sql = "select stock from chicken"+index+"order by stock desc limit 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();
			rs.next();
			stock = rs.getInt("stock");
			day = rs.getString("day");
			
			sql = "update chicken" + index + " set stock = ? price = ? where day = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,addedStock);
			pstmt.setString(2, day);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {

		}
		if (result > 0) {
			return true;
		} // ������� 0���� ũ�� �� ������ �Ǿ��ٴ� �ǹ��� ���ǹ�
		else {
			return false;
		}
	}

	public int getKey() { // �޴�����Ʈ�� ������ �ε����� ��� �޼ҵ�
		int result = 0;
		sql = "select mainkey from menulist order by mainkey desc limit 1";

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("mainkey");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getSales(int index) { // ���̺��� ������ �ε����� �Ǹŷ��� ��Ÿ���ش�
		int result = 0;
		sql = "select sales from chicken" + index + " desc limit 1"; // int,String,String���� ���̺� �����ϴ� ������

		try {
			connectDB(); // DB�� ����
			rs = pstmt.executeQuery(sql);
			result = rs.getInt("sales");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getStock(int index) { // ���̺��� ������ �ε����� �Ǹŷ��� ��Ÿ���ش�
		int result = 0;
		sql = "select stock from chicken" + index + " desc limit 1"; // int,String,String���� ���̺� �����ϴ� ������

		try {
			connectDB(); // DB�� ����
			rs = pstmt.executeQuery(sql);
			result = rs.getInt("sales");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getPrice(int index) { // ���̺��� ������ �ε����� �Ǹŷ��� ��Ÿ���ش�
		int result = 0;
		sql = "select price from chicken" + index + " desc limit 1"; // int,String,String���� ���̺� �����ϴ� ������

		try {
			connectDB(); // DB�� ����
			rs = pstmt.executeQuery(sql);
			result = rs.getInt("sales");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
