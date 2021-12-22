import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JTable;

public class Server extends JFrame {
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private JTree tree;
	static String value="";
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JTabbedPane tabMain = new JTabbedPane(JTabbedPane.TOP);
		tabMain.setBounds(0, 0,730,561);
		contentPane.add(tabMain);
		
		//
		JPanel tabMyFile = new JPanel();
		tabMyFile.setLayout(null);
		tabMain.addTab("My file", null, tabMyFile, null);
		
		File fileRoot = new File("C:\\Users\\Admin\\Desktop");
		root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);
        
        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {

			}
		});
        

        
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBounds(0, 0, 437, 533);
	    
        createChildren(fileRoot, root);
	    tabMyFile.add(scrollPane);
	    
	    JButton btnSync = new JButton("Sync");
	    btnSync.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		value = "";
	    		TreePath[] paths = tree.getSelectionPaths();
	    		for(int i=0; i<paths.length; i++) {
	    			Object elements[] = paths[i].getPath();
	    		    for (int j = 0, n = elements.length; j < n; j++) {          	   
	    		    	value+=elements[j]+"\\";    		            	   
	    		    }
	    		    value += "\n";
	    		}
	    		String str[] = value.split(" ");
	    		System.out.println(str[str.length-1]);           	
	    	}
	    });
	    btnSync.setBounds(475, 30, 89, 23);    
	    tabMyFile.add(btnSync);
	    
	    //
	    JPanel tabUserManager = new JPanel();
		tabUserManager.setLayout(null);
		tabMain.addTab("Quan ly User", null, tabUserManager, null);
		
		String column[] = { "ID", "Password", "Role" };
		String data[][] = {
				{"leminhtri", "cutedethuong", "bigboss"}
		};
		table = new JTable(data,column);
		table.setBounds(10, 11, 705, 185);
		JScrollPane scrollPaneTable = new JScrollPane(table);
		scrollPaneTable.setBounds(10, 11, 705, 136);
		tabUserManager.add(scrollPaneTable);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			}
		});
		btnEdit.setBounds(218, 174, 89, 23);
		tabUserManager.add(btnEdit);
		
		JButton btnDelete = new JButton("Del");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDelete.setBounds(393, 174, 89, 23);
		tabUserManager.add(btnDelete);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAdd.setBounds(62, 174, 89, 23);
		tabUserManager.add(btnAdd);
		
		
		
	}
	private void createChildren(File fileRoot, 
            DefaultMutableTreeNode node) {
        File[] files = fileRoot.listFiles();
        if (files == null) return;

        for (File file : files) {
            DefaultMutableTreeNode childNode = 
                    new DefaultMutableTreeNode(new FileNode(file));
            node.add(childNode);
            if (file.isDirectory()) {
                createChildren(file, childNode);
            }
        }
    }

    public class FileNode {

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
}
