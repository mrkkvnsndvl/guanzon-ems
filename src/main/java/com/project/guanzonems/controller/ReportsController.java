package com.project.guanzonems.controller;

import com.project.guanzonems.dao.EmployeeDAO;
import com.project.guanzonems.model.Employee;
import com.project.guanzonems.utilities.SonnerUtility;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ReportsController implements Initializable {

    @FXML
    private AnchorPane reportsAnchorPane;
    @FXML
    private TableView<Map.Entry<String, Integer>> reportsTableView;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> departmentsTableColumn;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> employeeCountsTableColumn;
    @FXML
    private BarChart<String, Number> reportsBarChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupReportsTableColumns();
        loadEmployeeCountsToTableView();
        loadEmployeeCountsToBarChart();
    }

    @FXML
    private void refreshButtonOnAction(ActionEvent event) {
        loadEmployeeCountsToTableView();
    }

    @FXML
    private void exportToCsvButtonOnAction(ActionEvent event) {
        try {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            List<Employee> employees = employeeDAO.readEmployees();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Employee Data");
            fileChooser.setInitialFileName("employees-export.csv");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            Stage stage = (Stage) reportsAnchorPane.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            if (file == null) {
                return;
            }
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("ID,Full Name,Age,Email,Phone Number,Position,Department,Date of Joining,Salary\n");

                for (Employee employee : employees) {
                    writer.append(String.format("%d,%s,%d,%s,%s,%s,%s,%s,%.2f\n",
                            employee.getLnId(),
                            employee.getLsFullName(),
                            employee.getLnAge(),
                            employee.getLsEmail(),
                            employee.getLsPhoneNumber(),
                            employee.getLsPosition(),
                            employee.getLsDepartment(),
                            employee.getLdDateOfJoining(),
                            employee.getLdSalary()));
                }
            }
            SonnerUtility.show(stage, "Success", "Employee data exported successfully!");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            SonnerUtility.show(reportsAnchorPane.getScene().getWindow(), "Failed", "Failed to export employee data.");
        }
    }

    private void setupReportsTableColumns() {
        departmentsTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        employeeCountsTableColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getValue()).asObject());
    }

    private void loadEmployeeCountsToTableView() {
        try {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            Map<String, Integer> departmentCounts = employeeDAO.getEmployeeCountsByDepartment();
            ObservableList<Map.Entry<String, Integer>> data = FXCollections.observableArrayList(departmentCounts.entrySet());
            reportsTableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEmployeeCountsToBarChart() {
        try {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            Map<String, Integer> departmentCounts = employeeDAO.getEmployeeCountsByDepartment();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Employees per Department");
            for (Map.Entry<String, Integer> entry : departmentCounts.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            reportsBarChart.getData().clear();
            reportsBarChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
