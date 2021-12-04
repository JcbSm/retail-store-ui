package com.company.containers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Branch {

    private final Connection connection;
    private final int id;

    public Branch(int id, Connection connection) throws Exception {

        this.id = id;
        this.connection = connection;

        ResultSet rs = connection.prepareStatement(String.format("SELECT id FROM branches WHERE id = %d", id)).executeQuery();

        if (!rs.next()) throw new Exception("Invalid branch ID. No branch found.");

    }

    public void addStock(int productId, int condition) throws SQLException {

        connection.prepareStatement(String.format("INSERT INTO stock (branch_id, product_id, condition) VALUES (%d, %d, %d);", this.id, productId, condition)).executeUpdate();

    }

    public void removeStock(int productId, int condition) throws SQLException {

        connection.prepareStatement(String.format("DELETE FROM stock WHERE id in (SELECT id from stock WHERE branch_id = %d AND product_id = %d AND condition = %d LIMIT 1)", this.id, productId, condition)).executeUpdate();

    }

    public void changeStockCondition(int productId, int oldCondition, int newCondition) throws SQLException {

        connection.prepareStatement(String.format("UPDATE stock SET condition = %d WHERE id IN (SELECT id FROM stock WHERE condition = %d AND branch_id = %d AND product_id = %d LIMIT 1);", newCondition, oldCondition, this.id, productId)).executeUpdate();

    }

    public void addEmployee(String fName, String lName, Date dob, int departmentId) throws SQLException {

        connection.prepareStatement(String.format("INSERT INTO employees (first_name, last_name, dob, department_id, branch_id) VALUES ('%s', '%s', '%s', %d, %d);", fName, lName, dob, departmentId, this.id)).executeUpdate();

    }

    /**
     * @return Postcode for the branch
     * @throws SQLException SQL
     */
    public String getPostcode() throws SQLException {

        ResultSet rs = connection.prepareStatement(String.format("SELECT postcode FROM branches WHERE id = %d", id)).executeQuery();

        if (rs.next()) {
            return rs.getString(1);
        } else {
            return "null";
        }

    }

    public String getName() {

        String name = "none";

        ResultSet rs;

        try {

            rs = connection.prepareStatement(String.format("SELECT name FROM branches WHERE id = %d", id)).executeQuery();


            if (rs.next()) {
                name = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;

    }

    public int getId() {
        return this.id;
    }
}
