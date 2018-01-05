package fina;
import java.sql.*;
import java.util.*;

public class ClientDAOManager {
	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc드라이버의 경로 저장
	Connection conn; // DB와 연결할 변수 선언
	PreparedStatement pstmt; // 쿼리를 날리기전 미리 컴파일 하여 날리는 변수 선언
	ResultSet rs; // DB의 결과값을 저장할 변수 선언
	String jdbcUrl = "jdbc:mysql://localhost/chicken"; // DB의 경로를 저장할 변수 선언
	String sql; // DB의 명령문을 저장할 변수 선언
	Vector<String> items = null;
	
	public ClientDAOManager() {
		ClientAppManager.getInstance().setDAOManger(this);
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

	public int getPrice(int index) { // 테이블의 마지막 인덱스의 가격을 나타내준다
		int result = 0;
		sql = "select price from chicken" + index + " order by day desc limit 1"; 

		try {
			connectDB(); // DB에 연걸
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
			result = rs.getInt("price");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB연결을 해제한다
		return result;
	}
	
	
	public Vector<String> getComboIndex() {		//콤보박스의 인덱스를
		
		items = new Vector<String>(); 
		sql = "select cname from menulist"; 
		try {
			connectDB(); // DB에 연결한다.
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 받은 결과값의 다음이 없을때 까지 반복문 실행
				items.add(String.valueOf(rs.getString("cname"))); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB연결을 해제한다
		return items;
	}
}
