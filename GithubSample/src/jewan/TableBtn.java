package jewan;



import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TableBtn extends JButton implements EventAction, MouseListener, MouseMotionListener{
	private int index;
	private int boxAmount;
	private ArrayList<ReceiptItem> tInfo=null;
	private int backX;
	private int backY;
	private int price;
	
	private int time=0;
	private Timer timer=null;
	private boolean timerOn = false;
	private boolean blackRed = false;
	
//=====================================================================================
//	#생성자
//=====================================================================================	
	public TableBtn(int index){
		this.index=index;
		boxAmount = 0;
		price = 0;
		timeReset();
		this.setText("테이블"+index);
		
		this.updateUI();
		
		this.setVisible(true);
		this.setSize(100,100);
		this.setLocation(100+index*10,100+index*10);
		this.setBorderPainted(false);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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
		time=60*AppManager.getInstance().getTimerSet();
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
		AppManager.getInstance().setTid(index);
		AppManager.getInstance().getChickenDialog().setMode(4);
		AppManager.getInstance().getChickenMain().allTableClean();
		this.setForeground(Color.RED);
		this.setBorderPainted(true);
	}

//=====================================================================================
//	#마우스 이벤트 핸들링
//=====================================================================================	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(AppManager.getInstance().getFrameMode() == 2) {
			timerOff();
			AppManager.getInstance().getChickenMain().getTabelPanel().remove(this);
			AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
			AppManager.getInstance().getTableArray().remove(this);
			AppManager.getInstance().getChickenMain().tableArrayRefresh();
			AppManager.getInstance().getChickenMain().allTableClean();
			AppManager.getInstance().setFrameMode(1);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		backX=this.getX();
		backY=this.getY();
	
		if(AppManager.getInstance().getFrameMode() == 2) {
			timerOff();
			AppManager.getInstance().getChickenMain().getTabelPanel().remove(this);
			AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
			AppManager.getInstance().getTableArray().remove(this);
			AppManager.getInstance().getChickenMain().tableArrayRefresh();
			AppManager.getInstance().getChickenMain().allTableClean();
			AppManager.getInstance().setFrameMode(1);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(AppManager.getInstance().getFrameMode()==0) {
			if(this.getX()<120&&this.getY()>409) {
				JTextField[] txf=AppManager.getInstance().getChickenMain().getTxfCash();
				txf[0].setText(""+price);
				txf[2].setText(""+(Integer.parseInt(txf[1].getText())-price));
			}
			this.setLocation(backX,backY);
			AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
		}
		else if(AppManager.getInstance().getFrameMode()==1) {
			if(this.getX()<0||this.getX()>708||this.getY()<0||this.getY()>409)
				AppManager.getInstance().setMouseOut(true);
			else
				AppManager.getInstance().setMouseOut(false);
			
			if(!AppManager.getInstance().getMouseOut()) {
				this.setLocation(this.getX()+e.getX()-50,this.getY()+e.getY()-50);
				AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
			}
			else {
				this.setLocation(backX,backY);
				AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(AppManager.getInstance().getFrameMode()<2) {
			this.setLocation(this.getX()+e.getX()-50,this.getY()+e.getY()-50);
			AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
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
