package changgyu;


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
	
	//������ ��������
	JTextField menuTxt ; //�޴��� �ؽ�Ʈ�ʵ�
	JTextField stockTxt ; //��� �ؽ�Ʈ�ʵ�
	JTextField priceTxt ; //���� �ؽ�Ʈ�ʵ�
	
	
	//���̺�UI��
	private JPanel boxSpace;
	private CardLayout cardLayout2;
	private int selectedTid;
	private ArrayList<BoxLabel> boxUI;
	private JLabel lblTable;
	
	ArrayList<DayList> datas = new ArrayList<DayList>();
	JTextArea ta;
	DAOManager dao = new DAOManager();
	
	//������
	public ChickenDialog() {
		AppManager.getInstance().setChickenDialog(this);
		
		//Dialog ������ ����
		this.setSize(875,625);
		this.setLocation(500,300);
		
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
	public void setTableIndex(int index) {
		selectedTid=index;
	}
	public JPanel getTableUI(){
		return boxSpace;
	}
	
	public void refreshUI() {
		if(mode==1){ // 1: ������UI
			//ī�� ���̾ƿ����� ������ â ��ȯ
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"item");
		}
		else if(mode == 2) { // 2: �������UI
			//ī�� ���̾ƿ����� �������â���� ��ȯ�ϱ�
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"sales");
		}
		else if(mode == 3) { // 3: ȯ�Ἲ��UI
			//ī�� ���̾ƿ����� ȯ�漳��â���� ��ȯ�ϱ�
			this.cardLayout2.show(this.menuTab,"menu");
			this.cardLayout.show(this.tab,"option");
		}
		else if(mode == 4) { // 4: ���̺� �ֹ��� UI
			this.cardLayout.show(this.tab,"table");
			this.cardLayout2.show(this.menuTab,"table");
		}
		this.setVisible(true);
	}
	
	//��� ���� â UI
	public void ItemManagerUI(){
		//â ���� ����
		setTitle("��� ����");
		String[] comboIndex = new String[15];
		//�� �г� p1 ����
		JPanel p1 = new JPanel(); //p1�г� ����
		p1.setLayout(new GridLayout(4,1)); //p1�г� �׸��� ���̾ƿ�
		p1.add(new JLabel("�޴���ȣ")); //�޴���ȣ ��
		p1.add(new JLabel("�޴���")); //�޴��� ��
		p1.add(new JLabel("���")); //��� ��
		p1.add(new JLabel("����")); //���� ��
		
		//�Ӽ� ����� �г� p2����
		JPanel p2 = new JPanel(); //p2�г� ����
		JComboBox cb = new JComboBox(comboIndex); //�޴���ȣ �޺��ڽ�
		menuTxt = new JTextField(); //�޴��� �ؽ�Ʈ�ʵ�
		stockTxt = new JTextField(); //��� �ؽ�Ʈ�ʵ�
		priceTxt = new JTextField(); //���� �ؽ�Ʈ�ʵ�
		//dao.getComboIndex();
		cb.setModel(new DefaultComboBoxModel(dao.getComboIndex()));
		
		p2.setLayout(null); //p2�г� �׸��� ���̾ƿ�
		cb.setBounds(120, 50, 230, 50);
		menuTxt.setBounds(120, 150, 230, 50);
		stockTxt.setBounds(120, 250, 230, 50);
		priceTxt.setBounds(120, 350, 230, 50);
		p2.add(cb); //p2�гο� ����
		p2.add(menuTxt);
		p2.add(stockTxt);
		p2.add(priceTxt);
		
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
		ta = new JTextArea(10,40); //ta���� ����
		JScrollPane scroll = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
		//chickenTableData(2);
		stockTableData();
		

		
		//item Panel ���̾ƿ� ���� �� �μ� �гε� ���̱�
		itemPanel.setLayout(new BorderLayout());
		itemPanel.add(p1, BorderLayout.LINE_START); //p1�г� ����
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
	
	
	public void stockTableData() {		
		ta.append("�̸�     �Ǹŷ�    ���     ����  \n");	//����� �������ֱ� ���Ͽ� �߰�
		datas=dao.getStockTable();
		if (datas != null) {			//����� �����Ͱ� �ִ� ���
			for (DayList d : datas) {	//�������� ���̱���
				StringBuffer sb = new StringBuffer();	//�����͸� ������ ���ۻ���
				sb.append(d.getName() + "           ");	
				sb.append(d.getSales() + "             ");
				sb.append(d.getStock() + "             ");
				sb.append(d.getPrice() + "\n");
				ta.append(sb.toString());		//�ϳ��� ���ڿ��� ��� �߰��Ѵ�.
				
			}
		}
	}
	
	//���� ����â UI
	public void salesManagerUI(){
		//â ����
		setTitle("���� ����");
		
		//��¥ �˻� �г� p1 ����
		JPanel p1 = new JPanel();
		JLabel lb = new JLabel("��¥ �˻�: ");
		JTextField tf = new JTextField(15);
		JButton searchBtn = new JButton("�˻�");
		
		//p1���̾ƿ�
		p1.setLayout(new FlowLayout());
		p1.add(lb);
		p1.add(tf);
		p1.add(searchBtn);
		
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
	
	public void chickenTableData(int index) {	//1���� ġŲ���̺� ���� ��¥,���Ǹŷ�,���Ǹž� �� ��� �Լ�
		ta.append("��¥      ���Ǹŷ�      ���Ǹž�  \n");	//����� �������ֱ� ���Ͽ� �߰�
		datas=dao.getChickenTable(index);
		if (datas != null) {			//����� �����Ͱ� �ִ� ���
			for (DayList d : datas) {	//�������� ���̱���
				StringBuffer sb = new StringBuffer();	//�����͸� ������ ���ۻ���
				sb.append(d.getDay() + "           ");	
				sb.append(d.getSales() + "             ");	
				sb.append(d.getSales()*d.getPrice() + "\n");


				ta.append(sb.toString());		//�ϳ��� ���ڿ��� ��� �߰��Ѵ�.
				
			}
		}
	}
	
	public void totalTableData() {		
		ta.append("��¥     �Ǹŷ�    ����  \n");	//����� �������ֱ� ���Ͽ� �߰�
		datas=dao.getTotalTable();
		if (datas != null) {			//����� �����Ͱ� �ִ� ���
			for (DayList d : datas) {	//�������� ���̱���
				StringBuffer sb = new StringBuffer();	//�����͸� ������ ���ۻ���
				sb.append(d.getDay() + "           ");	
				sb.append(d.getSales() + "             ");
				sb.append(d.getPrice() + "\n");
				ta.append(sb.toString());		//�ϳ��� ���ڿ��� ��� �߰��Ѵ�.
				
			}
		}
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
		
		ApplyBtn applyBtn;
		InsertBtn insertBtn;
		DeleteBtn deleteBtn;
		
		applyBtn = new ApplyBtn("Ȯ��");
		insertBtn = new InsertBtn("�߰�");
		deleteBtn = new DeleteBtn("����");
		
		JScrollPane boxSpaceScroll = new JScrollPane(boxSpace);
		boxSpaceScroll.setBounds(0,0,400,300);
		boxSpaceScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		boxSpaceScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel btnSpace =new JPanel();
		btnSpace.setBounds(0,300,400,50);
		
		btnSpace.add(applyBtn);
		btnSpace.add(insertBtn);
		btnSpace.add(deleteBtn);
		
		insertBtn.addActionListener(AppManager.getInstance().getChickenDialog());
		deleteBtn.addActionListener(AppManager.getInstance().getChickenDialog());
		
		tablePanel.add(boxSpaceScroll,BorderLayout.CENTER);
		tablePanel.add(btnSpace,BorderLayout.PAGE_END);
		
		this.add(menuTab,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		
		
		
	}
	
	public void refreshItem() {
		
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
			
		public BoxLabel(int index){
			this.index=index;
			
			if(index%2==0)
				this.setBounds(5,1+(index/2)*30,375,25);
			else
				this.setBounds(400,1+(index/2)*30,375,25);
			this.setLayout(null);
			
			menu= new JComboBox();
			addBtn = new AddBtn("+",index);
			minusBtn = new MinusBtn("-",index);
			amount = new JTextField(5);
			lblIndex = new JLabel(""+index);
			
			menu.setBounds(0,0, 205,25);
			minusBtn.setBounds(206,0,50,25);
			amount.setBounds(257,0,50,25);
			addBtn.setBounds(307,0,50,25);
			
			addBtn.addActionListener(AppManager.getInstance().getChickenDialog());
			minusBtn.addActionListener(AppManager.getInstance().getChickenDialog());
			
			this.add(menu);
			this.add(minusBtn);
			this.add(amount);
			this.add(addBtn);
			
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