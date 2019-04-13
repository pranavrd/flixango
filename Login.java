import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;
	private JButton btnSignUp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.setText("");
		txtUsername.setBounds(174, 66, 86, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		pwdPassword = new JPasswordField();
		pwdPassword.setText("");
		pwdPassword.setBounds(174, 97, 86, 20);
		contentPane.add(pwdPassword);
		
		JLabel exp = new JLabel("");
		exp.setBounds(141, 125, 158, 14);
		contentPane.add(exp);
		
		JButton btnSignIn = new JButton("Sign In");
		btnSignIn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				try {
					        Class.forName("oracle.jdbc.driver.OracleDriver");
					        Connection con;
					        con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:XE", "system", "root");
							User u=new User();
							u=u.findByEMail(con, txtUsername.getText());
							if(u!=null && pwdPassword.getText().contentEquals(u.Password))
							{
								try {
						            System.out.println("sign in successful.");
						            
						        } catch (Exception e) {
						            System.out.println("Exception:" + e);
						        }
							}
							else {
								exp.setText("Wrong username/password");
							}
							con.close();
				}
				catch(Exception e){
					System.out.println("exception:"+e);
				}
			}
		});
		btnSignIn.setBounds(174, 149, 89, 23);
		contentPane.add(btnSignIn);
		
		JFrame f2=new JFrame();
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setBounds(100, 100, 450, 300);
		
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					sign_up ff = new sign_up();
					ff.setVisible(true);
					setVisible(false);
				}
				catch(Exception e) {
					System.out.println(e);}
			}
		});
		btnSignUp.setBounds(174, 183, 89, 23);
		contentPane.add(btnSignUp);
		
		
		
		
	}
}
