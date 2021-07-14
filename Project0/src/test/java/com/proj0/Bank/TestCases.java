package com.proj0.Bank;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCases {
	static User user = new User(20210604,"Shivam",null,804.5,"C");
	Connection conn = ConnectionUtils.getConnection();
	
	@BeforeEach
	void beforeEach() {
		System.out.println("execution started");
	}
	@BeforeAll
	static void beforeAll() {
		System.out.println("STARTING");
	}
	@AfterAll
	static void afterAll() {
		System.out.println("DONE!!!");
	}
	@AfterEach
	void afterEach() {
		System.out.println("Execution done!!");
	}
	
	@Test
	void testOne() {
		System.out.println("=======Test one Execution Started!!=======");
		Assertions.assertEquals(user.getBalance(),Customer.viewBalance(user));
	}
	@Test
	void testTwo() {
		System.out.println("=======Test Two Transaction Execution Started!!=======");
		Assertions.assertEquals(0,Customer.transaction(user, 4.5, 1, conn));
	}
	@Test
	void testThree() {
		System.out.println("=======Test Three  Execution Started!!=======");
		Assertions.assertEquals(0,Customer.transferMoney(20210603, user, 100,conn));
	}

}
	
	
	
	
	