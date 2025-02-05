package com.project.guanzonemployeemanagementsystem.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class DashboardController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button sidebarDashboardButton;
    @FXML
    private Button sidebarEmployeeButton;
    @FXML
    private Button sidebarDepartmentButton;
    @FXML
    private WebView webView;
    @FXML
    private WebEngine webEngine;

    private Button activeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borderPane.setCenter(anchorPane);

        setActiveButton(sidebarDashboardButton);
        
        webEngine = webView.getEngine();
        loadPage();
    }

    @FXML
    private void handleDashboardButtonClick() {
        borderPane.setCenter(anchorPane);
        setActiveButton(sidebarDashboardButton);
    }

    @FXML
    private void handleEmployeeButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/guanzonemployeemanagementsystem/fxml/employee.fxml"));
            Node employeePage = loader.load();
            setActiveButton(sidebarEmployeeButton);
            borderPane.setCenter(employeePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDepartmentButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/guanzonemployeemanagementsystem/fxml/department.fxml"));
            Node departmentPage = loader.load();
            setActiveButton(sidebarDepartmentButton);
            borderPane.setCenter(departmentPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.setStyle("-fx-background-color: transparent;");
        }

        activeButton = button;

        activeButton.setStyle("-fx-background-color: #e4e4e7;");
    }
    
    public void loadPage() {
        webEngine.load("https://www.guanzongroup.com.ph/");
    }
}
