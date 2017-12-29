package jewan;


import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

public class ChickenMain extends JFrame implements ActionListener {	// JFrame을 상속받는다
	
	private JPanel backgroundPanel[];  
	private JPanel belowPanel[];
	private JButton[] btnMenu;
	private JButton[] btnCash;
	private JButton[] pad;
	private JButton[] btnTableEdit;
	private Vector<TableBtn> table;
	private int tableNum;
	private final int tableMax=12;
	
	private JLabel[] lblCash;
	
	private JTextField[] txfCash;
	
	protected int frameMode;
//=====================================================================================
//	#생성자
//=====================================================================================	
	public ChickenMain() { // 프레임 선언
		AppManager.getInstance().setChickenMain(this);
		this.setLayout(new BorderLayout()); // BorderLayout으로 레이아웃 설정
		setSize(1050, 720); // 실행창 크기 설정
		setLocation(300, 100); // 실행창 위치설정
		setTitle("치킨"); // 프레임 타이틀 설정
		this.setBackground(Color.white);
		
		table = new Vector<TableBtn>();
		frameMode=0;
		
		backgroundPanel = new JPanel[3];
		for(int i=0; i<3;i++)
			backgroundPanel[i] = new JPanel();
		
		//-----------------------------------------------------------------------------
		//	#패널1 설정
		//-----------------------------------------------------------------------------
		backgroundPanel[0].setLayout(null); // 배경패널1의 레이아웃을 null로 설정
		backgroundPanel[0].setPreferredSize(new Dimension(200, 500));
		
		int menuBtnNum=4, menuWidth = 200,menuHeight = 130;
		
		btnMenu=new JButton[menuBtnNum];
		btnMenu[0] = new ItemManageBtn("재고관리");  
		btnMenu[1] = new SalesManageBtn("매출관리"); 
		btnMenu[2] = new TableEditBtn("테이블 편집"); 
		btnMenu[3] = new OptionBtn("환경설정");
		
		for(int i=0;i<menuBtnNum;i++)
		{
			btnMenu[i].setBounds(0, i*menuHeight, menuWidth, menuHeight);//메뉴버튼들의 위치 설정
			btnMenu[i].addActionListener(this);
			backgroundPanel[0].add(btnMenu[i]);
		}
		
		//-----------------------------------------------------------------------------
		//	#패널2 설정
		//-----------------------------------------------------------------------------
		backgroundPanel[1].setLayout(null); // 배경패널2의 레이아웃을 null로 설정
		backgroundPanel[1].setPreferredSize(new Dimension(200, 240));
		backgroundPanel[1].setBackground(Color.white);
		backgroundPanel[1].setBorder(new TitledBorder(new LineBorder(Color.black)));
		
		btnTableEdit= new JButton[2];
		btnTableEdit[0] = new TableAdd("+");
		btnTableEdit[0].setBounds(700,10, 45,35);
		btnTableEdit[1] = new TableAdd("-");
		btnTableEdit[1].setBounds(750,10, 45,35);
		for(int i=0;i<2;i++) {
			backgroundPanel[1].add(btnTableEdit[i]);
			btnTableEdit[i].setVisible(false);
			btnTableEdit[i].addActionListener(this);
		}
		tableNum = 0;
		
		//-----------------------------------------------------------------------------
		//	#패널3 설정
		//-----------------------------------------------------------------------------
		backgroundPanel[2].setLayout(null); // 배경패널3의 레이아웃을 null로 설정
		backgroundPanel[2].setPreferredSize(new Dimension(1000, 210));
		backgroundPanel[2].setBackground(Color.white);
		
		belowPanel = new JPanel[3];
		for(int i = 0;i<3;i++)
		{
			belowPanel[i] = new JPanel();
			backgroundPanel[2].add(belowPanel[i]);
		}
		
		//-----------------------------------------------------------------------------
		//	#하부 패널1 계산서 패널 설정
		//----------------------------------------------------------------------------
		belowPanel[0].setBounds(0, 0, 375, 240);
		belowPanel[0].setBackground(Color.white);
		belowPanel[0].setLayout(null);
		
		lblCash = new JLabel[3];
		lblCash[0] = new JLabel("결제 금액");
		lblCash[0].setBounds(25,24,100,24);
		belowPanel[0].add(lblCash[0]);
		
		lblCash[1] = new JLabel("받은 금액");
		lblCash[1].setBounds(25,72,100,24);
		belowPanel[0].add(lblCash[1]);
		
		lblCash[2] = new JLabel("잔       돈");
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
		btnCash[0] = new JButton("카드결제");
		btnCash[0].setBounds(25,168, 125, 24);
		belowPanel[0].add(btnCash[0]);
		
		btnCash[1] = new JButton("현금결제");
		btnCash[1].setBounds(200,168,125,24);
		belowPanel[0].add(btnCash[1]);
		
		//-----------------------------------------------------------------------------
		//	#하부 패널2 숫자판 패널 설정
		//----------------------------------------------------------------------------
		belowPanel[1].setBounds(375,0,375,210);
		belowPanel[1].setBackground(Color.white);
		belowPanel[1].setLayout(new GridLayout(4,3));
		pad= new JButton[12];
		for(int i=0; i<12; i++) {	//pad[i].setSize(125, 40);
			if(i==9) { pad[i] = new JButton("00");}
			else if(i==10) { pad[i] = new JButton("0");}
			else if(i==11) { pad[i] = new JButton("C");}
			else { pad[i] = new JButton(""+(i+1));}
			belowPanel[1].add(pad[i]);
		}
		
		//-----------------------------------------------------------------------------
		//	#하부 패널3 이미지 패널 설정
		//----------------------------------------------------------------------------
		belowPanel[2].setBounds(750,0,300,240);
		belowPanel[2].setBackground(Color.black);
		
		this.getContentPane().add(backgroundPanel[0], BorderLayout.WEST); 	// 패널1을 왼쪽에 붙인다
		this.getContentPane().add(backgroundPanel[1], BorderLayout.CENTER); // 패널2를 가운데에 붙인다
		this.getContentPane().add(backgroundPanel[2], BorderLayout.SOUTH); 	// 패널3을 아래쪽에 붙인다
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setResizable(false);
	}
//=====================================================================================
//	#get/set메서드
//=====================================================================================	
//=====================================================================================
//	#함수
//=====================================================================================	
	
//=====================================================================================
//	#액션이벤트 핸들링 & 기타 클래스
//=====================================================================================
	@Override
	public void actionPerformed(ActionEvent e) {
		EventAction obj = (EventAction) e.getSource();
		
		obj.doAction();
	}
	
	public class ItemManageBtn extends JButton implements EventAction{
		public ItemManageBtn(String s){
			this.setText(s);
		}
		public void doAction() {
			AppManager.getInstance().getChickenDialog().setMode(1);
		}
	}
	
	public class SalesManageBtn extends JButton implements EventAction{
		public SalesManageBtn(String s){
			this.setText(s);
		}
		public void doAction() {
			AppManager.getInstance().getChickenDialog().setMode(2);
		}
	}
	
	public class TableEditBtn extends JButton implements EventAction{
		public TableEditBtn(String s){
			this.setText(s);
		}
		public void doAction() {
			if(frameMode == 0) {
				frameMode=1;
				for(int i=0;i<2;i++)
					btnTableEdit[i].setVisible(true);
			}
			else if(frameMode ==1) {
				frameMode=0;
				for(int i=0;i<2;i++)
					btnTableEdit[i].setVisible(false);
			}
		}
	}
	public class OptionBtn extends JButton implements EventAction{
		public OptionBtn(String s){
			this.setText(s);
		}
		public void doAction() {
			AppManager.getInstance().getChickenDialog().setMode(3);
		}
	}
	public class TableAdd extends JButton implements EventAction{
		public TableAdd(String s){
			this.setText(s);
		}

		@Override
		public void doAction() {
			
			if(frameMode==1)
			{
				TableBtn t=new TableBtn();
				t.addActionListener(AppManager.getInstance().getChickenMain());
				table.add(t);
				backgroundPanel[1].add(t);
				t.setVisible(true);
			}
		}
	}
	public class TableDelete extends JButton implements EventAction{
		public TableDelete(String s){
			this.setText(s);
		}

		@Override
		public void doAction() {
			// TODO Auto-generated method stub
			
		}
	}
	
}