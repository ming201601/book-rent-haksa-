import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Student extends JPanel {
   
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTextField tfId = null; // 전역변수로 설정한다.
   JTextField tfName = null;
   JTextField tfDepart = null;
   JTextField tfAddress = null;
   
   JTextArea taList = null;
   
   JButton btnInsert = null;
   JButton btnSelect = null;
   JButton btnUpdate = null;
   JButton btnDelete = null;
   
   JButton btnSearch = null;
   
   Connection conn = null;
   
   Statement stmt = null;
   
   JTable table = null;
   DefaultTableModel model = null;
   
   static String nameCheck;
   static String idCheck;
   
   public Student() {
      //db connection
      try {
         //oracle jdbc드라이버 로드
         Class.forName("oracle.jdbc.driver.OracleDriver");// jdbc driver load, 패키지.CLASS이름
         //Connection
         conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","ora_user","hong");// xe : 전역 데이터베이스 이름(sid), ora_user: ID, hong:PW
         //
         System.out.println("연결완료");
         
         
      } catch(Exception e) {
         e.printStackTrace();
      }
      
      setLayout(new FlowLayout());
      
      add(new JLabel("학번"));
      this.tfId = new JTextField(17);
      add(this.tfId);
      
      this.btnSearch = new JButton("검색");
      add(this.btnSearch);
      
      this.btnSearch.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Statement stmt = conn.createStatement();
               
               
               //목록 초기화
               model.setNumRows(0); // model의 행의 수를 0으로
               ResultSet rs = stmt.executeQuery("select*from student where id = '"+tfId.getText()+"'"); 
               
               
               while(rs.next()) {
                  tfName.setText(rs.getString("name"));
                  tfDepart.setText(rs.getString("dept"));
                  
                  String [] row = new String[4];
                  row[0] = rs.getString("name");
                  row[1] = rs.getString("id");
                  row[2] = rs.getString("dept");
                  row[3] = rs.getString("address");
                  model.addRow(row);
                  System.out.println(row[2]);
               }
               
               rs.close();
               stmt.close();
               
            } catch (Exception e1) {
               e1.printStackTrace();
            } 
         }
      });
      
      this.add(new JLabel("이름"));
      this.tfName = new JTextField(23);
      this.add(tfName);
      
      this.add(new JLabel("학과"));
      this.tfDepart = new JTextField(23);
      this.add(this.tfDepart);
      
      this.add(new JLabel("주소"));
      this.tfAddress = new JTextField(23);
      this.add(this.tfAddress);
      
      String [] colName = {"이름", "학번", "학과", "주소"}; // colum을 만든다
      this.model = new DefaultTableModel(colName, 0); // model을 만든다
      this.table = new JTable(this.model); // table을 만든다
      this.table.setPreferredScrollableViewportSize(new Dimension(300, 200)); // 판넬 크기 설정
      this.add(new JScrollPane(this.table)); // table에 삽입한다.
      
      //테이블의 특정행을 선택해서 TextFiled에 값을 입력
      this.table.addMouseListener(new MouseListener() {
         
         @Override
         public void mouseReleased(MouseEvent e) {}
         
         @Override
         public void mousePressed(MouseEvent e) {}
         
         @Override
         public void mouseExited(MouseEvent e) {}
         
         @Override
         public void mouseEntered(MouseEvent e) {}
         
         @Override
         public void mouseClicked(MouseEvent e) {
            // 이벤트가 발생한 컴포넌트(table)를 구한다.
            table = (JTable)e.getComponent();
            
            // table의 모델을 구한다.
            model = (DefaultTableModel)table.getModel();
            
            // 현재 선택된 행의 컬럼값을 구한다.
            tfName.setText((String)model.getValueAt(table.getSelectedRow(), 0));  // 이름
            tfId.setText((String)model.getValueAt(table.getSelectedRow(), 1));  // 학번
            tfDepart.setText((String)model.getValueAt(table.getSelectedRow(), 2));  // 학과
            tfAddress.setText((String)model.getValueAt(table.getSelectedRow(), 3));  // 주소
            
            nameCheck = (String)model.getValueAt(table.getSelectedRow(), 0);  // 이름
            idCheck =(String)model.getValueAt(table.getSelectedRow(), 1);  // 학번
         }
      });
      System.out.println("nameCheck : " + nameCheck);
      System.out.println("idCheck : " + idCheck);
      
      this.btnInsert = new JButton("등록");
      add(this.btnInsert);
      this.btnInsert.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            
            try {
               if(tfId.getText().equals("")) {
                  JOptionPane.showMessageDialog(null, "학번을 등록해주세요.");
                  return;
               }
               if(tfName.getText().length()==0) {
                  JOptionPane.showMessageDialog(null, "이름을 등록해주세요.");
                  return;
               }
               if(tfDepart.getText().equals("")) {
                  JOptionPane.showMessageDialog(null, "학과를 등록해주세요.");
                  return;
               }
               if(tfAddress.getText().equals("")) {
                  JOptionPane.showMessageDialog(null, "주소를 등록해주세요.");
                  return;
               }
               JOptionPane.showMessageDialog(null, "등록 되었습니다.");
               Statement stmt = conn.createStatement();
               //insert   자바로 짠 코드는 자동으로 commit이 된다.
               stmt.executeUpdate("insert into student( id, name, dept, address) values('"+tfId.getText()+"', '"+tfName.getText()+ "', '" + tfDepart.getText()+"', '"+ tfAddress.getText() +"')"); //  ora_user의 테이블 값과 순서가 일치 해야한다.
               
               //목록 초기화
               model.setNumRows(0); // 모델의 행의 수 0
               ResultSet rs = stmt.executeQuery("select*from student"); 
               
               
               while(rs.next()) {
                  String [] row = new String[4];
                  row[0] = rs.getString("name");
                  row[1] = rs.getString("id");
                  row[2] = rs.getString("dept");
                  row[3] = rs.getString("address");
                  
                  model.addRow(row);
               }
               
               rs.close();
               stmt.close();
               
            } catch (Exception e1) {
               e1.printStackTrace();
            } 
            
         }
      });
      
      this.btnSelect = new JButton("목록");
      add(this.btnSelect);
      this.btnSelect.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Statement stmt = conn.createStatement();
               
               
               model.setNumRows(0); // model의 행의 수를 0으로
               ResultSet rs = stmt.executeQuery("select*from student"); 
               
               while(rs.next()) {
                  String [] row = new String[4];
                  row[0] = rs.getString("name");
                  row[1] = rs.getString("id");
                  row[2] = rs.getString("dept");
                  row[3] = rs.getString("address");
                  model.addRow(row);
               }
               rs.close();
               stmt.close();
              
               
            } catch (Exception e1) {
               e1.printStackTrace();
            } 
            
         }
      });
      
      
      this.btnUpdate = new JButton("수정");
      add(this.btnUpdate);
      this.btnUpdate.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Statement stmt = conn.createStatement();

               //update
               stmt.executeUpdate("update student set name='"+ tfName.getText() +"', dept='"+ tfDepart.getText() +"', address='"+ tfAddress.getText() +"' where id = '"+ tfId.getText() +"'"); 
               
               //목록 초기화
               model.setNumRows(0); // model의 행의 수를 0으로
               
               ResultSet rs = stmt.executeQuery("select*from student"); // ora_user서버에 있는 student 테이블을 읽는다.
               
               while(rs.next()) {
                  String [] row = new String[4];
                  row[0] = rs.getString("name");
                  row[1] = rs.getString("id");
                  row[2] = rs.getString("dept");
                  row[3] = rs.getString("address");
                  
               }
               rs.close();
               stmt.close();
              
               
            } catch (Exception e1) {
               e1.printStackTrace();
            } 
            
         }
      });
      
      this.btnDelete = new JButton("삭제");
      add(this.btnDelete);
      this.btnDelete.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Statement stmt = conn.createStatement();
               
               //delete
               int result = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
               System.out.println(result); //yes = 0, no = 1, x버튼 = -1
               if (result == JOptionPane.YES_OPTION) {
                  JOptionPane.showMessageDialog(null, "삭제 되었습니다.");
               }
               
               stmt.executeUpdate("delete from student where id = '"+tfId.getText()+"'");
               
               //목록 초기화
               model.setNumRows(0);
               
               ResultSet rs = stmt.executeQuery("select*from student where id = '"+tfId.getText()+"'"); 
               
               while(rs.next()) {
                  String [] row = new String[4];
                  row[0] = rs.getString("name");
                  row[1] = rs.getString("id");
                  row[2] = rs.getString("dept");
                  row[3] = rs.getString("address");
                  
               }
               rs.close();
               stmt.close();
               
               tfId.setText("");
               tfName.setText("");
               tfDepart.setText("");
               tfAddress.setText("");
               
            } catch (Exception e1) {
               e1.printStackTrace();
            } 
            
         }
      });
      
      
      this.setSize(300, 500);
      this.setVisible(true);
   }
}