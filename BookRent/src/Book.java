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
   String query; // sql문을 불러온다.
   
   static String bookCode;

   public Book() {
      
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

      setLayout(null);//레이아웃설정. 레이아웃 사용 안함.
      
      JLabel l_dept=new JLabel("모든 도서");
      l_dept.setBounds(10, 10, 80, 20);
      add(l_dept);
  
      String colName[]={"번호","제목","저자","출판사", "대출 현황", "대출하기"}; 
      
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
        	// 현재 선택된 행의 컬럼값을 구한다.
              bookCode = (String)model.getValueAt(table.getSelectedRow(), 0); // 책번호
          }
          
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
          }
       });
      
      query = "select b.no, b.TITLE, b.AUTHOR, b.publisher, b.loan " 
         		+ "from books b order by b.no";  
      list(); // 목록 출력
      setSize(490, 400);//화면크기
      setVisible(true);
      
      repaint();
   }
   
   public void list(){
       try{
           System.out.println("연결되었습니다.....");
           System.out.println(query);       //쿼리문을 출력해본다 
           
           // Select문 실행     
           ResultSet rs=stmt.executeQuery(query);
          
           //JTable 초기화
           model.setNumRows(0);
       
           while(rs.next()){
        	   String[] row=new String[6];//컬럼의 갯수가 5
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
			btn = new JButton("대출");
			// 이 부분에서 버튼을 누르는 경우 이벤트를 발생 시킨다
			btn.addActionListener(new ActionListener() {
				RentLogin dialog = new RentLogin(hacksa, "로그인");
				
				@Override
				public void actionPerformed(ActionEvent e) {
					bookCode = (String)model.getValueAt(table.getSelectedRow(), 0); // 학번
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