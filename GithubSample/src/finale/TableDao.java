package finale;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;



public class TableDao {
	private static TableDao tableDao;
	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc드라이버의 경로 저장
	Connection conn; // DB와 연결할 변수 선언
	PreparedStatement pstmt; // 쿼리를 날리기전 미리 컴파일 하여 날리는 변수 선언
	ResultSet rs; // DB의 결과값을 저장할 변수 선언
	String jdbcUrl = "jdbc:mysql://localhost/chicken"; // DB의 경로를 저장할 변수 선언
	String sql; // DB의 명령문을 저장할 변수 선언
	int lastIndex = 0; // 메뉴리스트 테이블이 갖고있는 마지막 인덱스 변수
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
	
	public void connectDB() { // DB를 연결할 함수 선언
		try {

			Class.forName(jdbcDriver); // jdbc드라이버 로딩
			conn = DriverManager.getConnection(jdbcUrl, "root", "0305"); // DB경로, 사용자id,p/w를 통하여 연결하는 변수 생성
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// Connect DB

	public void closeDB() { // DB 연결을 해제할 함수 선언
		try {
			pstmt.close();
			//rs.close();
			conn.close(); // 연결을 종료한다
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
			connectDB(); // DB에 연걸
			
			sql = "delete from tablelist where tindex = ?";
			
			for(int i=0; i <=key; i++ ) {
				
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,i);
				pstmt.executeUpdate();			
				
			}
			
			sql = "insert into tablelist(tindex,getx,gety) values(?,?,?)"; // int,String,String값의 테이블에 삽입하는 쿼리문

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
		} // 결과값이 0보다 크면 잘 전송이 되었다는 의미의 조건문
		else {
			return false;
		}
	}
	
	public int getKey() { // 메뉴리스트의 마지막 인덱스를 얻는 메소드
		int result = 0;
		sql = "select tindex from tablelist order by tindex desc limit 1";

		try {
			connectDB(); // DB에 연걸
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("tindex");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB연결을 해제한다
		return result;
	}
	
	
}
