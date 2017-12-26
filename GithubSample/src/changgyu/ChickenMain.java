package changgyu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ChickenMain {

	public static class MyFrame extends JFrame implements ActionListener { // JFrame�� ��ӹ޴´�

		String[] prCode = new String[15]; // ��ǰ�ڵ��ȣ�� ������ ���� ����
		JLabel msg = new JLabel("�޼��� ��º�"); // �޼����� ��Ÿ�� ���̺� ����

		JPanel p1 = new JPanel(); 
		JPanel p2 = new JPanel(); 
		JPanel p3 = new JPanel(); 
		JPanel p3_1 = new JPanel();
		JPanel p3_2 = new JPanel();
		
		JButton btn1 = new JButton("BTN1"); 
		JButton btn2 = new JButton("BTN2"); 
		JButton btn3 = new JButton("BTN3"); 
		JButton btn4 = new JButton("BTN4"); 
		JButton btn5 = new JButton("���ݰ���");
		JButton table = new JButton("table");
		//JLabel table = new JLabel("table");
	
		

		boolean editmode; // �������� ���������� �����ϴ� �÷��װ� ���� false=����, true=����

		public MyFrame() { // ������ ����

			
			
			this.setLayout(new BorderLayout()); // BorderLayout���� ���̾ƿ� ����
			setSize(1000, 700); // ����â ũ�� ����
			setLocation(300, 100); // ����â ��ġ����
			setTitle("ġŲ"); // ������ Ÿ��Ʋ ����
			this.setBackground(Color.white);
			
			msg.setHorizontalAlignment(SwingConstants.CENTER); // �޼����� ��ġ�� ����� ����
			msg.setVerticalAlignment(SwingConstants.CENTER);
			this.add(msg, BorderLayout.NORTH); // �޼����� ���̾ƿ��� ���ʿ� ����

			p1.setLayout(null); // �г�1�� ���̾ƿ��� null�� ����
			p1.setPreferredSize(new Dimension(200, 500));
			btn1.setBounds(0, 0, 200, 75); // 1�� ��ġ�� ����
			btn2.setBounds(0, 75, 200, 75); // 2�� ��ġ�� ����
			btn3.setBounds(0, 150, 200, 75); // 3�� ��ġ�� ����
			btn4.setBounds(0, 225, 200, 75); // 4�� ��ġ�� ����
			p1.add(btn1); // 1�� �г�1�� �߰�
			p1.add(btn2); // 2�� �г�1�� �߰�
			p1.add(btn3); // 3�� �г�1�� �߰�
			p1.add(btn4); // 4�� �г�1�� �߰�


			p2.setLayout(null); // �г�2�� ���̾ƿ��� null�� ����
			p2.setPreferredSize(new Dimension(800, 500));
			p2.setBackground(Color.white);
			p2.setBorder(new TitledBorder(new LineBorder(Color.black)));
			table.setBounds(100,100,100,100);
			p2.add(table);
			
	
			p3.setLayout(null); // �г�3�� ���̾ƿ��� null�� ����
			p3.setPreferredSize(new Dimension(1000, 200));
			p3.setBackground(Color.white);
			p3_1.setBounds(0, 0, 500, 200);
			p3_1.setBackground(Color.white);
			
			btn5.setBounds(550, 0, 50, 50);
			p3_1.add(btn5);
			
			p3_2.setBounds(500, 0, 500, 200);
			p3_2.setBackground(Color.black);
			
			
			p3.add(p3_1);
			p3.add(p3_2);
			this.add(p1, BorderLayout.WEST); // �г�1�� ���ʿ� ���δ�
			this.add(p2, BorderLayout.CENTER); // �г�2�� ����� ���δ�
			this.add(p3, BorderLayout.SOUTH); // �г�3�� �Ʒ��ʿ� ���δ�

			btn1.addActionListener(this); 
			btn2.addActionListener(this); 
			btn3.addActionListener(this); 
			table.addActionListener(this); 
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			setResizable(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj =e.getSource();
			if(obj==table) {
				receipt();	
				
			}

		}

		 public void receipt() {
				JDialog dialog = new JDialog(this,"��꼭");
				JButton payBtn = new JButton("����");
				JButton okBtn = new JButton("���");
				JButton[] upBtn = new JButton[3];
				JButton[] downBtn = new JButton[3];
				JTextField[] amount = new JTextField[3];

				
				String[] menuList = {"-------------------------","�Ķ��̵�ġŲ(15000)","���ġŲ(15000)","����ġŲ(15000)"};				
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
				dialog.setSize(300,300);
				dialog.setLocation(300,300);
				dialog.setVisible(true);
				
				
		 }
		
		
		public static void main(String[] args) {
			new MyFrame();
		}//main

	}
}
