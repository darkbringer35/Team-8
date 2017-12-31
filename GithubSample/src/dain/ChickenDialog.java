package dain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChickenDialog extends JDialog implements ActionListener{
	private int mode;
	
	public ChickenDialog() {
		//AppManager.getInstance().setChickenDialog(this);
		mode=0;
		makeUI();
	}
	
	public void setMode(int m) {
		mode=m;
		makeUI();
	}
	
	public void makeUI() {
		if(mode==1){
			ItemManagerUI();
		}
		else if(mode == 2) {
			salesManagerUI();
		}
		else if(mode == 3) {
			TableEditManagerUI();
		}
		else if(mode == 4) {
			OptionUI();
		}
		else if(mode == 5) {	//���̺� �ֹ��� UI
			TableUI();
		}
	}
	public void ItemManagerUI(){
		
	}
	public void salesManagerUI(){
		
	}
	public void TableEditManagerUI(){
		
	}
	public void OptionUI() {
		setTitle("ȯ�� ����"); //â ����

		this.setSize(400,300);
		
		JLabel lblOption = new JLabel("��� ���� �ð��� �������ּ���."); 
		JTextField txfOption = new JTextField();
		JButton btnOption = new JButton("Ȯ��");
		
		
		lblOption.setBounds(100,50,300,50);
		txfOption.setBounds(100,150,300,50);
		btnOption.setBounds(190,250,210,50);
	
		
		this.add(lblOption);
		this.add(txfOption);
		this.add(btnOption);
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