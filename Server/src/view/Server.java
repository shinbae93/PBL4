package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JTable;

import model.bean.FileInfo;
import model.bean.User;
import model.bo.FileBO;
import model.bo.UserBO;

public class Server extends JFrame {
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private JTree tree;
	static String value = "";
	private JPanel contentPane;
	private static JTable table;
	private JButton btnRefresh;

	static ArrayList<Socket> listSK = new ArrayList<Socket>();
	static ArrayList<FileInfo> myFiles = new ArrayList<FileInfo>();

	public static void main(String[] args) {
		Server frame = new Server();
		frame.setVisible(true);
		frame.open();
	}

	public Server() {
		UserBO userBO = new UserBO();
		FileBO fileBO = new FileBO();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JTabbedPane tabMain = new JTabbedPane(JTabbedPane.TOP);
		tabMain.setBounds(0, 0, 730, 561);
		contentPane.add(tabMain);

		// Server Files Tab
		JPanel tabMyFile = new JPanel();
		tabMyFile.setLayout(null);
		tabMain.addTab("My file", null, tabMyFile, null);

		loadFile();

		File fileRoot = new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp/test");
		root = new DefaultMutableTreeNode(new FileNode(fileRoot));
		treeModel = new DefaultTreeModel(root);

		tree = new JTree(treeModel);
		tree.setShowsRootHandles(true);

		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBounds(0, 0, 437, 533);

		createChildren(fileRoot, root);
		tabMyFile.add(scrollPane);

		deleteDir(new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp"));

		JButton btnDelFile = new JButton("Delete");
		btnDelFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TreePath[] paths = tree.getSelectionPaths();
				for (int i = 0; paths != null && i < paths.length; i++) {
					value = "";
					Object elements[] = paths[i].getPath();
					for (int j = 0, n = elements.length; j < n; j++) {
						value += elements[j] + "\\";
					}
					String path = value.substring(0, value.length() - 1);
					if (path.indexOf(".") != -1) {
						try {
							fileBO.deleteFile(path);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
				btnRefresh.doClick();
			}
		});
		btnDelFile.setBounds(475, 30, 89, 23);
		tabMyFile.add(btnDelFile);

		JButton btnAccess = new JButton("Access...");
		btnAccess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath[] paths = tree.getSelectionPaths();
				if (paths.length == 1) {
					// select access user
					value = "";
					Object elements[] = paths[0].getPath();
					for (int j = 0, n = elements.length; j < n; j++) {
						value += elements[j] + "\\";
					}
					String path = value.substring(0, value.length() - 1);
					try {
						ArrayList<Integer> ids = fileBO.getListUserID(fileBO.getFile(path).getId());
						SelectUser frame = new SelectUser(ids, fileBO.getFile(path).getId());
						frame.setVisible(true);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnAccess.setBounds(475, 123, 89, 23);
		tabMyFile.add(btnAccess);

		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// refresh tree 1: server
				loadFile();
				root.removeAllChildren();
				File fileRoot = new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp/test");
				createChildren(fileRoot, root);
				deleteDir(new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp"));
				treeModel.reload();
			}
		});
		btnRefresh.setBounds(475, 213, 89, 23);
		tabMyFile.add(btnRefresh);

		// User management tab
		JPanel tabUserManager = new JPanel();
		tabUserManager.setLayout(null);
		tabMain.addTab("Quan ly User", null, tabUserManager, null);

		loadUser();
		table.setBounds(10, 11, 705, 185);
		JScrollPane scrollPaneTable = new JScrollPane(table);
		scrollPaneTable.setBounds(10, 11, 705, 136);
		tabUserManager.add(scrollPaneTable);

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] index = table.getSelectedRows();
				if (index.length == 1) {
					int id = Integer.parseInt(table.getModel().getValueAt(index[0], 0).toString());
					EditUser frame = new EditUser(id);
					frame.setVisible(true);
				}
			}
		});
		btnEdit.setBounds(218, 174, 89, 23);
		tabUserManager.add(btnEdit);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] index = table.getSelectedRows();
				for (int i = 0; i < index.length; ++i) {
					try {
						userBO.deleteUser(Integer.parseInt(table.getModel().getValueAt(index[i], 0).toString()));
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				reloadUser();
			}
		});
		btnDelete.setBounds(393, 174, 89, 23);
		tabUserManager.add(btnDelete);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddUser frame = new AddUser();
				frame.setVisible(true);
			}
		});
		btnAdd.setBounds(62, 174, 89, 23);
		tabUserManager.add(btnAdd);

		JButton btnRefreshUser = new JButton("Refresh");
		btnRefreshUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadUser();
			}
		});
		btnRefreshUser.setBounds(546, 174, 89, 23);
		tabUserManager.add(btnRefreshUser);
	}

	private void loadFile() {
		FileBO fileBO = new FileBO();
		try {
			ArrayList<FileInfo> files = fileBO.getAllFile();
			ArrayList<String> paths = new ArrayList<String>();
			for (int i = 0; i < files.size(); ++i) {
				paths.add(files.get(i).getPath());
				File file = new File(
						System.getProperty("user.home") + "/OneDrive/Desktop/test_temp/" + files.get(i).getPath());
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
		String header[] = { "ID", "Username", "Name", "Role" };
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < listUser.size(); ++i) {
			User user = listUser.get(i);
			ArrayList<String> row = new ArrayList<String>(Arrays.asList(Integer.toString(user.getId()),
					user.getUsername(), user.getName(), user.getRole() == 1 ? "admin" : "user"));
			data.add(row);
		}
		String value[][] = data.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
		table = new JTable(value, header);
		AbstractTableModel tableModel = (AbstractTableModel) table.getModel();
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));
		tableModel.fireTableDataChanged();
	}

	protected static void reloadUser() {
		UserBO userBO = new UserBO();
		ArrayList<User> listUser = new ArrayList<User>();
		try {
			listUser = userBO.getAllUser();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String header[] = { "ID", "Username", "Name", "Role" };
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < listUser.size(); ++i) {
			User user = listUser.get(i);
			ArrayList<String> row = new ArrayList<String>(Arrays.asList(Integer.toString(user.getId()),
					user.getUsername(), user.getName(), user.getRole() == 1 ? "admin" : "user"));
			data.add(row);
		}
		String value[][] = data.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
		DefaultTableModel tableModel = new DefaultTableModel(value, header);
		table.setModel(tableModel);
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));
	}

	// delete temporary folder
	public static void deleteDir(File file) {
		if (file.isDirectory()) {
			String[] files = file.list();
			for (String child : files) {
				System.out.println(child);
				File childDir = new File(file, child);
				if (childDir.isDirectory()) {
					deleteDir(childDir);
				} else {
					childDir.delete();
				}
			}
			if (file.list().length == 0) {
				file.delete();
			}
		} else {
			file.delete();
		}
	}

	private void createChildren(File fileRoot, DefaultMutableTreeNode node) {
		File[] files = fileRoot.listFiles();
		if (files == null)
			return;

		for (File file : files) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(file));
			node.add(childNode);
			if (file.isDirectory()) {
				createChildren(file, childNode);
			}
		}
	}

	class FileNode {

		private File file;

		public FileNode(File file) {
			this.file = file;
		}

		@Override
		public String toString() {
			String name = file.getName();
			if (name.equals("")) {
				return file.getAbsolutePath();
			} else {
				return name;
			}
		}
	}

	private void open() {
		try {
			ServerSocket server = new ServerSocket(1234);
			System.out.println("Server is listening...");
			while (true) {
				Socket socket = server.accept();
				System.out.println("Đã kết nối với " + socket);
				Server.listSK.add(socket);
				new ServerThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ServerThread extends Thread {
		private Socket soc = null;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;

		public ServerThread(Socket s) {
			this.soc = s;
		}

		public void run() {
			try {
				FileBO fileBO = new FileBO();
				UserBO userBO = new UserBO();
				dis = new DataInputStream(soc.getInputStream());
				int cmd = dis.readInt();
				System.out.println(cmd);
				switch (cmd) {
				// receive request file path
				case 1: {
					try {
						int n = dis.readInt();
						DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
						dos.writeInt(1);
						dos.writeInt(n);
						for (int i = 0; i < n; ++i) {
							String path = dis.readUTF();
							FileInfo file = fileBO.getFile(path);
							// send to client
							dos.writeUTF(file.getName());
							dos.writeUTF(file.getPath());
							dos.writeInt(file.getData().length);
							dos.write(file.getData());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
					break;
				// receive file
				case 2: {
					try {
						int n = dis.readInt();
						for (int i = 0; i < n; ++i) {
							String name = dis.readUTF();
							System.out.println(name);
							String path = dis.readUTF();
							System.out.println(path);
							String created = dis.readUTF();
							System.out.println(created);
							String modified = dis.readUTF();
							System.out.println(modified);
							int size = dis.readInt();
//							System.out.println(size);
							byte[] data = dis.readNBytes(size);
//							System.out.println(new String(data));
							// if file already exist
							if (fileBO.isExistFile(path)) {
								FileInfo file = fileBO.getFile(path);
								// if file has been modified
								if (cmpDate(file.getCreated(), created) || cmpDate(file.getModified(), modified)
										|| !cmpMD5(file.getData(), data)) {
									// edit file
									fileBO.editFile(name, path, created, modified, data);
								}
							} else {
								// add new file
								fileBO.addFile(name, path, created, modified, data);
							}
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
					break;
				// validate login
				case 3: {
					try {
						dis = new DataInputStream(soc.getInputStream());
						String username = dis.readUTF();
						String password = dis.readUTF();
						boolean valid = false;
						valid = userBO.isValidUser(username, password);
						dos = new DataOutputStream(soc.getOutputStream());
						dos.writeBoolean(valid);
						if (valid) {
							int id = userBO.getUser(username).getId();
							dos.writeInt(id);
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
					break;
				// edit user
				case 4: {
					dis = new DataInputStream(soc.getInputStream());
					int id = dis.readInt();
					String password = dis.readUTF();
					String name = dis.readUTF();
					try {
						userBO.editUser(id, password, name);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
					break;
				// get all file path from server to client tree view
				case 5: {
					try {
						dis = new DataInputStream(soc.getInputStream());
						int id = dis.readInt();
						dos = new DataOutputStream(soc.getOutputStream());
						ArrayList<FileInfo> data = fileBO.getFileByUser(id);
						dos.writeInt(data.size());
						for (int i = 0; i < data.size(); ++i) {
							dos.writeUTF(data.get(i).getPath());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
					break;
				// get user detail
				case 6: {
					try {
						dis = new DataInputStream(soc.getInputStream());
						int id = dis.readInt();
						User user = userBO.getUser(id);
						dos = new DataOutputStream(soc.getOutputStream());
						dos.writeUTF(user.getUsername());
						dos.writeUTF(user.getPassword());
						dos.writeUTF(user.getName());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Compare MD5 checksum to check if file has changed
		private boolean cmpMD5(byte[] data1, byte[] data2) {
			try {
				MessageDigest md1 = MessageDigest.getInstance("MD5");
				md1.update(data1);
				byte[] digest1 = md1.digest();
				MessageDigest md2 = MessageDigest.getInstance("MD5");
				md2.update(data2);
				byte[] digest2 = md2.digest();
				return Arrays.equals(digest1, digest2);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return false;
		}

		// Compare date time. Return if first parameter is before second parameter
		private boolean cmpDate(String date1, String date2) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			try {
				return sdf.parse(date1).before(sdf.parse(date2));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

}
