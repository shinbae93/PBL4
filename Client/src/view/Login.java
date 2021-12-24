package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtID;
	private JPasswordField txtPassword;

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
				if (!"".equals(txtID.getText()) && !"".equals(new String(txtPassword.getPassword()))) {
					try {
						Socket soc = new Socket("localhost", 1234);
						DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
						dos.writeInt(3);
						dos.writeUTF(txtID.getText());
						dos.writeUTF(new String(txtPassword.getPassword()));
						DataInputStream dis = new DataInputStream(soc.getInputStream());
						boolean valid = dis.readBoolean();
						if (valid) {
							setVisible(false);
							dispose();
							int iduser = dis.readInt();
							Client frame = new Client(iduser);
							frame.setVisible(true);
						} else {
							InvalidUser frame = new InvalidUser();
							frame.setVisible(true);
						}
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					EmptyUser frame = new EmptyUser();
					frame.setVisible(true);
				}
			}
		});
		btnLogin.setBounds(124, 122, 89, 23);
		contentPane.add(btnLogin);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(124, 79, 189, 20);
		contentPane.add(txtPassword);
	}

}
