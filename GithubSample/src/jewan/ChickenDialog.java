package jewan;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChickenDialog extends JDialog implements ActionListener{
	//버튼에 따른 UI를 불러오기 위한 mode변수
	private int mode;
	
	//화면 구성 전환을 위한 카드 레이아웃
	protected Container tab;
	protected CardLayout cardLayout;
	
	//메뉴버튼에 모두 붙여야되는 메뉴바패널과 버튼들
	JPanel menuPanel = new JPanel(); // 메뉴바 패널로 생성
	JButton itemBtn = new JButton("재고관리"); //재고관리 버튼
	JButton salesBtn = new JButton("매출관리"); //매출관리 버튼
	JButton optionBtn = new JButton("환경설정"); //환경설정 버튼
	
	//Dialog의 메뉴패널 밑에 붙일 각 메뉴 패널들 (카드레이아웃을 이용해 이 패널들 중 하나가 선택됨)
	JPanel itemPanel = new JPanel(); //재고 관리 패널
	JPanel salesPanel = new JPanel(); //매출 관리 패널
	JPanel optionPanel = new JPanel(); //환경 설정 패널
	
	
	//생성자
	public ChickenDialog() {
		//Dialog 사이즈 설정
		this.setSize(875,625);
		this.setLocation(500,300);
		
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
		
		
		AppManager.getInstance().setChickenDialog(this);
	}
	
	
	//-----------메소드---------------
	
	public void setMode(int m) {
		mode=m;
		makeUI();
	}
	
	public void makeUI() {
		if(mode==1){ // 1: 재고관리UI
			ItemManagerUI();
		}
		else if(mode == 2) { // 2: 매출관리UI
			salesManagerUI();
		}
		else if(mode == 3) { // 3: 환결성정UI
			OptionUI();
		}
		else if(mode == 4) { // 4: 테이블 주문서 UI
			TableUI();
		}
		this.setVisible(true);
	}
	
	//재고 관리 창 UI
	public void ItemManagerUI(){
		//창 제목 설정
		setTitle("재고 관리");
		
		//라벨 패널 p1 구성
		JPanel p1 = new JPanel(); //p1패널 생성
		p1.setLayout(new GridLayout(4,1)); //p1패널 그리드 레이아웃
		p1.add(new JLabel("메뉴번호")); //메뉴번호 라벨
		p1.add(new JLabel("메뉴명")); //메뉴명 라벨
		p1.add(new JLabel("재고")); //재고 라벨
		p1.add(new JLabel("가격")); //가격 라벨
		
		//속성 입출력 패널 p2구성
		JPanel p2 = new JPanel(); //p2패널 생성
		JComboBox cb = new JComboBox(); //메뉴번호 콤보박스
		JTextField t1 = new JTextField(); //메뉴명 텍스트필드
		JTextField t2 = new JTextField(); //재고 텍스트필드
		JTextField t3 = new JTextField(); //가격 텍스트필드
		p2.setLayout(new GridLayout(4,1)); //p2패널 그리드 레이아웃
		p2.add(cb); //p2패널에 부착
		p2.add(t1);
		p2.add(t2);
		p2.add(t3);
		
		//버튼 패널 p3 구성
		JPanel p3 = new JPanel(); //p3패널 생성
		JButton b1 = new JButton("등록"); //등록 버튼
		JButton b2 = new JButton("조회"); //조회 버튼
		JButton b3 = new JButton("삭제"); //삭제 버튼
		p3.setLayout(new FlowLayout()); //p3패널 플로우 레이아웃
		p3.add(b1); //p3패널에 부착
		p3.add(b2);
		p3.add(b3);
		
		//재고 목록 TextField 구성
		JTextArea ta = new JTextArea(10,40); //ta영역 생성
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ta.append("메뉴번호\t메뉴명\t\t재고\t가격\n"); //ta에 나타낼 항목들
		
		//item Panel 레이아웃 설정 및 부속 패널들 붙이기
		itemPanel.setLayout(new BorderLayout());
		itemPanel.add(p1, BorderLayout.LINE_START); //p1패널 왼쪽
		itemPanel.add(p2, BorderLayout.CENTER); //p2패널 중간
		itemPanel.add(p3, BorderLayout.PAGE_END); //p3패널 맨아래
		itemPanel.add(scroll, BorderLayout.LINE_END); //scroll패널 오른쪽
	
		//dialog 레이아웃 및 위젯 설정
		this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		//카드 레이아웃으로 재고관리 창 전환
		this.cardLayout.show(this.tab,"item");
		
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
		p1.setLayout(new BorderLayout());
		p1.add(lb, BorderLayout.WEST);
		p1.add(tf, BorderLayout.CENTER);
		p1.add(btn, BorderLayout.EAST);

		//매출 목록 패널 p2 생성
		JPanel p2 = new JPanel();
		JTextArea ta = new JTextArea(25,70); //ta영역 생성
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ta.append("날짜\t매출액\t\t판매량\n"); //ta에 나타낼 항목들
		p2.add(scroll);
		
		//salesPanel에 부속 패널 붙이기
		salesPanel.add(p1,BorderLayout.PAGE_END);
		salesPanel.add(p2,BorderLayout.CENTER);
		
		//dialog 레이아웃 및 위젯 설정
		this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		//카드 레이아웃으로 매출관리창으로 전환하기
		this.cardLayout.show(this.tab,"sales");
	}
	
	//환경 설정창 UI
	public void OptionUI() {
		//창 제목
		setTitle("환경 설정");

		//라벨 텍스트필드 버튼 생성
		JLabel lblOption = new JLabel("배달 제한 시간을 설정해주세요."); 
		JTextField txfOption = new JTextField();
		JButton btnOption = new JButton("확인");
		
		//위젯 설정
		optionPanel.setLayout(null);
		lblOption.setBounds(340,150,300,50);
		txfOption.setBounds(240,250,375,50);
		btnOption.setBounds(320,350,210,50);
				
		optionPanel.add(lblOption);
		optionPanel.add(txfOption);
		optionPanel.add(btnOption);
		
		//dialog 레이아웃 및 위젯 설정
		this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		//카드 레이아웃으로 환경설정창으로 전환하기
		this.cardLayout.show(this.tab,"option");
	}
	
	public void TableUI() {
		JDialog dialog = new JDialog(this,"계산서1234");
		JButton payBtn = new JButton("결제");
		JButton okBtn = new JButton("등록");
		JButton[] upBtn = new JButton[3];
		JButton[] downBtn = new JButton[3];
		JTextField[] amount = new JTextField[3];
		JScrollPane scroll = new JScrollPane(dialog,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		String[] menuList = {"-------------------------","후라이드치킨(15000)","양념치킨(15000)","간장치킨(15000)"};				
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