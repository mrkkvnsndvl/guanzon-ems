package com.project.guanzonemployeemanagementsystem.dao;

import com.project.guanzonemployeemanagementsystem.model.Employee;
import com.project.guanzonemployeemanagementsystem.utilities.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (full_name, age, email, phone_number, position, department, date_of_joining, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.getLsFullName());
            stmt.setInt(2, employee.getLnAge());
            stmt.setString(3, employee.getLsEmail());
            stmt.setString(4, employee.getLsPhoneNumber());
            stmt.setString(5, employee.getLsPosition());
            stmt.setString(6, employee.getLsDepartment());
            stmt.setDate(7, Date.valueOf(employee.getLdDateOfJoining()));
            stmt.setDouble(8, employee.getLdSalary());
            stmt.executeUpdate();
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setLnId(rs.getInt("id"));
                employee.setLsFullName(rs.getString("full_name"));
                employee.setLnAge(rs.getInt("age"));
                employee.setLsEmail(rs.getString("email"));
                employee.setLsPhoneNumber(rs.getString("phone_number"));
                employee.setLsPosition(rs.getString("position"));
                employee.setLsDepartment(rs.getString("department"));
                employee.setLdDateOfJoining(rs.getDate("date_of_joining").toLocalDate());
                employee.setLdSalary(rs.getDouble("salary"));
                employees.add(employee);
            }
        }
        return employees;
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET full_name = ?, age = ?, email = ?, phone_number = ?, position = ?, department = ?, date_of_joining = ?, salary = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lnId);
            stmt.executeUpdate();
        }
    }
}
