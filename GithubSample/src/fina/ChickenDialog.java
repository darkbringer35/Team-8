package fina;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.jfree.chart.*;

public class ChickenDialog extends JDialog implements ActionListener {
	// 버튼에 따른 UI를 불러오기 위한 mode변수
	private int mode;

	// 화면 구성 전환을 위한 카드 레이아웃
	private Container tab;
	private Container menuTab;
	private CardLayout cardLayout;
	private CardLayout cardLayout2;

	// 메뉴버튼에 모두 붙여야되는 메뉴바패널과 버튼들
	private JPanel menuPanel; // 메뉴바 패널로 생성

	private MenuBtn itemBtn; // 재고관리 버튼
	private MenuBtn salesBtn; // 매출관리 버튼
	private MenuBtn optionBtn; // 환경설정 버튼

	// Dialog의 메뉴패널 밑에 붙일 각 메뉴 패널들 (카드레이아웃을 이용해 이 패널들 중 하나가 선택됨)
	private JPanel itemPanel; // 재고 관리 패널
	private JPanel salesPanel; // 매출 관리 패널
	private JPanel optionPanel; // 환경 설정 패널
	private JPanel tablePanel;

	// 테이블UI용
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
	private JTextArea itemTA;

	// 사용될 테마 컬러들 정의
	private Color color1 = new Color(255, 218, 175);
	private Color color2 = new Color(255, 144, 0);
	private Color color3 = new Color(255, 218, 175);

	private ArrayList<DayList> datas;
	private JTextField searchDayTX;
	private JTextField searchTX;
	private SalesChart salesChart;
	private JFreeChart chart;
	private JTextArea salesTA;
	private SearchBtn btnSearch;
	private ChartPanel gp;
	private JPanel p2_sales;

	// =====================================================================================
	// #생성자
	// =====================================================================================
	public ChickenDialog() {
		AppManager.getInstance().setChickenDialog(this);

		// Dialog 사이즈 설정
		this.setLocation(500, 300);
		this.setResizable(false);

		// 환경설정패널에 이미지 삽입
		ImageIcon optionIcon = new ImageIcon("optionImage0.png");
		Image optionImg = optionIcon.getImage();

		// Dialog의 메뉴패널 밑에 붙일 각 메뉴 패널들 (카드레이아웃을 이용해 이 패널들 중 하나가 선택됨)
		itemPanel = new JPanel(); // 재고 관리 패널
		salesPanel = new JPanel(); // 매출 관리 패널
		optionPanel = new JPanel() { // 환경 설정 패널
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(optionImg, 0, 0, this);
			}
		};

		tablePanel = new JPanel();

		// 메뉴버튼에 모두 붙여야되는 메뉴바패널과 버튼들
		menuPanel = new JPanel(); // 메뉴바 패널로 생성
		itemBtn = new MenuBtn("재고관리", 1); // 재고관리 버튼
		salesBtn = new MenuBtn("매출관리", 2); // 매출관리 버튼
		optionBtn = new MenuBtn("환경설정", 3); // 환경설정 버튼
		menuPanel.setBackground(Color.WHITE);

		// 액션리스너와 연동
		itemBtn.addActionListener(this);
		salesBtn.addActionListener(this);
		optionBtn.addActionListener(this);

		// 카드 레이아웃 세팅
		tab = new JPanel();
		cardLayout = new CardLayout();
		tab.setLayout(cardLayout);

		// 각 메뉴 버튼 메뉴바 패널에 부착
		menuPanel.add(itemBtn);
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);

		// 창 변환을 위한 CardLayout패널 tab에 itemPanel 추가하기
		tab.add(itemPanel, "item");
		tab.add(salesPanel, "sales");
		tab.add(optionPanel, "option");
		tab.add(tablePanel, "table");

		cardLayout2 = new CardLayout();
		lblTable = new JLabel("테이블 주문서");
		lblTable.setHorizontalAlignment(JLabel.CENTER);
		menuTab = new JPanel();
		menuTab.setLayout(cardLayout2);
		menuTab.add(menuPanel, "menu");
		menuTab.add(lblTable, "table");

		// makeUI
		ItemManagerUI();
		salesManagerUI();
		OptionUI();
		TableUI();

		refreshComboBox();

	}

	// =====================================================================================
	// #get/set메서드
	// =====================================================================================
	public void setMode(int m) {
		mode = m;
		refreshUI();
	}

	public JPanel getTableUI() {
		return boxSpace;
	}

	public ChartPanel getGraphPanel() {
		return gp;
	}

	// =====================================================================================
	// #함수
	// =====================================================================================
	public void refreshComboBox() {
		int index = itemCB.getSelectedIndex();
		itemCB.setModel(new DefaultComboBoxModel(AppManager.getInstance().getDAOManger().getComboIndex()));
		itemCB.setSelectedIndex(index);
		
		for (BoxLabel bl : boxUI) {
			index = bl.getSelectedBox();
			bl.getMenuBox().setModel(new DefaultComboBoxModel(AppManager.getInstance().getDAOManger().getComboIndex()));
			bl.selectBox(index);
		}
		if(AppManager.getInstance().getChickenServer() != null)AppManager.getInstance().getChickenServer().msgType(4);
	}

	// 테이블UI용
	public void refreshTable(int index) {

		for (int i = boxUI.size(); i > 0; i--)
			receiptBtn[3].doClick();

		selectedTable = AppManager.getInstance().getTableArray().get(index);
		ArrayList<ReceiptItem> info = selectedTable.getTInfo();

		for (int i = 0; i < selectedTable.getBoxNum() - 1; i++) {
			receiptBtn[2].doClick();
		}

		if (info != null) {
			for (int i = 0; i < boxUI.size(); i++) {
				boxUI.get(i).selectBox(info.get(i).getItemIndex());
				boxUI.get(i).setAmount(info.get(i).getItemAmount());
				boxUI.get(i).refreshBox();
			}
		} else {
			boxUI.get(0).selectBox(0);
			boxUI.get(0).setAmount(0);
		}

		boxUI.get(0).refreshBox();
	}

	public void refreshUI() {
		// 메뉴 버튼들 기본 색상 설정
		itemBtn.setBackground(color1);
		salesBtn.setBackground(color1);
		optionBtn.setBackground(color1);

		if (mode == 1) { // 1: 재고관리UI
			// 재고관리 버튼만 컬러 번경
			itemBtn.setBackground(color2);
			// 카드 레이아웃으로 재고관리 창 전환
			setTitle("재고 관리");
			itemCB.setSelectedIndex(0);
			stockTableData();
			this.setSize(875, 525);
			this.cardLayout2.show(this.menuTab, "menu");
			this.cardLayout.show(this.tab, "item");
		} else if (mode == 2) { // 2: 매출관리UI
			// 매출관리 버튼만 컬러 변경
			salesBtn.setBackground(color2);

			totalTableData();
			// 카드 레이아웃으로 매출관리창으로 전환하기
			setTitle("매출 관리");
			this.setSize(875, 525);
			this.cardLayout2.show(this.menuTab, "menu");
			this.cardLayout.show(this.tab, "sales");
		} else if (mode == 3) { // 3: 환결성정UI
			// 환경설정 버튼만 컬러 변경
			optionBtn.setBackground(color2);
			// 카드 레이아웃으로 환경설정창으로 전환하기
			setTitle("환경 설정");
			this.setSize(875, 525);
			txfOption.setText("" + AppManager.getInstance().getTimerSet());
			this.cardLayout2.show(this.menuTab, "menu");
			this.cardLayout.show(this.tab, "option");
		} else if (mode == 4) { // 4: 테이블 주문서 UI
			this.setTitle("테이블 주문서");
			this.setSize(500, 500);
			this.cardLayout.show(this.tab, "table");
			this.cardLayout2.show(this.menuTab, "table");
			refreshTable(AppManager.getInstance().getTid());
		}
		this.setVisible(true);
	}

	public String getToday() {
		Date d = new Date();
		return (d.getYear() + 1900) + "-" + (d.getMonth() + 1) + "-" + d.getDate();
	}

	public void stockTableData() {
		itemTA.setText("");
		itemTA.append("이름\t판매량\t재고\t가격\n"); // 목록을 구분해주기 위하여 추가
		datas = AppManager.getInstance().getDAOManger().getStockTable();
		if (datas != null) { // 저장된 데이터가 있는 경우
			for (DayList d : datas) { // 데이터의 길이까지
				StringBuffer sb = new StringBuffer(); // 데이터를 저장할 버퍼생성
				sb.append(d.getName() + "\t");
				sb.append(d.getSales() + "\t");
				sb.append(d.getStock() + "\t");
				sb.append(d.getPrice() + "\n");
				itemTA.append(sb.toString()); // 하나의 문자열로 묶어서 추가한다.
			}
		}
	}

	public void chickenTableData(int index) { // 1개의 치킨테이블에 대한 날짜,총판매량,총판매액 을 얻는 함수
		itemTA.append("\n날짜\t총판매량\t총판매액 \n"); // 목록을 구분해주기 위하여 추가
		datas = AppManager.getInstance().getDAOManger().getChickenTable(index);
		if (datas != null) { // 저장된 데이터가 있는 경우
			for (DayList d : datas) { // 데이터의 길이까지
				StringBuffer sb = new StringBuffer(); // 데이터를 저장할 버퍼생성
				sb.append(d.getDay() + "\t");
				sb.append(d.getSales() + "\t");
				sb.append(d.getSales() * d.getPrice() + "\n");

				itemTA.append(sb.toString()); // 하나의 문자열로 묶어서 추가한다.
			}
		}
	}

	public void totalTableData() {
		salesTA.setText("");
		salesTA.append("날짜\t총판매량\t총판매액\n"); // 목록을 구분해주기 위하여 추가
		datas = AppManager.getInstance().getDAOManger().getTotalTable();
		if (datas != null) { // 저장된 데이터가 있는 경우
			for (DayList d : datas) { // 데이터의 길이까지
				StringBuffer sb = new StringBuffer(); // 데이터를 저장할 버퍼생성
				sb.append(d.getDay() + "\t");
				sb.append(d.getSales() + "\t");
				sb.append(d.getPrice() + "\n");
				salesTA.append(sb.toString()); // 하나의 문자열로 묶어서 추가한다.

			}
		}
	}

	public void totalTableData(String day) {
		salesTA.setText("");
		salesTA.append("날짜\t총판매량\t총판매액\n"); // 목록을 구분해주기 위하여 추가
		DayList d = AppManager.getInstance().getDAOManger().getOneTotalTable(day);
		StringBuffer sb = new StringBuffer(); // 데이터를 저장할 버퍼생성
		sb.append(d.getDay() + "\t");
		sb.append(d.getSales() + "\t");
		sb.append(d.getPrice() + "\n");
		salesTA.append(sb.toString()); // 하나의 문자열로 묶어서 추가한다.
	}

	// =====================================================================================
	// #UI구성 함수
	// =====================================================================================
	// 재고 관리 창 UI
	public void ItemManagerUI() {
		// 창 제목 설정
		setTitle("재고 관리");

		// 텍스트필드 패널 p1 구성
		JPanel p1 = new JPanel();
		// 재고 목록 TextField 구성
		itemTA = new JTextArea(23, 40); // ta영역 생성
		JScrollPane scroll = new JScrollPane(itemTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		itemTA.setBackground(Color.white);
		stockTableData();

		p1.setLayout(new BorderLayout());
		p1.add(scroll, BorderLayout.CENTER);
		p1.setBackground(Color.white);

		// 속성 입출력 패널 p2구성
		JPanel p2 = new JPanel(); // p2패널 생성
		p2.setLayout(null); // p2패널 레이아웃 없음
		p2.setBackground(Color.white);

		itemCB = new JComboBox(); // 메뉴번호 콤보박스
		itemCB.setBounds(120, 50, 230, 50);
		p2.add(itemCB);
		itemCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int index = itemCB.getSelectedIndex();
				if(index>=0) {
					itemTXT[0].setText((String) itemCB.getSelectedItem());
					itemTXT[1].setText("" + AppManager.getInstance().getDAOManger().getStock(index));
					itemTXT[2].setText("" + AppManager.getInstance().getDAOManger().getPrice(index));
				}
				if (index == 0) {
					stockTableData();
				} else {
					stockTableData();
					chickenTableData(index);
				}
			}

		});

		itemTXT = new JTextField[3]; // 메뉴명,재고,가격 텍스트필드
		for (int i = 0; i < 3; i++) {
			itemTXT[i] = new JTextField();
			itemTXT[i].setBounds(120, 150 + 100 * i, 230, 50);
			p2.add(itemTXT[i]);
		}

		JLabel[] lblItem = new JLabel[4];
		lblItem[0] = new JLabel("메뉴번호");// 메뉴번호 라벨
		lblItem[1] = new JLabel("메뉴명");// 메뉴명 라벨
		lblItem[2] = new JLabel("재고");// 재고 라벨
		lblItem[3] = new JLabel("가격");// 가격 라벨
		for (int i = 0; i < 4; i++) {
			lblItem[i].setBounds(30, 50 + 100 * i, 100, 50);
			p2.add(lblItem[i]);
		}
		p2.setBackground(Color.white);

		// 버튼 패널 p3 구성
		JPanel p3 = new JPanel(); // p3패널 생성
		p3.setLayout(new FlowLayout()); // p3패널 플로우 레이아웃
		p3.setBackground(Color.white);

		JButton[] btnItem = new JButton[4];
		btnItem[0] = new RegisterBtn("등록"); // 등록 버튼
		btnItem[1] = new EditBtn("수정"); // 조회 버튼
		btnItem[2] = new DelItemBtn("삭제"); // 삭제 버튼

		for (int i = 0; i < 3; i++) {
			p3.add(btnItem[i]);
			btnItem[i].addActionListener(this);
			btnItem[i].setBackground(color1);
		}

		// item Panel 레이아웃 설정 및 부속 패널들 붙이기
		itemPanel.setLayout(new BorderLayout());
		itemPanel.add(p1, BorderLayout.LINE_END); // scroll패널 오른쪽
		itemPanel.add(p2, BorderLayout.CENTER); // p2패널 중간
		itemPanel.add(p3, BorderLayout.PAGE_END); // p3패널 맨아래

		// dialog 레이아웃 및 위젯 설정
		this.add(menuTab, BorderLayout.PAGE_START);
		// this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);

	}

	// 매출 관리창 UI
	public void salesManagerUI() {
		// 창 제목
		setTitle("매출 관리");

		// 날짜 검색 패널 p1 생성
		JPanel p1 = new JPanel();
		JLabel lb1 = new JLabel("검색 일 수: ");
		JLabel lb2 = new JLabel("날짜 검색: ");
		searchDayTX = new JTextField(3);
		searchTX = new JTextField(15);
		SearchBtn btnSearch = new SearchBtn("검색");
		btnSearch.addActionListener(this);

		// p1레이아웃
		p1.setLayout(new FlowLayout());
		p1.add(lb1);
		p1.add(searchDayTX);
		p1.add(lb2);
		p1.add(searchTX);
		p1.add(btnSearch);

		// p1패널 배경색상 화이트로 설정
		p1.setBackground(Color.white);
		// 검색버튼 색상 color1로 변경
		btnSearch.setBackground(color1);

		// 그래프와 매출차트가 붙어있는 p2패널 생성
		p2_sales = new JPanel();

		// 매출 차트 생성
		// SalesChart 객체 생성
		salesChart = new SalesChart();
		// JFeeChart 객체 생성
		chart = salesChart.getChart(3, "2018-1-1");
		// 매출차트 붙일 차트패널 생성
		gp = new ChartPanel(chart);

		// 매출 목록 list패널 생성
		JPanel list = new JPanel();
		salesTA = new JTextArea(20, 35); // ta영역 생성
		JScrollPane scroll = new JScrollPane(salesTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		list.add(scroll);

		// p2레이아웃
		p2_sales.setLayout(new GridLayout(1, 2)); // 그리드 레이아웃 설정
		p2_sales.add(list);
		p2_sales.add(gp);

		// p2패널 배경색상 화이트로 설정
		chart.setBackgroundPaint(Color.WHITE);
		list.setBackground(Color.white);

		// salesPanel에 부속 패널 붙이기
		salesPanel.setLayout(new BorderLayout());
		salesPanel.add(p1, BorderLayout.PAGE_START);
		salesPanel.add(p2_sales, BorderLayout.CENTER);

		// dialog 레이아웃 및 위젯 설정
		this.add(menuTab, BorderLayout.PAGE_START);
		// this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);

	}

	// 환경 설정창 UI
	public void OptionUI() {
		// 창 제목
		setTitle("환경 설정");

		// 라벨 텍스트필드 버튼 생성
		// JLabel lblOption = new JLabel("배달 제한 시간을 설정해주세요.");
		txfOption = new JTextField();
		JButton btnOption = new JButton("확인");

		// 위젯 설정
		optionPanel.setLayout(null);
		// lblOption.setBounds(340,150,300,50);
		txfOption.setBounds(240, 250, 375, 50);
		btnOption.setBounds(320, 350, 210, 50);

		// 환경설정패널 색상 화이트로 설정
		// optionPanel.setBackground(Color.white);
		// 확인 버튼 색상 color1로 설정
		btnOption.setBackground(color1);
		txfOption.setBackground(Color.white);

		// optionPanel.add(lblOption);
		optionPanel.add(txfOption);
		optionPanel.add(btnOption);

		txfOption.setHorizontalAlignment(JTextField.RIGHT);

		btnOption.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AppManager.getInstance().setTimerSet(Integer.parseInt(txfOption.getText()));
				AppManager.getInstance().getChickenServer().msgType(2);
				AppManager.getInstance().getChickenDialog().setVisible(false);
			}

		});

		// dialog 레이아웃 및 위젯 설정
		this.add(menuTab, BorderLayout.PAGE_START);
		// this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);

	}

	// 테이블 UI
	public void TableUI() {

		this.setTitle("테이블 주문서");
		tablePanel.setLayout(new BorderLayout());

		boxSpace = new JPanel();
		boxSpace.setBackground(Color.white);
		boxSpace.setLayout(null);

		boxUI = new ArrayList<BoxLabel>();
		boxUI.add(new BoxLabel(0));

		for (BoxLabel bp : boxUI)
			boxSpace.add(bp);

		receiptBtn = new JButton[5];

		receiptBtn[0] = new ApplyBtn("확인");
		receiptBtn[1] = new CashBtn("결제");
		receiptBtn[2] = new InsertBtn("추가");
		receiptBtn[3] = new DeleteBtn("삭제");
		receiptBtn[4] = new ServingBtn("서빙");

		JScrollPane boxSpaceScroll = new JScrollPane(boxSpace);
		boxSpaceScroll.setBounds(0, 0, 400, 100);
		boxSpaceScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		boxSpaceScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel btnSpace = new JPanel();
		btnSpace.setSize(400, 50);
		menuTab.setBackground(color1);
		btnSpace.setBackground(color1);

		for (int i = 0; i < 5; i++) {
			btnSpace.add(receiptBtn[i]);
			receiptBtn[i].addActionListener(this);
			receiptBtn[i].setBackground(color2);
		}

		JLabel lblColumn = new JLabel(
				"	인덱스                          품목명                                             갯수                          가격");
		lblColumn.setBounds(2, 1, 450, 25);
		boxSpace.add(lblColumn);

		JPanel pricePanel = new JPanel();
		pricePanel.setSize(400, 50);
		pricePanel.setBackground(color1);

		JLabel lblPrice = new JLabel("총 가격");
		lblPrice.setBackground(color1);

		tfTotalPrice = new JTextField(6);
		tfTotalPrice.setText("0");
		tfTotalPrice.setHorizontalAlignment(JTextField.RIGHT);
		tfTotalPrice.setEditable(false);
		tfTotalPrice.setBackground(Color.WHITE);

		pricePanel.add(lblPrice);

		pricePanel.add(tfTotalPrice);

		tablePanel.add(boxSpaceScroll, BorderLayout.CENTER);
		tablePanel.add(pricePanel, BorderLayout.PAGE_START);
		tablePanel.add(btnSpace, BorderLayout.PAGE_END);

		this.add(menuTab, BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);

	}

	// =====================================================================================
	// #액션이벤트 핸들링 & 기타 클래스
	// =====================================================================================
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		EventAction obj = (EventAction) e.getSource();

		obj.doAction();
	}

	public class BoxLabel extends JLabel {
		private int index;
		private JComboBox menu;
		private AddBtn addBtn;
		private MinusBtn minusBtn;
		private JTextField amount;
		private JLabel lblIndex;
		private JTextField price;

		public BoxLabel(int index) {
			this.index = index;

			this.setBounds(2, 1 + (index + 1) * 30, 450, 25);
			this.setLayout(null);

			menu = new JComboBox();
			addBtn = new AddBtn("+", index);
			minusBtn = new MinusBtn("-", index);
			amount = new JTextField(5);
			amount.setHorizontalAlignment(JTextField.RIGHT);
			amount.setText("0");
			amount.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
					boxUI.get(index).refreshBox();
				}

				public void keyTyped(KeyEvent e) {
				}
			});

			price = new JTextField(5);
			price.setText("0");
			price.setHorizontalAlignment(JTextField.RIGHT);
			price.setEditable(false);
			price.setBackground(Color.WHITE);
			lblIndex = new JLabel("#" + index);
			lblIndex.setHorizontalAlignment(JLabel.CENTER);

			menu.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					refreshBox();

				}

			});

			lblIndex.setBounds(0, 0, 30, 25);
			menu.setBounds(30, 0, 205, 25);
			minusBtn.setBounds(236, 0, 50, 25);
			amount.setBounds(287, 0, 40, 25);
			addBtn.setBounds(327, 0, 50, 25);
			price.setBounds(378, 0, 70, 25);

			addBtn.addActionListener(AppManager.getInstance().getChickenDialog());
			minusBtn.addActionListener(AppManager.getInstance().getChickenDialog());

			this.add(lblIndex);
			this.add(menu);
			this.add(minusBtn);
			this.add(amount);
			this.add(addBtn);
			this.add(price);

		}

		public int getAmount() {
			return Integer.parseInt(amount.getText());
		}

		public void setAmount(int amount) {
			this.amount.setText("" + amount);
		}

		public int getPrice() {
			return Integer.parseInt(price.getText());
		}

		public JComboBox getMenuBox() {
			return menu;
		}

		public int getSelectedBox() {
			return menu.getSelectedIndex();
		}

		public void selectBox(int index) {
			menu.setSelectedIndex(index);
		}

		public void refreshBox() {
			int pr = 0;
			if (menu.getSelectedIndex() >= 0)
				price.setText("" + (Integer.parseInt(amount.getText())
						* AppManager.getInstance().getDAOManger().getPrice(menu.getSelectedIndex())));
			else
				price.setText("0");

			for (BoxLabel bl : boxUI)
				pr += bl.getPrice();
			tfTotalPrice.setText("" + pr);
		}
	}

	public class RegisterBtn extends JButton implements EventAction {

		public RegisterBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {
			AppManager.getInstance().getDAOManger().addMenu(itemTXT[0].getText());
			AppManager.getInstance().getChickenDialog().refreshComboBox();

			AppManager.getInstance().getDAOManger().insertList(AppManager.getInstance().getDAOManger().getKey(),
					AppManager.getInstance().getChickenDialog().getToday(), 0, Integer.parseInt(itemTXT[1].getText()),
					Integer.parseInt(itemTXT[2].getText()));

			int index = itemCB.getSelectedIndex();

			itemTXT[0].setText((String) itemCB.getSelectedItem());
			itemTXT[1].setText("" + AppManager.getInstance().getDAOManger().getStock(index));
			itemTXT[2].setText("" + AppManager.getInstance().getDAOManger().getPrice(index));

			if (index == 0) {
				stockTableData();
			} else {
				stockTableData();
				chickenTableData(index);
			}

			itemTXT[0].setText("");
			itemTXT[1].setText("0");
			itemTXT[2].setText("0");

		}
	}

	public class EditBtn extends JButton implements EventAction {
		public EditBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {

			AppManager.getInstance().getDAOManger().chickenUpdate(itemCB.getSelectedIndex(), itemTXT[0].getText(),
					Integer.parseInt(itemTXT[1].getText()), Integer.parseInt(itemTXT[2].getText()));
			AppManager.getInstance().getChickenDialog().refreshComboBox();
			itemCB.setSelectedIndex(0);

		}
	}

	public class DelItemBtn extends JButton implements EventAction {

		public DelItemBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {
	
			int index;
			if (itemCB.getSelectedIndex() != 0) {
				index=itemCB.getSelectedIndex();
				AppManager.getInstance().getDAOManger().delMenu(index);
				refreshComboBox();
				stockTableData();
				
				itemCB.setSelectedIndex(0);

				itemTXT[0].setText((String)itemCB.getSelectedItem());
				itemTXT[1].setText("0");
				itemTXT[2].setText("0");
				
			}
		}
	}

	public class SearchBtn extends JButton implements EventAction {

		public SearchBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {
			if (!searchTX.getText().equals("")) {

				p2_sales.remove(gp);
				totalTableData(searchTX.getText());

				chart = salesChart.getChart(Integer.parseInt(searchDayTX.getText()), searchTX.getText());
				// 매출차트 붙일 차트패널 생성
				gp = new ChartPanel(chart);
				p2_sales.add(gp, JPanel.LEFT_ALIGNMENT);

				AppManager.getInstance().getChickenDialog().getGraphPanel().updateUI();
				p2_sales.repaint();

			} else
				totalTableData();
		}
	}

	public class AddBtn extends JButton implements EventAction {
		private int index;

		public AddBtn(String s, int index) {
			this.setText(s);
			this.index = index;
		}

		@Override
		public void doAction() {
			BoxLabel bl = boxUI.get(index);
			bl.setAmount(bl.getAmount() + 1);
			bl.refreshBox();
		}

	}

	public class MinusBtn extends JButton implements EventAction {
		private int index;

		public MinusBtn(String s, int index) {
			this.setText(s);
			this.index = index;
		}

		@Override
		public void doAction() {
			BoxLabel bl = boxUI.get(index);
			if (bl.getAmount() > 0)
				bl.setAmount(bl.getAmount() - 1);
			bl.refreshBox();
		}

	}

	public class ApplyBtn extends JButton implements EventAction {
		public ApplyBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {
			if (Integer.parseInt(tfTotalPrice.getText()) != 0) {
				selectedTable.setBoxNum(boxUI.size());
				ArrayList<ReceiptItem> info = new ArrayList<ReceiptItem>();
				for (BoxLabel bl : boxUI) {
					ReceiptItem ri = new ReceiptItem();
					ri.setItemIndex(bl.getSelectedBox());
					ri.setItemAmount(bl.getAmount());
					info.add(ri);
				}
				selectedTable.setPrice(Integer.parseInt(tfTotalPrice.getText()));

				selectedTable.setTInfo(info);
				AppManager.getInstance().getChickenServer().msgType(6);
				AppManager.getInstance().getChickenServer().msgType(3);
				selectedTable.timerStart();
			}
			AppManager.getInstance().getChickenDialog().setVisible(false);
		}

	}

	public class CashBtn extends JButton implements EventAction {
		public CashBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {
			JTextField[] txf;
			int x = Integer.parseInt(tfTotalPrice.getText());
			txf = AppManager.getInstance().getChickenMain().getTxfCash();
			txf[0].setText("" + x);
			x = x - Integer.parseInt(txf[1].getText());
			txf[2].setText("" + (-x));
			AppManager.getInstance().getChickenDialog().setVisible(false);
		}

	}

	public class InsertBtn extends JButton implements EventAction {
		public InsertBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {
			int index = boxUI.size();
			if (index < 10) {
				BoxLabel bp = new BoxLabel(index);
				boxUI.add(bp);
				AppManager.getInstance().getChickenDialog().getTableUI().add(bp);

				refreshComboBox();
				bp.selectBox(0);

				bp.updateUI();

			}
		}

	}

	public class DeleteBtn extends JButton implements EventAction {
		public DeleteBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {

			int index = boxUI.size() - 1;
			if (index > 0) {
				BoxLabel bp = boxUI.get(index);
				AppManager.getInstance().getChickenDialog().getTableUI().remove(bp);
				AppManager.getInstance().getChickenDialog().getTableUI().repaint();
				boxUI.remove(bp);
			}
		}
	}

	public class ServingBtn extends JButton implements EventAction {
		public ServingBtn(String s) {
			this.setText(s);
		}

		@Override
		public void doAction() {
			selectedTable.timerOff();
		}
	}

	public class MenuBtn extends JButton implements EventAction {
		int index;

		public MenuBtn(String s, int i) {
			this.setText(s);
			index = i;
		}

		@Override
		public void doAction() {
			setMode(index);
		}
	}
}