package fina;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ClientDialog extends JDialog implements ActionListener {

	// 메뉴버튼에 모두 붙여야되는 메뉴바패널과 버튼들
	private JPanel menuPanel; // 메뉴바 패널로 생성

	private JPanel tablePanel;

	// 테이블UI용
	private JPanel boxSpace;
	private int selectedTid;
	private ArrayList<BoxLabel> boxUI;
	private JLabel lblTable;
	private JTextField tfTotalPrice;
	private ClientTableBtn selectedTable;

	private JButton[] receiptBtn;
	private JTextField txfOption;

	private JComboBox itemCB;
	private JTextField[] itemTXT;
	private JTextArea itemTA;

	// 사용될 테마 컬러들 정의
	private Color color1 = new Color(255, 218, 175);
	private Color color2 = new Color(255, 144, 0);

	// =====================================================================================
	// #생성자
	// =====================================================================================
	public ClientDialog() {
		ClientAppManager.getInstance().setChickenDialog(this);

		// Dialog 사이즈 설정
		this.setLocation(500, 300);
		this.setResizable(false);

		tablePanel = new JPanel();

		this.add(tablePanel);


		lblTable = new JLabel("테이블 주문서");
		lblTable.setHorizontalAlignment(JLabel.CENTER);
		this.add(lblTable);

		// makeUI
		TableUI();

		refreshComboBox();

	}

	// =====================================================================================
	// #get/set메서드
	// =====================================================================================

	public JPanel getTableUI() {
		return boxSpace;
	}

	// =====================================================================================
	// #함수
	// =====================================================================================
	public void refreshComboBox() {
		int index;
		for (BoxLabel bl : boxUI) {
			index = bl.getSelectedBox();
			bl.getMenuBox().setModel(new DefaultComboBoxModel(ClientAppManager.getInstance().getDAOManger().getComboIndex()));
			bl.selectBox(index);
		}
	}

	// 테이블UI용
	public void refreshTable(int index) {

		for (int i = boxUI.size(); i > 0; i--)
			receiptBtn[2].doClick();

		selectedTable = ClientAppManager.getInstance().getTableArray().get(index);
		ArrayList<ReceiptItem> info = selectedTable.getTInfo();

		for (int i = 0; i < selectedTable.getBoxNum() - 1; i++) {
			receiptBtn[1].doClick();
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

		this.setSize(500, 500);
		refreshTable(ClientAppManager.getInstance().getTid());
		this.setVisible(true);
	}

	// =====================================================================================
	// #UI구성 함수
	// =====================================================================================

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

		receiptBtn = new JButton[4];

		receiptBtn[0] = new ApplyBtn("확인");
		receiptBtn[1] = new InsertBtn("추가");
		receiptBtn[2] = new DeleteBtn("삭제");
		receiptBtn[3] = new ServingBtn("서빙");

		JScrollPane boxSpaceScroll = new JScrollPane(boxSpace);
		boxSpaceScroll.setBounds(0, 0, 400, 100);
		boxSpaceScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		boxSpaceScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel btnSpace = new JPanel();
		btnSpace.setSize(400, 50);
		btnSpace.setBackground(color1);

		for (int i = 0; i < 4; i++) {
			btnSpace.add(receiptBtn[i]);
			receiptBtn[i].addActionListener(this);
			receiptBtn[i].setBackground(color2);
		}

		JLabel lblColumn = new JLabel("	인덱스                          품목명                                             갯수                          가격");
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

		this.add(lblTable, BorderLayout.PAGE_START);
		this.add(tablePanel,BorderLayout.CENTER);
		 

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

			addBtn.addActionListener(ClientAppManager.getInstance().getChickenDialog());
			minusBtn.addActionListener(ClientAppManager.getInstance().getChickenDialog());

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
			if (menu.getSelectedIndex() >= 0) {
				price.setText("" + (Integer.parseInt(amount.getText())
						* ClientAppManager.getInstance().getDAOManger().getPrice(menu.getSelectedIndex())));
			}
			else
				price.setText("0");

			for (BoxLabel bl : boxUI)
				pr += bl.getPrice();
			tfTotalPrice.setText("" + pr);
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
				ClientAppManager.getInstance().getClient().msgType(1);
				ClientAppManager.getInstance().getClient().msgType(3);
				selectedTable.timerStart();
			}
			ClientAppManager.getInstance().getChickenDialog().setVisible(false);
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
				ClientAppManager.getInstance().getChickenDialog().getTableUI().add(bp);

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
				ClientAppManager.getInstance().getChickenDialog().getTableUI().remove(bp);
				ClientAppManager.getInstance().getChickenDialog().getTableUI().repaint();
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
			ClientAppManager.getInstance().getClient().msgType(2);
			selectedTable.timerOff();
		}
	}

}
