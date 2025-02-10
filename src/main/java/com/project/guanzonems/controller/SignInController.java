package com.project.guanzonems.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.guanzonems.dao.AdminDAO;
import com.project.guanzonems.utilities.SceneSwitcherUtility;
import com.project.guanzonems.utilities.SonnerUtility;
import com.project.guanzonems.validator.AdminValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SignInController {
    @FXML
    private TextField emailTextField;
    @FXML
    private Text emailValidatorText;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Text passwordValidatorText;

    private final AdminDAO adminDAO = new AdminDAO();
    private final AdminValidator adminValidator = new AdminValidator();

    @FXML
    private void signInButtonOnAction(ActionEvent event) {
        String email = emailTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();

        emailValidatorText.setText("");
        passwordValidatorText.setText("");
        
        ObjectNode validationErrors = adminValidator.validateAdmin(email, password);
        if (!validationErrors.isEmpty()) {
            displayValidationErrors(validationErrors);
            return;
        }

        try {
            if (adminDAO.authenticateAdmin(email, password)) {
                SonnerUtility.show(
                        (Stage) emailTextField.getScene().getWindow(),
                        "Success",
                        "Sign in successful!"
                );
                SceneSwitcherUtility.switchToDashboard(event);
            } else {
                SonnerUtility.show(
                        (Stage) emailTextField.getScene().getWindow(),
                        "Error",
                        "Invalid email or password."
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SonnerUtility.show(
                    (Stage) emailTextField.getScene().getWindow(),
                    "Error",
                    "An error occurred while signing in."
            );
        }
    }
    
    private void displayValidationErrors(ObjectNode validationErrors) {
        emailValidatorText.setText(validationErrors.path("email").asText(""));
        passwordValidatorText.setText(validationErrors.path("password").asText(""));
    }
}