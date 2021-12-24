package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InvalidUser extends JFrame {
	private JPanel contentPane;

	public InvalidUser() {
		setDefaultCloseOperation(2);
		setBounds(100, 100, 197, 178);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("Invalid User !");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(40, 11, 142, 59);
		contentPane.add(lblNewLabel);
		JFrame f = this;

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.dispose();
			}
		});
		btnOK.setBounds(50, 81, 89, 23);
		contentPane.add(btnOK);
	}
}
