package jewan;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class TableBtn extends JButton implements EventAction, Runnable, MouseListener{
	private int index;

//=====================================================================================
//	#������
//=====================================================================================	
	public TableBtn(int index){
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
//=====================================================================================
//	#�Լ�
//=====================================================================================
	
//=====================================================================================
//	#�׼��̺�Ʈ �ڵ鸵
//=====================================================================================
	public void doAction() {
		AppManager.getInstance().getChickenDialog().setMode(4);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

//=====================================================================================
//	#���콺 �̺�Ʈ �ڵ鸵
//=====================================================================================	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(AppManager.getInstance().getChickenMain().getFrameMode()==1)
			this.setLocation(this.getX()+e.getX(),this.getY()+e.getY());
			AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
	}
}
