package com.project.guanzonems.utilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcherUtility {

    public static void switchToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcherUtility.class.getResource("/com/project/guanzonems/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToSignIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcherUtility.class.getResource("/com/project/guanzonems/fxml/SignIn.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
