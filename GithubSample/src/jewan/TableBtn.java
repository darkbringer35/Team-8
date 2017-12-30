package jewan;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;

public class TableBtn extends JButton implements EventAction, Runnable, MouseListener{
	private int index;
	private int boxAmount;
	private ArrayList<Table> tInfo=null;
	
	private Thread timer=null;
	
//=====================================================================================
//	#생성자
//=====================================================================================	
	public TableBtn(int index){
		this.index=index;
		boxAmount=0;
		
		this.setText("테이블"+index);
		
		this.setVisible(true);
		this.setSize(100,100);
		this.setLocation(100+index*10,100+index*10);
		this.setBorderPainted(false);
		
		this.addMouseListener(this);
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
	public ArrayList<Table> getTInfo(){
		return getTInfo();
	}
	public void setTInfo(ArrayList<Table> t) {
		tInfo= t;
	}
//=====================================================================================
//	#함수
//=====================================================================================
	
	
//=====================================================================================
//	#액션이벤트 핸들링
//=====================================================================================
	public void doAction() {
		AppManager.getInstance().setTid(index);
		AppManager.getInstance().getChickenDialog().setMode(4);
		AppManager.getInstance().getChickenMain().allTableClean();
		this.setBorderPainted(true);
	}
	
	
	@Override
	public void run() {
	}

//=====================================================================================
//	#마우스 이벤트 핸들링
//=====================================================================================	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(AppManager.getInstance().getFrameMode()==1) {
			this.setLocation(this.getX()+e.getX()-50,this.getY()+e.getY()-50);
			AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
		}
	}
}
