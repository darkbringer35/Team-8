package finale;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;



public class TableDao {
	private static TableDao tableDao;
	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc����̹��� ��� ����
	Connection conn; // DB�� ������ ���� ����
	PreparedStatement pstmt; // ������ �������� �̸� ������ �Ͽ� ������ ���� ����
	ResultSet rs; // DB�� ������� ������ ���� ����
	String jdbcUrl = "jdbc:mysql://localhost/chicken"; // DB�� ��θ� ������ ���� ����
	String sql; // DB�� ��ɹ��� ������ ���� ����
	int lastIndex = 0; // �޴�����Ʈ ���̺��� �����ִ� ������ �ε��� ����
	Vector<String> items = null;
	Vector<TableBtn> tables = null;

	
	public TableDao() {
		AppManager.getInstance().setTableDao(this);
		tables= new Vector<TableBtn>();
	}

	public static TableDao getInstance() {
		if (tableDao == null)
			tableDao = new TableDao();
		return tableDao;
	}

	public TableDao getTableDao() {return tableDao;}
	public void setTableDao(TableDao dao) {this.tableDao = dao;}
	
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
	
	
	public Vector<TableBtn> getTableList(){
		connectDB();
		sql = "select * from tablelist";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("tindex");
				TableBtn t = new TableBtn(id);
				System.out.println(t.getX()+" "+ t.getY());
				
				t.setLocation(rs.getInt("getx"),rs.getInt("gety"));
				tables.add(t);	
			}
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	public boolean insertTable(Vector<TableBtn> table) {
		int result=0;	
		int key = getKey();
			
		try {
			connectDB(); // DB�� ����
			
			sql = "delete from tablelist where tindex = ?";
			
			for(int i=0; i <=key; i++ ) {
				
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,i);
				pstmt.executeUpdate();			
				
			}
			
			sql = "insert into tablelist(tindex,getx,gety) values(?,?,?)"; // int,String,String���� ���̺� �����ϴ� ������

			for(TableBtn tb : table) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, tb.getIndex());
				pstmt.setInt(2, tb.getX());
				pstmt.setInt(3, tb.getY()); //
				result = pstmt.executeUpdate();				
			}
				
			
		} catch (Exception e) {
			e.printStackTrace();
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
		sql = "select tindex from tablelist order by tindex desc limit 1";

		try {
			connectDB(); // DB�� ����
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("tindex");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB������ �����Ѵ�
		return result;
	}
	
	
}
