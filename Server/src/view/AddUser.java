package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.bo.UserBO;

public class AddUser extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JTextField txtName;

	public AddUser() {
		UserBO userBO = new UserBO();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 277);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lbUsername = new JLabel("Username");
		lbUsername.setBounds(24, 21, 101, 14);
		contentPane.add(lbUsername);

		JLabel lbPassword = new JLabel("Password");
		lbPassword.setBounds(24, 62, 101, 14);
		contentPane.add(lbPassword);

		JLabel lbName = new JLabel("Name");
		lbName.setBounds(24, 107, 101, 14);
		contentPane.add(lbName);

		JLabel lbRole = new JLabel("Role");
		lbRole.setBounds(24, 159, 101, 14);
		contentPane.add(lbRole);

		txtUsername = new JTextField();
		txtUsername.setBounds(135, 19, 238, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(135, 60, 238, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);

		txtName = new JTextField();
		txtName.setBounds(135, 105, 238, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);

		Object[] data = { "Admin", "User" };
		JComboBox comboBox = new JComboBox(data);
		comboBox.setBounds(135, 154, 238, 24);
		contentPane.add(comboBox);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					userBO.addUser(txtUsername.getText(), new String(txtPassword.getPassword()), txtName.getText(),
							comboBox.getSelectedIndex() + 1);
					Server.reloadUser();
					dispose();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnOK.setBounds(67, 201, 89, 23);
		contentPane.add(btnOK);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(263, 201, 89, 23);
		contentPane.add(btnCancel);
	}

}
