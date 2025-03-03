package com.project.guanzonems.components_controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.guanzonems.controller.EmployeeController;
import com.project.guanzonems.dao.DepartmentDAO;
import com.project.guanzonems.dao.EmployeeDAO;
import com.project.guanzonems.model.Department;
import com.project.guanzonems.model.Employee;
import com.project.guanzonems.utilities.SonnerUtility;
import com.project.guanzonems.validator.EmployeeValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class EmployeeDialogController {
    @FXML
    private AnchorPane employeeDialogAnchorPane;
    @FXML
    private TextField lsFullNameTextField;
    @FXML
    private Text lsFullNameValidatorText;
    @FXML
    private TextField lnAgeTextField;
    @FXML
    private Text lnAgeValidatorText;
    @FXML
    private TextField lsEmailTextField;
    @FXML
    private Text lsEmailValidatorText;
    @FXML
    private TextField lsPhoneNumberTextField;
    @FXML
    private Text lsPhoneNumberValidatorText;
    @FXML
    private TextField lsPositionTextField;
    @FXML
    private Text lsPositionValidatorText;
    @FXML
    private ChoiceBox<Department> lsDepartmentChoiceBox;
    @FXML
    private Text lsDepartmentValidatorText;
    @FXML
    private DatePicker ldDateOfJoiningDatePicker;
    @FXML
    private Text ldDateOfJoiningValidatorText;
    @FXML
    private TextField ldSalaryTextField;
    @FXML
    private Text ldSalaryValidatorText;
    @FXML
    private Button createEmployeeButton;

    private AnchorPane ownerAnchorPane;
    private Employee selectedEmployee;
    private final EmployeeValidator employeeValidator = new EmployeeValidator();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private EmployeeController employeeController;

    @FXML
    private void initialize() {
        loadDepartmentsToChoiceBox();
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) employeeDialogAnchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void createEmployeeButtonOnAction(ActionEvent event) {
        ObjectNode validationErrors = validateEmployeeFields();
        clearValidationMessagesAndStyles();
        if (validationErrors.isEmpty()) {
            try {
                if (selectedEmployee == null) {
                    Employee employee = createEmployeeObject();
                    employeeDAO.createEmployee(employee);
                    SonnerUtility.show(ownerAnchorPane.getScene().getWindow(), "Success", "Employee created successfully!");
                } else {
                    Employee updatedEmployee = createEmployeeObject();
                    updatedEmployee.setLnId(selectedEmployee.getLnId());
                    employeeDAO.updateEmployee(updatedEmployee);
                    SonnerUtility.show(ownerAnchorPane.getScene().getWindow(), "Success", "Employee updated successfully!");
                }
                clearFieldsAndValidationMessages();
                Stage stage = (Stage) employeeDialogAnchorPane.getScene().getWindow();
                stage.close();
                employeeController.loadEmployeesToTableView();
            } catch (SQLException e) {
                e.printStackTrace();
                SonnerUtility.show(ownerAnchorPane.getScene().getWindow(), "Failed", "Operation failed!");
            }
        } else {
            displayValidationErrors(validationErrors);
        }
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
        populateFieldsForUpdate();
    }

    public void setEmployeeController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }

    private void populateFieldsForUpdate() {
        if (selectedEmployee != null) {
            lsFullNameTextField.setText(selectedEmployee.getLsFullName());
            lnAgeTextField.setText(String.valueOf(selectedEmployee.getLnAge()));
            lsEmailTextField.setText(selectedEmployee.getLsEmail());
            lsPhoneNumberTextField.setText(selectedEmployee.getLsPhoneNumber());
            lsPositionTextField.setText(selectedEmployee.getLsPosition());
            ldDateOfJoiningDatePicker.setValue(selectedEmployee.getLdDateOfJoining());
            ldSalaryTextField.setText(String.valueOf(selectedEmployee.getLdSalary()));
            for (Department department : lsDepartmentChoiceBox.getItems()) {
                if (department.getLsDepartment().equals(selectedEmployee.getLsDepartment())) {
                    lsDepartmentChoiceBox.setValue(department);
                    break;
                }
            }
            createEmployeeButton.setText("Update Employee");
        }
    }

    public void setOwnerAnchorPane(AnchorPane ownerAnchorPane) {
        this.ownerAnchorPane = ownerAnchorPane;
    }

    private void loadDepartmentsToChoiceBox() {
        try {
            ObservableList<Department> departments = FXCollections.observableArrayList(departmentDAO.readDepartments());
            lsDepartmentChoiceBox.setItems(departments);
            lsDepartmentChoiceBox.setConverter(new javafx.util.StringConverter<Department>() {
                @Override
                public String toString(Department department) {
                    return department == null ? "" : department.getLsDepartment();
                }
                @Override
                public Department fromString(String string) {
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Employee createEmployeeObject() {
        String fullName = lsFullNameTextField.getText();
        int age = Integer.parseInt(lnAgeTextField.getText());
        String email = lsEmailTextField.getText();
        String phoneNumber = lsPhoneNumberTextField.getText();
        String position = lsPositionTextField.getText();
        Department department = lsDepartmentChoiceBox.getValue();
        LocalDate dateOfJoining = ldDateOfJoiningDatePicker.getValue();
        double salary = Double.parseDouble(ldSalaryTextField.getText());
        int lnId = 0;
        return new Employee(lnId, fullName, age, email, phoneNumber, position, department.getLsDepartment(), dateOfJoining, salary);
    }

    private ObjectNode validateEmployeeFields() {
        String fullName = lsFullNameTextField.getText();
        String age = lnAgeTextField.getText();
        String email = lsEmailTextField.getText();
        String phoneNumber = lsPhoneNumberTextField.getText();
        String position = lsPositionTextField.getText();
        Department department = lsDepartmentChoiceBox.getValue();
        LocalDate dateOfJoining = ldDateOfJoiningDatePicker.getValue();
        String salary = ldSalaryTextField.getText();
        Integer employeeId = selectedEmployee != null ? selectedEmployee.getLnId() : null;
        return employeeValidator.validateEmployee(
                fullName,
                age,
                email,
                phoneNumber,
                position,
                department != null ? department.getLsDepartment() : "",
                dateOfJoining != null ? dateOfJoining.toString() : "",
                salary,
                employeeId
        );
    }

    private void displayValidationErrors(ObjectNode validationErrors) {
        resetFieldStyles();

        if (!validationErrors.path("lsFullNameValidatorText").asText("").isEmpty()) {
            lsFullNameTextField.setStyle("-fx-border-color: #ef4444;");
            lsFullNameValidatorText.setText(validationErrors.path("lsFullNameValidatorText").asText(""));
        } else {
            lsFullNameValidatorText.setText("");
        }

        if (!validationErrors.path("lnAgeValidatorText").asText("").isEmpty()) {
            lnAgeTextField.setStyle("-fx-border-color: #ef4444;");
            lnAgeValidatorText.setText(validationErrors.path("lnAgeValidatorText").asText(""));
        } else {
            lnAgeValidatorText.setText("");
        }

        if (!validationErrors.path("lsEmailValidatorText").asText("").isEmpty()) {
            lsEmailTextField.setStyle("-fx-border-color: #ef4444;");
            lsEmailValidatorText.setText(validationErrors.path("lsEmailValidatorText").asText(""));
        } else {
            lsEmailValidatorText.setText("");
        }

        if (!validationErrors.path("lsPhoneNumberValidatorText").asText("").isEmpty()) {
            lsPhoneNumberTextField.setStyle("-fx-border-color: #ef4444;");
            lsPhoneNumberValidatorText.setText(validationErrors.path("lsPhoneNumberValidatorText").asText(""));
        } else {
            lsPhoneNumberValidatorText.setText("");
        }

        if (!validationErrors.path("lsPositionValidatorText").asText("").isEmpty()) {
            lsPositionTextField.setStyle("-fx-border-color: #ef4444;");
            lsPositionValidatorText.setText(validationErrors.path("lsPositionValidatorText").asText(""));
        } else {
            lsPositionValidatorText.setText("");
        }

        if (!validationErrors.path("lsDepartmentValidatorText").asText("").isEmpty()) {
            lsDepartmentChoiceBox.setStyle("-fx-border-color: #ef4444;");
            lsDepartmentValidatorText.setText(validationErrors.path("lsDepartmentValidatorText").asText(""));
        } else {
            lsDepartmentValidatorText.setText("");
        }

        if (!validationErrors.path("ldDateOfJoiningValidatorText").asText("").isEmpty()) {
            ldDateOfJoiningDatePicker.setStyle("-fx-border-color: #ef4444;");
            ldDateOfJoiningValidatorText.setText(validationErrors.path("ldDateOfJoiningValidatorText").asText(""));
        } else {
            ldDateOfJoiningValidatorText.setText("");
        }

        if (!validationErrors.path("ldSalaryValidatorText").asText("").isEmpty()) {
            ldSalaryTextField.setStyle("-fx-border-color: #ef4444;");
            ldSalaryValidatorText.setText(validationErrors.path("ldSalaryValidatorText").asText(""));
        } else {
            ldSalaryValidatorText.setText("");
        }
    }

    private void resetFieldStyles() {
        lsFullNameTextField.setStyle("");
        lnAgeTextField.setStyle("");
        lsEmailTextField.setStyle("");
        lsPhoneNumberTextField.setStyle("");
        lsPositionTextField.setStyle("");
        lsDepartmentChoiceBox.setStyle("");
        ldDateOfJoiningDatePicker.setStyle("");
        ldSalaryTextField.setStyle("");
    }

    private void clearValidationMessagesAndStyles() {
        lsFullNameValidatorText.setText("");
        lnAgeValidatorText.setText("");
        lsEmailValidatorText.setText("");
        lsPhoneNumberValidatorText.setText("");
        lsPositionValidatorText.setText("");
        lsDepartmentValidatorText.setText("");
        ldDateOfJoiningValidatorText.setText("");
        ldSalaryValidatorText.setText("");
        resetFieldStyles();
    }

    private void clearFieldsAndValidationMessages() {
        lsFullNameTextField.clear();
        lnAgeTextField.clear();
        lsEmailTextField.clear();
        lsPhoneNumberTextField.clear();
        lsPositionTextField.clear();
        lsDepartmentChoiceBox.setValue(null);
        ldDateOfJoiningDatePicker.setValue(null);
        ldSalaryTextField.clear();
        clearValidationMessagesAndStyles();
    }
}