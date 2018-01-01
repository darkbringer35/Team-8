package wan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;


public class DAOManager {


	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc����̹��� ��� ����
	Connection conn; // DB�� ������ ���� ����
	PreparedStatement pstmt; // ������ �������� �̸� ������ �Ͽ� ������ ���� ����
	ResultSet rs; // DB�� ������� ������ ���� ����
	String jdbcUrl = "jdbc:mysql://localhost/chicken"; // DB�� ��θ� ������ ���� ����
	String sql; // DB�� ��ɹ��� ������ ���� ����
	int lastIndex = 0; // �޴�����Ʈ ���̺��� �����ִ� ������ �ε��� ����
	Vector<String> items = null;
	
	public DAOManager() {
		AppManager.getInstance().setDAOManger(this);
	}
	
	public void connectDB() { // DB�� ������ �Լ� ����
		try {

			Class.forName(jdbcDriver); // jdbc����̹� �ε�
			conn = DriverManager.getConnection(jdbcUrl, "javabook", "0305"); // DB���, �����id,p/w�� ���Ͽ� �����ϴ� ���� ����
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
		
			sql = "create table chicken" + (lastIndex+1) + "(day varchar(30) not null primary key, sales int(30), stock int(30), price int(30));";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		closeDB(); // DB������ �����Ѵ�
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean delMenu(int index) {

		int result = 0;
		int key = getKey();

		try {
			connectDB();
			if (index != key) {
				for (int i = index; i < key; i++) {
					sql = "select cname from menulist where mainkey = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, i + 1);
					rs = pstmt.executeQuery();
					rs.next();
					String cName = rs.getString("cname"); // ������ �ε���+1 ���� �������ε������� -1ĭ�� ������ ��ܿ´�

					sql = "update menulist set cname = ? where mainkey = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, cName);
					pstmt.setInt(2, i);
					pstmt.executeUpdate();

				}
			}

			sql = "drop table chicken" + index;
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();

			if (index != key) {
				for (int i = index; i < key; i++) {

					sql = "alter table chicken" + (i + 1) + " rename chicken" + i;
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
				}
			}
			
			sql = "delete from menulist where mainkey = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, key);
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		closeDB();
		if (result > 0) {// ������� 0���� ũ�� �� ������ �Ǿ��ٴ� �ǹ��� ���ǹ�
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<DayList> getTotalTable() {			
		connectDB();
		ArrayList<DayList> days = new ArrayList<DayList>();
		sql = "select * from totallist";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DayList d = new DayList();
				d.setDay(rs.getString("day"));
				d.setSales(rs.getInt("totalsales"));
				d.setStock(rs.getInt("totalprice"));
				days.add(d);
			}
		} catch (Exception e) {
		}
		closeDB(); // DB������ �����Ѵ�
		return days;
	}

	public DayList getOneTotalTable(String day) {			
		connectDB();
		sql = "select * from totallist where day = ?";
		DayList d = new DayList();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, day);
			
			rs = pstmt.executeQuery();			
			d.setDay(rs.getString("day"));
			d.setSales(rs.getInt("totalsales"));
			d.setPrice(rs.getInt("totalprice"));
	
		} catch (Exception e) {
		}
		closeDB(); // DB������ �����Ѵ�
		return d;
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
		closeDB(); // DB������ �����Ѵ�
		return d;
	}


	
	public ArrayList<DayList> getChickenTable(int index) {			
		connectDB();
		ArrayList<DayList> days = new ArrayList<DayList>();
		sql = "select * from chicken" + index;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
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
		closeDB(); // DB������ �����Ѵ�
		return days;
	}

	public ArrayList<DayList> getStockTable() {

		ArrayList<DayList> days = new ArrayList<DayList>();
		
		int key = getKey();
		int cSales;
		int cStock;
		int cPrice;
		String cName;
		try {
			//sql = "select * from chicken" + index;
			connectDB();
			for (int i = 1; i <= key; i++) {
				DayList d = new DayList();
				
				
				sql = "select sales from chicken"+i+" order by day desc limit 1";				
				pstmt = conn.prepareStatement(sql);				
				rs=pstmt.executeQuery();
				if(rs.next()==true) {
				cSales = rs.getInt("sales");
				}
				else { cSales = 0; }
				
				sql = "select stock from chicken"+i+" order by day desc limit 1";
				pstmt = conn.prepareStatement(sql);
				rs=pstmt.executeQuery();
				if(rs.next()==true) {
				cStock = rs.getInt("stock");
				}
				else { cStock = 0; }
				
				sql = "select price from chicken"+i+" order by day desc limit 1";
				pstmt = conn.prepareStatement(sql);
				rs=pstmt.executeQuery();
				if(rs.next()==true) {
				cPrice = rs.getInt("price");
				}
				else { cPrice = 0; }
							
				sql = "select cname from menulist where id = ?";	
				pstmt = conn.prepareStatement(sql);
				String chicken = "chicken" + i;
				pstmt.setString(1, chicken);
				rs = pstmt.executeQuery();
				rs.next();
				cName = rs.getString("cname");
								
				d.setSales(cSales);
				d.setStock(cStock);
				d.setPrice(cPrice);
				d.setName(cName);	
				
				days.add(d);
				
				
				
			}
		} catch (Exception e) {
		}
		closeDB(); // DB������ �����Ѵ�
		return days;
	}
	

	
	public boolean insertList(int index, String day, int sales, int stock, int price) { //ġŲ���̺� �� �������̺� �ε����� �߰� �� ������Ʈ�ϴ� �Լ� 
		int result = 0;
		connectDB();
		try {
			sql = "select sales,stock,price from chicken" + index + " where day=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, day);
			rs = pstmt.executeQuery();
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
			////////////////////////////////////////////////////////////////////////////////////////////////ġŲ���̺� ����߰�
			
			sql = "select totalsales,totalprice from totallist where day = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, day);
			rs = pstmt.executeQuery();
			flag = rs.next();			
			if(flag==false) {
				sql = "insert into totallist(day, totalsales, totalprice) values(?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, day);
				pstmt.setInt(2, sales);
				pstmt.setInt(3, sales*price);
				result = pstmt.executeUpdate();
			}
			else if(flag == true) {
				sql = "update totallist set totalsales = ?, totalprice= ? where day =? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,rs.getInt("totalsales")+sales);
				pstmt.setInt(2,rs.getInt("totalprice")+sales*price);
				pstmt.setString(3,day);
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

	public boolean chickenUpdate(int index, String name, int addedStock, int price) {
		int result = 0;
		int stock=0;
		String day="";
		connectDB();
		try {
			sql = "select stock,day from chicken"+index+" order by stock desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();			
			stock = rs.getInt("stock");		
			day = rs.getString("day");
			
			//if(rs.next()==false) {
			
			sql = "update chicken" + index + " set stock = ?, price = ? where day = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,stock + addedStock);
			pstmt.setInt(2, price);	
			pstmt.setString(3, day);			
			pstmt.executeUpdate();
			
			sql = "update menulist set cname=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,name);
			pstmt.setString(2, "chicken"+index);
			result = pstmt.executeUpdate();

			//}
		} catch (Exception e) {

		}
		closeDB(); // DB������ �����Ѵ�
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
		closeDB(); // DB������ �����Ѵ�
		return result;
	}

	public int getSales(int index) { // ���̺��� ������ �ε����� �Ǹŷ��� ��Ÿ���ش�
		int result = 0;
		sql = "select sales from chicken" + index + " order by day desc limit 1"; 

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("sales");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�
		return result;
	}

	public int getStock(int index) { // ���̺��� ������ �ε����� �Ǹŷ��� ��Ÿ���ش�
		int result = 0;
		sql = "select stock from chicken" + index + " order by day desc limit 1";

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("stock");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�
		return result;
	}

	public int getPrice(int index) { // ���̺��� ������ �ε����� ������ ��Ÿ���ش�
		int result = 0;
		sql = "select price from chicken" + index + " order by day desc limit 1"; 

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("price");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�
		return result;
	}
	
	public String getCName(int index) { // ���̺��� ������ �ε����� �Ǹŷ��� ��Ÿ���ش�
		String result ="" ;
		sql = "select cname from menulist where id = ?"; 

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			String chicken = "chicken"+index;
			pstmt.setString(1, chicken);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getString("cname");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�
		return result;
	}
	
	public String getId(int index) { // ���̺��� ������ �ε����� �Ǹŷ��� ��Ÿ���ش�
		String result ="" ;
		sql = "select id from menulist where mainkey = ?"; 

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getString("id");
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
	
	public Vector<String> getItems(){return items;}
	
	
	public void updatePay(ArrayList<ReceiptItem> receipt,String day) {
		//ġŲ �����ͺ��̽��� ���°� receipt.get(1).getItemIndex();
		//���� �Ǹŷ� ��ȭ receipt.get(0).getItemAmount();
		try {
			for(ReceiptItem ri : receipt) {
				insertList(ri.getItemIndex(), day, ri.getItemAmount(), 100, getPrice(ri.getItemIndex()));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getTotalDay(String str) {
		sql = "select day from totallist where day = ?";
		String day="";
		try {
			connectDB(); // DB�� �����Ѵ�.

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, str);
			rs = pstmt.executeQuery();
			rs.next();
			day = rs.getString("day");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�

				
				
		return day;
	}
	public int getOneTotalPrice(String str) {
		sql = "select totalprice from totallist where day = ?";
		int price=0;
		try {
			connectDB(); // DB�� �����Ѵ�.

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, str);
			rs = pstmt.executeQuery();
			rs.next();
			price = rs.getInt("totalprice");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�		
		return price;
	}
	
}
