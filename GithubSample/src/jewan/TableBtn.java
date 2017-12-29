package jewan;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class TableBtn extends JButton implements EventAction, Runnable, MouseListener{
	ChickenMain cMain;
	ChickenDialog cDia;
	private int x;
	private int y;
	
	public TableBtn(){
		cMain = AppManager.getInstance().getChickenMain();
		this.setVisible(true);
		this.setBounds(100,100,100,100);
		x=100;
		y=100;
		this.addMouseListener(this);
	}

	public void doAction() {
		AppManager.getInstance().getChickenDialog().setMode(4);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

//=====================================================================================
//	#마우스 이벤트 핸들링
//=====================================================================================	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(cMain.frameMode==1) {
			System.out.println("pick"+ e.getX()+e.getY());
			
		}
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
		if(cMain.frameMode==1)
			this.setLocation(e.getX(), e.getY());
	}
}
