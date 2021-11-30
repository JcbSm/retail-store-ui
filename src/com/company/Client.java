package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Client {
	
	private final String jdbc = "jdbc:postgresql://localhost:5432/retail_store";
	private final String username = "postgres";
	private final String password = "password";
		
	private Connection connection;
	private Scanner reader = new Scanner(System.in);
	
	public Client() {
		
		
	}
	
	public void start() {
		
		System.out.println("Starting application...");
		
		connect();
		
		int branchID;
		
		do {
			
			System.out.print("Enter branch number: ");
			
		} while (!branchID)
		
		
	}
	
	/**
	 * Connects the Client to the PostgreSQL server.
	 * @return boolean
	 */
	public boolean connect() {
		
		try {
			
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(jdbc, username, password);
			System.out.println("Connected to PostgreSQL server.");
			
			return true;
			
		} catch (SQLException e) {
			
			System.out.println("Error in connecting to PostgreSQL server.");
			e.printStackTrace();
			
			return false;
			
		}
		
	}
	
	/**
	 * Disconnects the Client to the PostgreSQL server.
	 * @return
	 */
	public boolean disconnect() {
		
		try {
			
			System.out.println("Disconnecting from PostgreSQL server...");
			connection.close();
			System.out.println("Disconnected.");
			return true;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
			
		}
		
		
	}
	
	
}
