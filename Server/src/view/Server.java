package view;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;

import model.bean.FileInfo;
import model.bean.User;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

public class Server extends JFrame {

	static ArrayList<FileInfo> myFiles = new ArrayList<FileInfo>();
	static ArrayList<Socket> listSK;

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lbTitle = new JLabel("File Sync Server");
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbTitle.setBounds(107, 10, 182, 26);
		contentPane.add(lbTitle);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 43, 416, 210);
		contentPane.add(scrollPane);

		try {
			ServerSocket server = new ServerSocket(1234);
			while (true) {
				Socket soc = server.accept();
				new ServerThread(soc).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			dis = new DataInputStream(soc.getInputStream());
//
//			int fileNameLength = dis.readInt();
//
//			if (fileNameLength > 0) {
//				byte[] fileNameBytes = new byte[fileNameLength];
//				dis.readFully(fileNameBytes, 0, fileNameLength);
//				String fileName = new String(fileNameBytes);
//
//				int fileContentLength = dis.readInt();
//
//				if (fileContentLength > 0) {
//					byte[] fileContentBytes = new byte[fileContentLength];
//					dis.readFully(fileContentBytes, 0, fileContentLength);
//
//					System.out.println(fileName);
//				}
//			}
			
			String cmd = dis.readUTF();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Receive list files from client
	private void receiveFile(ArrayList<FileInfo> files) {
//			try {
//				FileOutputStream fos = new FileOutputStream(fileName);
//				fos.write(fileData);
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

		// check files
		// if changed -> change file in database
		// if not exist -> save in database
	}

	// Send list files to client
	private void sendFile(ArrayList<FileInfo> files) {
		// get file info from database
		// add to array list
		// send to client
	}

	// Send list files to all client connected
	private void sendAll(ArrayList<FileInfo> files) {
		// get file info from database
		// add to array list
		// send to all client connected
	}

	// Delete list file
	private void deleteFile(ArrayList<FileInfo> files) {

	}

	// Add user
	private void addUser(User user) {

	}

	// Edit user
	private void editUser(int idUser, int idGroup) {

	}

	// Delete user
	private void deleteUser(int id) {

	}
}
