package com.proj0.Bank;

import java.sql.Connection;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainClass {
	
	private static final Logger logger = LogManager.getLogger(MainClass.class);
	
	public static void main(String[] args) {
		Connection conn = ConnectionUtils.getConnection();
		Scanner scan = new Scanner(System.in);
		int choice;
		User user = new User();
		int flag = 0;

		System.out.println("Welcome!");

		while (flag == 0) {
			System.out.println("1)Login\n2)Apply for a bank account");
			choice = scan.nextInt();
			if (choice == 1) {
				user = User.login(conn);
				if(user.name!=null) {
					System.out.println("Logged in!! "+user.name);
					//log
					logger.info(user.role + " "+ user.getId()+" Has logged in ");
					flag=1;
				}
			}else {
				System.out.println("Register as a Customer");
				
					Customer.applyBankAcc(user,conn);
				
			}
		}
		
		if(user.role.equals("C")) {
			Customer.menu(user,conn);
		}else {
			Employee.menu(conn);
		}
		
	}
}
