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
		
		super(frame, title, true); //모달리스
		
		try {
	         //DB연결
	         Class.forName("oracle.jdbc.driver.OracleDriver");
	         conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ora_user", "hong");
	         //System.out.println("연결완료");
	         stmt=conn.createStatement();
	         }
	      catch(Exception e) {
	         e.printStackTrace();
	         }  
		
		setLayout(new FlowLayout());
		JLabel nameLabel = new JLabel("이름");
		nameLabel.setBounds(10, 10, 80, 20);
	    add(nameLabel);
		add(name);
		
		JLabel idLabel = new JLabel("학번");
		idLabel.setBounds(40, 10, 80, 20);
	    add(idLabel);
		add(id);
		add(okBtn);
		setSize(400, 90);
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
					if(name.getText().length()==0) {
						JOptionPane.showMessageDialog(null, "이름을 등록해주세요.");
						return;
					}
					if(id.getText().length()==0) {
						JOptionPane.showMessageDialog(null, "학번을 입력해주세요");
						return;
					}
					
					JOptionPane.showMessageDialog(null, "대출 되었습니다.");
					
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
					
					HackSa.panel.removeAll(); //모든컴포넌트 삭제
					HackSa.panel.revalidate(); //다시 활성화
					HackSa.panel.repaint();    //다시 그리기
					HackSa.panel.add(new Book()); //화면 생성.
					HackSa.panel.setLayout(null); //레이아웃적용안함
					
					setVisible(false);
				}
		});
	}
}