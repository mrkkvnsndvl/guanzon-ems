package com.project.guanzonemployeemanagementsystem.components_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class EmployeeDialogController {

    @FXML
    private AnchorPane employeeDialogAnchorPane;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private Text fullNameValidationText;
    @FXML
    private TextField ageTextField;
    @FXML
    private Text ageValidationText;
    @FXML
    private TextField emailTextField;
    @FXML
    private Text emailValidationText;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private Text phoneNumberValidationText;
    @FXML
    private TextField positionTextField;
    @FXML
    private Text positionValidationText;
    @FXML
    private ChoiceBox<?> departmentChoiceBox;
    @FXML
    private Text departmentValidationText;
    @FXML
    private DatePicker dateOfJoiningDatePicker;
    @FXML
    private Text dateOfJoiningValidationText;
    @FXML
    private TextField salaryTextField;
    @FXML
    private Text salaryValidationText;

    @FXML
    private void cancelButtonOnAction(ActionEvent event) {
    }

    @FXML
    private void addEmployeeButtonOnAction(ActionEvent event) {
    }
    
}
