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
		else if(mode == 4) {	//테이블 주문서 UI
			TableUI();
		}
		this.setVisible(true);
		
		
	}
	public void ItemManagerUI(){
		setTitle("재고 관리");

		JPanel menuPanel = new JPanel(); // 메뉴바 패널로 생성
		JButton itemBtn = new JButton("재고관리"); //재고관리 버튼
		JButton salesBtn = new JButton("매출관리"); //매출관리 버튼
		JButton optionBtn = new JButton("환경설정"); //환경설정 버튼
		menuPanel.add(itemBtn); // 각 메뉴 버튼 패널에 부착
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);
	
		JPanel p1 = new JPanel(); //p1패널 생성
		p1.setLayout(new GridLayout(4,1)); //p1패널 그리드 레이아웃
		p1.add(new JLabel("메뉴번호")); //메뉴번호 라벨
		p1.add(new JLabel("메뉴명")); //메뉴명 라벨
		p1.add(new JLabel("재고")); //재고 라벨
		p1.add(new JLabel("가격")); //가격 라벨
		
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
		
		JPanel p3 = new JPanel(); //p3패널 생성
		JButton b1 = new JButton("등록"); //등록 버튼
		JButton b2 = new JButton("조회"); //조회 버튼
		JButton b3 = new JButton("삭제"); //삭제 버튼
		p3.setLayout(new FlowLayout()); //p3패널 플로우 레이아웃
		p3.add(b1); //p3패널에 부착
		p3.add(b2);
		p3.add(b3);
		
		JTextArea ta = new JTextArea(10,40); //ta영역 생성
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ta.append("메뉴번호\t메뉴명\t\t재고\t가격\n"); //ta에 나타낼 항목들
		
		this.setLayout(new BorderLayout()); //전체 레이아웃 설정
		add(menuPanel, BorderLayout.PAGE_START); //메뉴패널 맨위
		add(p1, BorderLayout.LINE_START); //p1패널 왼쪽
		add(p2, BorderLayout.CENTER); //p2패널 중간
		add(p3, BorderLayout.PAGE_END); //p3패널 맨아래
		add(scroll, BorderLayout.LINE_END); //scroll패널 오른쪽
		
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

		this.setSize(875,625);
		//setVisible(true);
	}
	public void salesManagerUI(){
		setTitle("매출 관리"); //창 제목
		this.setLayout(new BorderLayout());
		
		JPanel menuPanel = new JPanel(); // 메뉴바 패널로 생성
		JButton itemBtn = new JButton("재고관리"); //재고관리 버튼
		JButton salesBtn = new JButton("매출관리"); //매출관리 버튼
		JButton optionBtn = new JButton("환경설정"); //환경설정 버튼
		menuPanel.add(itemBtn); // 각 메뉴 버튼 패널에 부착
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);
		
		JPanel p1 = new JPanel();
		JLabel lb = new JLabel("날짜 검색: ");
		JTextField tf = new JTextField();
		JButton btn = new JButton("검색");
		p1.add(lb);
		p1.add(tf);
		p1.add(btn);
		
		JPanel p2 = new JPanel();
		JTextArea ta = new JTextArea(25,70); //ta영역 생성
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ta.append("날짜\t매출액\t\t판매량\n"); //ta에 나타낼 항목들
		p2.add(scroll);

		this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(p1,BorderLayout.PAGE_END);
		this.add(p2,BorderLayout.CENTER);
		
		setSize(875,625);
	}
	public void OptionUI() {
		setTitle("환경 설정"); //창 제목
		this.setLayout(null);
		
		JPanel menuPanel = new JPanel(); // 메뉴바 패널로 생성
		JButton itemBtn = new JButton("재고관리"); //재고관리 버튼
		JButton salesBtn = new JButton("매출관리"); //매출관리 버튼
		JButton optionBtn = new JButton("환경설정"); //환경설정 버튼
		menuPanel.add(itemBtn); // 각 메뉴 버튼 패널에 부착
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);
		
		JLabel lblOption = new JLabel("배달 제한 시간을 설정해주세요."); 
		JTextField txfOption = new JTextField();
		JButton btnOption = new JButton("확인");
		
		menuPanel.setBounds(0,0,875,50);
		menuPanel.setBackground(Color.black); //임의로...
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