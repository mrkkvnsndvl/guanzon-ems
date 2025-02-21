package com.project.guanzonems.components_controller;

import com.project.guanzonems.dao.DepartmentDAO;
import com.project.guanzonems.model.Department;
import com.project.guanzonems.validator.DepartmentValidator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.guanzonems.controller.DepartmentController;
import com.project.guanzonems.utilities.SonnerUtility;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DepartmentDialogController {

    @FXML
    private AnchorPane departmentDialogAnchorPane;
    @FXML
    private TextField lsDepartmentTextField;
    @FXML
    private Text lsDepartmentValidatorText;
    @FXML
    private Button createDepartmentButton;

    private AnchorPane ownerAnchorPane;
    private Department selectedDepartment;
    private final DepartmentValidator departmentValidator = new DepartmentValidator();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private DepartmentController departmentController;

    @FXML
    private void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) departmentDialogAnchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void createDepartmentButtonOnAction(ActionEvent event) {
        ObjectNode validationErrors = validateDepartmentFields();

        clearValidationMessagesAndStyles();

        if (validationErrors.isEmpty()) {
            try {
                if (selectedDepartment == null) {
                    Department department = createDepartmentObject();
                    departmentDAO.createDepartment(department);
                    SonnerUtility.show(ownerAnchorPane.getScene().getWindow(), "Success", "Department created successfully!");
                } else {
                    Department updatedDepartment = createDepartmentObject();
                    updatedDepartment.setLnId(selectedDepartment.getLnId());
                    departmentDAO.updateDepartment(updatedDepartment);
                    SonnerUtility.show(ownerAnchorPane.getScene().getWindow(), "Success", "Department updated successfully!");
                }
                clearFieldsAndValidationMessages();
                Stage stage = (Stage) departmentDialogAnchorPane.getScene().getWindow();
                stage.close();
                departmentController.loadDepartmentsToTableView();
            } catch (SQLException e) {
                e.printStackTrace();
                SonnerUtility.show(ownerAnchorPane.getScene().getWindow(), "Failed", "Operation failed!");
            }
        } else {
            displayValidationErrors(validationErrors);
        }
    }

    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
        populateFieldsForUpdate();
    }

    public void setDepartmentController(DepartmentController departmentController) {
        this.departmentController = departmentController;
    }

    private void populateFieldsForUpdate() {
        if (selectedDepartment != null) {
            lsDepartmentTextField.setText(selectedDepartment.getLsDepartment());
            createDepartmentButton.setText("Update Department");
        }
    }

    public void setOwnerAnchorPane(AnchorPane ownerAnchorPane) {
        this.ownerAnchorPane = ownerAnchorPane;
    }

    private Department createDepartmentObject() {
        String department = lsDepartmentTextField.getText();

        int lnId = 0;

        return new Department(lnId, department);
    }

    private ObjectNode validateDepartmentFields() {
        String department = lsDepartmentTextField.getText();

        Integer departmentId = selectedDepartment != null ? selectedDepartment.getLnId() : null;

        return departmentValidator.validateDepartment(
                department,
                departmentId
        );
    }

    private void displayValidationErrors(ObjectNode validationErrors) {
        resetFieldStyles();

        if (!validationErrors.path("departmentName").asText("").isEmpty()) {
            lsDepartmentTextField.setStyle("-fx-border-color: #ef4444;");
            lsDepartmentValidatorText.setText(validationErrors.path("departmentName").asText(""));
        } else {
            lsDepartmentValidatorText.setText("");
        }
    }

    private void resetFieldStyles() {
        lsDepartmentTextField.setStyle("");
    }

    private void clearValidationMessagesAndStyles() {
        lsDepartmentValidatorText.setText("");

        resetFieldStyles();
    }

    private void clearFieldsAndValidationMessages() {
        lsDepartmentTextField.clear();
        clearValidationMessagesAndStyles();
    }
}
