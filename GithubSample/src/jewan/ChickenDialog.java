package jewan;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChickenDialog extends JDialog implements ActionListener{
	private int mode;
	
	public ChickenDialog() {
		AppManager.getInstance().setChickenDialog(this);
		mode=0;
		makeUI();
	}
	
	
	public void makeUI() {
		if(mode==1){
			
		}
		else if(mode == 5) {	//테이블 주문서 UI
			
		}
	}
	public void ItemManagerUI(){
		
	}
	public void salesManagerUI(){
		
	}
	public void TableEditManagerUI(){
		
	}
	public void OptionUI() {
	
	}
	public void TableUI() {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		EventAction obj= (EventAction) e.getSource();
		
		obj.doAction();
	}
	
}