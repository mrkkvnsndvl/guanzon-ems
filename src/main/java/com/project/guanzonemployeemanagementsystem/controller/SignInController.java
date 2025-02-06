package com.project.guanzonemployeemanagementsystem.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SignInController implements Initializable {

    @FXML
    private TextField emailTextField;
    @FXML
    private Text emailValidationText;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Text passwordValidationText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void signInButtonOnAction(ActionEvent event) {
    }
}
