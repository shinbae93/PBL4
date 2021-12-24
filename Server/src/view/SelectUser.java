package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import model.bean.User;
import model.bo.FileBO;
import model.bo.UserBO;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;

public class SelectUser extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private ArrayList<Integer> ids;
	private int idfile;

	public SelectUser(ArrayList<Integer> ids, int idfile) {
		this.ids = ids;
		this.idfile = idfile;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 316, 241);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// load user
		loadUser();
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				default:
					return Boolean.class;
				}
			}
		};
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(1));
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setBounds(10, 11, 105, 185);
		JScrollPane scrollPaneTable = new JScrollPane(table);
		scrollPaneTable.setBounds(10, 11, 274, 136);
		contentPane.add(scrollPaneTable);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileBO fileBO = new FileBO();
				try {
					fileBO.deleteShareFile(idfile);
					for (int i = 0; i < table.getRowCount(); i++) {
						if ((boolean) table.getModel().getValueAt(i, 0)) {
							fileBO.addShareFile(idfile, Integer.parseInt(table.getModel().getValueAt(i, 1).toString()));
						}
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
		btnOK.setBounds(20, 168, 89, 23);
		contentPane.add(btnOK);

	}

	private void loadUser() {
		UserBO userBO = new UserBO();
		ArrayList<User> listUser = new ArrayList<User>();
		try {
			listUser = userBO.getAllUser();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String header[] = { "Select", "ID", "Username", "Role" };
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		for (int i = 0; i < listUser.size(); ++i) {
			User user = listUser.get(i);
			ArrayList<Object> row = new ArrayList<Object>(Arrays.asList(ids.contains(user.getId()) || ids.size() == 0,
					Integer.toString(user.getId()), user.getUsername(), user.getRole() == 1 ? "admin" : "user"));
			data.add(row);
		}
		Object value[][] = data.stream().map(u -> u.toArray(new Object[0])).toArray(Object[][]::new);
		model = new DefaultTableModel(value, header);
	}
}
