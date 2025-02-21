package com.project.guanzonems.controller;

import com.project.guanzonems.dao.DepartmentDAO;
import com.project.guanzonems.dao.EmployeeDAO;
import com.project.guanzonems.model.Employee;
import com.project.guanzonems.model.Department;
import com.project.guanzonems.utilities.AlertDialogUtility;
import com.project.guanzonems.utilities.SceneSwitcherUtility;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private BorderPane dashboardBorderPane;
    @FXML
    private AnchorPane dashboardAnchorPane;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button employeeButton;
    @FXML
    private Button departmentButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button attendanceButton;
    @FXML
    private Button signOutButton;
    @FXML
    private Text employeesText;
    @FXML
    private Text departmentsText;
    @FXML
    private WebView guanzonWebsiteWebView;

    private WebEngine webEngine;
    private Button activeButton;
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (dashboardBorderPane != null) {
            dashboardBorderPane.setCenter(dashboardAnchorPane);
        }

        setActiveButton(dashboardButton);

        setHoverButton(dashboardButton);
        setHoverButton(employeeButton);
        setHoverButton(departmentButton);
        setHoverButton(reportsButton);
        setHoverButton(attendanceButton);
        setHoverButton(signOutButton);

        if (guanzonWebsiteWebView != null) {
            webEngine = guanzonWebsiteWebView.getEngine();
            loadGuanzonWebPage();
        }

        employeesCount();

        departmentsCount();
    }

    @FXML
    private void dashboardButtonOnAction(ActionEvent event) {
        if (dashboardBorderPane != null) {
            dashboardBorderPane.setCenter(dashboardAnchorPane);
            setActiveButton(dashboardButton);
        }
    }

    @FXML
    private void employeeButtonOnAction(ActionEvent event) {
        loadFXMLPage("/com/project/guanzonems/fxml/Employee.fxml", employeeButton);
    }

    @FXML
    private void departmentButtonOnAction(ActionEvent event) {
        loadFXMLPage("/com/project/guanzonems/fxml/Department.fxml", departmentButton);
    }

    @FXML
    private void reportsButtonOnAction(ActionEvent event) {
        loadFXMLPage("/com/project/guanzonems/fxml/Reports.fxml", reportsButton);
    }
    
    @FXML
    private void attendanceButtonOnAction(ActionEvent event) {
        loadFXMLPage("/com/project/guanzonems/fxml/Attendance.fxml", attendanceButton);
    }

    @FXML
    private void signOutButtonOnAction(ActionEvent event) {
        Stage ownerStage = (Stage) signOutButton.getScene().getWindow();
        AlertDialogUtility.show(
                ownerStage,
                "Confirm Sign Out",
                "Are you sure you want to sign out?",
                null,
                () -> {
                    SceneSwitcherUtility.switchToSignIn(event);
                }
        );
    }

    private void loadFXMLPage(String resourcePath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Node page = loader.load();

            if (dashboardBorderPane != null) {
                dashboardBorderPane.setCenter(page);
                setActiveButton(button);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading FXML page: " + resourcePath, e);
        }
    }

    private void setActiveButton(Button button) {
        if (button == null) {
            return;
        }

        if (activeButton != null) {
            activeButton.setStyle("-fx-background-color: transparent;");
        }

        activeButton = button;
        activeButton.setStyle("-fx-background-color: #e4e4e7;");
    }

    private void setHoverButton(Button button) {
        if (button == null) {
            return;
        }

        String buttonOnHoverStyle = "-fx-background-color: #e4e4e7;";
        String buttonDefaultStyle = "-fx-background-color: transparent;";

        button.setOnMouseEntered(event -> {
            if (button != activeButton) {
                button.setStyle(buttonOnHoverStyle);
            }
        });

        button.setOnMouseExited(event -> {
            if (button != activeButton) {
                button.setStyle(buttonDefaultStyle);
            }
        });
    }

    private void loadGuanzonWebPage() {
        try {
            if (webEngine != null) {
                webEngine.load("https://www.guanzongroup.com.ph/");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading webpage", e);
        }
    }

    private void employeesCount() {
        try {
            List<Employee> employees = employeeDAO.readEmployees();
            int employeeCount = employees.size();
            employeesText.setText(String.valueOf(employeeCount));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching employee count", e);
            employeesText.setText("0");
        }
    }

    private void departmentsCount() {
        try {
            List<Department> departments = departmentDAO.readDepartments();
            int departmentCount = departments.size();
            departmentsText.setText(String.valueOf(departmentCount));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching employee count", e);
            departmentsText.setText("0");
        }
    }
}
