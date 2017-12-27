package jewan;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChickenDialog extends JDialog implements ActionListener{
	private int mode;
	private static ChickenDialog cDia=null;
	
	private ChickenDialog(int m) {
		mode=m;
		makeUI();
	}
	
	public static ChickenDialog getInstance(int m) {
		if(cDia == null) cDia = new ChickenDialog(m);
		return cDia;
	}
	
	public void makeUI() {
		if(mode==1){
			
		}
		else if(mode == 5) {
			
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