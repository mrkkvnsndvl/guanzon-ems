package com.project.guanzonems.components_controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class SonnerController implements Initializable {

    @FXML
    private AnchorPane sonnerAnchorPane;
    @FXML
    private Text titleText;
    @FXML
    private Text subHeadingText;

    private Popup popup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    public void setSubHeading(String subHeading) {
        subHeadingText.setText(subHeading);
    }

    public void show(Window ownerWindow) {
        if (popup == null) {
            popup = new Popup();
            popup.getContent().add(sonnerAnchorPane);
        }
        double offsetX = 20;
        double offsetY = 20;
        popup.setX(ownerWindow.getX() + ownerWindow.getWidth() - sonnerAnchorPane.getWidth() - offsetX);
        popup.setY(ownerWindow.getY() + ownerWindow.getHeight() - sonnerAnchorPane.getHeight() - offsetY);
        sonnerAnchorPane.setOpacity(0);
        popup.show(ownerWindow);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), sonnerAnchorPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            popup.hide();
        });
        pause.play();
    }
}
