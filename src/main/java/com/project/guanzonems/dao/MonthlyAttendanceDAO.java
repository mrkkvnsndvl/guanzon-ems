package com.project.guanzonems.dao;

import com.project.guanzonems.model.DailyAttendance;
import com.project.guanzonems.model.MonthlyAttendance;
import com.project.guanzonems.utilities.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthlyAttendanceDAO {

    private final DailyAttendanceDAO dailyAttendanceDAO = new DailyAttendanceDAO();

    public List<MonthlyAttendance> getMonthlyAttendance(LocalDate startDate, LocalDate endDate) {
        List<DailyAttendance> dailyAttendances = dailyAttendanceDAO.getDailyAttendanceByDateRange(startDate, endDate);
        Map<Integer, MonthlyAttendance> monthlyAttendanceMap = new HashMap<>();
        for (DailyAttendance dailyAttendance : dailyAttendances) {
            int employeeId = dailyAttendance.getLnId();
            String fullName = dailyAttendance.getLsFullName();
            String department = dailyAttendance.getLsDepartment();
            String status = dailyAttendance.getLsStatus();
            MonthlyAttendance monthlyAttendance = monthlyAttendanceMap.computeIfAbsent(employeeId,
                    id -> new MonthlyAttendance(id, fullName, department, 0, 0, 0, 0, 0.0));
            switch (status) {
                case "Present":
                    monthlyAttendance.setLnPresentDays(monthlyAttendance.getLnPresentDays() + 1);
                    break;
                case "Late":
                    monthlyAttendance.setLnLateDays(monthlyAttendance.getLnLateDays() + 1);
                    break;
                case "Absent":
                    monthlyAttendance.setLnAbsentDays(monthlyAttendance.getLnAbsentDays() + 1);
                    break;
                case "On Leave":
                    monthlyAttendance.setLnOnLeaveDays(monthlyAttendance.getLnOnLeaveDays() + 1);
                    break;
            }
        }
        for (MonthlyAttendance monthlyAttendance : monthlyAttendanceMap.values()) {
            int totalWorkingDays = monthlyAttendance.getLnPresentDays() + monthlyAttendance.getLnLateDays()
                    + monthlyAttendance.getLnAbsentDays();
            if (totalWorkingDays > 0) {
                double attendancePercentage = ((double) monthlyAttendance.getLnPresentDays() / totalWorkingDays) * 100;
                monthlyAttendance.setLdAttendancePercentage(attendancePercentage);
            }
        }
        return new ArrayList<>(monthlyAttendanceMap.values());
    }

    public void saveMonthlyAttendance(MonthlyAttendance monthlyAttendance, YearMonth yearMonth) {
        String query = "INSERT INTO monthly_attendance (nEmployId, dYearMonth, nPresentDays, nLateDays, nAbsentDays, nOnLeaveDays, dAttendancePercentage) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                       "ON DUPLICATE KEY UPDATE " +
                       "nPresentDays = VALUES(nPresentDays), " +
                       "nLateDays = VALUES(nLateDays), " +
                       "nAbsentDays = VALUES(nAbsentDays), " +
                       "nOnLeaveDays = VALUES(nOnLeaveDays), " +
                       "dAttendancePercentage = VALUES(dAttendancePercentage)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, monthlyAttendance.getLnId());
            statement.setDate(2, Date.valueOf(yearMonth.atDay(1))); // Store the first day of the month
            statement.setInt(3, monthlyAttendance.getLnPresentDays());
            statement.setInt(4, monthlyAttendance.getLnLateDays());
            statement.setInt(5, monthlyAttendance.getLnAbsentDays());
            statement.setInt(6, monthlyAttendance.getLnOnLeaveDays());
            statement.setDouble(7, monthlyAttendance.getLdAttendancePercentage());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MonthlyAttendance> getStoredMonthlyAttendance(YearMonth yearMonth) {
        List<MonthlyAttendance> monthlyAttendances = new ArrayList<>();
        String query = "SELECT ma.nEmployId, e.sFullName, e.sDprtment, ma.nPresentDays, ma.nLateDays, ma.nAbsentDays, ma.nOnLeaveDays, ma.dAttendancePercentage " +
                       "FROM monthly_attendance ma " +
                       "JOIN employees e ON ma.nEmployId = e.nIdxxxxxx " +
                       "WHERE ma.dYearMonth = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(yearMonth.atDay(1)));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int lnId = resultSet.getInt("nEmployId");
                String lsFullName = resultSet.getString("sFullName");
                String lsDepartment = resultSet.getString("sDprtment");
                int lnPresentDays = resultSet.getInt("nPresentDays");
                int lnLateDays = resultSet.getInt("nLateDays");
                int lnAbsentDays = resultSet.getInt("nAbsentDays");
                int lnOnLeaveDays = resultSet.getInt("nOnLeaveDays");
                double ldAttendancePercentage = resultSet.getDouble("dAttendancePercentage");
                monthlyAttendances.add(new MonthlyAttendance(lnId, lsFullName, lsDepartment, lnPresentDays, lnLateDays, lnAbsentDays, lnOnLeaveDays, ldAttendancePercentage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyAttendances;
    }

    public int getTotalEmployeesWithAttendance(YearMonth yearMonth) {
        String query = "SELECT COUNT(DISTINCT nEmployId) " +
                       "FROM daily_attendance " +
                       "WHERE dDatexxxx BETWEEN ? AND ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(yearMonth.atDay(1)));
            statement.setDate(2, Date.valueOf(yearMonth.atEndOfMonth()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getWorkingDays(YearMonth yearMonth) {
        int workingDays = 0;
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!date.getDayOfWeek().toString().equalsIgnoreCase("SATURDAY") && !date.getDayOfWeek().toString().equalsIgnoreCase("SUNDAY")) {
                workingDays++;
            }
        }
        return workingDays;
    }

    public List<String> getDepartments() {
        List<String> departments = new ArrayList<>();
        String query = "SELECT DISTINCT sDprtment FROM employees";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                departments.add(resultSet.getString("sDprtment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public List<MonthlyAttendance> getStoredMonthlyAttendanceByDepartment(YearMonth yearMonth, String department) {
        List<MonthlyAttendance> monthlyAttendances = new ArrayList<>();
        String query = "SELECT ma.nEmployId, e.sFullName, e.sDprtment, ma.nPresentDays, ma.nLateDays, ma.nAbsentDays, ma.nOnLeaveDays, ma.dAttendancePercentage " +
                       "FROM monthly_attendance ma " +
                       "JOIN employees e ON ma.nEmployId = e.nIdxxxxxx " +
                       "WHERE ma.dYearMonth = ? AND e.sDprtment = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(yearMonth.atDay(1)));
            statement.setString(2, department);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int lnId = resultSet.getInt("nEmployId");
                String lsFullName = resultSet.getString("sFullName");
                String lsDepartment = resultSet.getString("sDprtment");
                int lnPresentDays = resultSet.getInt("nPresentDays");
                int lnLateDays = resultSet.getInt("nLateDays");
                int lnAbsentDays = resultSet.getInt("nAbsentDays");
                int lnOnLeaveDays = resultSet.getInt("nOnLeaveDays");
                double ldAttendancePercentage = resultSet.getDouble("dAttendancePercentage");
                monthlyAttendances.add(new MonthlyAttendance(lnId, lsFullName, lsDepartment, lnPresentDays, lnLateDays, lnAbsentDays, lnOnLeaveDays, ldAttendancePercentage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyAttendances;
    }
}