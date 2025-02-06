package com.project.guanzonemployeemanagementsystem.controller;

import com.project.guanzonemployeemanagementsystem.utilities.AlertDialog;
import com.project.guanzonemployeemanagementsystem.utilities.Sonner;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    private Button signOutButton;
    @FXML
    private WebView guanzonWebsiteWebView;

    private WebEngine webEngine;
    private Button activeButton;
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
        setHoverButton(signOutButton);

        if (guanzonWebsiteWebView != null) {
            webEngine = guanzonWebsiteWebView.getEngine();
            loadGuanzonWebsite();
        }
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
        loadFXMLPage("/com/project/guanzonemployeemanagementsystem/fxml/employee.fxml", employeeButton);
    }

    @FXML
    private void departmentButtonOnAction(ActionEvent event) {
        loadFXMLPage("/com/project/guanzonemployeemanagementsystem/fxml/department.fxml", departmentButton);
    }

    @FXML
    private void signOutButtonOnAction(ActionEvent event) {
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

    private void loadGuanzonWebsite() {
        try {
            if (webEngine != null) {
                webEngine.load("https://www.guanzongroup.com.ph/");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading webpage", e);
        }
    }
}
