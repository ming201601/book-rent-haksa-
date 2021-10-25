import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;



public class Book extends JPanel {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
DefaultTableModel model = null;
   JTable table = null;
   Connection conn = null;
   JButton btn = null;
   Statement stmt;  
   String query; // sql���� �ҷ��´�.
   
   static String bookCode;

   public Book() {
      
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

      setLayout(null);//���̾ƿ�����. ���̾ƿ� ��� ����.
      
      JLabel l_dept=new JLabel("��� ����");
      l_dept.setBounds(10, 10, 80, 20);
      add(l_dept);
  
      String colName[]={"��ȣ","����","����","���ǻ�", "���� ��Ȳ", "�����ϱ�"}; 
      
      model=new DefaultTableModel(colName,0);
      table = new JTable(model);
      
      table.getColumnModel().getColumn(5).setCellRenderer(new TableCell());;
      table.getColumnModel().getColumn(5).setCellEditor(new TableCell());;
      
      table.setPreferredScrollableViewportSize(new Dimension(470,200));
      add(table);
      JScrollPane sp=new JScrollPane(table);
      sp.setBounds(10, 40, 460, 250);
      add(sp);  
      
      this.table.addMouseListener(new MouseListener() {
          
          @Override
          public void mouseReleased(MouseEvent e) {}
          
          @Override
          public void mousePressed(MouseEvent e) {
        	// ���� ���õ� ���� �÷����� ���Ѵ�.
              bookCode = (String)model.getValueAt(table.getSelectedRow(), 0); // å��ȣ
          }
          
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
          }
       });
      
      query = "select b.no, b.TITLE, b.AUTHOR, b.publisher, b.loan " 
         		+ "from books b order by b.no";  
      list(); // ��� ���
      setSize(490, 400);//ȭ��ũ��
      setVisible(true);
      
      repaint();
   }
   
   public void list(){
       try{
           System.out.println("����Ǿ����ϴ�.....");
           System.out.println(query);       //�������� ����غ��� 
           
           // Select�� ����     
           ResultSet rs=stmt.executeQuery(query);
          
           //JTable �ʱ�ȭ
           model.setNumRows(0);
       
           while(rs.next()){
        	   String[] row=new String[6];//�÷��� ������ 5
        	   row[0]=rs.getString("no");
        	   row[1]=rs.getString("TITLE");
        	   row[2]=rs.getString("AUTHOR");
        	   row[3]=rs.getString("publisher");
        	   row[4]=rs.getString("loan");
        	   model.addRow(row);
           }
           rs.close();
       }
       catch(Exception e1){
        //e.getStackTrace();
    	  System.out.println(e1.getMessage());
       }                     
    }
   
   class TableCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
	   	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		JButton btn;
		HackSa hacksa;
		String bookCode;
		
		public TableCell() {
			btn = new JButton("����");
			// �� �κп��� ��ư�� ������ ��� �̺�Ʈ�� �߻� ��Ų��
			btn.addActionListener(new ActionListener() {
				RentLogin dialog = new RentLogin(hacksa, "�α���");
				
				@Override
				public void actionPerformed(ActionEvent e) {
					bookCode = (String)model.getValueAt(table.getSelectedRow(), 0); // �й�
					dialog.setVisible(true);
					System.out.println(bookCode);
				}
			});
		
		}
		
		@Override
		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			return btn;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			return btn;
		}
		
	}
}