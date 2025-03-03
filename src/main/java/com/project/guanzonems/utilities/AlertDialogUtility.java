package com.project.guanzonems.utilities;

import com.project.guanzonems.components_controller.AlertDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.stage.StageStyle;

public class AlertDialogUtility {

    public static void show(Stage ownerStage, String title, String subHeading, Runnable onCancelAction, Runnable onConfirmAction) {
        try {
            FXMLLoader loader = new FXMLLoader(AlertDialogUtility.class.getResource("/com/project/guanzonems/components/AlertDialog.fxml"));
            Parent root = loader.load();
            AlertDialogController controller = loader.getController();
            controller.setTitle(title);
            controller.setSubHeading(subHeading);
            controller.setOnCancelAction(onCancelAction);
            controller.setOnConfirmAction(onConfirmAction);
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(ownerStage);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}