package fina;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.Gson;

public class ChickenServer {
	
	private ServerSocket ss = null;			//서버 소켓 선언
	private Socket s = null;				//클라이언트 소켓 선언
	
	private ArrayList<ChickenThread> chickenThreads = new ArrayList<ChickenThread>();	//연결된 클라이언트 스레드를 관리하는 ArrayList
	
	private ArrayList<ReceiptItem> info;	//임시로 사용
	
	public ChickenServer() {
		AppManager.getInstance().setChickenServer(this);
		this.start();
	}
	private Gson gson = new Gson();		//gson형태의 파싱을 수행하는 객체 생성
	
	//서버 시작 함수
	public void start() {
		
		try {
			ss = new ServerSocket(7890);						//서버 소켓 생성
			
			while(true) {										//무한 루프를 돌면서 클라이언트 연결을 기다린다.	
				System.out.println("준비");
				s = ss.accept();								//클라이언트 소켓의 연결이 감지되면 s객체에 저장
				System.out.println("성공");
				ChickenThread cThread = new ChickenThread();	//연결된 클라이언트에 대해 스레드 객체 생성
				
				chickenThreads.add(cThread);					//클라이언트 리스트에 현재 스레드 추가
				System.out.println(chickenThreads.size());
				cThread.start();								//현재 생성된 스레드 시작
			}
		}catch(Exception e) {
			e.printStackTrace();								//예외메세지
		}
	}
	
	//클라이언트에게 메세지를 전달
	public void msgSendAll(String msg){
		for(ChickenThread ct: chickenThreads) {
			ct.outMsg.println(msg);
		}
	}
	
	public void msgType(int type) {
		String msg;
		
		if(info == null)
			info=new ArrayList<ReceiptItem>();
		
		//테이블 구조 갱신
		if(type == 1) {
			msg=gson.toJson(new Message("tableArrange",0,0,info));
			msgSendAll(msg);
		}
		//타이머 시간 설정
		else if(type == 2) {
			msg=gson.toJson(new Message("timerSet",0,AppManager.getInstance().getTimerSet(),info));
			msgSendAll(msg);
		}
		//주문서 갱신
		else if(type == 3) {
			int index= AppManager.getInstance().getTid();
			TableBtn tb = AppManager.getInstance().getTableArray().get(index);
			msg=gson.toJson(new Message("Order",index,tb.getBoxNum(),tb.getTInfo()));
			msgSendAll(msg);
		}
		//콤보박스 갱신
		else if(type == 4) {
			msg=gson.toJson(new Message("getItem",0,0,info));
			msgSendAll(msg);
		}
		//서빙
		else if(type == 5) {
			int index= AppManager.getInstance().getTid();
			msg=gson.toJson(new Message("timerOff",index,0,info));
			msgSendAll(msg);
		}
		//주문서 가격 값 갱신
		else if(type == 6) {
			int index= AppManager.getInstance().getTid();
			TableBtn tb = AppManager.getInstance().getTableArray().get(index);
			msg=gson.toJson(new Message("orderPriceEdit",index,tb.getPrice(),info));
			msgSendAll(msg);
		}
		//결제 완료
		else if(type == 7) {
			int index= AppManager.getInstance().getTid();
			msg=gson.toJson(new Message("pay",index,0,info));
			msgSendAll(msg);
		}
	}
	
	//각 클라이언트를 관리할 스레드 클래스
	public class ChickenThread extends Thread{
		private String msg;					//여러 목적으로 사용할 문자 객체		
		private String mType;
		
		private BufferedReader inMsg = null;	//입력스트림
		private PrintWriter outMsg = null;		//출력스트림
		
		private boolean status;					//클라이언트와의 연결 여부
		
		private Message m;
		
		private Gson threadGson;
		
		@Override
		public void run() {
			status = true;				//연결 성공
			threadGson = new Gson();		//gson형태의 파싱을 수행하는 객체 생성
			
			try {
				inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));		//입력스트림 생성
				outMsg = new PrintWriter(s.getOutputStream(),true);							//출력스트림 생성
				
				msgType(2);
				
				while(status) {					//연결되어 있는 상태이면 루프		
					
					msg = inMsg.readLine();		//수신된 메세지를 msg변수에 저장
					
					m = threadGson.fromJson(msg, Message.class);		//JSON 메세지를 message 객체로 매핑
					
					mType = m.getType();
					
					// 주문서 갱신 명령
					if (mType.equals("Order")) {
						int index = m.getIndex();
						TableBtn tb = AppManager.getInstance().getTableArray().get(index);
						tb.setBoxNum(m.getNum());
						tb.setTInfo(m.getTInfo());
						tb.timerStart();
						
						msg=gson.toJson(new Message("Order",index,tb.getBoxNum(),tb.getTInfo()));
						msgSendAll(msg);
						
					}
					// 서빙 명령
					else if (mType.equals("timerOff")) {
						int index= m.getIndex();
						
						AppManager.getInstance().getTableArray().get(index).timerOff();
						
						msg=gson.toJson(new Message("timerOff",index,0,info));
						msgSendAll(msg);
						
						
					}
					// 주문서 가격 갱신
					else if (mType.equals("orderPriceEdit")) {
						int index = m.getIndex();
						TableBtn tb = AppManager.getInstance().getTableArray().get(index);
						
						AppManager.getInstance().getTableArray().get(index).setPrice(m.getNum());
						
						msg=gson.toJson(new Message("orderPriceEdit",index,tb.getPrice(),info));
						msgSendAll(msg);
					}
					
				}
			}catch(Exception e) {
				//예외시 데이터베이스에서 로그인 상태를 로그오프로 바꾸기 위함
				chickenThreads.remove(this);	//예외로 처리될 경우도 리스트에서 제거하기 위함
				status=false;
				e.printStackTrace();		//예외처리
			}
			this.interrupt();		//루프를 벗어나면 클라이언트 연결이 종료되므로 스레드 인터럽트
		}
	}
}
