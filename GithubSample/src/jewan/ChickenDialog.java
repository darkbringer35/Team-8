package jewan;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChickenDialog extends JDialog implements ActionListener{
	private int mode;
	public ChickenDialog() {
		AppManager.getInstance().setChickenDialog(this);
		
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
			OptionUI();
		}
		else if(mode == 4) {	//���̺� �ֹ��� UI
			TableUI();
		}
		this.setVisible(true);
		
		
	}
	public void ItemManagerUI(){
		setTitle("��� ����");

		JPanel menuPanel = new JPanel(); // �޴��� �гη� ����
		JButton itemBtn = new JButton("������"); //������ ��ư
		JButton salesBtn = new JButton("�������"); //������� ��ư
		JButton optionBtn = new JButton("ȯ�漳��"); //ȯ�漳�� ��ư
		menuPanel.add(itemBtn); // �� �޴� ��ư �гο� ����
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);
	
		JPanel p1 = new JPanel(); //p1�г� ����
		p1.setLayout(new GridLayout(4,1)); //p1�г� �׸��� ���̾ƿ�
		p1.add(new JLabel("�޴���ȣ")); //�޴���ȣ ��
		p1.add(new JLabel("�޴���")); //�޴��� ��
		p1.add(new JLabel("���")); //��� ��
		p1.add(new JLabel("����")); //���� ��
		
		JPanel p2 = new JPanel(); //p2�г� ����
		JComboBox cb = new JComboBox(); //�޴���ȣ �޺��ڽ�
		JTextField t1 = new JTextField(); //�޴��� �ؽ�Ʈ�ʵ�
		JTextField t2 = new JTextField(); //��� �ؽ�Ʈ�ʵ�
		JTextField t3 = new JTextField(); //���� �ؽ�Ʈ�ʵ�
		p2.setLayout(new GridLayout(4,1)); //p2�г� �׸��� ���̾ƿ�
		p2.add(cb); //p2�гο� ����
		p2.add(t1);
		p2.add(t2);
		p2.add(t3);
		
		JPanel p3 = new JPanel(); //p3�г� ����
		JButton b1 = new JButton("���"); //��� ��ư
		JButton b2 = new JButton("��ȸ"); //��ȸ ��ư
		JButton b3 = new JButton("����"); //���� ��ư
		p3.setLayout(new FlowLayout()); //p3�г� �÷ο� ���̾ƿ�
		p3.add(b1); //p3�гο� ����
		p3.add(b2);
		p3.add(b3);
		
		JTextArea ta = new JTextArea(10,40); //ta���� ����
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ta.append("�޴���ȣ\t�޴���\t\t���\t����\n"); //ta�� ��Ÿ�� �׸��
		
		this.setLayout(new BorderLayout()); //��ü ���̾ƿ� ����
		add(menuPanel, BorderLayout.PAGE_START); //�޴��г� ����
		add(p1, BorderLayout.LINE_START); //p1�г� ����
		add(p2, BorderLayout.CENTER); //p2�г� �߰�
		add(p3, BorderLayout.PAGE_END); //p3�г� �ǾƷ�
		add(scroll, BorderLayout.LINE_END); //scroll�г� ������
		
		/*
		-----DB & Controller ��Ʈ : �����͸� �����ϸ� �޺��ڽ� ������ ����
		
		datas = dao.getAll();
		cb.setModel(new DefaultComboBoxModel(dao.getItems()));
		
		itemBtn.addActionListener(this); //�� ��ư �׼� ������
		salesBtn.addActionListener(this);
		optionBtn.addActionListener(this); 
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		
		*/

		this.setSize(875,625);
		//setVisible(true);
	}
	public void salesManagerUI(){
		setTitle("���� ����"); //â ����
		this.setLayout(new BorderLayout());
		
		JPanel menuPanel = new JPanel(); // �޴��� �гη� ����
		JButton itemBtn = new JButton("������"); //������ ��ư
		JButton salesBtn = new JButton("�������"); //������� ��ư
		JButton optionBtn = new JButton("ȯ�漳��"); //ȯ�漳�� ��ư
		menuPanel.add(itemBtn); // �� �޴� ��ư �гο� ����
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);
		
		JPanel p1 = new JPanel();
		JLabel lb = new JLabel("��¥ �˻�: ");
		JTextField tf = new JTextField();
		JButton btn = new JButton("�˻�");
		p1.add(lb);
		p1.add(tf);
		p1.add(btn);
		
		JPanel p2 = new JPanel();
		JTextArea ta = new JTextArea(25,70); //ta���� ����
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ta.append("��¥\t�����\t\t�Ǹŷ�\n"); //ta�� ��Ÿ�� �׸��
		p2.add(scroll);

		this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(p1,BorderLayout.PAGE_END);
		this.add(p2,BorderLayout.CENTER);
		
		setSize(875,625);
	}
	public void OptionUI() {
		setTitle("ȯ�� ����"); //â ����
		this.setLayout(null);
		
		JPanel menuPanel = new JPanel(); // �޴��� �гη� ����
		JButton itemBtn = new JButton("������"); //������ ��ư
		JButton salesBtn = new JButton("�������"); //������� ��ư
		JButton optionBtn = new JButton("ȯ�漳��"); //ȯ�漳�� ��ư
		menuPanel.add(itemBtn); // �� �޴� ��ư �гο� ����
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);
		
		JLabel lblOption = new JLabel("��� ���� �ð��� �������ּ���."); 
		JTextField txfOption = new JTextField();
		JButton btnOption = new JButton("Ȯ��");
		
		menuPanel.setBounds(0,0,875,50);
		menuPanel.setBackground(Color.black); //���Ƿ�...
		lblOption.setBounds(340,150,300,50);
		txfOption.setBounds(240,250,375,50);
		btnOption.setBounds(320,350,210,50);
				
		this.add(menuPanel);
		this.add(lblOption);
		this.add(txfOption);
		this.add(btnOption);
		
		this.setSize(875, 625);
	}
	public void TableUI() {
		
	}
	public void UIoff() {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		EventAction obj= (EventAction) e.getSource();
		
		obj.doAction();
	}
}