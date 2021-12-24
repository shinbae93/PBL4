package model.bo;

import java.sql.SQLException;
import java.util.ArrayList;

import model.bean.FileInfo;
import model.dao.FileDAO;

public class FileBO {
	FileDAO fileDAO = new FileDAO();
	
	public boolean isExistFile(String path) throws ClassNotFoundException, SQLException {
		return fileDAO.isExistFile(path);
	}
	
	public ArrayList<FileInfo> getAllFile() throws ClassNotFoundException, SQLException {
		return fileDAO.getAllFile();
	}
	
	public ArrayList<FileInfo> getFileByUser(int id) throws ClassNotFoundException, SQLException {
		ArrayList<FileInfo> res = new ArrayList<FileInfo>();
		ArrayList<FileInfo> data = getAllFile();
		for(int i = 0; i < data.size(); ++i) {
			ArrayList<Integer> ids = getListUserID(data.get(i).getId());
			if(ids.size() > 0) {
				if(ids.contains(id)) res.add(data.get(i));
			} else {
				res.add(data.get(i));
			}
		}
		return res;
	}
	
	public FileInfo getFile(String path) throws ClassNotFoundException, SQLException {
		return fileDAO.getFile(path);
	}
	
	public void addFile(String name, String path, String created, String modified, byte[] data) throws ClassNotFoundException, SQLException {
		fileDAO.addFile(name, path, created, modified, data);
	}
	
	public void editFile(String name, String path, String created, String modified, byte[] data) throws ClassNotFoundException, SQLException {
		fileDAO.editFile(name, path, created, modified, data);
	}
	
	public void deleteFile(String path) throws ClassNotFoundException, SQLException {
		fileDAO.deleteFile(path);
	}
	
	public ArrayList<Integer> getListUserID(int idfile) throws ClassNotFoundException, SQLException {
		return fileDAO.getListUserID(idfile);
	}
	
	public void addShareFile(int idfile, int iduser) throws ClassNotFoundException, SQLException {
		fileDAO.addShareFile(idfile, iduser);
	}
	
	public void deleteShareFile(int idfile) throws ClassNotFoundException, SQLException {
		fileDAO.deleteShareFile(idfile);
	}
}
