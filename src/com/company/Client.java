package com.company;

import com.company.containers.Branch;
import com.company.containers.Product;
import com.company.containers.Stock;

import java.sql.*;
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
		System.out.println("2. Write on");
		System.out.println("3. Write off");
		System.out.println("4. Change condition");
		System.out.println("5. Home");
		System.out.println();
		System.out.println("-------------------------------------------------\n");

		return selectMenuOption(5);

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
			case 2 -> writeOn();
			case 3 -> writeOff();
			case 4 -> changeCondition();
			case 5 -> menuMain();
		}

	}

	private void queryStock() {

		boolean restart;

		do {

			restart = false;

			System.out.println("\n\n----------| Stock Query |------------");
			System.out.print("Product code: ");

			int input;

			try {

				input = reader.nextInt();

				Product product = new Product(input, connection);

				ResultSet rs = connection.prepareStatement(String.format("SELECT id FROM stock WHERE product_id = %d AND branch_id = %d", product.getId(), branch.getId())).executeQuery();
				int[] counts = countStockConditions(rs);

				System.out.printf("%n%s %s%n%n", product.getManufacturer(), product.getModel());
				displayStock(counts, false);

				System.out.print("\nWould you like to query stock again?: (y) ");
				if (Objects.equals(reader.next().toLowerCase(), "y")) restart = true;

			} catch (Exception ex) {
				ex.printStackTrace();
				restart = true;
			}

			reader.nextLine();

		} while (restart);

		menuStockManagement();

	}

	private void writeOn() {

		boolean restart;

		do {

			restart = false;

			System.out.println("\n\n----------| Write On |------------");
			System.out.print("Enter product code: ");

			int input;

			try {

				input = reader.nextInt();

				Product product = new Product(input, connection);
				String mf = product.getManufacturer(); String md = product.getModel();

				ResultSet rs = connection.prepareStatement(String.format("SELECT id FROM stock WHERE product_id = %d AND branch_id = %d", product.getId(), branch.getId())).executeQuery();
				int[] counts = countStockConditions(rs);

				System.out.printf("%n%s %s%n%n", product.getManufacturer(), product.getModel());
				displayStock(counts, false);

				System.out.printf("%nHow many items are your writing on: ");
				int count = reader.nextInt();

				System.out.printf("%nWriting on %dx %s %s (Pristine)...%n", count, mf, md);
				for (int i = 0; i < count; i++) {

					branch.addStock(product.getId(), 0);

				}
				System.out.println("Success!");

				System.out.print("\nWould you like to write-on stock again?: (y) ");
				if (Objects.equals(reader.next().toLowerCase(), "y")) restart = true;

			} catch(Exception ex) {
				ex.printStackTrace();
				restart = true;
			}

			reader.nextLine();

		} while (restart);

		menuStockManagement();

	}

	private void writeOff() {

		boolean restart;

		do {

			restart = false;

			System.out.println("\n\n----------| Write Off |------------");
			System.out.print("Enter product code: ");

			int input;

			try {

				input = reader.nextInt();

				Product product = new Product(input, connection);
				String mf = product.getManufacturer(); String md = product.getModel();

				ResultSet rs = connection.prepareStatement(String.format("SELECT id FROM stock WHERE product_id = %d AND branch_id = %d", product.getId(), branch.getId())).executeQuery();
				int[] counts = countStockConditions(rs);

				System.out.printf("%n%s %s%n%n", product.getManufacturer(), product.getModel());
				displayStock(counts, false);

				System.out.printf("%nHow many items are your writing off: ");
				int count = reader.nextInt();

				// Check there are enough products to write off
				int currentPristine = counts[0];

				if (currentPristine < count) throw new Exception("Not enough pristine stock to write off this many items.");

				System.out.printf("%nWriting off %dx %s %s (Pristine)...%n", count, mf, md);
				for (int i = 0; i < count; i++) {

					branch.removeStock(product.getId(), 0);

				}
				System.out.println("Success!");

				System.out.print("\nWould you like to write-on stock again?: (y) ");
				if (Objects.equals(reader.next().toLowerCase(), "y")) restart = true;

			} catch(Exception ex) {
				ex.printStackTrace();
				restart = true;
			}

			reader.nextLine();

		} while (restart);

		menuStockManagement();

	}

	private void changeCondition() {

		boolean restart;

		do {

			restart = false;

			System.out.println("\n\n----------| Change Condition |------------");
			System.out.print("Enter product code: ");

			int input;

			try {

				input = reader.nextInt();

				// Get product info
				Product product = new Product(input, connection);
				ResultSet rs = connection.prepareStatement(String.format("SELECT id, condition FROM stock WHERE product_id = %d AND branch_id = %d", product.getId(), branch.getId())).executeQuery();
				int[] counts = countStockConditions(rs);

				int oldCondition = 0;

				while (oldCondition == 0) {

					// Display current stock
					System.out.printf("%n%s %s%n%n", product.getManufacturer(), product.getModel());
					// Ask for old condition
					System.out.println("What condition is the product currently displayed as?");
					displayStock(counts, true);
					System.out.println();

					oldCondition = selectMenuOption(4);
					System.out.println();

					if (counts[oldCondition - 1] == 0) {

						System.out.println("No stock listed under this condition.");

						oldCondition = 0;
						continue;
					}

				}

				int newCondition = 0;

				while (newCondition == 0) {

					System.out.println("What condition is the product now?");
					displayStock(counts, true);
					System.out.println();

					newCondition = selectMenuOption(4);
					System.out.println();

				}

				if (oldCondition < 1 || oldCondition > 4) throw new Exception("Old condition can't be out of range 1-4");
				if (newCondition < 1 || newCondition > 4) throw new Exception("New condition can't be out of range 1-4");

				// -1 to values because they're stored from 0 :)
				branch.changeStockCondition(product.getId(), oldCondition - 1, newCondition - 1);

				System.out.println("Success!\n");
				System.out.println("------------------------------------------");

				System.out.print("\nWould you like to change stock condition again?: (y) ");
				if (Objects.equals(reader.next().toLowerCase(), "y")) restart = true;


			} catch(Exception ex) {
				ex.printStackTrace();
				restart = true;
			}

			reader.nextLine();

		} while (restart);

		menuStockManagement();

	}

	private void showMenuAdmin() {

	}

	private void exit() {

	}

	public Branch getBranch() {
		return branch;
	}

	/**
	 * Counts the stock from a given ResultSet
	 * @param rs ResultSet containing stock data from Postgres
	 * @return Array of values [0] - Pristine, [1] - Non-pristine, [2] - Faulty, [3] - Held-for
	 * @throws Exception If Stock can't be found or there is no rs.next()
	 */
	private int[] countStockConditions(ResultSet rs) throws Exception {

		int pp = 0;
		int np = 0;
		int ff = 0;
		int hf = 0;

		while (rs.next()) {

			Stock stock = new Stock(rs.getInt(1), connection);

			switch (stock.getCondition()) {
				case 0 -> pp = pp + 1;
				case 1 -> np = np + 1;
				case 2 -> ff = ff + 1;
				case 3 -> hf = hf + 1;
			}

		}

		return new int[]{pp, np, ff, hf};
	}

	private void displayStock(int[] counts, boolean numbered) throws Exception {

		if (numbered) {

			System.out.printf("1. Pristine: 		%d%n", counts[0]);
			System.out.printf("2. Non-Pristine: 	%d%n", counts[1]);
			System.out.printf("3. Faulty:			%d%n", counts[2]);
			System.out.printf("4. Held-for: 		%d%n", counts[3]);

		} else {

			System.out.printf("Pristine: 		%d%n", counts[0]);
			System.out.printf("Non-Pristine: 	%d%n", counts[1]);
			System.out.printf("Faulty:			%d%n", counts[2]);
			System.out.printf("Held-for: 		%d%n", counts[3]);

		}
	}

}
