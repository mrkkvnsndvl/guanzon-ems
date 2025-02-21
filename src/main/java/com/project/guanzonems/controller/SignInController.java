package com.project.guanzonems.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.guanzonems.dao.AdminDAO;
import com.project.guanzonems.utilities.SceneSwitcherUtility;
import com.project.guanzonems.utilities.SonnerUtility;
import com.project.guanzonems.validator.AdminValidator;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    @FXML
    private Button signInButton;

    private final AdminDAO adminDAO = new AdminDAO();
    private final AdminValidator adminValidator = new AdminValidator();

    @FXML
    private void signInButtonOnAction(ActionEvent event) {
        String email = emailTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();

        emailValidatorText.setText("");
        passwordValidatorText.setText("");
        resetFieldStyles();

        ObjectNode validationErrors = adminValidator.validateAdmin(email, password);
        if (!validationErrors.isEmpty()) {
            displayValidationErrors(validationErrors);
            return;
        }

        signInButton.setDisable(true);
        emailTextField.setDisable(true);
        passwordPasswordField.setDisable(true);
        signInButton.setText("Signing in...");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
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
            } catch (SQLException ex) {
                ex.printStackTrace();
                SonnerUtility.show(
                        (Stage) emailTextField.getScene().getWindow(),
                        "Error",
                        "An error occurred while signing in."
                );
            } finally {
                signInButton.setDisable(false);
                emailTextField.setDisable(false);
                passwordPasswordField.setDisable(false);
                signInButton.setText("Sign in");
            }
        });
        pause.play();
    }

    private void displayValidationErrors(ObjectNode validationErrors) {
        String emailError = validationErrors.path("email").asText("");
        String passwordError = validationErrors.path("password").asText("");

        emailValidatorText.setText(validationErrors.path("email").asText(""));
        passwordValidatorText.setText(validationErrors.path("password").asText(""));

        if (!emailError.isEmpty()) {
            emailTextField.setStyle("-fx-border-color: #ef4444;");
        }
        if (!passwordError.isEmpty()) {
            passwordPasswordField.setStyle("-fx-border-color: #ef4444;");
        }
    }

    private void resetFieldStyles() {
        emailTextField.setStyle("");
        passwordPasswordField.setStyle("");
    }
}
