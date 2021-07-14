package com.proj0.Bank;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Customer extends User {

	User user;
	private static final Logger logger = LogManager.getLogger(Customer.class);

	// 1
	/*
	 * public void applyBankAcc(String name, String password, double balance, String
	 * role) { user = new User(name, password, balance, role);
	 * System.out.println("Applied"); }
	 */
	@SuppressWarnings("deprecation")
	public static int transaction(User user, double amount, int transFlag, Connection conn) {
		PreparedStatement stmt;
		String query;
		int exeStatus = 1;
		logger.entry();

		if (transFlag == 0) {
			// call withdraw function in database

			if (user.getBalance() - amount > 0) {
				query = "update users set balance= ? where id=?";
				try {
					stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					stmt.setDouble(1, user.getBalance() - amount);
					stmt.setInt(2, user.getId());

					int rowAffected = stmt.executeUpdate();
					if (rowAffected == 1) {
						ResultSet rs = stmt.getGeneratedKeys();
						if (rs.next()) {
							user.setBalance(rs.getDouble(4));
							System.out.println("Current balance is: " + user.getBalance());
							exeStatus=0;
						}
						
					}else {
						exeStatus=1;
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				logger.info(user.getId() + " Withdrawed " + amount);
			} else {
				System.out.println("Insufficient Funds!! \n");
			}
		} else {
			// call deposit function in database

			query = "update users set balance= ? where id=?";
			try {
				stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				stmt.setDouble(1, user.getBalance() + amount);
				stmt.setInt(2, user.getId());

				int rowAffected = stmt.executeUpdate();
				if (rowAffected == 1) {
					ResultSet rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						user.setBalance(rs.getDouble(4));
						System.out.println("Current balance is: " + user.getBalance());
						exeStatus=0;
					}
				}else{
					exeStatus=1;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info(user.getId() + " Deposited " + amount);
		}
		logger.exit();
		return exeStatus;
	}

	public static Double viewBalance(User user) {
		return user.getBalance();
	}

	@SuppressWarnings("deprecation")
	public static int transferMoney(int receiver_id, User user, double amount, Connection conn) {
		// call function transferMoney db
		int exeStatus = 1;
		
		if (user.getBalance() - amount < 0) {
			System.out.println("Insuficient funds!!");
		} else {
			logger.entry();
			try {
				CallableStatement cstmt;
				cstmt = conn.prepareCall("call transfers(? , ? , ?)");
				cstmt.setInt(1, user.getId());
				cstmt.setInt(2, receiver_id);
				cstmt.setDouble(3, amount);

				if (!cstmt.execute()) {
					System.out.println("Sent!\n");
					user.setBalance(user.getBalance() - amount);
					logger.info(user.getId() + " Sent " + receiver_id + " " + amount);
					exeStatus=0;

				} else {
					System.out.println("Something went wrong! Try again later.");
					exeStatus=1;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			logger.exit();
		}
		return exeStatus;
	}

	public static void applyBankAcc(User user, Connection conn) {
		int flag = 0;
		Scanner scan = new Scanner(System.in);

		System.out.println("\nEnter your name:");
		user.setName(scan.next());
		System.out.println("Initial balance:");
		user.setBalance(scan.nextDouble());

		while (flag == 0) {
			System.out.println("Enter your password:");
			String pass = scan.next();
			System.out.println("Re-enter your password:");
			String pass1 = scan.next();
			if (pass.equals(pass1)) {
				user.setPassword(pass);
				flag = 1;
				System.out.println("please wait!!");
			} else {
				System.out.println("Not same... Try again!!");
			}
		}

		String query = "insert into users(name,password,balance,role,status) values(?,?,?,'C',1)"; // 1-applied(notactivated)
																									// 0-activated
																									// 11-deactivated
		try {

			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getPassword());
			stmt.setDouble(3, user.getBalance());

			int rowAffected = stmt.executeUpdate();
			if (rowAffected == 1) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					user.setId(rs.getInt(1));
					System.out.println("Your Id is: " + user.getId());
				}
				System.out.println("Registered successfully!! Please login.....\n\n");
			} else {
				System.out.println("Something went wrong!! Please try again...\n\n");
				user = new User();
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void menu(User user, Connection conn) {
		int flag = 0;
		Scanner scan = new Scanner(System.in);
		
		while (flag == 0) {
			System.out.println("\nCustomer\n1)Transfer money\n2)Withdraw \n3)Deposit \n4)View Balance \n0)Logout");
			
			int choice = scan.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Enter Receiver's id: ");
				int recId = scan.nextInt();
				System.out.println("Enter the amount: ");
				double amount = scan.nextDouble();
				if(amount>0) {
				transferMoney(recId, user, amount, conn);
				}else {
					System.out.println("Invalid amount!!");
				}
				break;
			case 2:
				System.out.println("Withdraw\nEnter amount: ");
				amount = scan.nextDouble();
				if(amount>0) {
					transaction(user, amount, 0, conn);
					}else {
						System.out.println("Invalid amount!!");
					}
				// withdraw
				break;
			case 3:
				System.out.println("Deposit\nEnter amount: ");
				amount = scan.nextDouble();
				if(amount>0) {
					transaction(user, amount, 1, conn);
					}else {
						System.out.println("Invalid amount!!");
					}
				// deposit
				break;
			case 4:
				System.out.println("Your current balance is " + viewBalance(user) + "\n");
				break;
			case 0:
				logger.info(user.getId() + " Logged Out!!");
				flag = 1;
				break;
			default:
				System.out.println("Please choose correct option\n");
				break;
			}

		}

	}

}
