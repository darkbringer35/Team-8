package jewan;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChickenDialog extends JDialog implements ActionListener{
	//��ư�� ���� UI�� �ҷ����� ���� mode����
	private int mode;
	
	//ȭ�� ���� ��ȯ�� ���� ī�� ���̾ƿ�
	protected Container tab;
	protected CardLayout cardLayout;
	
	//�޴���ư�� ��� �ٿ��ߵǴ� �޴����гΰ� ��ư��
	JPanel menuPanel = new JPanel(); // �޴��� �гη� ����
	JButton itemBtn = new JButton("������"); //������ ��ư
	JButton salesBtn = new JButton("�������"); //������� ��ư
	JButton optionBtn = new JButton("ȯ�漳��"); //ȯ�漳�� ��ư
	
	//Dialog�� �޴��г� �ؿ� ���� �� �޴� �гε� (ī�巹�̾ƿ��� �̿��� �� �гε� �� �ϳ��� ���õ�)
	JPanel itemPanel = new JPanel(); //��� ���� �г�
	JPanel salesPanel = new JPanel(); //���� ���� �г�
	JPanel optionPanel = new JPanel(); //ȯ�� ���� �г�
	
	
	//������
	public ChickenDialog() {
		//Dialog ������ ����
		this.setSize(875,625);
		this.setLocation(500,300);
		
		//ī�� ���̾ƿ� ����
		tab = new JPanel();
		cardLayout = new CardLayout();
		tab.setLayout(cardLayout);
		
		//�� �޴� ��ư �޴��� �гο� ����
		menuPanel.add(itemBtn);
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);
		
		//â ��ȯ�� ���� CardLayout�г� tab�� itemPanel �߰��ϱ�
		tab.add(itemPanel,"item");
		tab.add(salesPanel,"sales");
		tab.add(optionPanel,"option");
		
		
		AppManager.getInstance().setChickenDialog(this);
	}
	
	
	//-----------�޼ҵ�---------------
	
	public void setMode(int m) {
		mode=m;
		makeUI();
	}
	
	public void makeUI() {
		if(mode==1){ // 1: ������UI
			ItemManagerUI();
		}
		else if(mode == 2) { // 2: �������UI
			salesManagerUI();
		}
		else if(mode == 3) { // 3: ȯ�Ἲ��UI
			OptionUI();
		}
		else if(mode == 4) { // 4: ���̺� �ֹ��� UI
			TableUI();
		}
		this.setVisible(true);
	}
	
	//��� ���� â UI
	public void ItemManagerUI(){
		//â ���� ����
		setTitle("��� ����");
		
		//�� �г� p1 ����
		JPanel p1 = new JPanel(); //p1�г� ����
		p1.setLayout(new GridLayout(4,1)); //p1�г� �׸��� ���̾ƿ�
		p1.add(new JLabel("�޴���ȣ")); //�޴���ȣ ��
		p1.add(new JLabel("�޴���")); //�޴��� ��
		p1.add(new JLabel("���")); //��� ��
		p1.add(new JLabel("����")); //���� ��
		
		//�Ӽ� ����� �г� p2����
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
		
		//��ư �г� p3 ����
		JPanel p3 = new JPanel(); //p3�г� ����
		JButton b1 = new JButton("���"); //��� ��ư
		JButton b2 = new JButton("��ȸ"); //��ȸ ��ư
		JButton b3 = new JButton("����"); //���� ��ư
		p3.setLayout(new FlowLayout()); //p3�г� �÷ο� ���̾ƿ�
		p3.add(b1); //p3�гο� ����
		p3.add(b2);
		p3.add(b3);
		
		//��� ��� TextField ����
		JTextArea ta = new JTextArea(10,40); //ta���� ����
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ta.append("�޴���ȣ\t�޴���\t\t���\t����\n"); //ta�� ��Ÿ�� �׸��
		
		//item Panel ���̾ƿ� ���� �� �μ� �гε� ���̱�
		itemPanel.setLayout(new BorderLayout());
		itemPanel.add(p1, BorderLayout.LINE_START); //p1�г� ����
		itemPanel.add(p2, BorderLayout.CENTER); //p2�г� �߰�
		itemPanel.add(p3, BorderLayout.PAGE_END); //p3�г� �ǾƷ�
		itemPanel.add(scroll, BorderLayout.LINE_END); //scroll�г� ������
	
		//dialog ���̾ƿ� �� ���� ����
		this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		//ī�� ���̾ƿ����� ������ â ��ȯ
		this.cardLayout.show(this.tab,"item");
		
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
	}
	
	//���� ����â UI
	public void salesManagerUI(){
		//â ����
		setTitle("���� ����");
		
		//��¥ �˻� �г� p1 ����
		JPanel p1 = new JPanel();
		JLabel lb = new JLabel("��¥ �˻�: ");
		JTextField tf = new JTextField(15);
		JButton btn = new JButton("�˻�");
		
		//p1���̾ƿ�
		p1.setLayout(new BorderLayout());
		p1.add(lb, BorderLayout.WEST);
		p1.add(tf, BorderLayout.CENTER);
		p1.add(btn, BorderLayout.EAST);

		//���� ��� �г� p2 ����
		JPanel p2 = new JPanel();
		JTextArea ta = new JTextArea(25,70); //ta���� ����
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ta.append("��¥\t�����\t\t�Ǹŷ�\n"); //ta�� ��Ÿ�� �׸��
		p2.add(scroll);
		
		//salesPanel�� �μ� �г� ���̱�
		salesPanel.add(p1,BorderLayout.PAGE_END);
		salesPanel.add(p2,BorderLayout.CENTER);
		
		//dialog ���̾ƿ� �� ���� ����
		this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		//ī�� ���̾ƿ����� �������â���� ��ȯ�ϱ�
		this.cardLayout.show(this.tab,"sales");
	}
	
	//ȯ�� ����â UI
	public void OptionUI() {
		//â ����
		setTitle("ȯ�� ����");

		//�� �ؽ�Ʈ�ʵ� ��ư ����
		JLabel lblOption = new JLabel("��� ���� �ð��� �������ּ���."); 
		JTextField txfOption = new JTextField();
		JButton btnOption = new JButton("Ȯ��");
		
		//���� ����
		optionPanel.setLayout(null);
		lblOption.setBounds(340,150,300,50);
		txfOption.setBounds(240,250,375,50);
		btnOption.setBounds(320,350,210,50);
				
		optionPanel.add(lblOption);
		optionPanel.add(txfOption);
		optionPanel.add(btnOption);
		
		//dialog ���̾ƿ� �� ���� ����
		this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		//ī�� ���̾ƿ����� ȯ�漳��â���� ��ȯ�ϱ�
		this.cardLayout.show(this.tab,"option");
	}
	
	public void TableUI() {
		JDialog dialog = new JDialog(this,"��꼭1234");
		JButton payBtn = new JButton("����");
		JButton okBtn = new JButton("���");
		JButton[] upBtn = new JButton[3];
		JButton[] downBtn = new JButton[3];
		JTextField[] amount = new JTextField[3];
		JScrollPane scroll = new JScrollPane(dialog,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		String[] menuList = {"-------------------------","�Ķ��̵�ġŲ(15000)","���ġŲ(15000)","����ġŲ(15000)"};				
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
		dialog.setSize(1000,500);
		//dialog.setLocation(500,500);
		dialog.setVisible(true);
		dialog.show();
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