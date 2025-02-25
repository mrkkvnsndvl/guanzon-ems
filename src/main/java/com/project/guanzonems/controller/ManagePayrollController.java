package com.project.guanzonems.controller;

import com.project.guanzonems.dao.EmployeeDAO;
import com.project.guanzonems.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ManagePayrollController implements Initializable {

    @FXML
    private TextField employeeNameTextField;
    @FXML
    private ListView<String> employeeAutoCompleteListView;
    @FXML
    private TextField grossSalaryTextField;
    @FXML
    private TextField positionTextField;

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private ObservableList<String> employeeNames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadEmployeeNames();
        setupAutoComplete();
    }

    private void loadEmployeeNames() {
        try {
            List<Employee> employees = employeeDAO.readEmployees();
            for (Employee employee : employees) {
                employeeNames.add(employee.getLsFullName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupAutoComplete() {
        employeeAutoCompleteListView.setItems(employeeNames);
        employeeNameTextField.setOnKeyReleased(this::handleAutoComplete);
        employeeAutoCompleteListView.setOnMouseClicked(event -> {
            String selectedEmployee = employeeAutoCompleteListView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                employeeNameTextField.setText(selectedEmployee);
                employeeAutoCompleteListView.setVisible(false);
            }
        });
    }

    private void handleAutoComplete(KeyEvent event) {
        String userInput = employeeNameTextField.getText();
        if (userInput == null || userInput.trim().isEmpty()) {
            employeeAutoCompleteListView.setVisible(false);
            return;
        }
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (String name : employeeNames) {
            if (name.toLowerCase().contains(userInput.toLowerCase())) {
                filteredList.add(name);
            }
        }
        employeeAutoCompleteListView.setItems(filteredList);
        employeeAutoCompleteListView.setVisible(!filteredList.isEmpty());
    }
}
