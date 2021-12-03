package com.company;

import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException {

		Client client = new Client();
    	
    	client.init();
		client.disconnect();
    	
    }
}
