package fina;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ClientTDAO {
	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc드라이버의 경로 저장
	Connection conn; // DB와 연결할 변수 선언
	PreparedStatement pstmt; // 쿼리를 날리기전 미리 컴파일 하여 날리는 변수 선언
	ResultSet rs; // DB의 결과값을 저장할 변수 선언
	String jdbcUrl = "jdbc:mysql://localhost/chicken"; // DB의 경로를 저장할 변수 선언
	String sql; // DB의 명령문을 저장할 변수 선언
	ArrayList<ClientTableBtn> tables = null;

	
	public ClientTDAO() {
		ClientAppManager.getInstance().setTableDao(this);
	}
	
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
