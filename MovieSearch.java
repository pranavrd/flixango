import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.flixango.models.Movie;
import com.flixango.models.User;

import javax.swing.JTextField;
import java.awt.TextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class MovieSearch extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MovieSearch frame = new MovieSearch();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MovieSearch(User u) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		TextArea textArea = new TextArea();
		textArea.setBounds(29, 91, 820, 434);
		contentPane.add(textArea);
		
		textField = new JTextField();
		textField.setBounds(172, 11, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		JButton btnBack = new JButton("back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Home hf=new Home(u);
				hf.setVisible(true);
				setVisible(false);
			}
		});
		btnBack.setBounds(10, 0, 55, 20);
		contentPane.add(btnBack);
		
		JButton btnSearch = new JButton("search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
				Class.forName("oracle.jdbc.driver.OracleDriver");
		        Connection con;
		        con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:XE", "system", "root");
				ArrayList<Movie> mlist=new ArrayList<Movie>();
			    mlist=Movie.findByName(con, textField.getText());
			    for(Movie mk : mlist)
			    {
			     textArea.setText(mk.toString());
			    }
				}
				catch(SQLException x)
				{
					x.printStackTrace();
				}
				catch(Exception x)
				{
				  x.printStackTrace();
				}
			}
		});
		btnSearch.setBounds(286, 10, 89, 23);
		contentPane.add(btnSearch);
	}
}
