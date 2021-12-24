package model.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.bean.FileInfo;

public class FileDAO {
	private Connection con;
	private PreparedStatement ps;

	public FileDAO() {
		connectDB();
	}

	private void connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/pbl4", "root", "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isExistFile(String path) throws SQLException {
		String sqlString = "select * from file where path = ?";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, path);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}

	public ArrayList<FileInfo> getAllFile() throws SQLException {
		String sqlString = "select * from file";
		ps = con.prepareStatement(sqlString);
		ResultSet rs = ps.executeQuery();
		ArrayList<FileInfo> res = new ArrayList<FileInfo>();
		while (rs.next()) {
			FileInfo file = new FileInfo();
			file.setId(rs.getInt("idfile"));
			file.setName(rs.getString("name"));
			file.setPath(rs.getString("path"));
			file.setCreated(rs.getString("created"));
			file.setModified(rs.getString("modified"));
			file.setData(rs.getBytes("data"));
			res.add(file);
		}
		return res;
	}

	public FileInfo getFile(String path) throws SQLException {
		String sqlString = "select * from file where path = ?";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, path);
		ResultSet rs = ps.executeQuery();
		rs.next();
		FileInfo file = new FileInfo();
		file.setId(rs.getInt("idfile"));
		file.setName(rs.getString("name"));
		file.setPath(rs.getString("path"));
		file.setCreated(rs.getString("created"));
		file.setModified(rs.getString("modified"));
		file.setData(rs.getBytes("data"));
		return file;
	}

	public void addFile(String name, String path, String created, String modified, byte[] data) throws SQLException {
		String sqlString = "insert into file(name,path,created,modified,data) values(?,?,?,?,?)";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, name);
		ps.setString(2, path);
		ps.setString(3, created);
		ps.setString(4, modified);
		Blob blob = con.createBlob();
		blob.setBytes(1, data);
		ps.setBlob(5, blob);
		ps.executeUpdate();
	}
	
	public void editFile(String name, String path, String created, String modified, byte[] data) throws SQLException {
		String sqlString = "update file set name = ?, created = ?, modified = ?, data = ? where path = ?";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, name);
		ps.setString(2, created);
		ps.setString(3, modified);
		Blob blob = con.createBlob();
		blob.setBytes(1, data);
		ps.setBlob(4, blob);
		ps.setString(5, path);
		ps.executeUpdate();
	}

	public void deleteFile(String path) throws SQLException {
		String sqlString = "delete from file where path = ?";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, path);
		ps.executeUpdate();
	}

	public ArrayList<Integer> getListUserID(int idfile) throws SQLException {
		String sqlString = "select * from sharefile where idfile = ?";
		ps = con.prepareStatement(sqlString);
		ps.setInt(1, idfile);
		ResultSet rs = ps.executeQuery();
		ArrayList<Integer> res = new ArrayList<Integer>();
		while(rs.next()) {
			res.add(rs.getInt("iduser"));
		}
		return res;
	}

	public void addShareFile(int idfile, int iduser) throws SQLException {
		String sqlString = "insert into sharefile(idfile, iduser) values(?,?)";
		ps = con.prepareStatement(sqlString);
		ps.setInt(1, idfile);
		ps.setInt(2, iduser);
		ps.executeUpdate();
	}

	public void deleteShareFile(int idfile) throws SQLException {
		String sqlString = "delete from sharefile where idfile = ?";
		ps = con.prepareStatement(sqlString);
		ps.setInt(1, idfile);
		ps.executeUpdate();
	}
}
