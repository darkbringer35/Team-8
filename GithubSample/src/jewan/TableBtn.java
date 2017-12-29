
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class TableBtn extends JButton implements EventAction, Runnable, MouseListener{
	private int x;
	private int y;
	
	public TableBtn(){
		this.setVisible(true);
		this.setSize(100,100);
		this.setLocation(100,100);
		
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
			AppManager.getInstance().getChickenMain().getTabelPanel().remove(this);
			System.out.println("pick"+ e.getX()+e.getY());
			this.setLocation(this.getX()+e.getX(),this.getY()+e.getY());
			System.out.println(this.getX()+" "+this.getY());
			AppManager.getInstance().getChickenMain().getTabelPanel().add(this);
			AppManager.getInstance().getChickenMain().getTabelPanel().repaint();
	}
}
