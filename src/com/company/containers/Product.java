package com.company.containers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {

    private final Connection connection;
    private final int id;

    public Product(int id, Connection connection) throws Exception {

        this.id = id;
        this.connection = connection;

        ResultSet rs = connection.prepareStatement(String.format("SELECT id FROM products WHERE id = %d", id)).executeQuery();

        if (!rs.next()) throw new Exception("Invalid product ID. No product found.");

    }

    public String getManufacturer() {

        String name = "none";

        ResultSet rs;

        try {

            rs = connection.prepareStatement(String.format("SELECT m.name FROM products p INNER JOIN manufacturers m ON p.manufacturer_id = m.id WHERE p.id = %d", id)).executeQuery();


            if (rs.next()) {
                name = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;

    }

    public String getModel() {

        String name = "N";

        ResultSet rs;

        try {

            rs = connection.prepareStatement(String.format("SELECT model FROM products WHERE id = %d", id)).executeQuery();


            if (rs.next()) {
                name = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;

    }
}
