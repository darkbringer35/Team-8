package changgyu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;


public class DAOManager {

	private static DAOManager daoManager;

	String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc드라이버의 경로 저장
	Connection conn; // DB와 연결할 변수 선언
	PreparedStatement pstmt; // 쿼리를 날리기전 미리 컴파일 하여 날리는 변수 선언
	ResultSet rs; // DB의 결과값을 저장할 변수 선언
	String jdbcUrl = "jdbc:mysql://localhost/example"; // DB의 경로를 저장할 변수 선언
	String sql; // DB의 명령문을 저장할 변수 선언
	int lastIndex = 0; // 메뉴리스트 테이블이 갖고있는 마지막 인덱스 변수
	Vector<String> items = null;
	
	public DAOManager() {

	}

	public static DAOManager getInstance() {
		if (daoManager == null)
			daoManager = new DAOManager();
		return daoManager;
	}

	public DAOManager getDAOManager() {return daoManager;}
	public void setDAOManager(DAOManager dao) {this.daoManager = dao;}
	
	public void connectDB() { // DB를 연결할 함수 선언
		try {

			Class.forName(jdbcDriver); // jdbc드라이버 로딩
			conn = DriverManager.getConnection(jdbcUrl, "root", "ghktks12"); // DB경로, 사용자id,p/w를 통하여 연결하는 변수 생성
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

	public boolean addMenu(String menuname) {// 콤보박스의 인덱스길이, 메뉴를 추가할 이름, 메뉴의 아이디값을 매개변수로한다
		int result = 0;
		lastIndex = getKey();
		String chicken = "chicken";
		sql = "insert into menulist(mainkey,cname,id) values(?,?,?)"; // int,String,String값의 테이블에 삽입하는 쿼리문

		try {
			connectDB(); // DB에 연걸
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
		
		closeDB(); // DB연결을 해제한다
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	
	public boolean delMenu(int selected) {
		sql = "delete from menulist where mainkey = ?";
		int result = 0;
		try {
			connectDB(); // DB에 연걸
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, selected);
			pstmt.executeUpdate();
			
			sql = "drop table chicken"+selected;
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB연결을 끊는다
		if (result > 0) {
			return true;
		} // 결과값이 0보다 크면 잘 전송이 되었다는 의미의 조건문
		else {
			return false;
		}
	}// delProduct

	

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
		closeDB(); // DB연결을 해제한다
		return days;
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
		closeDB(); // DB연결을 해제한다
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
		closeDB(); // DB연결을 해제한다
		return days;
	}

	public ArrayList<DayList> getStockTable() {			
		
		ArrayList<DayList> days = new ArrayList<DayList>();
		
		
		try {
			connectDB();
			for(int i=1;i<=getKey();i++) {
		
				DayList d = new DayList();
			
				d.setSales(getSales(i));
				d.setStock(getStock(i));
				d.setPrice(getPrice(i));
				System.out.println(""+d.getStock());
				sql = "select cname from menulist where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "\'chicken\'"+i);
				
				rs=pstmt.executeQuery();
				//rs.next();
				
				d.setName(rs.getString("cname"));
				days.add(d);
			}
		} catch (Exception e) {
		}
		closeDB(); // DB연결을 해제한다
		return days;
	}
	

	
	public boolean insertList(int index, String day, int sales, int stock, int price) { //치킨테이블 과 총합테이블 인덱스를 추가 및 업데이트하는 함수 
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
				pstmt.setInt(3, getStock(index)-sales);
				pstmt.setInt(4, getPrice(index));
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
			////////////////////////////////////////////////////////////////////////////////////////////////치킨테이블 목록추가
			
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
			System.out.println(""+flag);
			
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		closeDB(); // DB연결을 끊는다

		if (result > 0) {
			return true;
		} // 결과값이 0보다 크면 잘 전송이 되었다는 의미의 조건문
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
		closeDB(); // DB연결을 해제한다
		if (result > 0) {
			return true;
		} // 결과값이 0보다 크면 잘 전송이 되었다는 의미의 조건문
		else {
			return false;
		}
	}

	public int getKey() { // 메뉴리스트의 마지막 인덱스를 얻는 메소드
		int result = 0;
		sql = "select mainkey from menulist order by mainkey desc limit 1";

		try {
			connectDB(); // DB에 연걸
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("mainkey");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB연결을 해제한다
		return result;
	}

	public int getSales(int index) { // 테이블의 마지막 인덱스의 판매량을 나타내준다
		int result = 0;
		sql = "select sales from chicken" + index + " order by day desc limit 1"; 

		try {
			connectDB(); // DB에 연걸
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("sales");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB연결을 해제한다
		return result;
	}

	public int getStock(int index) { // 테이블의 마지막 인덱스의 판매량을 나타내준다
		int result = 0;
		sql = "select stock from chicken" + index + " order by day desc limit 1";

		try {
			connectDB(); // DB에 연걸
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("stock");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB연결을 해제한다
		return result;
	}

	public int getPrice(int index) { // 테이블의 마지막 인덱스의 판매량을 나타내준다
		int result = 0;
		sql = "select price from chicken" + index + " order by day desc limit 1"; 

		try {
			connectDB(); // DB에 연걸
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("price");
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB(); // DB연결을 해제한다
		return result;
	}
	
	public String getCName(int index) { // 테이블의 마지막 인덱스의 판매량을 나타내준다
		String result ="" ;
		sql = "select cname from menulist where id = ?"; 

		try {
			connectDB(); // DB에 연걸
			pstmt = conn.prepareStatement(sql);
			String chicken = "chicken"+index;
			pstmt.setString(1, chicken);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getString("cname");
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
	
	public Vector<String> getItems(){return items;}
	

}
