package com.project.guanzonems.dao;

import com.project.guanzonems.model.Employee;
import com.project.guanzonems.utilities.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDAO {

    public void createEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (nIdxxxxxx, sFullName, nAgexxxxx, sEmailxxx, sPhnNmber, sPosition, sDprtment, dDtOfJnng, nSalaryxx) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employee.getLnId());
            stmt.setString(2, employee.getLsFullName());
            stmt.setInt(3, employee.getLnAge());
            stmt.setString(4, employee.getLsEmail());
            stmt.setString(5, employee.getLsPhoneNumber());
            stmt.setString(6, employee.getLsPosition());
            stmt.setString(7, employee.getLsDepartment());
            stmt.setDate(8, Date.valueOf(employee.getLdDateOfJoining()));
            stmt.setDouble(9, employee.getLdSalary());
            stmt.executeUpdate();
        }
    }

    public List<Employee> readEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT nIdxxxxxx, sFullName, nAgexxxxx, sEmailxxx, sPhnNmber, sPosition, sDprtment, dDtOfJnng, nSalaryxx FROM employees";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setLnId(rs.getInt("nIdxxxxxx"));
                employee.setLsFullName(rs.getString("sFullName"));
                employee.setLnAge(rs.getInt("nAgexxxxx"));
                employee.setLsEmail(rs.getString("sEmailxxx"));
                employee.setLsPhoneNumber(rs.getString("sPhnNmber"));
                employee.setLsPosition(rs.getString("sPosition"));
                employee.setLsDepartment(rs.getString("sDprtment"));
                employee.setLdDateOfJoining(rs.getDate("dDtOfJnng").toLocalDate());
                employee.setLdSalary(rs.getDouble("nSalaryxx"));
                employees.add(employee);
            }
        }
        return employees;
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET sFullName = ?, nAgexxxxx = ?, sEmailxxx = ?, sPhnNmber = ?, sPosition = ?, sDprtment = ?, dDtOfJnng = ?, nSalaryxx = ? WHERE nIdxxxxxx = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.getLsFullName());
            stmt.setInt(2, employee.getLnAge());
            stmt.setString(3, employee.getLsEmail());
            stmt.setString(4, employee.getLsPhoneNumber());
            stmt.setString(5, employee.getLsPosition());
            stmt.setString(6, employee.getLsDepartment());
            stmt.setDate(7, Date.valueOf(employee.getLdDateOfJoining()));
            stmt.setDouble(8, employee.getLdSalary());
            stmt.setInt(9, employee.getLnId());
            stmt.executeUpdate();
        }
    }

    public void deleteEmployee(int lnId) throws SQLException {
        String sql = "DELETE FROM employees WHERE nIdxxxxxx = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lnId);
            stmt.executeUpdate();
        }
    }

    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE sEmailxxx = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean isEmailBelongsToEmployee(String email, int employeeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE sEmailxxx = ? AND nIdxxxxxx = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<String> getUniquePositions() throws SQLException {
        List<String> positions = new ArrayList<>();
        String sql = "SELECT DISTINCT sPosition FROM employees ORDER BY sPosition";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                positions.add(rs.getString("sPosition"));
            }
        }
        return positions;
    }

    public Map<String, Integer> getEmployeeCountsByDepartment() throws SQLException {
        Map<String, Integer> departmentCounts = new HashMap<>();
        String sql = "SELECT sDprtment, COUNT(*) AS employeeCount FROM employees GROUP BY sDprtment";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String department = rs.getString("sDprtment");
                int count = rs.getInt("employeeCount");
                departmentCounts.put(department, count);
            }
        }
        return departmentCounts;
    }
}
