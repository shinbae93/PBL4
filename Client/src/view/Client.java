package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.FileInfo;
import model.User;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
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
import java.util.ArrayList;
import java.util.stream.Stream;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Client frame = new Client();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		ArrayList<String> files = getListFiles(new File("C:\\Users\\nguoi\\OneDrive\\Desktop\\test"));
		for(int i=0; i < files.size(); ++i) {
			System.out.println(files.get(i));
		}
	}

	public Client() {

		final File[] fileToSend = new File[1];

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lbTitle = new JLabel("File Sync");
		lbTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbTitle.setBounds(129, 10, 157, 28);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lbTitle);

		JLabel lbFileName = new JLabel("");
		lbFileName.setBounds(87, 60, 238, 13);
		contentPane.add(lbFileName);

		JButton btnChoose = new JButton("CHOOSE");
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Choose file to send");

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileToSend[0] = chooser.getSelectedFile();
					lbFileName.setText("File you want to send is:" + fileToSend[0].getName());
				}
			}
		});
		btnChoose.setBounds(240, 83, 85, 21);
		contentPane.add(btnChoose);

		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileToSend[0] == null) {
					lbFileName.setText("Please choose a file first.");
				} else {
					try {
						FileInputStream fis = new FileInputStream(fileToSend[0].getAbsolutePath());
						Socket soc = new Socket("localhost", 1234);

						DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

						String fileName = fileToSend[0].getName();
						byte[] fileNameBytes = fileName.getBytes();
						byte[] fileBytes = new byte[(int) fileToSend[0].length()];
						fis.read(fileBytes);

						dos.writeInt(fileNameBytes.length);
						dos.write(fileNameBytes);

						dos.writeInt(fileBytes.length);
						dos.write(fileBytes);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnSend.setBounds(87, 83, 85, 21);
		contentPane.add(btnSend);
	}
	
	// send request to server to get list files
	private void requestFile(ArrayList<String> filePaths) {
		// send list file path to server
		// server check if time modified == -> do nothing
		// else if time modified != -> send file to client 
	}

	// Receive list files from server
	private void ReceiveFile(ArrayList<FileInfo> files) {
//		try {
//			FileOutputStream fos = new FileOutputStream(fileName);
//			fos.write(fileData);
//			fos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// sync files
		// if file receive 
	}

	// Send list files to server
	private void SendFile(ArrayList<FileInfo> files) {

	}

	// Get list paths of files from a directory
	private static ArrayList<String> getListFiles(File file) {
		ArrayList<String> listPaths = new ArrayList<String>();
		Stream<Path> paths = null;
		try {
			paths = Files.walk(Paths.get(file.getAbsolutePath()));
			paths.filter(Files::isRegularFile).map(path -> path.toString()).forEach(listPaths::add);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return listPaths;
	}
	
	// Get md5 hashing of file
	private String getMD5(String path) {
		return "";
	}
	
	// Check id md5 of 2 files
	private boolean checkMD5(File myFile, File receiveFile) {
		return true;
	}
	
	// Update list of files in view
	private void updateView() {
		// Update server file tree
		
		// Update client file tree
	}
	
	// Get file info to show detail of a file
	private FileInfo getFileInfo(String path) {
		// sync file path
		
		// get file info
		return new FileInfo();
	}
	
	// Show file's detail
	private void showFileDetail(FileInfo file) {
		
	}
	
	// Validate user login
	private boolean isValidUser(String username, String password) {
		
		return true;
	}
	
	// Edit user info
	private void editUser(User user) {
		
	}
}
