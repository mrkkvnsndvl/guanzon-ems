package com.project.guanzonemployeemanagementsystem.controller;

import com.project.guanzonemployeemanagementsystem.dao.DepartmentDAO;
import com.project.guanzonemployeemanagementsystem.dao.EmployeeDAO;
import com.project.guanzonemployeemanagementsystem.model.Department;
import com.project.guanzonemployeemanagementsystem.model.Employee;
import com.project.guanzonemployeemanagementsystem.util.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeController {

    /* FXML controls for the employee table */
    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> fullNameColumn;
    @FXML
    private TableColumn<Employee, Integer> ageColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumberColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> departmentColumn;
    @FXML
    private TableColumn<Employee, String> dateOfJoiningColumn;
    @FXML
    private TableColumn<Employee, Double> salaryColumn;

    /* FXML controls for searching */
    @FXML
    private TextField searchEmployeeField;

    /* Data access object for employees */
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    /**
     * Initializes the employee table and loads employee data.
     */
    @FXML
    public void initialize() {
        configureTableColumns();
        loadEmployeeData();
    }

    /**
     * Configures the table columns.
     */
    private void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("lnId"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("lsFullName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("lnAge"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("lsEmail"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("lsPhoneNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("lsPosition"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("lsDepartment"));
        dateOfJoiningColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            LocalDate doj = employee.getLdDateOfJoining();
            return new SimpleStringProperty(doj != null ? doj.toString() : "");
        });
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("ldSalary"));
    }

    /**
     * Loads all employees from the database into the table.
     */
    private void loadEmployeeData() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            employeeTable.getItems().setAll(employees);
        } catch (SQLException e) {
            showAlert("Database Error", Validation.createErrorMessage("Failed to load employees: " + e.getMessage()));
        }
    }

    /**
     * Populates a given department ChoiceBox with the list of departments from the database.
     *
     * @param departmentChoiceBox the ChoiceBox to populate
     */
    private void populateDepartmentChoiceBox(ChoiceBox<String> departmentChoiceBox) {
        DepartmentDAO departmentDAO = new DepartmentDAO();
        try {
            List<Department> departments = departmentDAO.getAllDepartments();
            departmentChoiceBox.getItems().clear();
            for (Department dept : departments) {
                departmentChoiceBox.getItems().add(dept.getLsDepartmentName());
            }
        } catch (SQLException e) {
            showAlert("Database Error", Validation.createErrorMessage("Failed to load departments: " + e.getMessage()));
        }
    }

    /* =========================
       Employee CRUD Operations
       ========================= */

    /**
     * Handles adding a new employee.
     */
    @FXML
    private void handleAddEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/guanzonemployeemanagementsystem/fxml/employee-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            // Populate the department ChoiceBox in the dialog
            ChoiceBox<String> departmentChoiceBox = (ChoiceBox<String>) dialogPane.lookup("#departmentChoiceBox");
            populateDepartmentChoiceBox(departmentChoiceBox);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add an Employee");
            dialog.setResizable(false);
            dialog.initStyle(StageStyle.UNDECORATED);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Look up controls
                TextField fullNameField = (TextField) dialogPane.lookup("#fullNameField");
                TextField ageField = (TextField) dialogPane.lookup("#ageField");
                TextField emailField = (TextField) dialogPane.lookup("#emailField");
                TextField phoneNumberField = (TextField) dialogPane.lookup("#phoneNumberField");
                TextField positionField = (TextField) dialogPane.lookup("#positionField");
                DatePicker dateOfJoiningPicker = (DatePicker) dialogPane.lookup("#dateOfJoiningPicker");
                TextField salaryField = (TextField) dialogPane.lookup("#salaryField");

                // Validate input
                if (fullNameField.getText().isEmpty() || ageField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || phoneNumberField.getText().isEmpty() ||
                    positionField.getText().isEmpty() || departmentChoiceBox.getValue() == null ||
                    dateOfJoiningPicker.getValue() == null || salaryField.getText().isEmpty()) {
                    showAlert("Input Error", Validation.createErrorMessage("All fields are required."));
                    return;
                }

                int age;
                double salary;
                try {
                    age = Integer.parseInt(ageField.getText());
                    salary = Double.parseDouble(salaryField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Input Error", Validation.createErrorMessage("Age and Salary must be valid numbers."));
                    return;
                }

                // Create and save new employee
                Employee employee = new Employee();
                employee.setLsFullName(fullNameField.getText());
                employee.setLnAge(age);
                employee.setLsEmail(emailField.getText());
                employee.setLsPhoneNumber(phoneNumberField.getText());
                employee.setLsPosition(positionField.getText());
                employee.setLsDepartment(departmentChoiceBox.getValue());
                employee.setLdDateOfJoining(dateOfJoiningPicker.getValue());
                employee.setLdSalary(salary);

                employeeDAO.addEmployee(employee);
                showAlert("Success", Validation.createSuccessMessage("Employee added successfully."));
                loadEmployeeData();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert("Error", Validation.createErrorMessage("An error occurred while adding the employee."));
        }
    }

    /**
     * Handles updating an existing employee.
     */
    @FXML
    private void handleUpdateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Selection Error", Validation.createErrorMessage("Please select an employee to update."));
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/guanzonemployeemanagementsystem/fxml/employee-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            // Look up controls and populate department ChoiceBox
            TextField fullNameField = (TextField) dialogPane.lookup("#fullNameField");
            TextField ageField = (TextField) dialogPane.lookup("#ageField");
            TextField emailField = (TextField) dialogPane.lookup("#emailField");
            TextField phoneNumberField = (TextField) dialogPane.lookup("#phoneNumberField");
            TextField positionField = (TextField) dialogPane.lookup("#positionField");
            ChoiceBox<String> departmentChoiceBox = (ChoiceBox<String>) dialogPane.lookup("#departmentChoiceBox");
            populateDepartmentChoiceBox(departmentChoiceBox);
            DatePicker dateOfJoiningPicker = (DatePicker) dialogPane.lookup("#dateOfJoiningPicker");
            TextField salaryField = (TextField) dialogPane.lookup("#salaryField");

            // Pre-fill fields with selected employee's data
            fullNameField.setText(selectedEmployee.getLsFullName());
            ageField.setText(String.valueOf(selectedEmployee.getLnAge()));
            emailField.setText(selectedEmployee.getLsEmail());
            phoneNumberField.setText(selectedEmployee.getLsPhoneNumber());
            positionField.setText(selectedEmployee.getLsPosition());
            departmentChoiceBox.setValue(selectedEmployee.getLsDepartment());
            dateOfJoiningPicker.setValue(selectedEmployee.getLdDateOfJoining());
            salaryField.setText(String.valueOf(selectedEmployee.getLdSalary()));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Update Employee");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (fullNameField.getText().isEmpty() || ageField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || phoneNumberField.getText().isEmpty() ||
                    positionField.getText().isEmpty() || departmentChoiceBox.getValue() == null ||
                    dateOfJoiningPicker.getValue() == null || salaryField.getText().isEmpty()) {
                    showAlert("Input Error", Validation.createErrorMessage("All fields are required."));
                    return;
                }

                int age;
                double salary;
                try {
                    age = Integer.parseInt(ageField.getText());
                    salary = Double.parseDouble(salaryField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Input Error", Validation.createErrorMessage("Age and Salary must be valid numbers."));
                    return;
                }

                // Update employee data
                selectedEmployee.setLsFullName(fullNameField.getText());
                selectedEmployee.setLnAge(age);
                selectedEmployee.setLsEmail(emailField.getText());
                selectedEmployee.setLsPhoneNumber(phoneNumberField.getText());
                selectedEmployee.setLsPosition(positionField.getText());
                selectedEmployee.setLsDepartment(departmentChoiceBox.getValue());
                selectedEmployee.setLdDateOfJoining(dateOfJoiningPicker.getValue());
                selectedEmployee.setLdSalary(salary);

                employeeDAO.updateEmployee(selectedEmployee);
                showAlert("Success", Validation.createSuccessMessage("Employee updated successfully."));
                loadEmployeeData();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert("Error", Validation.createErrorMessage("An error occurred while updating the employee."));
        }
    }

    /**
     * Handles deleting the selected employee.
     */
    @FXML
    private void handleDeleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Selection Error", Validation.createErrorMessage("Please select an employee to delete."));
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Employee");
        confirmation.setContentText("Are you sure you want to delete this employee?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                employeeDAO.deleteEmployee(selectedEmployee.getLnId());
                showAlert("Success", Validation.createSuccessMessage("Employee deleted successfully."));
                loadEmployeeData();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", Validation.createErrorMessage("Failed to delete the employee."));
            }
        }
    }

    /**
     * Refreshes the employee table data.
     */
    @FXML
    private void handleRefresh() {
        loadEmployeeData();
    }

    /**
     * Handles searching employees by full name.
     */
    @FXML
    private void handleSearchEmployee() {
        String searchText = searchEmployeeField.getText().toLowerCase();
        try {
            List<Employee> allEmployees = employeeDAO.getAllEmployees();
            List<Employee> filteredEmployees = allEmployees.stream()
                    .filter(employee -> employee.getLsFullName().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            employeeTable.getItems().setAll(filteredEmployees);
        } catch (SQLException e) {
            showAlert("Database Error", Validation.createErrorMessage("Failed to search employees: " + e.getMessage()));
        }
    }

    /* =========================
       Utility Methods
       ========================= */

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the alert message (in JSON format, as per the Validation utility)
     */
    private void showAlert(String title, String message) {
        Alert alert;
        if ("Success".equalsIgnoreCase(title)) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
