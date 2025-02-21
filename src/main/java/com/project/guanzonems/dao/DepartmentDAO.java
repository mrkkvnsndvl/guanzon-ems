package com.project.guanzonems.dao;

import com.project.guanzonems.model.Department;
import com.project.guanzonems.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public void createDepartment(Department department) throws SQLException {
        String sql = "INSERT INTO departments (sDprtment) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, department.getLsDepartment());
            stmt.executeUpdate();
        }
    }

    public List<Department> readDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Department department = new Department();
                department.setLnId(rs.getInt("nIdxxxxxx"));
                department.setLsDepartment(rs.getString("sDprtment"));
                departments.add(department);
            }
        }
        return departments;
    }

    public void updateDepartment(Department department) throws SQLException {
        String sql = "UPDATE departments SET sDprtment = ? WHERE nIdxxxxxx = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, department.getLsDepartment());
            stmt.setInt(2, department.getLnId());
            stmt.executeUpdate();
        }
    }

    public void deleteDepartment(int lnId) throws SQLException {
        String sql = "DELETE FROM departments WHERE nIdxxxxxx = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lnId);
            stmt.executeUpdate();
        }
    }

    public boolean isDepartmentExists(String departmentName) throws SQLException {
        if (departmentName == null || departmentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty.");
        }

        String sql = "SELECT COUNT(*) FROM departments WHERE sDprtment = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departmentName.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public boolean isDepartmentBelongsToId(String departmentName, int departmentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM departments WHERE sDprtment = ? AND nIdxxxxxx = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departmentName.trim());
            stmt.setInt(2, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public List<String> getUniqueDepartments() throws SQLException {
        List<String> departments = new ArrayList<>();
        String sql = "SELECT DISTINCT sDprtment FROM employees ORDER BY sDprtment";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(rs.getString("sDprtment"));
            }
        }
        return departments;
    }
}
