import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.flixango.models.User;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Home extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
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
	public Home(User u) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(17, 0, 407, 21);
		contentPane.add(menuBar);
		
		JMenu mnSearch = new JMenu("search");
		menuBar.add(mnSearch);
		
		JMenuItem mntmMovie = new JMenuItem("movie");
		mntmMovie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MovieSearch msf=new MovieSearch(u);
				msf.setVisible(true);
				setVisible(false);
			}
		});
		mnSearch.add(mntmMovie);
		
		JMenu mnWatchlist = new JMenu("watchlist");
		menuBar.add(mnWatchlist);
		
		JMenuItem mntmShowWatchlist = new JMenuItem("show watchlists");
		mntmShowWatchlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShowWatchlist shf=new ShowWatchlist(u);
				shf.setVisible(true);
				setVisible(false);
			}
		});
		mnWatchlist.add(mntmShowWatchlist);
		
		JMenu mnAccount = new JMenu("Account");
		menuBar.add(mnAccount);
		
		JMenuItem mntmChangeEmail = new JMenuItem("change email");
		mntmChangeEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateEmail uef=new UpdateEmail(u);
				uef.setVisible(true);
				setVisible(false);
			}
		});
		mnAccount.add(mntmChangeEmail);
		
		JMenuItem mntmChangePhoneNumber = new JMenuItem("change phone number");
		mntmChangePhoneNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdatePhone upf=new UpdatePhone(u);
				upf.setVisible(true);
				setVisible(false);
			}
		});
		mnAccount.add(mntmChangePhoneNumber);
		
		JMenuItem mntmSignOut = new JMenuItem("sign out");
		mnAccount.add(mntmSignOut);
		mntmSignOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Login jf=new Login();
				jf.setVisible(true);
				setVisible(false);
			}
		});
		
		JLabel lblNewLabel = new JLabel("Welcome, "+u.Name.toUpperCase()+"!");
		lblNewLabel.setBounds(150, 126, 274, 14);
		contentPane.add(lblNewLabel);
	}
}
