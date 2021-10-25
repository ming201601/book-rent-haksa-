import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class HackSa extends JFrame {
   
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
   //JLabel imgeLabel = null;
   
   Connection conn = null;
   
   Statement stmt = null;
   
   JTable table = null;
   DefaultTableModel model = null;
   
   static JPanel panel;
   
//   타이틀 그림 설정 class
   class TitleLogo extends JPanel{
       /**
	 * 
	 */
	private static final long serialVersionUID = -1192375206466733183L;
	/*
	 * ImageIcon icon = new ImageIcon("img/titleLogo.jpeg"); Image img =
	 * icon.getImage();
	 */
       
       public void paintComponent(Graphics g){
           super.paintComponent(g);
			/* g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this); */
       }
   }
   
   public HackSa() {
      this.setTitle("학사 관리");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
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
      //db close, 윈도우가 종료 될때 실행
      this.addWindowListener(new WindowListener() {
         
         @Override
         public void windowOpened(WindowEvent e) {}
         
         @Override
         public void windowIconified(WindowEvent e) {}
         
         @Override
         public void windowDeiconified(WindowEvent e) {}
         
         @Override
         public void windowDeactivated(WindowEvent e) {}
         
         @Override
         public void windowClosing(WindowEvent e) {
            // 윈도우가 close 될 때 
            try {
               if(conn != null) {
                  conn.close();
               }
            } catch(Exception e1) {
               e1.printStackTrace();
            }
            
         }
         @Override
         public void windowClosed(WindowEvent e) {}
         
         @Override
         public void windowActivated(WindowEvent e) {}
      });
      
      JMenuBar bar=new JMenuBar();
      JMenu m_student=new JMenu("학생관리");//File메뉴
      bar.add(m_student);
      JMenu m_book=new JMenu("도서관리");
      bar.add(m_book);
      JMenu m_chart=new JMenu("대출 현황");
      bar.add(m_chart);
     
      JMenuItem mi_list=new JMenuItem("학생정보"); // 학생관리 부가 메뉴
      m_student.add(mi_list);
      
      mi_list.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
	          panel.removeAll(); //모든컴포넌트 삭제
	          panel.revalidate(); //다시 활성화
	          panel.repaint();    //다시 그리기
	          panel.add(new Student()); //화면 생성.
	          panel.setLayout(null); //레이아웃적용안함
	          setSize(316, 550);
		}
	});
      
      
      JMenuItem mi_book = new JMenuItem("모든 도서"); // 도서관리 부가 메뉴 1
      m_book.add(mi_book);
      mi_book.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			panel.removeAll(); //모든컴포넌트 삭제
            panel.revalidate(); //다시 활성화
            panel.repaint();    //다시 그리기
            panel.add(new Book()); //화면 생성.
            panel.setLayout(null); //레이아웃적용안함
            setSize(500, 400);
			
		}
	});
      
      JMenuItem mi_bookRent = new JMenuItem("대출목록"); // 도서관리 부가 메뉴 2
      m_book.add(mi_bookRent);
      mi_bookRent.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            panel.removeAll(); //모든컴포넌트 삭제
            panel.revalidate(); //다시 활성화
            panel.repaint();    //다시 그리기
            panel.add(new BookRent()); //화면 생성.
            panel.setLayout(null); //레이아웃적용안함
            setSize(500, 400);
            
         }
      });
      
      JMenuItem mi_bookRead = new JMenuItem("학과별 대출");
      m_chart.add(mi_bookRead);
      mi_bookRead.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            panel.removeAll(); //모든컴포넌트 삭제
            panel.revalidate(); //다시 활성화
            panel.repaint();    //다시 그리기
            try {
				panel.add(new BookChart());
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
            panel.setLayout(null); //레이아웃적용안함
            setSize(710, 500);
           
            
         }
      });
      
//    글자 크기 및 폰트
      JLabel titleLabel = new JLabel("학사관리 프로그램", SwingConstants.CENTER);
      Font font = new Font("맑은 고딕", Font.PLAIN, 40);
      titleLabel.setFont(font);
      
//      메뉴바 추가
      this.setJMenuBar(bar);
      
//     타이틀 이미지 및 글자 추가
      panel = new TitleLogo();
      panel.add(titleLabel);
      
      add(panel);
      
      this.setSize(700, 500);
      this.setVisible(true);
   }
   
   public static void main(String[] args) {
      new HackSa();

   }
   
}
