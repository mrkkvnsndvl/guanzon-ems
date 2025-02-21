package com.project.guanzonems.dao;

import com.project.guanzonems.model.DailyAttendance;
import com.project.guanzonems.utilities.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DailyAttendanceDAO {

    public List<DailyAttendance> getDailyAttendanceByDate(LocalDate ldDate) {
        List<DailyAttendance> attendanceList = new ArrayList<>();
        String query = "SELECT e.nIdxxxxxx AS lnId, e.sFullName AS lsFullName, e.sDprtment AS lsDepartment, "
                + "da.sStatusxx AS lsStatus, da.tTimeInxx AS ltTimeIn, da.tTimeOutx AS ltTimeOut, da.dDatexxxx AS ldDate "
                + "FROM employees e "
                + "LEFT JOIN daily_attendance da ON e.nIdxxxxxx = da.nEmployId AND da.dDatexxxx = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(ldDate));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int lnId = resultSet.getInt("lnId");
                String lsFullName = resultSet.getString("lsFullName");
                String lsDepartment = resultSet.getString("lsDepartment");
                String lsStatus = resultSet.getString("lsStatus");
                LocalTime ltTimeIn = resultSet.getTime("ltTimeIn") != null ? resultSet.getTime("ltTimeIn").toLocalTime() : null;
                LocalTime ltTimeOut = resultSet.getTime("ltTimeOut") != null ? resultSet.getTime("ltTimeOut").toLocalTime() : null;
                LocalDate ldDateResult = resultSet.getDate("ldDate") != null ? resultSet.getDate("ldDate").toLocalDate() : null;
                attendanceList.add(new DailyAttendance(lnId, lsFullName, lsDepartment, lsStatus, ltTimeIn, ltTimeOut, ldDateResult));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }

    public List<DailyAttendance> getDailyAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        List<DailyAttendance> attendanceList = new ArrayList<>();
        String query = "SELECT e.nIdxxxxxx AS lnId, e.sFullName AS lsFullName, e.sDprtment AS lsDepartment, "
                + "da.sStatusxx AS lsStatus, da.tTimeInxx AS ltTimeIn, da.tTimeOutx AS ltTimeOut, da.dDatexxxx AS ldDate "
                + "FROM employees e "
                + "LEFT JOIN daily_attendance da ON e.nIdxxxxxx = da.nEmployId "
                + "WHERE da.dDatexxxx BETWEEN ? AND ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2, Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int lnId = resultSet.getInt("lnId");
                String lsFullName = resultSet.getString("lsFullName");
                String lsDepartment = resultSet.getString("lsDepartment");
                String lsStatus = resultSet.getString("lsStatus");
                LocalTime ltTimeIn = resultSet.getTime("ltTimeIn") != null ? resultSet.getTime("ltTimeIn").toLocalTime() : null;
                LocalTime ltTimeOut = resultSet.getTime("ltTimeOut") != null ? resultSet.getTime("ltTimeOut").toLocalTime() : null;
                LocalDate ldDateResult = resultSet.getDate("ldDate") != null ? resultSet.getDate("ldDate").toLocalDate() : null;
                attendanceList.add(new DailyAttendance(lnId, lsFullName, lsDepartment, lsStatus, ltTimeIn, ltTimeOut, ldDateResult));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }

    public boolean saveDailyAttendance(DailyAttendance attendance) {
        if (attendance.getLdDate() == null || attendance.getLnId() == 0) {
            System.err.println("Error: Attendance date (ldDate) or employee ID (lnId) cannot be null.");
            return false;
        }
        String checkQuery = "SELECT COUNT(*) FROM daily_attendance WHERE nEmployId = ? AND dDatexxxx = ?";
        String insertQuery = "INSERT INTO daily_attendance (nEmployId, sStatusxx, tTimeInxx, tTimeOutx, dDatexxxx) VALUES (?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE daily_attendance SET sStatusxx = ?, tTimeInxx = ?, tTimeOutx = ? WHERE nEmployId = ? AND dDatexxxx = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, attendance.getLnId());
                checkStatement.setDate(2, Date.valueOf(attendance.getLdDate()));
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, attendance.getLsStatus());
                        updateStatement.setTime(2, attendance.getLtTimeIn() != null ? Time.valueOf(attendance.getLtTimeIn()) : null);
                        updateStatement.setTime(3, attendance.getLtTimeOut() != null ? Time.valueOf(attendance.getLtTimeOut()) : null);
                        updateStatement.setInt(4, attendance.getLnId());
                        updateStatement.setDate(5, Date.valueOf(attendance.getLdDate()));
                        int rowsAffected = updateStatement.executeUpdate();
                        return rowsAffected > 0;
                    }
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, attendance.getLnId());
                        insertStatement.setString(2, attendance.getLsStatus());
                        insertStatement.setTime(3, attendance.getLtTimeIn() != null ? Time.valueOf(attendance.getLtTimeIn()) : null);
                        insertStatement.setTime(4, attendance.getLtTimeOut() != null ? Time.valueOf(attendance.getLtTimeOut()) : null);
                        insertStatement.setDate(5, Date.valueOf(attendance.getLdDate()));
                        int rowsAffected = insertStatement.executeUpdate();
                        return rowsAffected > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
