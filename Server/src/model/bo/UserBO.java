package model.bo;

import java.sql.SQLException;
import java.util.ArrayList;

import model.bean.User;
import model.dao.UserDAO;

public class UserBO {
	UserDAO userDAO = new UserDAO();
	
	public boolean isValidUser(String username, String password) throws ClassNotFoundException, SQLException {
		return userDAO.isExistUser(username, password);
	}
	
	public boolean isAdmin(String username, String password) throws ClassNotFoundException, SQLException {
		return userDAO.isAdmin(username, password);
	}
	
	public ArrayList<User> getAllUser() throws ClassNotFoundException, SQLException {
		return userDAO.getAllUser();
	}
	
	public User getUser(int id) throws ClassNotFoundException, SQLException {
		return userDAO.getUser(id);
	}
	
	public User getUser(String username) throws ClassNotFoundException, SQLException {
		return userDAO.getUser(username);
	}
	
	public void addUser(String username, String password, String name, int role) throws ClassNotFoundException, SQLException {
		userDAO.addUser(username, password, name, role);
	}
	
	public void editUser(int id, String password, String name) throws ClassNotFoundException, SQLException {
		userDAO.editUser(id, password, name);
	}
	
	public void editUser(int id, String password, String name, int role) throws ClassNotFoundException, SQLException {
		userDAO.editUser(id, password, name, role);
	}
	
	public void deleteUser(int id) throws ClassNotFoundException, SQLException {
		userDAO.deleteUser(id);
	}
}
