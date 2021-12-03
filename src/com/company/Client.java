package com.company;

import com.company.containers.Branch;
import com.company.containers.Product;
import com.company.containers.Stock;

import java.sql.*;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class Client {
		
	private Connection connection;
	private final Scanner reader = new Scanner(System.in);

	private Branch branch;
	
	public Client() {
		
		
	}

	/**
	 * Initiates the client with a branch.
	 */
	public void init(){
		
		System.out.println("Starting application...");

		// Connect to the PSQL server.
		connect();

		while (true) {

			System.out.println();

			int id;

			// Ask for input
			System.out.print("Enter branch number: ");
			// Check and catch
			try {
				id = reader.nextInt();
			} catch (Exception ex) {
				id = 0;
			}

			// Go to next line
			reader.nextLine();

			// If int
			if (id > 0) {

				try {

					branch = new Branch(id, connection);
					break;

				} catch (Exception ex) {

					System.out.println("No branch found.");

				}

			} else {
				System.out.println("Invalid branch number.");
			}
			
		}

		menuMain();
		
	}
	
	/**
	 * Connects the Client to the Postgres server.
	 */
	public void connect() {

		String jdbc = "jdbc:postgresql://localhost:5432/retail_store";
		String username = "postgres";
		String password = "password";
		
		try {
			
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(jdbc, username, password);
			System.out.println("Connected to Postgres server.");
			
		} catch (SQLException e) {
			
			System.out.println("Error in connecting to Postgres server.");
			e.printStackTrace();

		}
		
	}

	/**
	 * Disconnects the Client to the Postgres server.
	 */
	public void disconnect() {
		
		try {
			
			System.out.println("Disconnecting from Postgres server...");
			connection.close();
			System.out.println("Disconnected.");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		
	}

	/**
	 * Prompts the user to select an option from a menu
	 * @param max The maximum number of options to choose from.
	 * @return selected integer
	 */
	private int selectMenuOption(int max) {

		System.out.print("Select an option: ");

		int selected;

		try {
			selected = reader.nextInt();
		} catch (Exception ex) {
			selected = 0;
		}

		reader.nextLine();

		return (selected >= 1 && selected <= max) ? selected : 0;

	}

	private int showMenuMain() {

		// 1-4 Choices
		System.out.println("\n\n\n\n------------| MAIN MENU |------------");
		System.out.printf("Branch: %s%n", branch.getName());
		System.out.println();
		System.out.println("1. Sales");
		System.out.println("2. Stock Management");
		System.out.println("3. Admin");
		System.out.println("4. Quit");
		System.out.println();
		System.out.println("-------------------------------------\n");

		return selectMenuOption(4);

	}

	private void menuMain() {

		int selected = 0;

		while (selected == 0) {

			selected = showMenuMain();

		}

		switch (selected) {
			case 1 -> menuSales();
			case 2 -> menuStockManagement();
			case 3 -> showMenuAdmin();
			case 4 -> exit();
		}

	}

	private int showMenuSales() {

		// 1-4 Choices
		System.out.println("\n\n\n\n------------| SALES MENU |------------");
		System.out.printf("Branch: %s%n", branch.getName());
		System.out.println();
		System.out.println("1. Sell");
		System.out.println("2. Returns");
		System.out.println("3. Home");
		System.out.println();
		System.out.println("--------------------------------------\n");
		System.out.print("Select an option: ");

		return selectMenuOption(3);

	}

	private void menuSales() {

		int selected = 0;

		while (selected == 0) {

			selected = showMenuSales();

		}

		switch (selected) {
			case 1 -> sale();
			case 2 -> returns();
			case 3 -> menuMain();
		}

	}

	private void sale() {}
	private void returns() {}

	private int showMenuStockManagement() {

		// 1-4 Choices
		System.out.println("\n\n\n\n------------| STOCK MANAGEMENT MENU |------------");
		System.out.printf("Branch: %s%n", branch.getName());
		System.out.println();
		System.out.println("1. Query Stock levels");
		System.out.println("2. Home");
		System.out.println();
		System.out.println("-------------------------------------------------\n");
		System.out.print("Select an option: ");

		return selectMenuOption(2);

	}

	private void menuStockManagement() {

		int selected = 0;

		while (selected == 0) {

			selected = showMenuStockManagement();

		}

		switch (selected) {
			default -> {
			}
			case 1 -> queryStock();
			case 2 -> menuMain();
		}

	}

	private void queryStock() {

		boolean restart;

		do {

			restart = false;

			System.out.print("\n\nEnter the product code: ");

			int input;

			try {

				input = reader.nextInt();
				System.out.println();

				ResultSet rs = connection.prepareStatement(String.format("SELECT id FROM stock WHERE product_id = %d AND branch_id = %d", input, branch.getId())).executeQuery();

				while (rs.next()) {

					Stock stock = new Stock(rs.getInt(1), connection);
					Product product = stock.getProduct();
					Branch branch = stock.getBranch();

					System.out.println(product.getManufacturer() + " | " + product.getModel() + " | " + stock.getConditionStr());

				}

				System.out.print("\nWould you like to query stock again?: (y) ");
				if (Objects.equals(reader.next().toLowerCase(), "y")) restart = true;

			} catch (Exception ex) {
				ex.printStackTrace();
				restart = true;
			}

			reader.nextLine();

		} while (restart);

	}

	private void showMenuAdmin() {

	}

	private void exit() {

	}

	public Branch getBranch() {
		return branch;
	}
	
}
