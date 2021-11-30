package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Client {
	
	private final String jdbc = "jdbc:postgresql://localhost:5432/retail_store";
	private final String username = "postgres";
	private final String password = "password";
	
	private Connection connection;
	
	public Client() {
		
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
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
			
		}
		
		
	}
	
}
