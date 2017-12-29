package jewan;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;

public class TableBtn extends JButton implements EventAction, Runnable, MouseListener{
	private int index;
	private int boxNum;
	private ArrayList<String> boxIndex;
	
//=====================================================================================
//	#������
//=====================================================================================	
	public TableBtn(int index){
		this.index=index;
		boxNum=0;
		
		this.setVisible(true);
		this.setSize(100,100);
		this.setLocation(100+index*10,100+index*10);
		
		this.addMouseListener(this);
	}
	
//=====================================================================================
//	#get/set�޼���
//=====================================================================================	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index=index;
	}
	public int getBoxNum() {
		return boxNum;
	}
	public void setBoxNum() {
		
	}
//=====================================================================================
//	#�Լ�
//=====================================================================================
	
//=====================================================================================
//	#�׼��̺�Ʈ �ڵ鸵
//=====================================================================================
	public void doAction() {
		AppManager.getInstance().getChickenDialog().setTableIndex(index);
		AppManager.getInstance().getChickenDialog().setMode(4);
	}
	
	
	@Override
	public void run() {
	}

//=====================================================================================
//	#���콺 �̺�Ʈ �ڵ鸵
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
		if(AppManager.getInstance().getChickenMain().getFrameMode()==1)
			this.setLocation(this.getX()+e.getX(),this.getY()+e.getY());
			AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
	}
}
