package com.project.guanzonemployeemanagementsystem.dao;

import com.project.guanzonemployeemanagementsystem.model.Department;
import com.project.guanzonemployeemanagementsystem.utilities.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public void addDepartment(Department department) throws SQLException {
        String sql = "INSERT INTO departments (department_name) VALUES (?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, department.getLsDepartmentName());
            stmt.executeUpdate();
        }
    }

    public List<Department> getAllDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Department department = new Department();
                department.setLnId(rs.getInt("id"));
                department.setLsDepartmentName(rs.getString("department_name"));
                departments.add(department);
            }
        }
        return departments;
    }

    public void updateDepartment(Department department) throws SQLException {
        String sql = "UPDATE departments SET department_name = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, department.getLsDepartmentName());
            stmt.setInt(2, department.getLnId());
            stmt.executeUpdate();
        }
    }

    public void deleteDepartment(int lnId) throws SQLException {
        String sql = "DELETE FROM departments WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lnId);
            stmt.executeUpdate();
        }
    }
}
