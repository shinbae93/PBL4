package view;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;

public class Client extends JFrame {
	private DefaultMutableTreeNode root1;
	private DefaultMutableTreeNode root2;
	private DefaultTreeModel treeModel1;
	private DefaultTreeModel treeModel2;
	private JTree tree1;
	private JTree tree2;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JTextField txtName;
	private JButton btnRefresh;
	private JScrollPane scrollPane2;
	static String value1 = "";
	static String value2 = "";
	final File[] fileToSend = new File[1];
	private int iduser;

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
//		ArrayList<String> data = getListFiles(new File("C:\\Users\\nguoi\\OneDrive\\Desktop\\test"));
//		for (int i = 0; i < data.size(); ++i) {
//			System.out.println(data.get(i));
//		}
	}

	public Client(int iduser) {
		this.iduser = iduser;

		init();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JTabbedPane tabMain = new JTabbedPane(JTabbedPane.TOP);
		tabMain.setBounds(0, 0, 874, 676);
		contentPane.add(tabMain);

		JPanel tabMyFile = new JPanel();
		tabMyFile.setLayout(null);
		tabMain.addTab("My file", null, tabMyFile, null);

		// tree 1: server folder

		// load folder from server
		loadFile();

		File fileRoot1 = new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp/test");
		root1 = new DefaultMutableTreeNode(new FileNode(fileRoot1));
		treeModel1 = new DefaultTreeModel(root1);

		tree1 = new JTree(treeModel1);
		tree1.setShowsRootHandles(true);

		JScrollPane scrollPane1 = new JScrollPane(tree1);
		scrollPane1.setBounds(40, 61, 310, 570);

		createChildren(fileRoot1, root1);
		tabMyFile.add(scrollPane1);

		// delete temporary folder
		deleteDir(new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp"));

		JButton btnSync = new JButton("Sync");
		btnSync.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> selection1 = new ArrayList<String>();
				TreePath[] paths1 = tree1.getSelectionPaths();
				for (int i = 0; paths1 != null && i < paths1.length; i++) {
					value1 = "";
					Object elements[] = paths1[i].getPath();
					for (int j = 0, n = elements.length; j < n; j++) {
						value1 += elements[j] + "\\";
					}
					String path = value1.substring(0, value1.length() - 1);
					selection1.add(path);
				}
				ArrayList<String> selection2 = new ArrayList<String>();
				TreePath[] paths2 = tree2.getSelectionPaths();
				for (int i = 0; paths2 != null && i < paths2.length; i++) {
					value2 = "";
					Object elements[] = paths2[i].getPath();
					for (int j = 0, n = elements.length; j < n; j++) {
						value2 += elements[j] + "\\";
					}
					String path = value2.substring(0, value2.length() - 1);
					File file = new File(System.getProperty("user.home") + "/OneDrive/Desktop/" + path);
					if (file.isDirectory()) {
						ArrayList<String> listFiles = getListFiles(file);
						for (int j = 0; j < listFiles.size(); ++j) {
							selection2.add(listFiles.get(j));
						}
					} else {
						selection2.add(path);
					}
				}

				// log for debugging
//				for (int i = 0; i < selection1.size(); ++i) {
//					System.out.println(selection1.get(i));
//				}
//				for (int i = 0; i < selection2.size(); ++i) {
//					System.out.println(selection2.get(i));
//				}

				try {
					Socket soc = new Socket("localhost", 1234);
					int n1 = selection1.size();
					int n2 = selection2.size();
					if (n1 > 0) {
						DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
						dos.writeInt(1);
						dos.writeInt(n1);
						for (int i = 0; i < n1; ++i) {
							dos.writeUTF(selection1.get(i));
							dos.flush();
						}
						DataInputStream dis = new DataInputStream(soc.getInputStream());
						int cmd = dis.readInt();
						int n = dis.readInt();
						for (int i = 0; i < n; ++i) {
							String name = dis.readUTF();
							System.out.println(name);
							String path = dis.readUTF();
							System.out.println(path);
							int size = dis.readInt();
							byte[] data = dis.readNBytes(size);
							File file = new File(System.getProperty("user.home") + "/OneDrive/Desktop/" + path);
							// if file exist, delete old file
							if (file.exists()) {
								file.delete();
							}
							file.getParentFile().mkdirs();
							// create new file
							file.createNewFile();
							Files.write(file.toPath(), data, StandardOpenOption.SYNC);
						}
					}
					if (n2 > 0) {
						DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
						dos.writeInt(2);
						dos.writeInt(n2);
						for (int i = 0, n = selection2.size(); i < n; ++i) {
							File file = new File(
									System.getProperty("user.home") + "/OneDrive/Desktop/" + selection2.get(i));
							BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
							dos.writeUTF(file.getName());
							dos.writeUTF(selection2.get(i));
							dos.writeUTF(formatDateTime(attr.creationTime()));
							dos.writeUTF(formatDateTime(attr.lastModifiedTime()));
							byte[] data = Files.readAllBytes(file.toPath());
							dos.writeInt(data.length);
							dos.write(data);
							dos.flush();
						}
					}

					// clear selection
					tree1.clearSelection();
					tree2.clearSelection();

					// refresh view
					btnRefresh.doClick();
					
					new ReceiveClient(soc).start();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSync.setBounds(385, 96, 89, 23);
		tabMyFile.add(btnSync);

		// tree 2: client folder
		File fileRoot2 = new File("C:\\Users\\nguoi\\OneDrive\\Desktop\\test");
		root2 = new DefaultMutableTreeNode(new FileNode(fileRoot2));
		treeModel2 = new DefaultTreeModel(root2);

		tree2 = new JTree(treeModel2);
		tree2.setShowsRootHandles(true);

		scrollPane2 = new JScrollPane(tree2);
		scrollPane2.setBounds(526, 61, 310, 570);

		createChildren(fileRoot2, root2);
		tabMyFile.add(scrollPane2);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(385, 305, 89, 23);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// refresh tree 1: server
				loadFile();
				root1.removeAllChildren();
				File fileRoot1 = new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp/test");
				createChildren(fileRoot1, root1);
				treeModel1.reload();
				// refresh tree 2: client
				root2.removeAllChildren();
				File fileRoot2 = new File("C:\\Users\\nguoi\\OneDrive\\Desktop\\test");
				createChildren(fileRoot2, root2);
				treeModel2.reload();
				// delete temporary folder
				deleteDir(new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp"));
			}
		});
		tabMyFile.add(btnRefresh);
		tabMyFile.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				tree1.clearSelection();
				tree2.clearSelection();
			}
		});

		JLabel lbTitle = new JLabel("Synchronize Folder Application");
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbTitle.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbTitle.setBounds(248, 10, 367, 23);
		tabMyFile.add(lbTitle);

		JPanel tabUserInfo = new JPanel();
		tabUserInfo.setLayout(null);
		tabMain.addTab("User Information", null, tabUserInfo, null);

		JLabel lbUsername = new JLabel("Username");
		lbUsername.setBounds(32, 35, 101, 14);
		tabUserInfo.add(lbUsername);

		JLabel lbPassword = new JLabel("Password");
		lbPassword.setBounds(32, 75, 101, 14);
		tabUserInfo.add(lbPassword);

		JLabel lbName = new JLabel("Name");
		lbName.setBounds(32, 115, 101, 14);
		tabUserInfo.add(lbName);

		txtUsername = new JTextField();
		txtUsername.setEditable(false);
		txtUsername.setBounds(177, 32, 156, 20);
		tabUserInfo.add(txtUsername);
		txtUsername.setColumns(10);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(177, 72, 156, 20);
		tabUserInfo.add(txtPassword);
		txtPassword.setColumns(10);

		txtName = new JTextField();
		txtName.setBounds(177, 112, 156, 20);
		tabUserInfo.add(txtName);
		txtName.setColumns(10);

		try {
			Socket sk = new Socket("localhost", 1234);
			DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
			dos.writeInt(6);
			dos.writeInt(iduser);
			DataInputStream dis = new DataInputStream(sk.getInputStream());
			txtUsername.setText(dis.readUTF());
			txtPassword.setText(dis.readUTF());
			txtName.setText(dis.readUTF());
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Socket soc = new Socket("localhost", 1234);
					DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
					dos.writeInt(4);
					dos.writeInt(iduser);
					dos.writeUTF(new String(txtPassword.getPassword()));
					dos.writeUTF(txtName.getText());
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnOK.setBounds(69, 182, 89, 23);
		tabUserInfo.add(btnOK);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtName.setText("");
				txtPassword.setText("");
			}
		});
		btnCancel.setBounds(253, 182, 89, 23);
		tabUserInfo.add(btnCancel);

//		Socket soc;
//		try {
//			soc = new Socket("localhost", 1234);
//			new ReceiveClient(soc).start();
//		} catch (UnknownHostException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
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

	// load file from server
	public void loadFile() {
		try {
			Socket soc = new Socket("localhost", 1234);
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			dos.writeInt(5);
			dos.writeInt(iduser);
			DataInputStream dis = new DataInputStream(soc.getInputStream());
			int n = dis.readInt();
			System.out.println(n);
			for (int i = 0; i < n; ++i) {
				String path = dis.readUTF();
				System.out.println(path);
				File file = new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp/" + path);
				System.out.println(file.getParentFile().mkdirs());
				System.out.println(file.createNewFile());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// create temporary folder
//	public void createDir(ArrayList<String> paths) {
//		try {
//			for (int i = 0; i < paths.size(); ++i) {
//				File file = new File(System.getProperty("user.home") + "/OneDrive/Desktop/test_temp/test" + paths.get(i));
//				file.getParentFile().mkdirs();
//				file.createNewFile();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

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

	// format date time
	public static String formatDateTime(FileTime fileTime) {
		LocalDateTime localDateTime = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
	}

	// make default share folder if not exist and load folder from server
	private void init() {
		File share = new File(System.getProperty("user.home") + "/OneDrive/Desktop/test");
		if (!share.exists()) {
			System.out.println(share.mkdir());
		}
	}

	// Get list paths of files from a directory
	private static ArrayList<String> getListFiles(File file) {
		ArrayList<String> listPaths = new ArrayList<String>();
		Stream<Path> paths = null;
		try {
			paths = Files.walk(Paths.get(file.getAbsolutePath()));
			paths.filter(Files::isRegularFile).map(path -> path.toString().substring(path.toString().indexOf("test")))
					.forEach(listPaths::add);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return listPaths;
	}

	class ReceiveClient extends Thread {
		private Socket client;

		public ReceiveClient(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			DataInputStream dis = null;
			try {
				dis = new DataInputStream(client.getInputStream());
				while (true) {
					int cmd = dis.readInt();
					switch (cmd) {
					// receive file
					case 1: {
						String name = dis.readUTF();
						System.out.println(name);
						String path = dis.readUTF();
						System.out.println(path);
						int size = dis.readInt();
						byte[] data = dis.readNBytes(size);
						File file = new File(System.getProperty("user.home") + "/OneDrive/Desktop/" + path);
						// if file exist, delete old file
						if (file.exists()) {
							file.delete();
						}
						// create new file
						file.createNewFile();
						Files.write(file.toPath(), data, StandardOpenOption.SYNC);
					}
						break;
					}
				}
			} catch (Exception e) {
				try {
					dis.close();
					client.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
