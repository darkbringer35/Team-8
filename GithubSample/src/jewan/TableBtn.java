package jewan;
import java.awt.BorderLayout;

import javax.swing.*;

public class TableBtn extends JButton implements EventAction, Runnable{
	ChickenMain chickenFrame;
	ChickenDialog cDia;
	
	public TableBtn(){
		chickenFrame = ChickenMain.getInstance();
	}
	
	public void doAction() {
		JDialog dialog = new JDialog(chickenFrame,"계산서");
		JButton payBtn = new JButton("결제");
		JButton okBtn = new JButton("등록");
		JButton[] upBtn = new JButton[3];
		JButton[] downBtn = new JButton[3];
		JTextField[] amount = new JTextField[3];
			
		String[] menuList = {"-------------------------","후라이드치킨(15000)","양념치킨(15000)","간장치킨(15000)"};				
		JComboBox[] menu = new JComboBox[3];
	
		for(int i=0;i<3;i++) {
			menu[i] = new JComboBox(menuList);
			menu[i].setBounds(0,i*50+30,125,25);
				
			downBtn[i] = new JButton("-");
			downBtn[i].setBounds(125,i*50+30,40,25); 
				
			amount[i] = new JTextField();
			amount[i].setBounds(165,i*50+30,50,25);
				
				
			upBtn[i] = new JButton("+");
			upBtn[i].setBounds(225,i*50+30,50,25);
			
				
			dialog.add(menu[i]);
			dialog.add(downBtn[i]);
			dialog.add(upBtn[i]);
			dialog.add(amount[i]);
		}
			
		payBtn.setBounds(100,200,50,50);
			
		dialog.setLayout(new BorderLayout());	
		dialog.setSize(300,300);
		dialog.setLocation(300,300);
		dialog.setVisible(true);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
