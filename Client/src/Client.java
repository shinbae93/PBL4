import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Client extends JFrame {
    private DefaultMutableTreeNode root1;
    private DefaultMutableTreeNode root2;
    private DefaultTreeModel treeModel1;
    private DefaultTreeModel treeModel2;
    private JTree tree1;
    private JTree tree2;
	private JPanel contentPane;
	static String value1="";
	static String value2="";

	public Client() {
		
		makeFolder();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//tree 1: server folder
		File fileRoot1 = new File("C:\\Users\\nguoi\\OneDrive\\Desktop\\test");
		root1 = new DefaultMutableTreeNode(new FileNode(fileRoot1));
        treeModel1 = new DefaultTreeModel(root1);
        
        tree1 = new JTree(treeModel1);
        tree1.setShowsRootHandles(true);
        tree1.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {

			}
		});
        

        
        JScrollPane scrollPane1 = new JScrollPane(tree1);
        scrollPane1.setBounds(40, 61, 310, 570);
	    
        createChildren(fileRoot1, root1);
	    getContentPane().add(scrollPane1);
	    
	    JButton btnSync = new JButton("Sync");
	    btnSync.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		value1 = "";
	    		TreePath[] paths = tree1.getSelectionPaths();
	    		for(int i=0; paths!=null && i<paths.length; i++) {
	    			Object elements[] = paths[i].getPath();
	    		               for (int j = 0, n = elements.length; j < n; j++) {          	   
	    		            	   value1+=elements[j]+"\\";    		            	   
	    		               }
	    		               value1 += "\n";
	    		}
	    		String str[] = value1.split(" ");
	    		System.out.println(str[str.length-1]);           	
	    	}
	    });
	    btnSync.setBounds(385, 96, 89, 23);    
	    contentPane.add(btnSync);
	    
	    //tree 2: client folder
	    File fileRoot2 = new File("C:\\Users\\nguoi\\OneDrive\\Desktop\\test");
	    root2 = new DefaultMutableTreeNode(new FileNode(fileRoot2));
        treeModel2 = new DefaultTreeModel(root2);
        
        tree2 = new JTree(treeModel2);
        tree2.setShowsRootHandles(true);
        tree2.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {

			}
		});
        
        JScrollPane scrollPane2 = new JScrollPane(tree2);
        scrollPane2.setBounds(526, 61, 310, 570);
        
        createChildren(fileRoot2, root2);
	    getContentPane().add(scrollPane2);
	    
	    JButton btnDetail = new JButton("Detail");
	    btnDetail.setBounds(385, 305, 89, 23);
	    btnDetail.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				value2 = "";
	    		TreePath[] paths = tree2.getSelectionPaths();
	    		for(int i=0; paths!=null && i<paths.length; i++) {
	    			Object elements[] = paths[i].getPath();
	    		               for (int j = 0, n = elements.length; j < n; j++) {          	   
	    		            	   value2+=elements[j]+"\\";    		            	   
	    		               }
	    		               value2 += "\n";
	    		}
	    		String str[] = value2.split(" ");
	    		System.out.println(str[str.length-1]);
			}
		});
	    contentPane.add(btnDetail);
	    
	    JLabel lbTitle = new JLabel("Synchronize Folder Application");
	    lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    lbTitle.setFont(new Font("Tahoma", Font.BOLD, 15));
	    lbTitle.setBounds(248, 10, 367, 23);
	    contentPane.add(lbTitle);
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
    
    private void makeFolder() {
    	File share = new File(System.getProperty("user.home") + "/OneDrive/Desktop/test/Share");
    	if(!share.exists()) {
    		System.out.println(share.mkdir());
    	}
    }
}

