package fina;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.google.gson.Gson;

public class ChickenClient extends JFrame implements ActionListener, Runnable {

	private JPanel tablePanel;
	private ArrayList<ClientTableBtn> table;

	private Socket socket = null; // �ܺο� ����� ���� ���� ��ü
	private String ip = "127.0.0.1"; // ������ ������ ip�ּ� ���� ������ String ��ü

	private BufferedReader inMsg = null; // ������ ���ؼ� ������ ���� ������ ��� ��ü
	private PrintWriter outMsg = null; // ������ ���ؼ� ������ �޼����� ������ ��� ��ü

	private Thread connThread;
	private Gson gson;

	private boolean status;
	private String msg;
	private String msgType;
	private Message m;
	private ArrayList<ReceiptItem> info;	//�ӽ÷� ���

	public ChickenClient() {

		ClientAppManager.getInstance().setClient(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("ġŲ�� ���� ���α׷�");

		gson = new Gson();

		makeUI();

		this.pack();
		this.setVisible(true);

	}

	public void makeUI() {

		tablePanel = new JPanel();

		tablePanel.setLayout(null);
		tablePanel.setPreferredSize(new Dimension(810, 510));
		tablePanel.setBackground(Color.white);
		tablePanel.setBorder(new TitledBorder(new LineBorder(Color.black)));

		connectServer();

		table = ClientAppManager.getInstance().getTableDao().getTableList();
		if (table == null) {
			table = new ArrayList<ClientTableBtn>();
		} else {
			for (ClientTableBtn tb : table) {
				tablePanel.add(tb);
				tb.addActionListener(this);
				tb.updateUI();
			}
		}
		ClientAppManager.getInstance().setTableArray(table);

		this.add(tablePanel);
	}

	// =====================================================================================
	// #�Լ�
	// =====================================================================================
	public void allTableClean() {
		tablePanel.setBackground(Color.white);
		for (ClientTableBtn tb : table) {
			tb.setBorderPainted(false);
			tb.setForeground(Color.black);
		}
		if (AppManager.getInstance().getTid() != -1) {
			ClientTableBtn tb = table.get(ClientAppManager.getInstance().getTid());
			tb.setBorderPainted(true);
			tb.setForeground(Color.red);
		}
	}

	public void tableArrayRefresh() {
		int index = 0;
		AppManager.getInstance().setTid(-1);
		for (ClientTableBtn tb : table) {
			tb.setIndex(index);
			tb.setText("���̺�" + index);
			index++;
		}
	}
	// =====================================================================================
	// #�׼��̺�Ʈ
	// =====================================================================================

	@Override
	public void actionPerformed(ActionEvent e) {
		EventAction obj = (EventAction) e.getSource();

		obj.doAction();
	}

	// =====================================================================================
	// #��Ű��� �Լ�
	// =====================================================================================
	public boolean connectServer() { // ���� ����/���� ���θ� ��ȯ�ϴ� ����, ���� �޼��� Ÿ���� �Ű� ������ �޴´�.
		try {
			socket = new Socket(ip, 7890); // ���� ����

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())); // �����κ��� �Է¹޴� ��Ʈ�� ����
			outMsg = new PrintWriter(socket.getOutputStream(), true); // ������ ����ϴ� ��Ʈ�� ����

			threadStart();

			return true; // ���� �������� true�� ��ȯ

		} catch (Exception e) {
			e.printStackTrace(); // ���ܹ߻�
			return false; // ���� ���з� false ��ȯ
		}
	}

	public void threadStart() {
		if (connThread == null) {
			connThread = new Thread(this);
			connThread.start();
		}
	}

	public void sendMsg(String msg) {
		outMsg.println(msg);
	}
	
	public void msgType(int type) {
		String msg;
		
		if(info == null)
			info=new ArrayList<ReceiptItem>();
		
		// �ֹ��� ���
		if (type == 1) {
			int index = ClientAppManager.getInstance().getTid();
			ClientTableBtn tb = ClientAppManager.getInstance().getTableArray().get(index);
			msg = gson.toJson(new Message("Order", index, tb.getBoxNum(), tb.getTInfo()));
			sendMsg(msg);
		}
		// ����
		else if (type == 2) {
			int index = ClientAppManager.getInstance().getTid();
			msg = gson.toJson(new Message("timerOff", index, 0, info));
			sendMsg(msg);
		}
		// �ֹ��� ���� �� ����
		else if (type == 3) {
			int index = ClientAppManager.getInstance().getTid();
			ClientTableBtn tb = ClientAppManager.getInstance().getTableArray().get(index);
			msg = gson.toJson(new Message("orderPriceEdit", index, tb.getPrice(), info));
			sendMsg(msg);
		}
	}

	public void run() {
		status = true;

		while (status) { // ������ ����� ���¸� ���� ����

			try {
				msg = inMsg.readLine(); // �Է� ��Ʈ���� ���� �޼��� ����
				m = gson.fromJson(msg, Message.class); // ���ŵ� �޼����� �Ľ�

				System.out.println(msg);
				// ���� �޼��� ������ ���̵�
				msgType = m.getType();

				// ���̺� ���� ���
				if (msgType.equals("tableArrange")) {
					
					for (ClientTableBtn tb : table) {
						tb.timerOff();
						tb.removeActionListener(this);
						tablePanel.remove(tb);
					}
					table = null;
					tablePanel.repaint();

					table = ClientAppManager.getInstance().getTableDao().getTableList();
					ClientAppManager.getInstance().setTableArray(table);

					for (ClientTableBtn tb : table) {
						tablePanel.add(tb);
						tb.addActionListener(this);
						tb.updateUI();
					}
					tablePanel.repaint();
					
				}
				// Ÿ�̸� ���� ���
				else if (msgType.equals("timerSet")) {
					ClientAppManager.getInstance().setTimerSet(m.getNum());
				}
				// �ֹ��� ���� ���
				else if (msgType.equals("Order")) {
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setBoxNum(m.getNum());
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setTInfo(m.getTInfo());
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).timerStart();
				}
				// �޺��ڽ� ���� ���
				else if (msgType.equals("getItem")) {
					ClientAppManager.getInstance().getChickenDialog().refreshComboBox();
				}
				// ���� ���
				else if (msgType.equals("timerOff")) {
					if(m.getIndex()!=-1)
						ClientAppManager.getInstance().getTableArray().get(m.getIndex()).timerOff();
				}
				// �ֹ��� ���� ����
				else if (msgType.equals("orderPriceEdit")) {
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setPrice(m.getNum());
				}
				// �ֹ��� ���� ����
				else if (msgType.equals("pay")) {
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setPrice(0);
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setTInfo(null);
					ClientAppManager.getInstance().getTableArray().get(m.getIndex()).setBoxNum(0);
				}

			} catch (Exception e) {
				e.printStackTrace();
				status=false;
				break;
			}
		}

		// ���� ����� ���� ����
		status=false;
		outMsg.close();
		try {
			inMsg.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		connThread.interrupted();
	}

}
