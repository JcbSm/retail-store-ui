package com.company.containers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock {

    private final Connection connection;
    private final int id;

    public Stock(int id, Connection connection) throws Exception {

        this.id = id;
        this.connection = connection;

        ResultSet rs = connection.prepareStatement(String.format("SELECT product_id, branch_id, condition FROM stock WHERE id = %d", id)).executeQuery();

        if (!rs.next()) throw new Exception("Invalid stock ID. No stock found.");

    }

    public Product getProduct() {

        Product product = null;

        ResultSet rs;

        try {

            rs = connection.prepareStatement(String.format("SELECT product_id FROM stock WHERE id = %d", id)).executeQuery();


            if (rs.next()) {
                product = new Product(rs.getInt(1), connection);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;

    }

    public Branch getBranch() {

        Branch branch = null;

        ResultSet rs;

        try {

            rs = connection.prepareStatement(String.format("SELECT branch_id FROM stock WHERE id = %d", id)).executeQuery();


            if (rs.next()) {
                branch = new Branch(rs.getInt(1), connection);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return branch;

    }

    public int getCondition() {

        int condition = 0;

        ResultSet rs;

        try {

            rs = connection.prepareStatement(String.format("SELECT condition FROM stock WHERE id = %d", id)).executeQuery();


            if (rs.next()) {
                condition = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return condition;

    }

    public String getConditionStr() {

        switch (getCondition()) {

            default:
            case 0:
                return "Pristine";
            case 1:
                return "Pre-owned";
            case 2:
                return "Faulty";
            case 3:
                return "Held-for";

        }
    }
}
