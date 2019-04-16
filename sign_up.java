import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.flixango.models.User;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sign_up extends JFrame {

	private JPanel contentPane;
	private JTextField namefield;
	private JTextField emailField;
	private JTextField phoneField;
	private JTextField passField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sign_up frame = new sign_up();
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
	public sign_up() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("Name");
		label.setBounds(10, 32, 46, 14);
		contentPane.add(label);
		
		JLabel lblEmailId = new JLabel("email id");
		lblEmailId.setBounds(10, 71, 46, 14);
		contentPane.add(lblEmailId);
		
		JLabel lblPhoneNumber = new JLabel("phone number");
		lblPhoneNumber.setBounds(10, 121, 98, 14);
		contentPane.add(lblPhoneNumber);
		
		JLabel lblEnterPassword = new JLabel("enter password");
		lblEnterPassword.setBounds(10, 168, 110, 14);
		contentPane.add(lblEnterPassword);
		
		namefield = new JTextField();
		namefield.setBounds(118, 29, 86, 20);
		contentPane.add(namefield);
		namefield.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(118, 68, 86, 20);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		phoneField = new JTextField();
		phoneField.setBounds(118, 118, 86, 20);
		contentPane.add(phoneField);
		phoneField.setColumns(10);
		
		passField = new JTextField();
		passField.setBounds(118, 165, 86, 20);
		contentPane.add(passField);
		passField.setColumns(10);
		
		JButton btnCreateUser = new JButton("Create User");
		btnCreateUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
		            Connection con;
		            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:XE", "system", "root");

		            User u=new User();
		            u.create(con, namefield.getText(), emailField.getText(), Long.parseLong(phoneField.getText()), passField.getText());
		            System.out.println("new user created.");
		            
		            Login jf=new Login();
		            jf.setVisible(true);
		            setVisible(false);
		            con.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		btnCreateUser.setBounds(174, 227, 110, 23);
		contentPane.add(btnCreateUser);
	}
}
