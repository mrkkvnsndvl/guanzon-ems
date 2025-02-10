package com.project.guanzonems.dao;

import com.project.guanzonems.utilities.DatabaseConnection;

import java.sql.*;

public class AdminDAO {
    public boolean authenticateAdmin(String email, String password) throws SQLException {
        String sql = "SELECT sPassword FROM admin WHERE sEmailxxx = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("sPassword");
                    return password.equals(storedPassword);
                }
            }
        }
        return false;
    }
}