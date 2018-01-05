package fina;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.Gson;

public class ChickenServer {
	
	private ServerSocket ss = null;			//���� ���� ����
	private Socket s = null;				//Ŭ���̾�Ʈ ���� ����
	
	private ArrayList<ChickenThread> chickenThreads = new ArrayList<ChickenThread>();	//����� Ŭ���̾�Ʈ �����带 �����ϴ� ArrayList
	
	private ArrayList<ReceiptItem> info;	//�ӽ÷� ���
	
	public ChickenServer() {
		AppManager.getInstance().setChickenServer(this);
		this.start();
	}
	private Gson gson = new Gson();		//gson������ �Ľ��� �����ϴ� ��ü ����
	
	//���� ���� �Լ�
	public void start() {
		
		try {
			ss = new ServerSocket(7890);						//���� ���� ����
			
			while(true) {										//���� ������ ���鼭 Ŭ���̾�Ʈ ������ ��ٸ���.	
				System.out.println("�غ�");
				s = ss.accept();								//Ŭ���̾�Ʈ ������ ������ �����Ǹ� s��ü�� ����
				System.out.println("����");
				ChickenThread cThread = new ChickenThread();	//����� Ŭ���̾�Ʈ�� ���� ������ ��ü ����
				
				chickenThreads.add(cThread);					//Ŭ���̾�Ʈ ����Ʈ�� ���� ������ �߰�
				System.out.println(chickenThreads.size());
				cThread.start();								//���� ������ ������ ����
			}
		}catch(Exception e) {
			e.printStackTrace();								//���ܸ޼���
		}
	}
	
	//Ŭ���̾�Ʈ���� �޼����� ����
	public void msgSendAll(String msg){
		for(ChickenThread ct: chickenThreads) {
			ct.outMsg.println(msg);
		}
	}
	
	public void msgType(int type) {
		String msg;
		
		if(info == null)
			info=new ArrayList<ReceiptItem>();
		
		//���̺� ���� ����
		if(type == 1) {
			msg=gson.toJson(new Message("tableArrange",0,0,info));
			msgSendAll(msg);
		}
		//Ÿ�̸� �ð� ����
		else if(type == 2) {
			msg=gson.toJson(new Message("timerSet",0,AppManager.getInstance().getTimerSet(),info));
			msgSendAll(msg);
		}
		//�ֹ��� ����
		else if(type == 3) {
			int index= AppManager.getInstance().getTid();
			TableBtn tb = AppManager.getInstance().getTableArray().get(index);
			msg=gson.toJson(new Message("Order",index,tb.getBoxNum(),tb.getTInfo()));
			msgSendAll(msg);
		}
		//�޺��ڽ� ����
		else if(type == 4) {
			msg=gson.toJson(new Message("getItem",0,0,info));
			msgSendAll(msg);
		}
		//����
		else if(type == 5) {
			int index= AppManager.getInstance().getTid();
			msg=gson.toJson(new Message("timerOff",index,0,info));
			msgSendAll(msg);
		}
		//�ֹ��� ���� �� ����
		else if(type == 6) {
			int index= AppManager.getInstance().getTid();
			TableBtn tb = AppManager.getInstance().getTableArray().get(index);
			msg=gson.toJson(new Message("orderPriceEdit",index,tb.getPrice(),info));
			msgSendAll(msg);
		}
		//���� �Ϸ�
		else if(type == 7) {
			int index= AppManager.getInstance().getTid();
			msg=gson.toJson(new Message("pay",index,0,info));
			msgSendAll(msg);
		}
	}
	
	//�� Ŭ���̾�Ʈ�� ������ ������ Ŭ����
	public class ChickenThread extends Thread{
		private String msg;					//���� �������� ����� ���� ��ü		
		private String mType;
		
		private BufferedReader inMsg = null;	//�Է½�Ʈ��
		private PrintWriter outMsg = null;		//��½�Ʈ��
		
		private boolean status;					//Ŭ���̾�Ʈ���� ���� ����
		
		private Message m;
		
		private Gson threadGson;
		
		@Override
		public void run() {
			status = true;				//���� ����
			threadGson = new Gson();		//gson������ �Ľ��� �����ϴ� ��ü ����
			
			try {
				inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));		//�Է½�Ʈ�� ����
				outMsg = new PrintWriter(s.getOutputStream(),true);							//��½�Ʈ�� ����
				
				msgType(2);
				
				while(status) {					//����Ǿ� �ִ� �����̸� ����		
					
					msg = inMsg.readLine();		//���ŵ� �޼����� msg������ ����
					
					m = threadGson.fromJson(msg, Message.class);		//JSON �޼����� message ��ü�� ����
					
					mType = m.getType();
					
					// �ֹ��� ���� ���
					if (mType.equals("Order")) {
						int index = m.getIndex();
						TableBtn tb = AppManager.getInstance().getTableArray().get(index);
						tb.setBoxNum(m.getNum());
						tb.setTInfo(m.getTInfo());
						tb.timerStart();
						
						msg=gson.toJson(new Message("Order",index,tb.getBoxNum(),tb.getTInfo()));
						msgSendAll(msg);
						
					}
					// ���� ���
					else if (mType.equals("timerOff")) {
						int index= m.getIndex();
						
						AppManager.getInstance().getTableArray().get(index).timerOff();
						
						msg=gson.toJson(new Message("timerOff",index,0,info));
						msgSendAll(msg);
						
						
					}
					// �ֹ��� ���� ����
					else if (mType.equals("orderPriceEdit")) {
						int index = m.getIndex();
						TableBtn tb = AppManager.getInstance().getTableArray().get(index);
						
						AppManager.getInstance().getTableArray().get(index).setPrice(m.getNum());
						
						msg=gson.toJson(new Message("orderPriceEdit",index,tb.getPrice(),info));
						msgSendAll(msg);
					}
					
				}
			}catch(Exception e) {
				//���ܽ� �����ͺ��̽����� �α��� ���¸� �α׿����� �ٲٱ� ����
				chickenThreads.remove(this);	//���ܷ� ó���� ��쵵 ����Ʈ���� �����ϱ� ����
				status=false;
				e.printStackTrace();		//����ó��
			}
			this.interrupt();		//������ ����� Ŭ���̾�Ʈ ������ ����ǹǷ� ������ ���ͷ�Ʈ
		}
	}
}
