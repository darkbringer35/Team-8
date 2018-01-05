package fina;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.google.gson.Gson;

public class ChickenClient extends JFrame implements ActionListener, Runnable {

	private JPanel tablePanel;
	private ArrayList<ClientTableBtn> table;

	private Socket socket = null; // 외부와 통신을 위한 소켓 객체
	private String ip = "127.0.0.1"; // 연결할 서버의 ip주소 값을 저장할 String 객체

	private BufferedReader inMsg = null; // 소켓을 통해서 들어오는 값을 저장할 통로 객체
	private PrintWriter outMsg = null; // 소켓을 통해서 밖으로 메세지를 내보낼 통로 객체

	private Thread connThread;
	private Gson gson;

	private boolean status;
	private String msg;
	private String msgType;
	private Message m;
	private ArrayList<ReceiptItem> info;	//임시로 사용

	public ChickenClient() {

		ClientAppManager.getInstance().setClient(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("치킨집 점원 프로그램");

		gson = new Gson();

		makeUI();

		this.pack();
		this.setVisible(true);

	}

	public void makeUI() {

		tablePanel = new JPanel();

		tablePanel.setLayout(null);
		tablePanel.setPreferredSize(new Dimension(810, 510));
		tablePanel.setBackground(Color.white);
		tablePanel.setBorder(new TitledBorder(new LineBorder(Color.black)));

		connectServer();

		table = ClientAppManager.getInstance().getTableDao().getTableList();
		if (table == null) {
			table = new ArrayList<ClientTableBtn>();
		} else {
			for (ClientTableBtn tb : table) {
				tablePanel.add(tb);
				tb.addActionListener(this);
				tb.updateUI();
			}
		}
		ClientAppManager.getInstance().setTableArray(table);

		this.add(tablePanel);
	}

	// =====================================================================================
	// #함수
	// =====================================================================================
	public void allTableClean() {
		tablePanel.setBackground(Color.white);
		for (ClientTableBtn tb : table) {
			tb.setBorderPainted(false);
			tb.setForeground(Color.black);
		}
		if (AppManager.getInstance().getTid() != -1) {
			ClientTableBtn tb = table.get(ClientAppManager.getInstance().getTid());
			tb.setBorderPainted(true);
			tb.setForeground(Color.red);
		}
	}

	public void tableArrayRefresh() {
		int index = 0;
		AppManager.getInstance().setTid(-1);
		for (ClientTableBtn tb : table) {
			tb.setIndex(index);
			tb.setText("테이블" + index);
			index++;
		}
	}
	// =====================================================================================
	// #액션이벤트
	// =====================================================================================

	@Override
	public void actionPerformed(ActionEvent e) {
		EventAction obj = (EventAction) e.getSource();

		obj.doAction();
	}

	// =====================================================================================
	// #통신관련 함수
	// =====================================================================================
	public boolean connectServer() { // 연결 성공/실패 여부를 반환하는 형태, 보낼 메세지 타입을 매개 변수로 받는다.
		try {
			socket = new Socket(ip, 7890); // 소켓 생성

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 서버로부터 입력받는 스트림 생성
			outMsg = new PrintWriter(socket.getOutputStream(), true); // 서버로 출력하는 스트림 생성

			threadStart();

			return true; // 연결 성공으로 true를 반환

		} catch (Exception e) {
			e.printStackTrace(); // 예외발생
			return false; // 연결 실패로 false 반환
		}
	}

	public void threadStart() {
		if (connThread == null) {
			connThread = new Thread(this);
			connThread.start();
		}
	}

	public void sendMsg(String msg) {
		outMsg.println(msg);
	}
	
	public void msgType(int type) {
		String msg;
		
		if(info == null)
			info=new ArrayList<ReceiptItem>();
		
		// 주문서 등록
		if (type == 1) {
			int index = ClientAppManager.getInstance().getTid();
			ClientTableBtn tb = ClientAppManager.getInstance().getTableArray().get(index);
			msg = gson.toJson(new Message("Order", index, tb.getBoxNum(), tb.getTInfo()));
			sendMsg(msg);
		}
		// 서빙
		else if (type == 2) {
			int index = ClientAppManager.getInstance().getTid();
			msg = gson.toJson(new Message("timerOff", index, 0, info));
			sendMsg(msg);
		}
		// 주문서 가격 값 갱신
		else if (type == 3) {
			int index = ClientAppManager.getInstance().getTid();
			ClientTableBtn tb = ClientAppManager.getInstance().getTableArray().get(index);
			msg = gson.toJson(new Message("orderPriceEdit", index, tb.getPrice(), info));
			sendMsg(msg);
		}
	}

	public void run() {
		status = true;

		while (status) { // 서버와 연결된 상태면 루프 지속

			try {
				msg = inMsg.readLine(); // 입력 스트림을 통해 메세지 수신
				m = gson.fromJson(msg, Message.class); // 수신된 메세지을 파싱

				System.out.println(msg);
				// 수신 메세지 종류와 아이디
				msgType = m.getType();

				// 테이블 생성 명령
				if (msgType.equals("tableArrange")) {
					
					for (ClientTableBtn tb : table) {
						tb.timerOff();
						tb.removeActionListener(this);
						tablePanel.remove(tb);
					}
					table = null;
					tablePanel.repaint();

					table = ClientAppManager.getInstance().getTableDao().getTableList();
					ClientAppManager.getInstance().setTableArray(table);

					for (ClientTableBtn tb : table) {
						tablePanel.add(tb);
						tb.addActionListener(this);
						tb.updateUI();
					}
					tablePanel.repaint();
					
				}
				// 타이머 설정 명령
				else if (msgType.equals("timerSet")) {
					ClientAppManager.getInstance().setTimerSet(m.getNum());
				}
				// 주문서 갱신 명령
				else if (msgType.equals("Order")) {
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setBoxNum(m.getNum());
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setTInfo(m.getTInfo());
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).timerStart();
				}
				// 콤보박스 갱신 명령
				else if (msgType.equals("getItem")) {
					ClientAppManager.getInstance().getChickenDialog().refreshComboBox();
				}
				// 서빙 명령
				else if (msgType.equals("timerOff")) {
					if(m.getIndex()!=-1)
						ClientAppManager.getInstance().getTableArray().get(m.getIndex()).timerOff();
				}
				// 주문서 가격 갱신
				else if (msgType.equals("orderPriceEdit")) {
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setPrice(m.getNum());
				}
				// 주문서 가격 갱신
				else if (msgType.equals("pay")) {
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setPrice(0);
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setTInfo(null);
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setBoxNum(0);
				}

			} catch (Exception e) {
				e.printStackTrace();
				status=false;
				break;
			}
		}

		// 연결 끊김시 연결 종료
		status=false;
		outMsg.close();
		try {
			inMsg.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		connThread.interrupted();
	}

}
