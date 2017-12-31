package jewan;




import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChickenMain extends JFrame implements ActionListener {	// JFrame�� ��ӹ޴´�
	
	private JPanel[] backgroundPanel;  
	private JPanel belowPanel[];
	private JButton[] btnMenu;
	private JButton[] btnCash;
	private JButton[] pad;
	private JButton[] btnTableEdit;
	private Vector<TableBtn> table;
	private final int tableMax=12;
	
	private JLabel[] lblCash;
	
	private JTextField[] txfCash;
	
	
//=====================================================================================
//	#������
//=====================================================================================	
	public ChickenMain() { // ������ ����
		AppManager.getInstance().setChickenMain(this);
		this.setLayout(new BorderLayout()); // BorderLayout���� ���̾ƿ� ����
		setSize(1050, 720); // ����â ũ�� ����
		setLocation(300, 100); // ����â ��ġ����
		setTitle("ġŲ"); // ������ Ÿ��Ʋ ����
		this.setBackground(Color.white);
		
		backgroundPanel = new JPanel[3];
		for(int i = 0;i < 3;i++) {
			backgroundPanel[i]=new JPanel();
		}

		//-----------------------------------------------------------------------------
		//	#�г�1 ����
		//-----------------------------------------------------------------------------
		backgroundPanel[0].setLayout(null); // ����г�1�� ���̾ƿ��� null�� ����
		backgroundPanel[0].setPreferredSize(new Dimension(200, 500));
		
		int menuBtnNum=4, menuWidth = 200,menuHeight = 130;
		
		
/*�̹���*/ImageIcon[] image = new ImageIcon[4];
/*�̹���*/for(int i = 0; i < 4; i++) {
/*�̹���*/	image[i] = new ImageIcon("menuBtn"+i+".png");	//������Ʈ ������ �ش�Ǵ� �̹��� ���� �̸����� ���� ��
/*�̹���*/}
		
		btnMenu = new JButton[menuBtnNum];
/*�̹���*/btnMenu[0] = new ItemManageBtn(image[0]);  			//��ư�� ������ �߰��ؼ� ������
		btnMenu[1] = new SalesManageBtn("�������"); 			//�޴� ��ư�鿡�� �̹��� �޴� �����ڸ� ���� ���� �־����
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
		
		btnTableEdit= new JButton[2];
		btnTableEdit[0] = new TableAdd("+");
		btnTableEdit[0].setBounds(700,10, 45,35);
		btnTableEdit[1] = new TableDelete("-");
		btnTableEdit[1].setBounds(750,10, 45,35);
		for(int i=0;i<2;i++) {
			backgroundPanel[1].add(btnTableEdit[i]);
			btnTableEdit[i].setVisible(false);
			btnTableEdit[i].addActionListener(this);
		}
		table = AppManager.getInstance().getTableDao().getTableList();
		if(table == null) {
			table = new Vector<TableBtn>();
		}
		else {
			for(TableBtn tb : table) {
				backgroundPanel[1].add(tb);
				tb.addActionListener(this);
				tb.updateUI();
				
			}
		}
		AppManager.getInstance().setTableArray(table);
		
		
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
		
		int txtNum=3,  txtGap=48, txtH = 24;
		
		lblCash = new JLabel[txtNum];
		lblCash[0] = new JLabel("���� �ݾ�");
		lblCash[1] = new JLabel("���� �ݾ�");
		lblCash[2] = new JLabel("��       ��");
		txfCash = new JTextField[txtNum];
		
		for(int i =0;i<txtNum;i++) {
			lblCash[i].setBounds(25,24+txtGap*i, 100,txtH);
			txfCash[i] = new JTextField("0");
			txfCash[i].setBounds(125,24+txtGap*i,175,txtH);
			txfCash[i].setEditable(false);
			belowPanel[0].add(lblCash[i]);
			belowPanel[0].add(txfCash[i]);
		}
		
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
		pad= new NumPadBtn[12];
		for(int i=0; i<12; i++) {	//pad[i].setSize(125, 40);
			if(i==9) { pad[i] = new NumPadBtn("00");}
			else if(i==10) { pad[i] = new NumPadBtn("0");}
			else if(i==11) { pad[i] = new NumPadBtn("C");}
			else { pad[i] = new NumPadBtn(""+(i+1));}
			belowPanel[1].add(pad[i]);
			pad[i].addActionListener(this);
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
	}
//=====================================================================================
//	#get/set�޼���
//=====================================================================================	
	public JPanel getTabelPanel	() {
		return backgroundPanel[1];
	}
	public JTextField[] getTxfCash() {
		return txfCash;
	}
//=====================================================================================
//	#�Լ�
//=====================================================================================	
	public void allTableClean() {
		backgroundPanel[1].setBackground(Color.white);
		for(TableBtn tb : table){
			tb.setBorderPainted(false);
			tb.setForeground(Color.black);
		}
		if(AppManager.getInstance().getTid()!=-1){
			TableBtn tb = table.get(AppManager.getInstance().getTid());
			tb.setBorderPainted(true);
			tb.setForeground(Color.red);
		}
	}
	
	public void tableArrayRefresh() {
		int index = 0;
		AppManager.getInstance().setTid(-1);
		for(TableBtn tb : table){
			tb.setIndex(index);
			tb.setText("���̺�"+index);
			index++;
		}
	}
	public void tableDeleteMode() {
		int index = 0;
		backgroundPanel[1].setBackground(Color.YELLOW);
		for(TableBtn tb : AppManager.getInstance().getTableArray()){
			tb.setBorderPainted(true);
		}
	}
	
//=====================================================================================
//	#�׼��̺�Ʈ �ڵ鸵 & ��Ÿ Ŭ����
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
		public ItemManageBtn(ImageIcon i){
			
			this.setIcon(i);
			
		}
		public void doAction() {
			AppManager.getInstance().getChickenDialog().setMode(1);
		}
	}
	
	public class SalesManageBtn extends JButton implements EventAction{
		public SalesManageBtn(String s){
			this.setText(s);
		}
		public SalesManageBtn(ImageIcon i){
			this.setIcon(i);
		}
		public void doAction() {
			AppManager.getInstance().getChickenDialog().setMode(2);
		}
	}
	
	public class TableEditBtn extends JButton implements EventAction{
		public TableEditBtn(String s){
			this.setText(s);
		}
		public TableEditBtn(ImageIcon i){
			this.setIcon(i);
		}
		public void doAction() {
			if(AppManager.getInstance().getFrameMode() == 0) {
				AppManager.getInstance().setFrameMode(1);
				allTableClean();
				for(int i=0;i<2;i++)
					btnTableEdit[i].setVisible(true);
				for(TableBtn tb : table)
					tb.setEnabled(false);
			}
			else if(AppManager.getInstance().getFrameMode() == 1) {
				AppManager.getInstance().setFrameMode(0);
				for(int i=0;i<2;i++)
					btnTableEdit[i].setVisible(false);
				for(TableBtn tb : table)
					tb.setEnabled(true);
				AppManager.getInstance().getTableDao().insertTable(table);
			}
			else if(AppManager.getInstance().getFrameMode() == 2) {
				AppManager.getInstance().setFrameMode(0);
				for(int i=0;i<2;i++)
					btnTableEdit[i].setVisible(false);
				for(TableBtn tb : table)
					tb.setEnabled(true);
				allTableClean();
			}
		}
	}
	public class OptionBtn extends JButton implements EventAction{
		public OptionBtn(String s){
			this.setText(s);
		}
		public OptionBtn(ImageIcon i){
			this.setIcon(i);
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
			
			if(table.size() < tableMax){
				TableBtn t = new TableBtn(table.size());
				t.addActionListener(AppManager.getInstance().getChickenMain());
				t.setEnabled(false);
				table.add(t);
				backgroundPanel[1].add(t);
				t.setVisible(true);
				backgroundPanel[1].repaint();
			}
		}
	}
	public class TableDelete extends JButton implements EventAction{
		public TableDelete(String s){
			this.setText(s);
		}

		@Override
		public void doAction() {
			if(AppManager.getInstance().getFrameMode() != 2) {
				AppManager.getInstance().setFrameMode(2);
				tableDeleteMode();
			}
			else
			{
				AppManager.getInstance().setFrameMode(1);
				allTableClean();
			}
		}
	}
	public class NumPadBtn extends JButton implements EventAction{
		String title;
		int x;
		String cash;
		JTextField[] txtCash;
		
		public NumPadBtn(String s) {
			title = s;
			this.setText(title);
			txtCash=AppManager.getInstance().getChickenMain().getTxfCash();
		}
		@Override
		public void doAction() {
			cash=txtCash[1].getText();
			x=Integer.parseInt(cash);
			
			if(title.equals("00")) {
				x*=100;
				txtCash[1].setText(""+x);	
			}
			else if(title.equals("C")) {
				x=0;
				txtCash[1].setText(""+x);
			}
			else {
				x=x*10+Integer.parseInt(title);
				txtCash[1].setText(""+x);
			}
			
			x=Integer.parseInt(txtCash[0].getText())-x;
			txtCash[2].setText(""+(-x));	
		}
	}
	
}