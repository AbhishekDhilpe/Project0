package com.proj0.Bank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Employee extends User {

	public static void requestsAcceptReject(Connection conn) {
		Scanner scan = new Scanner(System.in);
		int status;
		String query = "select * from users where status=1";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			int flag = 0;
			while (flag == 0) {
				while (rs.next()) {
					System.out.println("Id " + rs.getInt(1) + "  Name: " + rs.getString(2) + "  Balance: "
							+ rs.getString(4) + "Status: " + rs.getString(6));
					System.out.println("1)Accept\n2)Reject\3)exit");
					status = scan.nextInt();
					switch (status) {
					case 1:
						query = "update users set status=0 where id= ?";
						stmt = conn.prepareStatement(query);
						stmt.setInt(1, rs.getInt(1));
						stmt.execute();
						System.out.println("Accepted");
						break;
					case 2:
						query = "delete from users where id= ?";
						stmt = conn.prepareStatement(query);
						stmt.setInt(1, rs.getInt(1));
						stmt.execute();
						System.out.println("Rejected");
						break;
					case 3:
						flag = 1;
						System.out.println("Exited!!");
						break;
					default:
						System.out.println("Option mismatched!!");
						break;

					}
				}
			}
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void viewCustomerAcc(Connection conn) {
		Scanner scan = new Scanner(System.in);
		int id;
		System.out.println("Enter customer's id: ");
		id = scan.nextInt();
		String query = "select * from users where id= ?";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out
						.println("Id " + rs.getInt(1) + "  Name: " + rs.getString(2) + "  Balance: " + rs.getString(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public static void showLogs() {
		File file = new File("D:/JavaTraining/JDBC/Project0/BankLoggs.log");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			System.out.println("THE TRANSACTIONS PERFORMED TILL NOW: ");
			while ((st = br.readLine()) != null)
				System.out.println(st);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void menu(Connection conn) {
		int flag = 0;
		Scanner scan = new Scanner(System.in);
		while (flag == 0) {
			System.out.println("\nEmployee \n1)Requests \n2)View Customer Account \n3)Show logs");
			int choice = scan.nextInt();
			switch (choice) {
			case 1:
				requestsAcceptReject(conn);
				break;
			case 2:
				viewCustomerAcc(conn);
				break;
			case 3:
				showLogs();
				break;
			default:
				System.out.println("Please enter valid choice...");
				break;
			// enter correct choice

			}

		}
	}
}
