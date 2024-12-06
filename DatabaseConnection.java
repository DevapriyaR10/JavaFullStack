package com.mycompany.billingsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Static method to connect to the database
    public static Connection connect() throws SQLException {
        try {
            // Load and register the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Database connection details
            String url = "jdbc:mysql://localhost:3306/billing_system";  // Replace with your actual database name
            String username = "root";  // Your MySQL username
            String password = "1234";  // Your MySQL password

            // Establish the connection
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new SQLException("Error connecting to the database: " + ex.getMessage());
        }
    }

}
