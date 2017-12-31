package jewan;




import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.jfree.chart.*;

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
	
	private JButton[] receiptBtn;
	private JTextField txfOption;
	
	private JComboBox itemCB;
	private JTextField[] itemTXT;
	
	//생성자
	public ChickenDialog() {
		AppManager.getInstance().setChickenDialog(this);
		
		//Dialog 사이즈 설정
		this.setLocation(500,300);
		this.setResizable(false);
		
		//Dialog의 메뉴패널 밑에 붙일 각 메뉴 패널들 (카드레이아웃을 이용해 이 패널들 중 하나가 선택됨)
		itemPanel = new JPanel(); //재고 관리 패널
		salesPanel = new JPanel(); //매출 관리 패널
		optionPanel = new JPanel(); //환경 설정 패널
		tablePanel = new JPanel();
		
		//메뉴버튼에 모두 붙여야되는 메뉴바패널과 버튼들
		menuPanel = new JPanel(); 				//메뉴바 패널로 생성
		itemBtn = new MenuBtn("재고관리",1); 		//재고관리 버튼
		salesBtn = new MenuBtn("매출관리",2); 		//매출관리 버튼
		optionBtn = new MenuBtn("환경설정",3);	 	//환경설정 버튼
		
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
		tab.add(tablePanel, "table");
		
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

//=====================================================================================
//	#get/set메서드
//=====================================================================================	
	public void setMode(int m) {
		mode=m;
		refreshUI();
	}
	public JPanel getTableUI(){
		return boxSpace;
	}
//=====================================================================================
//	#함수
//=====================================================================================	
		
	//테이블UI용
	public void refreshTable(int index) {
		
		
		for(int i = boxUI.size(); i>0 ;i--)
			receiptBtn[3].doClick();
		
		selectedTable=AppManager.getInstance().getTableArray().get(index);
		ArrayList<ReceiptItem> info = selectedTable.getTInfo();
		
		for(int i=0; i<selectedTable.getBoxNum()-1;i++) {
			receiptBtn[2].doClick();
		}
		
		if(info!=null) {
			for(int i = 0; i<boxUI.size() ;i++) {
				System.out.println(""+info.get(i).getItemIndex());
				boxUI.get(i).selectBox(info.get(i).getItemIndex());
				boxUI.get(i).setAmount(info.get(i).getItemAmount());
			}
		}
		
		if(info==null) {
			boxUI.get(0).selectBox(0);
			boxUI.get(0).setAmount(0);
		}
		boxUI.get(0).refreshBox();
	}
	
	public void refreshUI() {
		if(mode==1){ // 1: 재고관리UI
			//카드 레이아웃으로 재고관리 창 전환
			setTitle("재고 관리");
			this.setSize(875,525);
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"item");
		}
		else if(mode == 2) { // 2: 매출관리UI
			//카드 레이아웃으로 매출관리창으로 전환하기
			setTitle("매출 관리");
			this.setSize(875,525);
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"sales");
		}
		else if(mode == 3) { // 3: 환결성정UI
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
	
	public String getToday() {
		Date d =new Date();
		return (d.getYear()+1900)+"-"+(d.getMonth()+1)+"-"+d.getDate();
	}

//=====================================================================================
//	#UI구성 함수
//=====================================================================================	
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
				ta.setBackground(Color.white);
				
				p1.setLayout(new BorderLayout());
				p1.add(scroll, BorderLayout.CENTER);
			
				//속성 입출력 패널 p2구성
				JPanel p2 = new JPanel(); 	//p2패널 생성
				p2.setLayout(null); 		//p2패널 레이아웃 없음
				
				itemCB = new JComboBox(); //메뉴번호 콤보박스
				itemCB.setBounds(120, 50, 230, 50);
				p2.add(itemCB);
				
				itemTXT = new JTextField[3];	//메뉴명,재고,가격 텍스트필드
				for(int i=0;i<3;i++) {
					itemTXT[i] = new JTextField();
					itemTXT[i].setBounds(120,150+100*i,230,50);
					p2.add(itemTXT[i]);
				}
				
				JLabel[] lblItem = new JLabel[4];
				lblItem[0] = new JLabel("메뉴번호");//메뉴번호 라벨
				lblItem[1] = new JLabel("메뉴명");//메뉴명 라벨
				lblItem[2] = new JLabel("재고");//재고 라벨
				lblItem[3] = new JLabel("가격");//가격 라벨
				for(int i =0; i < 4 ; i++) {
					lblItem[i].setBounds(30, 50+100*i, 100, 50);
					p2.add(lblItem[i]);
				}
				
				//버튼 패널 p3 구성
				JPanel p3 = new JPanel(); //p3패널 생성
				p3.setLayout(new FlowLayout()); //p3패널 플로우 레이아웃
				
				JButton[] btnItem = new JButton[3]; 
				btnItem[0] = new RegisterBtn("등록"); //등록 버튼
				btnItem[1] = new InquiryBtn("조회"); //조회 버튼
				btnItem[2] = new DelItemBtn("삭제"); //삭제 버튼
				
				for(int i = 0; i < 3; i++) {
					p3.add(btnItem[i]);
					btnItem[i].addActionListener(this);
				}
				
				

				//item Panel 레이아웃 설정 및 부속 패널들 붙이기
				itemPanel.setLayout(new BorderLayout());
				itemPanel.add(p2, BorderLayout.CENTER); //p2패널 중간
				itemPanel.add(p3, BorderLayout.PAGE_END); //p3패널 맨아래
				itemPanel.add(p1, BorderLayout.LINE_END); //scroll패널 오른쪽
			
				//dialog 레이아웃 및 위젯 설정
				this.add(menuTab,BorderLayout.PAGE_START);
				//this.add(menuPanel,BorderLayout.PAGE_START);
				this.add(tab, BorderLayout.CENTER);
		
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
		lblOption.setBounds(340,150,300,50);
		txfOption.setBounds(240,250,375,50);
		btnOption.setBounds(320,350,210,50);
				
		optionPanel.add(lblOption);
		optionPanel.add(txfOption);
		optionPanel.add(btnOption);
		
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
		
		receiptBtn = new JButton[5];
		
		receiptBtn[0] = new ApplyBtn("확인");
		receiptBtn[1] = new CashBtn("결제");
		receiptBtn[2] = new InsertBtn("추가");
		receiptBtn[3] = new DeleteBtn("삭제");
		receiptBtn[4] = new ServingBtn("서빙");
		
		
		JScrollPane boxSpaceScroll = new JScrollPane(boxSpace);
		boxSpaceScroll.setBounds(0,0,400,100);
		boxSpaceScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		boxSpaceScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel btnSpace =new JPanel();
		btnSpace.setSize(400,50);
		
		for(int i=0;i<5;i++) {
			btnSpace.add(receiptBtn[i]); 
			receiptBtn[i].addActionListener(this);
		}
		
		JLabel lblColumn = new JLabel("	인덱스                          품목명                                             갯수                          가격");
		lblColumn.setBounds(2,1,450,25);
		boxSpace.add(lblColumn);
		
		JPanel pricePanel = new JPanel();
		pricePanel.setSize(400,50);
		
		JLabel lblPrice = new JLabel("총 가격");
		
		tfTotalPrice = new JTextField(6);
		tfTotalPrice.setText("0");
		tfTotalPrice.setHorizontalAlignment(JTextField.RIGHT);
		tfTotalPrice.setEditable(false);
		tfTotalPrice.setBackground(Color.WHITE);
		
		
		pricePanel.add(lblPrice);
		
		pricePanel.add(tfTotalPrice);
		
		tablePanel.add(boxSpaceScroll,BorderLayout.CENTER);
		tablePanel.add(pricePanel, BorderLayout.PAGE_START);
		tablePanel.add(btnSpace,BorderLayout.PAGE_END);
		
		this.add(menuTab,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
	}

//=====================================================================================
//	#액션이벤트 핸들링 & 기타 클래스
//=====================================================================================
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
			
			String[] str = new String[10];
			
			menu= new JComboBox(str);
			addBtn = new AddBtn("+",index);
			minusBtn = new MinusBtn("-",index);
			amount = new JTextField(5);
			amount.setHorizontalAlignment(JTextField.RIGHT);
			amount.setText("0");
			amount.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {}
				public void keyReleased(KeyEvent e) {
					boxUI.get(index).refreshBox();
				}
				public void keyTyped(KeyEvent e) {}
			});
			
			price = new JTextField(5);
			price.setText("0");
			price.setHorizontalAlignment(JTextField.RIGHT);
			price.setEditable(false);
			price.setBackground(Color.WHITE);
			lblIndex = new JLabel("#"+index);
			lblIndex.setHorizontalAlignment(JLabel.CENTER);
			
			menu.setModel(new DefaultComboBoxModel(AppManager.getInstance().getDAOManger().getComboIndex()));
			menu.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					refreshBox();
						
				}
				
			});
			
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
		public int getPrice() {
			return Integer.parseInt(price.getText());
		}
		public int getSelectedBox(){
			return menu.getSelectedIndex();
		}
		public void selectBox(int index){
			menu.setSelectedItem(index);
		}
		public void refreshBox() {
			int pr=0;
			price.setText(""+(Integer.parseInt(amount.getText()) * AppManager.getInstance().getDAOManger().getPrice(menu.getSelectedIndex())));
			for(BoxLabel bl : boxUI)
				pr+=bl.getPrice();
				tfTotalPrice.setText(""+pr);
		}
	}
	public class RegisterBtn extends JButton implements EventAction{
		
		public RegisterBtn(String s){
			this.setText(s);
		}
		@Override
		public void doAction() {
			AppManager.getInstance().getDAOManger().addMenu(itemTXT[0].getText());
			
			AppManager.getInstance().getDAOManger().insertList(
					AppManager.getInstance().getDAOManger().getKey()
					, AppManager.getInstance().getChickenDialog().getToday()
					, 0
					, Integer.parseInt(itemTXT[1].getText())
					, Integer.parseInt(itemTXT[2].getText())
					);
			
		}
	}
	public class InquiryBtn extends JButton implements EventAction{
		
		public InquiryBtn(String s){
			this.setText(s);
		}
		@Override
		public void doAction() {
		}
	}
	
	public class DelItemBtn extends JButton implements EventAction{
		
		public DelItemBtn(String s){
			this.setText(s);
		}
		@Override
		public void doAction() {

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
			BoxLabel bl = boxUI.get(index);
			bl.setAmount(bl.getAmount()+1);
			bl.refreshBox();
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
			BoxLabel bl = boxUI.get(index);
			if(bl.getAmount()>0)
				bl.setAmount(bl.getAmount()-1);
			bl.refreshBox();
		}
		
	}
	public class ApplyBtn extends JButton implements EventAction{
		public ApplyBtn(String s){
			this.setText(s);
		}

		@Override
		public void doAction() {
			if(Integer.parseInt(tfTotalPrice.getText()) != 0) {
				selectedTable.setBoxNum(boxUI.size());
				ArrayList<ReceiptItem> info = new ArrayList<ReceiptItem>();
				for(BoxLabel bl : boxUI) {
					ReceiptItem ri = new ReceiptItem();
					ri.setItemIndex(bl.getSelectedBox());
					ri.setItemAmount(bl.getAmount());
					info.add(ri);
				}
				selectedTable.setPrice(Integer.parseInt(tfTotalPrice.getText()));
				selectedTable.setTInfo(info);
				selectedTable.timerStart();
			}
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
	public class ServingBtn extends JButton implements EventAction{
		public ServingBtn(String s){
			this.setText(s);
		}
		@Override
		public void doAction() {
			selectedTable.timerOff();
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