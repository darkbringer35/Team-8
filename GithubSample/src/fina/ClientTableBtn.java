package fina;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ClientTableBtn extends JButton implements EventAction {

	private int index;
	private int boxAmount;
	private ArrayList<ReceiptItem> tInfo;
	private int price;
	
	private int time=0;
	private Timer timer=null;
	private boolean timerOn = false;
	private boolean blackRed = false;
	
	//사용될 테마 컬러들 정의
	private Color color3 = new Color(255, 218, 175);
	
//=====================================================================================
//	#생성자
//=====================================================================================	
	public ClientTableBtn(int index){
		this.index=index;
		boxAmount = 0;
		price = 0;
		timeReset();
		this.setText("테이블"+index);
		
		this.updateUI();
		
		this.setBackground(color3);
		this.setVisible(true);
		this.setSize(100,100);
		this.setLocation(100+index*10,100+index*10);
		this.setBorderPainted(false);
	
	}
	
//=====================================================================================
//	#get/set메서드
//=====================================================================================	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index=index;
	}
	public int getBoxNum() {
		return boxAmount;
	}
	public void setBoxNum(int amount) {
		boxAmount=amount;
	}
	public ArrayList<ReceiptItem> getTInfo(){
		return tInfo;
	}
	public void setTInfo(ArrayList<ReceiptItem> t) {
		tInfo = t;
	}
	public void setPrice(int pr) {
		price = pr;
	}
	public int getPrice() {
		return price;
	}
//=====================================================================================
//	#함수
//=====================================================================================
	public void paintComponent(Graphics page) {
		String min, sec;
		
		super.paintComponent(page);
		
		min=""+time/60;
		
		if(time%60<10) sec="0"+time%60;
		else sec=""+time%60;
		if(timerOn) {
			if(blackRed)
				page.setColor(Color.red);
			else
				page.setColor(Color.black);
			
			page.drawString(min+":"+sec, 10, 10);
		}
	}
	public void timeReset() {
		time=60*ClientAppManager.getInstance().getTimerSet();
	}
	
	public void timerStart() { 
		if(timer==null) {
			timeReset();
			timer= new Timer();
			timerOn=true;
			timer.start();
		}
	}
	public void timerOff() {
		timerOn=false;
		if(timer!=null) timer.stop();
		timer=null;
		repaint();
		timeReset();
	}
//=====================================================================================
//	#액션이벤트 핸들링
//=====================================================================================
	public void doAction() {
		ClientAppManager.getInstance().setTid(index);
		ClientAppManager.getInstance().getChickenDialog().refreshUI();;
		
		ClientAppManager.getInstance().getClient().allTableClean();
		this.setForeground(Color.RED);
		this.setBorderPainted(true);
		System.out.println("성공");
	}


//=====================================================================================
//	#쓰레드 클래스
//=====================================================================================	
	public class Timer extends Thread{
		public void run() {
			try {
				while(time>0) {	
					repaint();
					sleep(1000);
					time--;
				}
				while(timerOn) {
					repaint();
					sleep(500);
					blackRed = !blackRed; 
				}
			}catch(Exception e){
				timerOn = false;
				e.printStackTrace();
			}
		}
	}
}

