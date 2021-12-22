import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtID;
	private JPasswordField txtPassword;

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
		setBounds(100, 100, 389, 216);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lbUsername = new JLabel("Username");
		lbUsername.setBounds(23, 35, 91, 14);
		contentPane.add(lbUsername);

		JLabel lbPassword = new JLabel("Password");
		lbPassword.setBounds(23, 82, 91, 14);
		contentPane.add(lbPassword);

		txtID = new JTextField();
		txtID.setBounds(124, 32, 189, 20);
		contentPane.add(txtID);
		txtID.setColumns(10);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				Client frame = new Client();
				frame.setVisible(true);
			}
		});
		btnLogin.setBounds(124, 122, 89, 23);
		contentPane.add(btnLogin);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(124, 79, 189, 20);
		contentPane.add(txtPassword);
	}

}
