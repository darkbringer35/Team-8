package jewan;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ChickenMain extends JFrame implements ActionListener {	// JFrame�� ��ӹ޴´�

	private static ChickenMain chickenFrame;
	private JPanel backgroundPanel[];  
	private JPanel belowPanel[];
	
	private JButton[] btnMenu;
	private JButton[] btnTable;
	private JButton[] btnCash;
	private JButton[]pad = new JButton[12];
	private JButton btnPlus = new JButton("+");
	private JButton btnMinus = new JButton("-");
	private TableBtn [] table;
	
	private JLabel[] lblCash;
	
	private JTextField[] txfCash;

//=====================================================================================
//	#������
//=====================================================================================	
	private ChickenMain() { // ������ ����
		
		this.setLayout(new BorderLayout()); // BorderLayout���� ���̾ƿ� ����
		setSize(1050, 720); // ����â ũ�� ����
		setLocation(300, 100); // ����â ��ġ����
		setTitle("ġŲ"); // ������ Ÿ��Ʋ ����
		this.setBackground(Color.white);
		
		backgroundPanel = new JPanel[3];
		for(int i=0; i<3;i++)
			backgroundPanel[i] = new JPanel();
		
		//-----------------------------------------------------------------------------
		//	#�г�1 ����
		//-----------------------------------------------------------------------------
		backgroundPanel[0].setLayout(null); // ����г�1�� ���̾ƿ��� null�� ����
		backgroundPanel[0].setPreferredSize(new Dimension(200, 500));
		
		int menuBtnNum=4, menuWidth = 200,menuHeight = 130;
		
		btnMenu=new JButton[menuBtnNum];
		btnMenu[0] = new ItemManageBtn("������");  
		btnMenu[1] = new SalesManageBtn("�������"); 
		btnMenu[2] = new TableEditBtn("���̺� ����"); 
		btnMenu[3] = new OptionBtn("ȯ�漳��");
		
		for(int i=0;i<menuBtnNum;i++)
		{
			btnMenu[i].setBounds(0, i*menuHeight, menuWidth, menuHeight);//�޴���ư���� ��ġ ����
			btnMenu[i].addActionListener(this);
			backgroundPanel[0].add(btnMenu[i]);
		}
		
		//-----------------------------------------------------------------------------
		//	#�г�2 ����
		//-----------------------------------------------------------------------------
		backgroundPanel[1].setLayout(null); // ����г�2�� ���̾ƿ��� null�� ����
		backgroundPanel[1].setPreferredSize(new Dimension(200, 240));
		backgroundPanel[1].setBackground(Color.white);
		backgroundPanel[1].setBorder(new TitledBorder(new LineBorder(Color.black)));
		
		int tableNum = 12;
		table = new TableBtn[tableNum];
		for(int i=0;i<tableNum;i++)
		{
			table[i]=new TableBtn();
			table[i].setBounds(100,100,100,100);
			table[i].setEnabled(false);
			table[i].setVisible(false);
			table[i].addActionListener(this);
			backgroundPanel[1].add(table[i]);
		}
		//btnPlus.setBounds(755, 24, 30, );
		//backgroundPanel[1].add(btnPlus);
		//backgroundPanel[1].add(btnMinus);
		table[0].setVisible(true);
		table[0].setEnabled(true);
		
		//-----------------------------------------------------------------------------
		//	#�г�3 ����
		//-----------------------------------------------------------------------------
		backgroundPanel[2].setLayout(null); // ����г�3�� ���̾ƿ��� null�� ����
		backgroundPanel[2].setPreferredSize(new Dimension(1000, 210));
		backgroundPanel[2].setBackground(Color.white);
		
		belowPanel = new JPanel[3];
		for(int i = 0;i<3;i++)
		{
			belowPanel[i] = new JPanel();
			backgroundPanel[2].add(belowPanel[i]);
		}
		
		//-----------------------------------------------------------------------------
		//	#�Ϻ� �г�1 ��꼭 �г� ����
		//----------------------------------------------------------------------------
		belowPanel[0].setBounds(0, 0, 375, 240);
		belowPanel[0].setBackground(Color.white);
		belowPanel[0].setLayout(null);
		
		lblCash = new JLabel[3];
		lblCash[0] = new JLabel("���� �ݾ�");
		lblCash[0].setBounds(25,24,100,24);
		belowPanel[0].add(lblCash[0]);
		
		lblCash[1] = new JLabel("���� �ݾ�");
		lblCash[1].setBounds(25,72,100,24);
		belowPanel[0].add(lblCash[1]);
		
		lblCash[2] = new JLabel("��       ��");
		lblCash[2].setBounds(25,120,100,24);
		belowPanel[0].add(lblCash[2]);
		
		txfCash = new JTextField[3];
		txfCash[0] = new JTextField();
		txfCash[0].setBounds(125,24,175,24);
		belowPanel[0].add(txfCash[0]);
		
		txfCash[1] = new JTextField();
		txfCash[1].setBounds(125,72,175,24);
		belowPanel[0].add(txfCash[1]);
		
		txfCash[2] = new JTextField();
		txfCash[2].setBounds(125,120,175,24);
		belowPanel[0].add(txfCash[2]);
		
		btnCash=new JButton[2];
		btnCash[0] = new JButton("ī�����");
		btnCash[0].setBounds(25,168, 125, 24);
		belowPanel[0].add(btnCash[0]);
		
		btnCash[1] = new JButton("���ݰ���");
		btnCash[1].setBounds(200,168,125,24);
		belowPanel[0].add(btnCash[1]);
		
		//-----------------------------------------------------------------------------
		//	#�Ϻ� �г�2 ������ �г� ����
		//----------------------------------------------------------------------------
		belowPanel[1].setBounds(375,0,375,210);
		belowPanel[1].setBackground(Color.white);
		belowPanel[1].setLayout(new GridLayout(4,3));
		for(int i=0; i<12; i++) {	//pad[i].setSize(125, 40);
			if(i==9) { pad[i] = new JButton("00");}
			else if(i==10) { pad[i] = new JButton("0");}
			else if(i==11) { pad[i] = new JButton("C");}
			else { pad[i] = new JButton(""+(i+1));}
			belowPanel[1].add(pad[i]);
		}
		
		//-----------------------------------------------------------------------------
		//	#�Ϻ� �г�3 �̹��� �г� ����
		//----------------------------------------------------------------------------
		belowPanel[2].setBounds(750,0,300,240);
		belowPanel[2].setBackground(Color.black);
		
		this.getContentPane().add(backgroundPanel[0], BorderLayout.WEST); 	// �г�1�� ���ʿ� ���δ�
		this.getContentPane().add(backgroundPanel[1], BorderLayout.CENTER); // �г�2�� ����� ���δ�
		this.getContentPane().add(backgroundPanel[2], BorderLayout.SOUTH); 	// �г�3�� �Ʒ��ʿ� ���δ�
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setResizable(false);
		chickenFrame=this;
	}
//=====================================================================================
//	#get/set�޼���
//=====================================================================================	
	public static ChickenMain getInstance()
	{
		if(chickenFrame == null) chickenFrame = new ChickenMain();
		return chickenFrame;
	}
//=====================================================================================
//	#�Լ�
//=====================================================================================	
	
//=====================================================================================
//	#�׼��̺�Ʈ �ڵ鸵 & ��Ÿ Ŭ����
//=====================================================================================
	@Override
	public void actionPerformed(ActionEvent e) {
		EventAction obj = (EventAction) e.getSource();
		
		obj.doAction();

	}
	
	interface EventAction{
		public void doAction();	//�߻�޼��� ����
	}
	
	public class ItemManageBtn extends JButton implements EventAction{
		public ItemManageBtn(String s){
			this.setText(s);
		}
		public void doAction() {
			System.out.println("�ȳ�");
		}
	}
	
	public class SalesManageBtn extends JButton implements EventAction{
		public SalesManageBtn(String s){
			this.setText(s);
		}
		public void doAction() {
			System.out.println("�ݰ���");
		}
	}
	
	public class TableEditBtn extends JButton implements EventAction{
		public TableEditBtn(String s){
			this.setText(s);
		}
		public void doAction() {
			System.out.println("����");
		}
	}
	public class OptionBtn extends JButton implements EventAction{
		public OptionBtn(String s){
			this.setText(s);
		}
		public void doAction() {
			System.out.println("����");
		}
	}
	public class TableBtn extends JButton implements EventAction{
		
		public void doAction() {
			JDialog dialog = new JDialog(chickenFrame,"��꼭");
			JButton payBtn = new JButton("����");
			JButton okBtn = new JButton("���");
			JButton[] upBtn = new JButton[3];
			JButton[] downBtn = new JButton[3];
			JTextField[] amount = new JTextField[3];
				
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
			dialog.setSize(300,300);
			dialog.setLocation(300,300);
			dialog.setVisible(true);
		}
	}
	
//=====================================================================================
//#���콺�̺�Ʈ �ڵ鸵
//=====================================================================================
	public class DragAndDrop implements MouseListener{

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
			
		}
	}
}

