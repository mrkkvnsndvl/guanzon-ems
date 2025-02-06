package com.project.guanzonemployeemanagementsystem.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EmployeeController implements Initializable {

    @FXML
    private ChoiceBox<?> positionChoiceBox;
    @FXML
    private ChoiceBox<?> departmentChoiceBox;
    @FXML
    private TableView<?> employeeTableView;
    @FXML
    private TableColumn<?, ?> idTableColumn;
    @FXML
    private TableColumn<?, ?> fullNameTableColumn;
    @FXML
    private TableColumn<?, ?> ageTableColumn;
    @FXML
    private TableColumn<?, ?> emailTableColumn;
    @FXML
    private TableColumn<?, ?> phoneNumberTableColumn;
    @FXML
    private TableColumn<?, ?> positionTableColumn;
    @FXML
    private TableColumn<?, ?> departmentTableColumn;
    @FXML
    private TableColumn<?, ?> dateOfJoiningTableColumn;
    @FXML
    private TableColumn<?, ?> salaryTableColumn;
    @FXML
    private TextField searchEmployeeTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void refreshButtonOnAction(ActionEvent event) {
    }

    @FXML
    private void addEmployeeButtonOnAction(ActionEvent event) {
        // Button click show the employee dialog fxml
    }

    @FXML
    private void searchEmployeeFieldOnAction(ActionEvent event) {
    }

    @FXML
    private void deleteEmployeeButtonOnAction(ActionEvent event) {
    }

    @FXML
    private void updateEmployeeButtonOnAction(ActionEvent event) {
        // Button click show the employee dialog fxml
    }
}
