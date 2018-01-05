package fina;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.jfree.chart.*;

public class ChickenDialog extends JDialog implements ActionListener {
	// ��ư�� ���� UI�� �ҷ����� ���� mode����
	private int mode;

	// ȭ�� ���� ��ȯ�� ���� ī�� ���̾ƿ�
	private Container tab;
	private Container menuTab;
	private CardLayout cardLayout;
	private CardLayout cardLayout2;

	// �޴���ư�� ��� �ٿ��ߵǴ� �޴����гΰ� ��ư��
	private JPanel menuPanel; // �޴��� �гη� ����

	private MenuBtn itemBtn; // ������ ��ư
	private MenuBtn salesBtn; // ������� ��ư
	private MenuBtn optionBtn; // ȯ�漳�� ��ư

	// Dialog�� �޴��г� �ؿ� ���� �� �޴� �гε� (ī�巹�̾ƿ��� �̿��� �� �гε� �� �ϳ��� ���õ�)
	private JPanel itemPanel; // ��� ���� �г�
	private JPanel salesPanel; // ���� ���� �г�
	private JPanel optionPanel; // ȯ�� ���� �г�
	private JPanel tablePanel;

	// ���̺�UI��
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

	// ���� �׸� �÷��� ����
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
	// #������
	// =====================================================================================
	public ChickenDialog() {
		AppManager.getInstance().setChickenDialog(this);

		// Dialog ������ ����
		this.setLocation(500, 300);
		this.setResizable(false);

		// ȯ�漳���гο� �̹��� ����
		ImageIcon optionIcon = new ImageIcon("optionImage0.png");
		Image optionImg = optionIcon.getImage();

		// Dialog�� �޴��г� �ؿ� ���� �� �޴� �гε� (ī�巹�̾ƿ��� �̿��� �� �гε� �� �ϳ��� ���õ�)
		itemPanel = new JPanel(); // ��� ���� �г�
		salesPanel = new JPanel(); // ���� ���� �г�
		optionPanel = new JPanel() { // ȯ�� ���� �г�
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(optionImg, 0, 0, this);
			}
		};

		tablePanel = new JPanel();

		// �޴���ư�� ��� �ٿ��ߵǴ� �޴����гΰ� ��ư��
		menuPanel = new JPanel(); // �޴��� �гη� ����
		itemBtn = new MenuBtn("������", 1); // ������ ��ư
		salesBtn = new MenuBtn("�������", 2); // ������� ��ư
		optionBtn = new MenuBtn("ȯ�漳��", 3); // ȯ�漳�� ��ư
		menuPanel.setBackground(Color.WHITE);

		// �׼Ǹ����ʿ� ����
		itemBtn.addActionListener(this);
		salesBtn.addActionListener(this);
		optionBtn.addActionListener(this);

		// ī�� ���̾ƿ� ����
		tab = new JPanel();
		cardLayout = new CardLayout();
		tab.setLayout(cardLayout);

		// �� �޴� ��ư �޴��� �гο� ����
		menuPanel.add(itemBtn);
		menuPanel.add(salesBtn);
		menuPanel.add(optionBtn);

		// â ��ȯ�� ���� CardLayout�г� tab�� itemPanel �߰��ϱ�
		tab.add(itemPanel, "item");
		tab.add(salesPanel, "sales");
		tab.add(optionPanel, "option");
		tab.add(tablePanel, "table");

		cardLayout2 = new CardLayout();
		lblTable = new JLabel("���̺� �ֹ���");
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
	// #get/set�޼���
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
	// #�Լ�
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

	// ���̺�UI��
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
		// �޴� ��ư�� �⺻ ���� ����
		itemBtn.setBackground(color1);
		salesBtn.setBackground(color1);
		optionBtn.setBackground(color1);

		if (mode == 1) { // 1: ������UI
			// ������ ��ư�� �÷� ����
			itemBtn.setBackground(color2);
			// ī�� ���̾ƿ����� ������ â ��ȯ
			setTitle("��� ����");
			itemCB.setSelectedIndex(0);
			stockTableData();
			this.setSize(875, 525);
			this.cardLayout2.show(this.menuTab, "menu");
			this.cardLayout.show(this.tab, "item");
		} else if (mode == 2) { // 2: �������UI
			// ������� ��ư�� �÷� ����
			salesBtn.setBackground(color2);

			totalTableData();
			// ī�� ���̾ƿ����� �������â���� ��ȯ�ϱ�
			setTitle("���� ����");
			this.setSize(875, 525);
			this.cardLayout2.show(this.menuTab, "menu");
			this.cardLayout.show(this.tab, "sales");
		} else if (mode == 3) { // 3: ȯ�Ἲ��UI
			// ȯ�漳�� ��ư�� �÷� ����
			optionBtn.setBackground(color2);
			// ī�� ���̾ƿ����� ȯ�漳��â���� ��ȯ�ϱ�
			setTitle("ȯ�� ����");
			this.setSize(875, 525);
			txfOption.setText("" + AppManager.getInstance().getTimerSet());
			this.cardLayout2.show(this.menuTab, "menu");
			this.cardLayout.show(this.tab, "option");
		} else if (mode == 4) { // 4: ���̺� �ֹ��� UI
			this.setTitle("���̺� �ֹ���");
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
		itemTA.append("�̸�\t�Ǹŷ�\t���\t����\n"); // ����� �������ֱ� ���Ͽ� �߰�
		datas = AppManager.getInstance().getDAOManger().getStockTable();
		if (datas != null) { // ����� �����Ͱ� �ִ� ���
			for (DayList d : datas) { // �������� ���̱���
				StringBuffer sb = new StringBuffer(); // �����͸� ������ ���ۻ���
				sb.append(d.getName() + "\t");
				sb.append(d.getSales() + "\t");
				sb.append(d.getStock() + "\t");
				sb.append(d.getPrice() + "\n");
				itemTA.append(sb.toString()); // �ϳ��� ���ڿ��� ��� �߰��Ѵ�.
			}
		}
	}

	public void chickenTableData(int index) { // 1���� ġŲ���̺� ���� ��¥,���Ǹŷ�,���Ǹž� �� ��� �Լ�
		itemTA.append("\n��¥\t���Ǹŷ�\t���Ǹž� \n"); // ����� �������ֱ� ���Ͽ� �߰�
		datas = AppManager.getInstance().getDAOManger().getChickenTable(index);
		if (datas != null) { // ����� �����Ͱ� �ִ� ���
			for (DayList d : datas) { // �������� ���̱���
				StringBuffer sb = new StringBuffer(); // �����͸� ������ ���ۻ���
				sb.append(d.getDay() + "\t");
				sb.append(d.getSales() + "\t");
				sb.append(d.getSales() * d.getPrice() + "\n");

				itemTA.append(sb.toString()); // �ϳ��� ���ڿ��� ��� �߰��Ѵ�.
			}
		}
	}

	public void totalTableData() {
		salesTA.setText("");
		salesTA.append("��¥\t���Ǹŷ�\t���Ǹž�\n"); // ����� �������ֱ� ���Ͽ� �߰�
		datas = AppManager.getInstance().getDAOManger().getTotalTable();
		if (datas != null) { // ����� �����Ͱ� �ִ� ���
			for (DayList d : datas) { // �������� ���̱���
				StringBuffer sb = new StringBuffer(); // �����͸� ������ ���ۻ���
				sb.append(d.getDay() + "\t");
				sb.append(d.getSales() + "\t");
				sb.append(d.getPrice() + "\n");
				salesTA.append(sb.toString()); // �ϳ��� ���ڿ��� ��� �߰��Ѵ�.

			}
		}
	}

	public void totalTableData(String day) {
		salesTA.setText("");
		salesTA.append("��¥\t���Ǹŷ�\t���Ǹž�\n"); // ����� �������ֱ� ���Ͽ� �߰�
		DayList d = AppManager.getInstance().getDAOManger().getOneTotalTable(day);
		StringBuffer sb = new StringBuffer(); // �����͸� ������ ���ۻ���
		sb.append(d.getDay() + "\t");
		sb.append(d.getSales() + "\t");
		sb.append(d.getPrice() + "\n");
		salesTA.append(sb.toString()); // �ϳ��� ���ڿ��� ��� �߰��Ѵ�.
	}

	// =====================================================================================
	// #UI���� �Լ�
	// =====================================================================================
	// ��� ���� â UI
	public void ItemManagerUI() {
		// â ���� ����
		setTitle("��� ����");

		// �ؽ�Ʈ�ʵ� �г� p1 ����
		JPanel p1 = new JPanel();
		// ��� ��� TextField ����
		itemTA = new JTextArea(23, 40); // ta���� ����
		JScrollPane scroll = new JScrollPane(itemTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		itemTA.setBackground(Color.white);
		stockTableData();

		p1.setLayout(new BorderLayout());
		p1.add(scroll, BorderLayout.CENTER);
		p1.setBackground(Color.white);

		// �Ӽ� ����� �г� p2����
		JPanel p2 = new JPanel(); // p2�г� ����
		p2.setLayout(null); // p2�г� ���̾ƿ� ����
		p2.setBackground(Color.white);

		itemCB = new JComboBox(); // �޴���ȣ �޺��ڽ�
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

		itemTXT = new JTextField[3]; // �޴���,���,���� �ؽ�Ʈ�ʵ�
		for (int i = 0; i < 3; i++) {
			itemTXT[i] = new JTextField();
			itemTXT[i].setBounds(120, 150 + 100 * i, 230, 50);
			p2.add(itemTXT[i]);
		}

		JLabel[] lblItem = new JLabel[4];
		lblItem[0] = new JLabel("�޴���ȣ");// �޴���ȣ ��
		lblItem[1] = new JLabel("�޴���");// �޴��� ��
		lblItem[2] = new JLabel("���");// ��� ��
		lblItem[3] = new JLabel("����");// ���� ��
		for (int i = 0; i < 4; i++) {
			lblItem[i].setBounds(30, 50 + 100 * i, 100, 50);
			p2.add(lblItem[i]);
		}
		p2.setBackground(Color.white);

		// ��ư �г� p3 ����
		JPanel p3 = new JPanel(); // p3�г� ����
		p3.setLayout(new FlowLayout()); // p3�г� �÷ο� ���̾ƿ�
		p3.setBackground(Color.white);

		JButton[] btnItem = new JButton[4];
		btnItem[0] = new RegisterBtn("���"); // ��� ��ư
		btnItem[1] = new EditBtn("����"); // ��ȸ ��ư
		btnItem[2] = new DelItemBtn("����"); // ���� ��ư

		for (int i = 0; i < 3; i++) {
			p3.add(btnItem[i]);
			btnItem[i].addActionListener(this);
			btnItem[i].setBackground(color1);
		}

		// item Panel ���̾ƿ� ���� �� �μ� �гε� ���̱�
		itemPanel.setLayout(new BorderLayout());
		itemPanel.add(p1, BorderLayout.LINE_END); // scroll�г� ������
		itemPanel.add(p2, BorderLayout.CENTER); // p2�г� �߰�
		itemPanel.add(p3, BorderLayout.PAGE_END); // p3�г� �ǾƷ�

		// dialog ���̾ƿ� �� ���� ����
		this.add(menuTab, BorderLayout.PAGE_START);
		// this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);

	}

	// ���� ����â UI
	public void salesManagerUI() {
		// â ����
		setTitle("���� ����");

		// ��¥ �˻� �г� p1 ����
		JPanel p1 = new JPanel();
		JLabel lb1 = new JLabel("�˻� �� ��: ");
		JLabel lb2 = new JLabel("��¥ �˻�: ");
		searchDayTX = new JTextField(3);
		searchTX = new JTextField(15);
		SearchBtn btnSearch = new SearchBtn("�˻�");
		btnSearch.addActionListener(this);

		// p1���̾ƿ�
		p1.setLayout(new FlowLayout());
		p1.add(lb1);
		p1.add(searchDayTX);
		p1.add(lb2);
		p1.add(searchTX);
		p1.add(btnSearch);

		// p1�г� ������ ȭ��Ʈ�� ����
		p1.setBackground(Color.white);
		// �˻���ư ���� color1�� ����
		btnSearch.setBackground(color1);

		// �׷����� ������Ʈ�� �پ��ִ� p2�г� ����
		p2_sales = new JPanel();

		// ���� ��Ʈ ����
		// SalesChart ��ü ����
		salesChart = new SalesChart();
		// JFeeChart ��ü ����
		chart = salesChart.getChart(3, "2018-1-1");
		// ������Ʈ ���� ��Ʈ�г� ����
		gp = new ChartPanel(chart);

		// ���� ��� list�г� ����
		JPanel list = new JPanel();
		salesTA = new JTextArea(20, 35); // ta���� ����
		JScrollPane scroll = new JScrollPane(salesTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		list.add(scroll);

		// p2���̾ƿ�
		p2_sales.setLayout(new GridLayout(1, 2)); // �׸��� ���̾ƿ� ����
		p2_sales.add(list);
		p2_sales.add(gp);

		// p2�г� ������ ȭ��Ʈ�� ����
		chart.setBackgroundPaint(Color.WHITE);
		list.setBackground(Color.white);

		// salesPanel�� �μ� �г� ���̱�
		salesPanel.setLayout(new BorderLayout());
		salesPanel.add(p1, BorderLayout.PAGE_START);
		salesPanel.add(p2_sales, BorderLayout.CENTER);

		// dialog ���̾ƿ� �� ���� ����
		this.add(menuTab, BorderLayout.PAGE_START);
		// this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);

	}

	// ȯ�� ����â UI
	public void OptionUI() {
		// â ����
		setTitle("ȯ�� ����");

		// �� �ؽ�Ʈ�ʵ� ��ư ����
		// JLabel lblOption = new JLabel("��� ���� �ð��� �������ּ���.");
		txfOption = new JTextField();
		JButton btnOption = new JButton("Ȯ��");

		// ���� ����
		optionPanel.setLayout(null);
		// lblOption.setBounds(340,150,300,50);
		txfOption.setBounds(240, 250, 375, 50);
		btnOption.setBounds(320, 350, 210, 50);

		// ȯ�漳���г� ���� ȭ��Ʈ�� ����
		// optionPanel.setBackground(Color.white);
		// Ȯ�� ��ư ���� color1�� ����
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

		// dialog ���̾ƿ� �� ���� ����
		this.add(menuTab, BorderLayout.PAGE_START);
		// this.add(menuPanel,BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);

	}

	// ���̺� UI
	public void TableUI() {

		this.setTitle("���̺� �ֹ���");
		tablePanel.setLayout(new BorderLayout());

		boxSpace = new JPanel();
		boxSpace.setBackground(Color.white);
		boxSpace.setLayout(null);

		boxUI = new ArrayList<BoxLabel>();
		boxUI.add(new BoxLabel(0));

		for (BoxLabel bp : boxUI)
			boxSpace.add(bp);

		receiptBtn = new JButton[5];

		receiptBtn[0] = new ApplyBtn("Ȯ��");
		receiptBtn[1] = new CashBtn("����");
		receiptBtn[2] = new InsertBtn("�߰�");
		receiptBtn[3] = new DeleteBtn("����");
		receiptBtn[4] = new ServingBtn("����");

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
				"	�ε���                          ǰ���                                             ����                          ����");
		lblColumn.setBounds(2, 1, 450, 25);
		boxSpace.add(lblColumn);

		JPanel pricePanel = new JPanel();
		pricePanel.setSize(400, 50);
		pricePanel.setBackground(color1);

		JLabel lblPrice = new JLabel("�� ����");
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
	// #�׼��̺�Ʈ �ڵ鸵 & ��Ÿ Ŭ����
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
				// ������Ʈ ���� ��Ʈ�г� ����
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