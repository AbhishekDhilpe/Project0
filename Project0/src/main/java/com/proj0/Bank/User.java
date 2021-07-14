package com.proj0.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class User {
	int id;
	String name;
	String password;
	double balance;
	String role;

	
	public User() {
		super();
	}

	public User(int id, String name, String password, double balance, String role) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.balance = balance;
		this.role = role;
	}

	public User(String name, String password, double balance, String role) {
		super();
		this.name = name;
		this.password = password;
		this.balance = balance;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", balance=" + balance + ", role="
				+ role + "]";
	}

	@SuppressWarnings("deprecation")
	public static User login(Connection conn) {
		// query select * from table_name where id=id and password=password;
		// if resultset != null then return true or else false
		
		User user = new User();
		int tempId;
		String tempPwd;
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your id");
		tempId = scan.nextInt();
		System.out.println("Enter your password");
		tempPwd = scan.next();

		String query = "select * from users where id=? and password=? and status=0";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, tempId);
			stmt.setString(2, tempPwd);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				user.id = rs.getInt(1);
				user.name = rs.getString(2);
				user.balance = rs.getDouble(4);
				user.role = rs.getString(5);
			} else {
				System.out.println("Please try again!!");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		
		return user;// check
		
	}

}
