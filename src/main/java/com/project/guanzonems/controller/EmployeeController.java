package com.project.guanzonems.controller;

import com.project.guanzonems.dao.DepartmentDAO;
import com.project.guanzonems.dao.EmployeeDAO;
import com.project.guanzonems.model.Department;
import com.project.guanzonems.model.Employee;
import com.project.guanzonems.utilities.AlertDialogUtility;
import com.project.guanzonems.utilities.EmployeeDialogUtility;
import com.project.guanzonems.utilities.SonnerUtility;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EmployeeController implements Initializable {

    @FXML
    private AnchorPane employeeAnchorPane;
    @FXML
    private ChoiceBox<String> positionChoiceBox;
    @FXML
    private ChoiceBox<String> departmentChoiceBox;
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, Integer> idTableColumn;
    @FXML
    private TableColumn<Employee, String> fullNameTableColumn;
    @FXML
    private TableColumn<Employee, Integer> ageTableColumn;
    @FXML
    private TableColumn<Employee, String> emailTableColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumberTableColumn;
    @FXML
    private TableColumn<Employee, String> positionTableColumn;
    @FXML
    private TableColumn<Employee, String> departmentTableColumn;
    @FXML
    private TableColumn<Employee, LocalDate> dateOfJoiningTableColumn;
    @FXML
    private TableColumn<Employee, Double> salaryTableColumn;
    @FXML
    private TextField searchEmployeeTextField;

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private ObservableList<Employee> employeeList;
    private FilteredList<Employee> filteredList;

    public void initialize(URL url, ResourceBundle rb) {
        setupEmployeeTableColumns();
        loadEmployeesToTableView();
        loadPositionsToChoiceBox();
        loadDepartmentsToChoiceBox();
        searchEmployeeTextField();
        clearSearchEmployeeTextField();
    }

    @FXML
    private void refreshButtonOnAction(ActionEvent event) {
        loadEmployeesToTableView();
        positionChoiceBox.setValue("All");
        departmentChoiceBox.setValue("All");
        positionAndDepartmentFilters();
    }

    @FXML
    private void deleteEmployeeButtonOnAction(ActionEvent event) {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            SonnerUtility.show(employeeAnchorPane.getScene().getWindow(), "Error", "Please select an employee to delete.");
            return;
        }
        Stage ownerStage = (Stage) employeeAnchorPane.getScene().getWindow();
        AlertDialogUtility.show(
                ownerStage,
                "Delete Employee",
                "Are you sure you want to delete this employee?",
                () -> {
                },
                () -> {
                    try {
                        employeeDAO.deleteEmployee(selectedEmployee.getLnId());
                        loadEmployeesToTableView();
                        SonnerUtility.show(ownerStage, "Success", "Employee deleted successfully!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        SonnerUtility.show(ownerStage, "Failed", "Failed to delete employee.");
                    }
                }
        );
    }

    @FXML
    private void updateEmployeeButtonOnAction(ActionEvent event) {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            SonnerUtility.show(employeeAnchorPane.getScene().getWindow(), "Error", "Please select an employee to update.");
            return;
        }

        EmployeeDialogUtility.showForUpdate(employeeAnchorPane, selectedEmployee, this);

        loadEmployeesToTableView();
    }

    @FXML
    private void createEmployeeButtonOnAction(ActionEvent event) {
        EmployeeDialogUtility.show(employeeAnchorPane, this);
    }

    private void setupEmployeeTableColumns() {
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnId"));
        fullNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsFullName"));
        ageTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnAge"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsEmail"));
        phoneNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsPhoneNumber"));
        positionTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsPosition"));
        departmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsDepartment"));
        dateOfJoiningTableColumn.setCellValueFactory(new PropertyValueFactory<>("ldDateOfJoining"));
        salaryTableColumn.setCellValueFactory(new PropertyValueFactory<>("ldSalary"));
    }

    public void loadEmployeesToTableView() {
        try {
            List<Employee> employees = employeeDAO.readEmployees();
            employeeList = FXCollections.observableArrayList(employees);

            filteredList = new FilteredList<>(employeeList, b -> true);
            employeeTableView.setItems(filteredList);

            searchEmployeeTextField();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchEmployeeTextField() {
        if (searchEmployeeTextField == null || employeeTableView == null) {
            System.err.println("Search text field or table view is null.");
            return;
        }
        if (employeeList == null) {
            employeeList = FXCollections.observableArrayList();
        }
        filteredList = new FilteredList<>(employeeList, b -> true);
        searchEmployeeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return employee.getLsFullName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        employeeTableView.setItems(filteredList);
    }

    private void clearSearchEmployeeTextField() {
        searchEmployeeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                searchEmployeeTextField.clear();
                searchEmployeeTextField();
            }
        });
    }

    private void loadPositionsToChoiceBox() {
        try {
            List<String> positions = employeeDAO.getUniquePositions();
            ObservableList<String> positionList = FXCollections.observableArrayList(positions);
            positionChoiceBox.setItems(positionList);

            positionList.add(0, "All");
            positionChoiceBox.setValue("All");

            positionChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                positionAndDepartmentFilters();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDepartmentsToChoiceBox() {
        try {
            List<Department> departments = departmentDAO.readDepartments();

            ObservableList<String> departmentList = FXCollections.observableArrayList();
            for (Department department : departments) {
                departmentList.add(department.getLsDepartment());
            }

            departmentList.add(0, "All");
            departmentChoiceBox.setItems(departmentList);
            departmentChoiceBox.setValue("All");

            departmentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                positionAndDepartmentFilters();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void positionAndDepartmentFilters() {
        String selectedPosition = positionChoiceBox.getValue();
        String selectedDepartment = departmentChoiceBox.getValue();

        filteredList.setPredicate(employee -> {
            boolean matchesPosition = selectedPosition == null || selectedPosition.equals("All") || employee.getLsPosition().equals(selectedPosition);

            boolean matchesDepartment = selectedDepartment == null || selectedDepartment.equals("All") || employee.getLsDepartment().equals(selectedDepartment);

            return matchesPosition && matchesDepartment;
        });
    }
}
