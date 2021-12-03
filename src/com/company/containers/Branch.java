package com.company.containers;

import java.sql.Connection;
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
