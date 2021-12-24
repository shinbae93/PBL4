package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.bean.User;

public class UserDAO {
	private Connection con;
	private PreparedStatement ps;

	public UserDAO() {
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

	public boolean isExistUser(String username, String password) throws SQLException {
		String sqlString = "select * from user where username = ? and password = ?";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, username);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}
	
	public boolean isAdmin(String username, String password) throws SQLException {
		String sqlString = "select * from user where username = ? and password = ? and role = 1";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, username);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}

	public ArrayList<User> getAllUser() throws SQLException {
		String sqlString = "select * from user";
		ps = con.prepareStatement(sqlString);
		ResultSet rs = ps.executeQuery();
		ArrayList<User> res = new ArrayList<User>();
		while (rs.next()) {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setName(rs.getString("name"));
			user.setRole(rs.getInt("role"));
			res.add(user);
		}
		return res;
	}
	
	public User getUser(String username) throws SQLException {
		String sqlString = "select * from user where username = ?";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setRole(rs.getInt("role"));
		return user;
	}

	public User getUser(int id) throws SQLException {
		String sqlString = "select * from user where id = ?";
		ps = con.prepareStatement(sqlString);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setRole(rs.getInt("role"));
		return user;
	}

	public void addUser(String username, String password, String name, int role) throws SQLException {
		String sqlString = "insert into user(username,password,name,role) values(?,?,?,?)";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, username);
		ps.setString(2, password);
		ps.setString(3, name);
		ps.setInt(4, role);
		ps.executeUpdate();
	}
	
	public void editUser(int id, String password, String name) throws SQLException {
		String sqlString = "update user set password = ?, name = ? where id = ?";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, password);
		ps.setString(2, name);
		ps.setInt(3, id);
		ps.executeUpdate();
	}

	public void editUser(int id, String password, String name, int role) throws SQLException {
		String sqlString = "update user set password = ?, name = ?, role = ? where id = ?";
		ps = con.prepareStatement(sqlString);
		ps.setString(1, password);
		ps.setString(2, name);
		ps.setInt(3, role);
		ps.setInt(4, id);
		ps.executeUpdate();
	}

	public void deleteUser(int id) throws SQLException {
		String sqlString = "delete from user where id = ?";
		ps = con.prepareStatement(sqlString);
		ps.setInt(1, id);
		ps.executeUpdate();
	}
}
