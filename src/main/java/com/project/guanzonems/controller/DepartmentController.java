package com.project.guanzonems.controller;

import com.project.guanzonems.dao.DepartmentDAO;
import com.project.guanzonems.model.Department;
import com.project.guanzonems.utilities.AlertDialogUtility;
import com.project.guanzonems.utilities.DepartmentDialogUtility;
import com.project.guanzonems.utilities.SonnerUtility;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DepartmentController implements Initializable {

    @FXML
    private AnchorPane departmentAnchorPane;
    @FXML
    private TableView<Department> departmentTableView;
    @FXML
    private TableColumn<Department, Integer> idTableColumn;
    @FXML
    private TableColumn<Department, String> departmentTableColumn;
    @FXML
    private TextField searchDepartmentTextField;

    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private ObservableList<Department> departmentList;
    private FilteredList<Department> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupDepartmentTableColumns();
        loadDepartmentsToTableView();
        searchDepartmentTextField();
        clearSearchEmployeeTextField();
    }

    @FXML
    private void refreshButtonOnAction(ActionEvent event) {
        loadDepartmentsToTableView();
    }

    @FXML
    private void searchDepartmentOnAction(ActionEvent event) {
    }

    @FXML
    private void deleteDepartmentOnAction(ActionEvent event) {
        Department selectedDepartment = departmentTableView.getSelectionModel().getSelectedItem();
        if (selectedDepartment == null) {
            SonnerUtility.show(departmentAnchorPane.getScene().getWindow(), "Error", "Please select a department to delete.");
            return;
        }
        Stage ownerStage = (Stage) departmentAnchorPane.getScene().getWindow();
        AlertDialogUtility.show(
                ownerStage,
                "Delete Department",
                "Are you sure you want to delete this department?",
                () -> {
                },
                () -> {
                    try {
                        departmentDAO.deleteDepartment(selectedDepartment.getLnId());
                        loadDepartmentsToTableView();
                        SonnerUtility.show(ownerStage, "Success", "Department deleted successfully!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        SonnerUtility.show(ownerStage, "Failed", "Failed to delete department.");
                    }
                }
        );
    }

    @FXML
    private void updateDepartmentOnAction(ActionEvent event) {
        Department selectedDepartment = departmentTableView.getSelectionModel().getSelectedItem();

        if (selectedDepartment == null) {
            SonnerUtility.show(departmentAnchorPane.getScene().getWindow(), "Error", "Please select a department to update.");
            return;
        }

        DepartmentDialogUtility.showForUpdate(departmentAnchorPane, selectedDepartment, this);

        loadDepartmentsToTableView();
    }

    @FXML
    private void createDepartmentButtonOnAction(ActionEvent event) {
        DepartmentDialogUtility.show(departmentAnchorPane, this);
    }

    private void setupDepartmentTableColumns() {
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnId"));
        departmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsDepartment"));
    }

    public void loadDepartmentsToTableView() {
        try {
            List<Department> departments = departmentDAO.readDepartments();
            departmentList = FXCollections.observableArrayList(departments);

            filteredList = new FilteredList<>(departmentList, b -> true);
            departmentTableView.setItems(filteredList);

            searchDepartmentTextField();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchDepartmentTextField() {
        if (searchDepartmentTextField == null || departmentTableView == null) {
            System.err.println("Search text field or table view is null.");
            return;
        }
        if (departmentList == null) {
            departmentList = FXCollections.observableArrayList();
        }
        filteredList = new FilteredList<>(departmentList, b -> true);
        searchDepartmentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(department -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return department.getLsDepartment().toLowerCase().contains(lowerCaseFilter);
            });
        });
        departmentTableView.setItems(filteredList);
    }

    private void clearSearchEmployeeTextField() {
        searchDepartmentTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                searchDepartmentTextField.clear();
                searchDepartmentTextField();
            }
        });
    }
}