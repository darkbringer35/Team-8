package jewan;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;


public class ChickenDialog extends JDialog implements ActionListener{
	//버튼에 따른 UI를 불러오기 위한 mode변수
	private int mode;
	
	//화면 구성 전환을 위한 카드 레이아웃
	private Container tab;
	private Container menuTab;
	private CardLayout cardLayout;
	private CardLayout cardLayout2;
	
	//메뉴버튼에 모두 붙여야되는 메뉴바패널과 버튼들
	private JPanel menuPanel; 	// 메뉴바 패널로 생성
	
	private MenuBtn itemBtn; 	//재고관리 버튼
	private MenuBtn salesBtn;	//매출관리 버튼
	private MenuBtn optionBtn; 	//환경설정 버튼
	
	//Dialog의 메뉴패널 밑에 붙일 각 메뉴 패널들 (카드레이아웃을 이용해 이 패널들 중 하나가 선택됨)
	private JPanel itemPanel;	 	//재고 관리 패널
	private JPanel salesPanel;	 	//매출 관리 패널
	private JPanel optionPanel;		//환경 설정 패널
	/*테이블 UI*/JPanel tablePanel;
	
	//테이블UI용
	private JPanel boxSpace;
	private int selectedTid;
	private ArrayList<BoxLabel> boxUI;
	private JLabel lblTable;
	private JTextField tfTotalPrice;
	private TableBtn selectedTable;
	private ApplyBtn applyBtn;
	private CashBtn cashBtn;
	private InsertBtn insertBtn;
	private DeleteBtn deleteBtn;

	private JTextField txfOption;
	
	//사용될 테마 컬러들 정의
	private Color color1 = new Color(255, 218, 175);
	private Color color2 = new Color(255, 144, 0);
	private Color color3 = new Color(255, 218, 175);
	
	//생성자
	public ChickenDialog() {
		AppManager.getInstance().setChickenDialog(this);
		
		//Dialog 위치 설정
		this.setLocation(500,300);
		this.setResizable(false);
		
		//Dialog의 메뉴패널 밑에 붙일 각 메뉴 패널들 (카드레이아웃을 이용해 이 패널들 중 하나가 선택됨)
		itemPanel = new JPanel(); //재고 관리 패널
		salesPanel = new JPanel(); //매출 관리 패널
		optionPanel = new JPanel(); //환경 설정 패널
		/*테이블 UI*/tablePanel = new JPanel();
		
		//메뉴버튼에 모두 붙여야되는 메뉴바패널과 버튼들
		menuPanel = new JPanel(); 				//메뉴바 패널로 생성
		itemBtn = new MenuBtn("재고관리",1); 		//재고관리 버튼
		salesBtn = new MenuBtn("매출관리",2); 		//매출관리 버튼
		optionBtn = new MenuBtn("환경설정",3);	 	//환경설정 버튼
		menuPanel.setBackground(Color.WHITE);
		
		//액션리스너와 연동
		itemBtn.addActionListener(this);
		salesBtn.addActionListener(this);
		optionBtn.addActionListener(this);
		
		//카드 레이아웃 세팅
		tab = new JPanel();
		cardLayout = new CardLayout();
		tab.setLayout(cardLayout);
		
		//각 메뉴 버튼 메뉴바 패널에 부착
		menuPanel.add(itemBtn);
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);
		
		//창 변환을 위한 CardLayout패널 tab에 itemPanel 추가하기
		tab.add(itemPanel,"item");
		tab.add(salesPanel,"sales");
		tab.add(optionPanel,"option");
		/*테이블 UI*/tab.add(tablePanel, "table");
		
		cardLayout2 = new CardLayout();
		lblTable=new JLabel("테이블 주문서");
		lblTable.setHorizontalAlignment(JLabel.CENTER);
		menuTab = new JPanel();
		menuTab.setLayout(cardLayout2);
		menuTab.add(menuPanel,"menu");
		menuTab.add(lblTable,"table");
		
		//makeUI
		ItemManagerUI();
		salesManagerUI();
		OptionUI();
		TableUI();
		
	}
	
	
	//-----------메소드---------------
	
	public void setMode(int m) {
		mode=m;
		refreshUI();
	}
	
	//테이블UI용
	public void refreshTable(int index) {
		selectedTable=AppManager.getInstance().getTableArray().get(index);
		if(selectedTable.getBoxNum()>0) {
		}
		else {
		}
	}
	public JPanel getTableUI(){
		return boxSpace;
	}
	
	public void refreshUI() {
		//메뉴 버튼들 기본 색상 설정
		itemBtn.setBackground(color1);
		salesBtn.setBackground(color1);
		optionBtn.setBackground(color1);
		
		if(mode==1){ // 1: 재고관리UI
			//재고관리 버튼만 컬러 번경
			itemBtn.setBackground(color2);
			//카드 레이아웃으로 재고관리 창 전환
			setTitle("재고 관리");
			this.setSize(875,525);
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"item");
		}
		else if(mode == 2) { // 2: 매출관리UI
			//매출관리 버튼만 컬러 변경
			salesBtn.setBackground(color2);
			//카드 레이아웃으로 매출관리창으로 전환하기
			setTitle("매출 관리");
			this.setSize(875,525);
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"sales");
		}
		else if(mode == 3) { // 3: 환결성정UI
			//환경설정 버튼만 컬러 변경
			optionBtn.setBackground(color2);
			//카드 레이아웃으로 환경설정창으로 전환하기
			setTitle("환경 설정");
			this.setSize(875,525);
			txfOption.setText(""+AppManager.getInstance().getTimerSet());
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"option");
		}
		else if(mode == 4) { // 4: 테이블 주문서 UI
			this.setTitle("테이블 주문서");
			this.setSize(500,500);
			this.cardLayout.show(this.tab,"table");
			this.cardLayout2.show(this.menuTab,"table");
			refreshTable(AppManager.getInstance().getTid());
		}
		this.setVisible(true);
	}
	
	//재고 관리 창 UI
	public void ItemManagerUI(){
		//창 제목 설정
		setTitle("재고 관리");
		
		//텍스트필드 패널 p1 구성
		JPanel p1 = new JPanel();
		//재고 목록 TextField 구성
		JTextArea ta = new JTextArea(23,40); //ta영역 생성
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ta.append("메뉴번호\t메뉴명\t\t재고\t가격\n"); //ta에 나타낼 항목들
		p1.setLayout(new BorderLayout());
		p1.add(scroll, BorderLayout.CENTER);
		p1.setBackground(Color.white);
	
		//속성 입출력 패널 p2구성
		JPanel p2 = new JPanel(); //p2패널 생성
		JComboBox cb = new JComboBox(); //메뉴번호 콤보박스
		JTextField t1 = new JTextField(); //메뉴명 텍스트필드
		JTextField t2 = new JTextField(); //재고 텍스트필드
		JTextField t3 = new JTextField(); //가격 텍스트필드
		JLabel l1 = new JLabel("메뉴번호");//메뉴번호 라벨
		JLabel l2 = new JLabel("메뉴명");//메뉴명 라벨
		JLabel l3 = new JLabel("재고");//재고 라벨
		JLabel l4 = new JLabel("가격");//가격 라벨
		//패널 p2 레이아웃 및 위젯 설정
		p2.setLayout(null); //p2패널 레이아웃 없음
		cb.setBounds(120, 50, 230, 50);
		t1.setBounds(120, 150, 230, 50);
		t2.setBounds(120, 250, 230, 50);
		t3.setBounds(120, 350, 230, 50);
		l1.setBounds(30,50,100,50);
		l2.setBounds(30,150,100,50);
		l3.setBounds(30,250,100,50);
		l4.setBounds(30,350,100,50);
		p2.add(cb); //p2패널에 부착
		p2.add(t1);
		p2.add(t2);
		p2.add(t3);
		p2.add(l1);
		p2.add(l2);
		p2.add(l3);
		p2.add(l4);
		p2.setBackground(Color.white);
		
		//버튼 패널 p3 구성
		JPanel p3 = new JPanel(); //p3패널 생성
		JButton b1 = new JButton("등록"); //등록 버튼
		JButton b2 = new JButton("조회"); //조회 버튼
		JButton b3 = new JButton("삭제"); //삭제 버튼
		p3.setLayout(new FlowLayout()); //p3패널 플로우 레이아웃
		p3.add(b1); //p3패널에 부착
		p3.add(b2);
		p3.add(b3);
		//버튼 컬러 예쁘게 변경
		p3.setBackground(Color.white);
		b1.setBackground(color1); 
		b2.setBackground(color1);
		b3.setBackground(color1);

		//item Panel 레이아웃 설정 및 부속 패널들 붙이기
		itemPanel.setLayout(new BorderLayout());
		itemPanel.add(p2, BorderLayout.CENTER); //p2패널 중간
		itemPanel.add(p3, BorderLayout.PAGE_END); //p3패널 맨아래
		itemPanel.add(p1, BorderLayout.LINE_END); //scroll패널 오른쪽
	
		//dialog 레이아웃 및 위젯 설정
		this.add(menuTab,BorderLayout.PAGE_START);
		//this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		
		
		
		/*
		-----DB & Controller 파트 : 데이터를 변경하면 콤보박스 데이터 갱신
		
		datas = dao.getAll();
		cb.setModel(new DefaultComboBoxModel(dao.getItems()));
		
		itemBtn.addActionListener(this); //각 버튼 액션 리스너
		salesBtn.addActionListener(this);
		optionBtn.addActionListener(this); 
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		
		*/
	}
	
	//매출 관리창 UI
	public void salesManagerUI(){
		//창 제목
		setTitle("매출 관리");
		
		//날짜 검색 패널 p1 생성
		JPanel p1 = new JPanel();
		JLabel lb = new JLabel("날짜 검색: ");
		JTextField tf = new JTextField(15);
		JButton btn = new JButton("검색");

		//p1레이아웃
		p1.setLayout(new FlowLayout());
		p1.add(lb);
		p1.add(tf);
		p1.add(btn);
		//p1패널 배경색상 화이트로 설정
		p1.setBackground(Color.white);
		//검색버튼 색상 color1로 변경
		btn.setBackground(color1);
		
		//그래프와 매출차트가 붙어있는 p2패널 생성
		JPanel p2 = new JPanel();
		
		//매출 차트 생성
		//SalesChart 객체 생성
    	SalesChart salesChart = new SalesChart();
    	//JFeeChart 객체 생성
    	JFreeChart chart = salesChart.getChart();
		//매출차트 붙일 차트패널 생성
		ChartPanel gp = new ChartPanel(chart);
		
		//매출 목록 list패널 생성
		JPanel list = new JPanel();
		JTextArea ta = new JTextArea(20,35); //ta영역 생성
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ta.append("날짜\t매출액\t\t판매량\n"); //ta에 나타낼 항목들
		list.add(scroll);
		
		//p2레이아웃
		p2.setLayout(new GridLayout(1,2)); //그리드 레이아웃 설정
		p2.add(gp);
		p2.add(list);
		
		//p2패널 배경색상 화이트로 설정
		chart.setBackgroundPaint(Color.WHITE);
		list.setBackground(Color.white);
		
		//salesPanel에 부속 패널 붙이기
		salesPanel.setLayout(new BorderLayout());
		salesPanel.add(p1,BorderLayout.PAGE_START);
		salesPanel.add(p2,BorderLayout.CENTER);
		
		//dialog 레이아웃 및 위젯 설정
		this.add(menuTab,BorderLayout.PAGE_START);
		//this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		
	}
	
	//환경 설정창 UI
	public void OptionUI() {
		//창 제목
		setTitle("환경 설정");

		//라벨 텍스트필드 버튼 생성
		JLabel lblOption = new JLabel("배달 제한 시간을 설정해주세요."); 
		txfOption = new JTextField();
		JButton btnOption = new JButton("확인");
		
		//위젯 설정
		optionPanel.setLayout(null);
		lblOption.setBounds(340,50,300,50);
		txfOption.setBounds(240,150,375,50);
		btnOption.setBounds(320,250,210,50);
				
		optionPanel.add(lblOption);
		optionPanel.add(txfOption);
		optionPanel.add(btnOption);
		
		//환경설정패널 색상 화이트로 설정
		optionPanel.setBackground(Color.white);
		//확인 버튼 색상 color1로 설정
		btnOption.setBackground(color1);

		
		txfOption.setHorizontalAlignment(JTextField.RIGHT);
		
		btnOption.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AppManager.getInstance().setTimerSet(Integer.parseInt(txfOption.getText()));
				AppManager.getInstance().getChickenDialog().setVisible(false);
			}
			
		});
		
		//dialog 레이아웃 및 위젯 설정
		this.add(menuTab,BorderLayout.PAGE_START);
		//this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		
	}
	
	//테이블 UI
	public void TableUI() {
		
		this.setTitle("테이블 주문서");
		tablePanel.setLayout(new BorderLayout());
		
		boxSpace = new JPanel();
		boxSpace.setBackground(Color.white);
		boxSpace.setLayout(null);
		
		boxUI= new ArrayList<BoxLabel>();
		boxUI.add(new BoxLabel(0));
		
		for(BoxLabel bp : boxUI)
			boxSpace.add(bp);
		
		applyBtn = new ApplyBtn("확인");
		cashBtn = new CashBtn("결제");
		insertBtn = new InsertBtn("추가");
		deleteBtn = new DeleteBtn("삭제");
		
		
		JScrollPane boxSpaceScroll = new JScrollPane(boxSpace);
		boxSpaceScroll.setBounds(0,0,400,100);
		boxSpaceScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		boxSpaceScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel btnSpace =new JPanel();
		btnSpace.setSize(400,50);
		
		btnSpace.add(applyBtn);
		btnSpace.add(cashBtn);
		btnSpace.add(insertBtn);
		btnSpace.add(deleteBtn);
		
		applyBtn.addActionListener(this);
		cashBtn.addActionListener(this);
		insertBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		
		JLabel lblColumn = new JLabel("	인덱스                          품목명                                             갯수                          가격");
		lblColumn.setBounds(2,1,450,25);
		boxSpace.add(lblColumn);
		
		JPanel pricePanel = new JPanel();
		pricePanel.setSize(400,50);
		
		JLabel lblPrice = new JLabel("총 가격");
		
		tfTotalPrice = new JTextField(6);
		tfTotalPrice.setText("100");
		tfTotalPrice.setHorizontalAlignment(JTextField.RIGHT);
		tfTotalPrice.setEditable(false);
		tfTotalPrice.setBackground(Color.WHITE);
		
		pricePanel.add(lblPrice);
		
		pricePanel.add(tfTotalPrice);
		
		insertBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		
		tablePanel.add(boxSpaceScroll,BorderLayout.CENTER);
		tablePanel.add(pricePanel, BorderLayout.PAGE_START);
		tablePanel.add(btnSpace,BorderLayout.PAGE_END);
		
		this.add(menuTab,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		//배경 색상 설정
		menuTab.setBackground(color1);
		btnSpace.setBackground(color1);
		lblPrice.setBackground(color1);
		pricePanel.setBackground(color1);
		applyBtn.setBackground(color2);
		cashBtn.setBackground(color2);
		insertBtn.setBackground(color2);
		deleteBtn.setBackground(color2);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		EventAction obj= (EventAction) e.getSource();
		
		obj.doAction();
	}
	
	public class BoxLabel extends JLabel{
		private int index;
		private JComboBox menu; 
		private AddBtn addBtn;
		private MinusBtn minusBtn;
		private JTextField amount;
		private JLabel lblIndex;
		private JTextField price;
			
		public BoxLabel(int index){
			this.index=index;
			
			this.setBounds(2,1+(index+1)*30,450,25);
			this.setLayout(null);
			
			menu= new JComboBox();
			addBtn = new AddBtn("+",index);
			minusBtn = new MinusBtn("-",index);
			amount = new JTextField(5);
			amount.setHorizontalAlignment(JTextField.RIGHT);
			amount.setText("1");
			price = new JTextField(5);
			price.setText("0");
			price.setHorizontalAlignment(JTextField.RIGHT);
			price.setEditable(false);
			price.setBackground(Color.WHITE);
			lblIndex = new JLabel("#"+index);
			lblIndex.setHorizontalAlignment(JLabel.CENTER);
			
			lblIndex.setBounds(0,0,30,25);
			menu.setBounds(30,0, 205,25);
			minusBtn.setBounds(236,0,50,25);
			amount.setBounds(287,0,40,25);
			addBtn.setBounds(327,0,50,25);
			price.setBounds(378,0,70,25);
			
			addBtn.addActionListener(AppManager.getInstance().getChickenDialog());
			minusBtn.addActionListener(AppManager.getInstance().getChickenDialog());
			
			this.add(lblIndex);
			this.add(menu);
			this.add(minusBtn);
			this.add(amount);
			this.add(addBtn);
			this.add(price);
			
		}
		
		public int getAmount(){
			return Integer.parseInt(amount.getText());
		}
		public void setAmount(int amount) {
			this.amount.setText(""+amount);
		}
		public int getSelectedBox(){
			return menu.getSelectedIndex();
		}
		public void refreshBoxPanel() {
			menu.repaint();
		}
	}
	
	public class AddBtn extends JButton implements EventAction{
		private int index;
		
		public AddBtn(String s, int index){
			this.setText(s);
			this.index = index;
		}
		@Override
		public void doAction() {
			
		}
		
	}
	public class MinusBtn extends JButton implements EventAction{
		private int index;
		
		public MinusBtn(String s, int index){
			this.setText(s);
			this.index = index;
		}
		@Override
		public void doAction() {
			
		}
		
	}
	public class ApplyBtn extends JButton implements EventAction{
		public ApplyBtn(String s){
			this.setText(s);
		}

		@Override
		public void doAction() {
			selectedTable.timerStart();
			AppManager.getInstance().getChickenDialog().setVisible(false);
		}
		
	}
	public class CashBtn extends JButton implements EventAction{
		public CashBtn(String s){
			this.setText(s);
		}

		@Override
		public void doAction() {
			JTextField[] txf;
			int x = Integer.parseInt(tfTotalPrice.getText());
			txf=AppManager.getInstance().getChickenMain().getTxfCash();
			txf[0].setText(""+x);
			x=x-Integer.parseInt(txf[1].getText());
			txf[2].setText(""+(-x));
			AppManager.getInstance().getChickenDialog().setVisible(false);
		}
		
	}
	public class InsertBtn extends JButton implements EventAction{
		public InsertBtn(String s){
			this.setText(s);
		}
		@Override
		public void doAction() {
			int index = boxUI.size();
			if(index<10)
			{
				BoxLabel bp = new BoxLabel(index);
				boxUI.add(bp);
				AppManager.getInstance().getChickenDialog().getTableUI().add(bp);
				bp.updateUI();
			}
		}
		
	}
	public class DeleteBtn extends JButton implements EventAction{
		public DeleteBtn(String s){
			this.setText(s);
		}
		@Override
		public void doAction() {
			
			int index = boxUI.size()-1;
			if(index>0) {
				BoxLabel bp = boxUI.get(index);
				AppManager.getInstance().getChickenDialog().getTableUI().remove(bp);
				AppManager.getInstance().getChickenDialog().getTableUI().repaint();
				boxUI.remove(bp);
			}
		}
	}
	public class MenuBtn extends JButton implements EventAction{
		int index;
		public MenuBtn(String s, int i){
			this.setText(s);
			index = i;
		}
		@Override
		public void doAction() {
			setMode(index);
		}
	}
}