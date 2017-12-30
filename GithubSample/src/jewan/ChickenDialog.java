package jewan;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class ChickenDialog extends JDialog implements ActionListener{
	//��ư�� ���� UI�� �ҷ����� ���� mode����
	private int mode;
	
	//ȭ�� ���� ��ȯ�� ���� ī�� ���̾ƿ�
	private Container tab;
	private Container menuTab;
	private CardLayout cardLayout;
	
	//�޴���ư�� ��� �ٿ��ߵǴ� �޴����гΰ� ��ư��
	private JPanel menuPanel; 	// �޴��� �гη� ����
	private JButton itemBtn; 	//������ ��ư
	private JButton salesBtn;	//������� ��ư
	private JButton optionBtn; 	//ȯ�漳�� ��ư
	
	//Dialog�� �޴��г� �ؿ� ���� �� �޴� �гε� (ī�巹�̾ƿ��� �̿��� �� �гε� �� �ϳ��� ���õ�)
	private JPanel itemPanel;	 	//��� ���� �г�
	private JPanel salesPanel;	 	//���� ���� �г�
	private JPanel optionPanel;		//ȯ�� ���� �г�
	/*���̺� UI*/JPanel tablePanel;
	
	//���̺�UI��
	private JPanel boxSpace;
	private CardLayout cardLayout2;
	private int selectedTid;
	private ArrayList<BoxLabel> boxUI;
	private JLabel lblTable;
	private JTextField tfTotalPrice;
	
	
	//������
	public ChickenDialog() {
		AppManager.getInstance().setChickenDialog(this);
		
		//Dialog ������ ����
		this.setSize(875,625);
		this.setLocation(500,300);
		this.setResizable(false);
		
		//Dialog�� �޴��г� �ؿ� ���� �� �޴� �гε� (ī�巹�̾ƿ��� �̿��� �� �гε� �� �ϳ��� ���õ�)
		itemPanel = new JPanel(); //��� ���� �г�
		salesPanel = new JPanel(); //���� ���� �г�
		optionPanel = new JPanel(); //ȯ�� ���� �г�
		/*���̺� UI*/tablePanel = new JPanel();
		
		//�޴���ư�� ��� �ٿ��ߵǴ� �޴����гΰ� ��ư��
		menuPanel = new JPanel(); 				//�޴��� �гη� ����
		itemBtn = new JButton("������"); 		//������ ��ư
		salesBtn = new JButton("�������"); 		//������� ��ư
		optionBtn = new JButton("ȯ�漳��");	 	//ȯ�漳�� ��ư
		
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
		/*���̺� UI*/tab.add(tablePanel, "table");
		
		cardLayout2 = new CardLayout();
		lblTable=new JLabel("���̺� �ֹ���");
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
	
	
	//-----------�޼ҵ�---------------
	
	public void setMode(int m) {
		mode=m;
		refreshUI();
	}
	
	//���̺�UI��
	
	public JPanel getTableUI(){
		return boxSpace;
	}
	
	public void refreshUI() {
		if(mode==1){ // 1: ������UI
			//ī�� ���̾ƿ����� ������ â ��ȯ
			setTitle("��� ����");
			this.setSize(875,625);
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"item");
		}
		else if(mode == 2) { // 2: �������UI
			//ī�� ���̾ƿ����� �������â���� ��ȯ�ϱ�
			setTitle("���� ����");
			this.setSize(875,625);
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"sales");
		}
		else if(mode == 3) { // 3: ȯ�Ἲ��UI
			//ī�� ���̾ƿ����� ȯ�漳��â���� ��ȯ�ϱ�
			setTitle("ȯ�� ����");
			this.setSize(875,625);
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"option");
		}
		else if(mode == 4) { // 4: ���̺� �ֹ��� UI
			this.setTitle("���̺� �ֹ���");
			this.setSize(500,500);
			this.cardLayout.show(this.tab,"table");
			this.cardLayout2.show(this.menuTab,"table");
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
		p2.setLayout(null); //p2�г� �׸��� ���̾ƿ�
		cb.setBounds(120, 50, 230, 50);
		t1.setBounds(120, 150, 230, 50);
		t2.setBounds(120, 250, 230, 50);
		t3.setBounds(120, 350, 230, 50);
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
		//itemPanel.add(p1, BorderLayout.LINE_START); //p1�г� ����
		itemPanel.add(p2, BorderLayout.CENTER); //p2�г� �߰�
		itemPanel.add(p3, BorderLayout.PAGE_END); //p3�г� �ǾƷ�
		itemPanel.add(scroll, BorderLayout.LINE_END); //scroll�г� ������
	
		//dialog ���̾ƿ� �� ���� ����
		this.add(menuTab,BorderLayout.PAGE_START);
		//this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		
		
		
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
		p1.setLayout(new FlowLayout());
		p1.add(lb);
		p1.add(tf);
		p1.add(btn);
		
		//�׷����� ���⸮��Ʈ�� �پ��ִ� p2�г� ����
		JPanel p2 = new JPanel();
	
		//���� �׷����� ǥ�õ� gp�г� ����
		JPanel gp = new JPanel();
		/*
		 * �׷��� ���� �ڸ�
		 * 
		 */
	
		//���� ��� list�г� ����
		JPanel list = new JPanel();
		JTextArea ta = new JTextArea(26,35); //ta���� ����
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ta.append("��¥\t�����\t\t�Ǹŷ�\n"); //ta�� ��Ÿ�� �׸��
		list.add(scroll);
		
		//p2���̾ƿ�
		p2.setLayout(new GridLayout(1,2)); //�׸��� ���̾ƿ� ����
		p2.add(gp);
		p2.add(list);
		
		//salesPanel�� �μ� �г� ���̱�
		salesPanel.setLayout(new BorderLayout());
		salesPanel.add(p1,BorderLayout.PAGE_START);
		salesPanel.add(p2,BorderLayout.CENTER);
		
		//dialog ���̾ƿ� �� ���� ����
		this.add(menuTab,BorderLayout.PAGE_START);
		//this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		
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
		this.add(menuTab,BorderLayout.PAGE_START);
		//this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		
	}
	
	//���̺� UI
	public void TableUI() {
		
		this.setTitle("���̺� �ֹ���");
		tablePanel.setLayout(new BorderLayout());
		
		boxSpace = new JPanel();
		boxSpace.setBackground(Color.white);
		boxSpace.setLayout(null);
		
		boxUI= new ArrayList<BoxLabel>();
		boxUI.add(new BoxLabel(0));
		
		for(BoxLabel bp : boxUI)
			boxSpace.add(bp);
		
		ApplyBtn applyBtn = new ApplyBtn("Ȯ��");
		CashBtn cashBtn = new CashBtn("����");
		InsertBtn insertBtn = new InsertBtn("�߰�");
		DeleteBtn deleteBtn = new DeleteBtn("����");
		
		
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
		
		JLabel lblColumn = new JLabel("	�ε���                          ǰ���                                             ����                          ����");
		lblColumn.setBounds(2,1,450,25);
		boxSpace.add(lblColumn);
		
		JPanel pricePanel = new JPanel();
		pricePanel.setSize(400,50);
		
		JLabel lblPrice = new JLabel("�� ����");
		
		tfTotalPrice = new JTextField("10");
		tfTotalPrice.setEditable(false);
		tfTotalPrice.setBackground(Color.WHITE);
		tfTotalPrice.setText("                     ");
		
		pricePanel.add(lblPrice);
		
		pricePanel.add(tfTotalPrice);
		
		insertBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		
		tablePanel.add(boxSpaceScroll,BorderLayout.CENTER);
		tablePanel.add(pricePanel, BorderLayout.PAGE_START);
		tablePanel.add(btnSpace,BorderLayout.PAGE_END);
		
		this.add(menuTab,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
	}
	
	public void refreshTable(int index) {
		TableBtn table=AppManager.getInstance().getTableArray().get(index);
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
		public int selectedBox(){
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
			
		}
		
	}
	public class CashBtn extends JButton implements EventAction{
		public CashBtn(String s){
			this.setText(s);
		}

		@Override
		public void doAction() {
			
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
}