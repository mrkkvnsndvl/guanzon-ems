package com.project.guanzonems.utilities;

import com.project.guanzonems.components_controller.SonnerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;

import java.io.IOException;

public class SonnerUtility {

    public static void show(Window ownerWindow, String title, String subHeading) {
        try {
            FXMLLoader loader = new FXMLLoader(SonnerUtility.class.getResource("/com/project/guanzonems/components/Sonner.fxml"));
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