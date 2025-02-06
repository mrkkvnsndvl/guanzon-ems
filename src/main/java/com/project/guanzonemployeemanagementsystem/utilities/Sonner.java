package com.project.guanzonemployeemanagementsystem.utilities;

import com.project.guanzonemployeemanagementsystem.components_controller.SonnerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;

import java.io.IOException;

public class Sonner {

    public static void show(Window ownerWindow, String title, String subHeading) {
        try {
            FXMLLoader loader = new FXMLLoader(Sonner.class.getResource("/com/project/guanzonemployeemanagementsystem/components/sonner.fxml"));
            Parent root = loader.load();

            SonnerController controller = loader.getController();
            controller.setTitle(title);
            controller.setSubHeading(subHeading);

            controller.show(ownerWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//        Stage ownerStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//        Sonner.show(
//                ownerStage,
//                "Success!",
//                "Your action was completed successfully."
//        );