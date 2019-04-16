

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.flixango.models.User;
import com.flixango.models.WatchList;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JList;

public class NewWatch extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewWatch frame = new NewWatch();
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
	public NewWatch(User u) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEnterWatchlistName = new JLabel("Enter watchlist name :");
		lblEnterWatchlistName.setBounds(22, 32, 131, 14);
		contentPane.add(lblEnterWatchlistName);
		
		textField = new JTextField();
		textField.setBounds(269, 29, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JList list = new JList();
		list.setBounds(38, 163, 317, -91);
		contentPane.add(list);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
		        Connection con;
		        con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:XE", "system", "root");
		        WatchList.create(con, textField.getText(), u);
		        ShowWatchlist swf=new ShowWatchlist(u);
				swf.setVisible(true);
				setVisible(false);
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
		btnCreate.setBounds(311, 227, 89, 23);
		contentPane.add(btnCreate);
		
		
	}
}
