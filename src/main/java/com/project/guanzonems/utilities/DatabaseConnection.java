package com.project.guanzonems.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mariadb://localhost:3306/guanzon_ems";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            if (connection != null) {
                System.out.println("Database connection successful!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
