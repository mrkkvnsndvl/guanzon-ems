package com.project.guanzonems.components_controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AlertDialogController implements Initializable {

    @FXML
    private AnchorPane alertDialogAnchorPane;
    @FXML
    private Text titleText;
    @FXML
    private Text subHeadingText;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    private Runnable onCancelAction;
    private Runnable onConfirmAction;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent event) {
        if (onCancelAction != null) {
            onCancelAction.run();
        }
        closeDialog();
    }

    @FXML
    private void confirmButtonOnAction(ActionEvent event) {
        if (onConfirmAction != null) {
            onConfirmAction.run();
        }
        closeDialog();
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    public void setSubHeading(String subHeading) {
        subHeadingText.setText(subHeading);
    }

    public void setOnCancelAction(Runnable onCancelAction) {
        this.onCancelAction = onCancelAction;
    }

    public void setOnConfirmAction(Runnable onConfirmAction) {
        this.onConfirmAction = onConfirmAction;
    }

    private void closeDialog() {
        Stage stage = (Stage) alertDialogAnchorPane.getScene().getWindow();
        stage.close();
    }
}
