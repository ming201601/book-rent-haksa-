import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

class RentLogin extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField name = new JTextField(10);
	private JTextField id = new JTextField(10);
	private JButton okBtn = new JButton("Ok");
	
	Connection conn = null;
	JButton btn = null;
	Statement stmt;  
	String query;
	
	
	public RentLogin(JFrame frame, String title) {
		
		super(frame, title, true); //��޸���
		
		try {
	         //DB����
	         Class.forName("oracle.jdbc.driver.OracleDriver");
	         conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ora_user", "hong");
	         //System.out.println("����Ϸ�");
	         stmt=conn.createStatement();
	         }
	      catch(Exception e) {
	         e.printStackTrace();
	         }  
		
		setLayout(new FlowLayout());
		JLabel nameLabel = new JLabel("�̸�");
		nameLabel.setBounds(10, 10, 80, 20);
	    add(nameLabel);
		add(name);
		
		JLabel idLabel = new JLabel("�й�");
		idLabel.setBounds(40, 10, 80, 20);
	    add(idLabel);
		add(id);
		add(okBtn);
		setSize(400, 90);
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
					if(name.getText().length()==0) {
						JOptionPane.showMessageDialog(null, "�̸��� ������ּ���.");
						return;
					}
					if(id.getText().length()==0) {
						JOptionPane.showMessageDialog(null, "�й��� �Է����ּ���");
						return;
					}
					
					JOptionPane.showMessageDialog(null, "���� �Ǿ����ϴ�.");
					
					try {
		               Statement stmt = conn.createStatement();
		               ResultSet rs = stmt.executeQuery("select*from student where name = '"+name.getText()+"'"); 
		               
		               System.out.println(Book.bookCode);
		               
		               while(rs.next()) {
		            	   
			               System.out.println(rs.getString("id"));
		            	   stmt.executeUpdate("insert into bookrent values(NO.nextval,'"+rs.getString("id")+"','"+Book.bookCode+"', sysdate)");
		            	   
		               }
		               
		               rs.close();
		               stmt.close();
		               
		            } catch (Exception e1) {
		               e1.printStackTrace();
		            } 
					
					HackSa.panel.removeAll(); //���������Ʈ ����
					HackSa.panel.revalidate(); //�ٽ� Ȱ��ȭ
					HackSa.panel.repaint();    //�ٽ� �׸���
					HackSa.panel.add(new Book()); //ȭ�� ����.
					HackSa.panel.setLayout(null); //���̾ƿ��������
					
					setVisible(false);
				}
		});
	}
}