import java.awt.Font;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.jdbc.JDBCPieDataset;

public class BookChart extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection conn=null;
	Statement stmt;  
	
	public BookChart() throws ClassNotFoundException, SQLException {
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:orcl","ora_user","hong");
		
		DefaultPieDataset pieDataset = new JDBCPieDataset(
				conn, "select s.dept, count(r.no) as book_dept "
			        	+ "from student s, bookrent r "
			        	+ "where s.id = r.id "
			        	+ "group by s.dept");
		
		//Create the chart
		JFreeChart chart = ChartFactory.createPieChart("ÇÐ°úº° ´ëÃâ", pieDataset, true, true, true);
		
		chart.getTitle().setFont(new Font("µ¸¿ò", Font.BOLD, 20));
		chart.getLegend().setItemFont(new Font("µ¸¿ò", Font.PLAIN, 10));

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("µ¸¿ò", Font.PLAIN, 12));

		//Render the frame
		ChartPanel chartPanel = new ChartPanel(chart);
		add(chartPanel);
		
		setVisible(true);
		setSize(685, 1000);
		
	}
}
