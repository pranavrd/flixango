import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.flixango.models.User;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UpdateEmail extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateEmail frame = new UpdateEmail();
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
	public UpdateEmail(User u) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEnterNewEmail = new JLabel("Enter new email id :");
		lblEnterNewEmail.setBounds(38, 36, 117, 19);
		contentPane.add(lblEnterNewEmail);
		
		textField = new JTextField();
		textField.setBounds(227, 35, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblEnterPassword = new JLabel("Enter password:");
		lblEnterPassword.setBounds(38, 103, 95, 14);
		contentPane.add(lblEnterPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(227, 100, 86, 20);
		contentPane.add(passwordField);
		
		label = new JLabel("");
		label.setBounds(154, 146, 46, 14);
		contentPane.add(label);
		
		JButton btnChange = new JButton("Change");
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u.EMail=textField.getText();
				if(passwordField.getText().contentEquals(u.Password))
				{
				   u.save();
				   Home hf=new Home(u);
				   hf.setVisible(true);
				   setVisible(false);
				}
				else
				{
					label.setText("Wrong Password");
				}
			}
		});
		btnChange.setBounds(167, 171, 89, 23);
		contentPane.add(btnChange);
		
		
	}
}
