package com.project.guanzonemployeemanagementsystem.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DepartmentController implements Initializable {

    @FXML
    private TableView<?> departmentTableView;
    @FXML
    private TableColumn<?, ?> idTableColumn;
    @FXML
    private TableColumn<?, ?> departmentTableColumn;
    @FXML
    private TextField searchDepartmentTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void refreshButtonOnAction(ActionEvent event) {
    }

    @FXML
    private void addDepartmentOnAction(ActionEvent event) {
    }

    @FXML
    private void searchDepartmentOnAction(ActionEvent event) {
    }

    @FXML
    private void deleteDepartmentOnAction(ActionEvent event) {
    }

    @FXML
    private void updateDepartmentOnAction(ActionEvent event) {
    }
    
}
