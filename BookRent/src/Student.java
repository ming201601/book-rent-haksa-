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
JTextField tfId = null; // ���������� �����Ѵ�.
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
         //oracle jdbc����̹� �ε�
         Class.forName("oracle.jdbc.driver.OracleDriver");// jdbc driver load, ��Ű��.CLASS�̸�
         //Connection
         conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","ora_user","hong");// xe : ���� �����ͺ��̽� �̸�(sid), ora_user: ID, hong:PW
         //
         System.out.println("����Ϸ�");
         
         
      } catch(Exception e) {
         e.printStackTrace();
      }
      
      setLayout(new FlowLayout());
      
      add(new JLabel("�й�"));
      this.tfId = new JTextField(17);
      add(this.tfId);
      
      this.btnSearch = new JButton("�˻�");
      add(this.btnSearch);
      
      this.btnSearch.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Statement stmt = conn.createStatement();
               
               
               //��� �ʱ�ȭ
               model.setNumRows(0); // model�� ���� ���� 0����
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
      
      this.add(new JLabel("�̸�"));
      this.tfName = new JTextField(23);
      this.add(tfName);
      
      this.add(new JLabel("�а�"));
      this.tfDepart = new JTextField(23);
      this.add(this.tfDepart);
      
      this.add(new JLabel("�ּ�"));
      this.tfAddress = new JTextField(23);
      this.add(this.tfAddress);
      
      String [] colName = {"�̸�", "�й�", "�а�", "�ּ�"}; // colum�� �����
      this.model = new DefaultTableModel(colName, 0); // model�� �����
      this.table = new JTable(this.model); // table�� �����
      this.table.setPreferredScrollableViewportSize(new Dimension(300, 200)); // �ǳ� ũ�� ����
      this.add(new JScrollPane(this.table)); // table�� �����Ѵ�.
      
      //���̺��� Ư������ �����ؼ� TextFiled�� ���� �Է�
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
            // �̺�Ʈ�� �߻��� ������Ʈ(table)�� ���Ѵ�.
            table = (JTable)e.getComponent();
            
            // table�� ���� ���Ѵ�.
            model = (DefaultTableModel)table.getModel();
            
            // ���� ���õ� ���� �÷����� ���Ѵ�.
            tfName.setText((String)model.getValueAt(table.getSelectedRow(), 0));  // �̸�
            tfId.setText((String)model.getValueAt(table.getSelectedRow(), 1));  // �й�
            tfDepart.setText((String)model.getValueAt(table.getSelectedRow(), 2));  // �а�
            tfAddress.setText((String)model.getValueAt(table.getSelectedRow(), 3));  // �ּ�
            
            nameCheck = (String)model.getValueAt(table.getSelectedRow(), 0);  // �̸�
            idCheck =(String)model.getValueAt(table.getSelectedRow(), 1);  // �й�
         }
      });
      System.out.println("nameCheck : " + nameCheck);
      System.out.println("idCheck : " + idCheck);
      
      this.btnInsert = new JButton("���");
      add(this.btnInsert);
      this.btnInsert.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            
            try {
               if(tfId.getText().equals("")) {
                  JOptionPane.showMessageDialog(null, "�й��� ������ּ���.");
                  return;
               }
               if(tfName.getText().length()==0) {
                  JOptionPane.showMessageDialog(null, "�̸��� ������ּ���.");
                  return;
               }
               if(tfDepart.getText().equals("")) {
                  JOptionPane.showMessageDialog(null, "�а��� ������ּ���.");
                  return;
               }
               if(tfAddress.getText().equals("")) {
                  JOptionPane.showMessageDialog(null, "�ּҸ� ������ּ���.");
                  return;
               }
               JOptionPane.showMessageDialog(null, "��� �Ǿ����ϴ�.");
               Statement stmt = conn.createStatement();
               //insert   �ڹٷ� § �ڵ�� �ڵ����� commit�� �ȴ�.
               stmt.executeUpdate("insert into student( id, name, dept, address) values('"+tfId.getText()+"', '"+tfName.getText()+ "', '" + tfDepart.getText()+"', '"+ tfAddress.getText() +"')"); //  ora_user�� ���̺� ���� ������ ��ġ �ؾ��Ѵ�.
               
               //��� �ʱ�ȭ
               model.setNumRows(0); // ���� ���� �� 0
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
      
      this.btnSelect = new JButton("���");
      add(this.btnSelect);
      this.btnSelect.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Statement stmt = conn.createStatement();
               
               
               model.setNumRows(0); // model�� ���� ���� 0����
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
      
      
      this.btnUpdate = new JButton("����");
      add(this.btnUpdate);
      this.btnUpdate.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Statement stmt = conn.createStatement();

               //update
               stmt.executeUpdate("update student set name='"+ tfName.getText() +"', dept='"+ tfDepart.getText() +"', address='"+ tfAddress.getText() +"' where id = '"+ tfId.getText() +"'"); 
               
               //��� �ʱ�ȭ
               model.setNumRows(0); // model�� ���� ���� 0����
               
               ResultSet rs = stmt.executeQuery("select*from student"); // ora_user������ �ִ� student ���̺��� �д´�.
               
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
      
      this.btnDelete = new JButton("����");
      add(this.btnDelete);
      this.btnDelete.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Statement stmt = conn.createStatement();
               
               //delete
               int result = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "�˸�", JOptionPane.YES_NO_OPTION);
               System.out.println(result); //yes = 0, no = 1, x��ư = -1
               if (result == JOptionPane.YES_OPTION) {
                  JOptionPane.showMessageDialog(null, "���� �Ǿ����ϴ�.");
               }
               
               stmt.executeUpdate("delete from student where id = '"+tfId.getText()+"'");
               
               //��� �ʱ�ȭ
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