package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import model.bean.User;
import model.bo.UserBO;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;

public class EditUser extends JFrame {

	private JPanel contentPane;
	private JTextField txtID;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JTextField txtName;
	private int id;

	public EditUser(int id) {
		this.id = id;
		UserBO userBO = new UserBO();
		User user = new User();
		try {
			user = userBO.getUser(id);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 314);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel lbID = new JLabel("ID");
		lbID.setBounds(24, 25, 101, 14);
		contentPane.add(lbID);
		
		JLabel lbUsername = new JLabel("Username");
		lbUsername.setBounds(24, 65, 101, 14);
		contentPane.add(lbUsername);
		
		JLabel lbPassword = new JLabel("Password");
		lbPassword.setBounds(24, 101, 101, 14);
		contentPane.add(lbPassword);
		
		JLabel lbName = new JLabel("Name");
		lbName.setBounds(24, 141, 101, 14);
		contentPane.add(lbName);
		
		JLabel lbRole = new JLabel("Role");
		lbRole.setBounds(24, 183, 101, 14);
		contentPane.add(lbRole);
		
		txtID = new JTextField();
		txtID.setEditable(false);
		txtID.setBounds(135, 22, 238, 20);
		contentPane.add(txtID);
		txtID.setColumns(10);
		txtID.setText(String.valueOf(user.getId()));
		
		txtUsername = new JTextField();
		txtUsername.setEditable(false);
		txtUsername.setBounds(135, 62, 238, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		txtUsername.setText(user.getUsername());
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(135, 98, 238, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		txtPassword.setText(user.getPassword());
		
		txtName = new JTextField();
		txtName.setBounds(135, 138, 238, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		txtName.setText(user.getName());
		
		JComboBox comboBox = new JComboBox(new String[]{"Admin", "User"});
		comboBox.setBounds(135, 178, 238, 24);
		comboBox.setSelectedIndex(user.getRole() - 1);
		contentPane.add(comboBox);
		 
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					userBO.editUser(id, new String(txtPassword.getPassword()), txtName.getText(), comboBox.getSelectedIndex() + 1);
					Server.reloadUser();
					dispose();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnOK.setBounds(67, 227, 89, 23);
		contentPane.add(btnOK);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(266, 227, 89, 23);
		contentPane.add(btnCancel);
	}
}
