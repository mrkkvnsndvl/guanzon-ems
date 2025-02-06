package com.project.guanzonemployeemanagementsystem.utilities;

import com.project.guanzonemployeemanagementsystem.components_controller.AlertDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.stage.StageStyle;

public class AlertDialog {

    public static void show(Stage ownerStage, String title, String subHeading, Runnable onCancelAction, Runnable onConfirmAction) {
        try {
            FXMLLoader loader = new FXMLLoader(AlertDialog.class.getResource("/com/project/guanzonemployeemanagementsystem/components/alert-dialog.fxml"));
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

//Stage ownerStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//        AlertDialog.show(
//                ownerStage,
//                "Confirm Sign Out",
//                "Are you sure you want to sign out?",
//                () -> System.out.println("Sign out canceled"),
//                () -> {
//                    try {
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/guanzonemployeemanagementsystem/fxml/sign-in.fxml"));
//                        Parent signInPage = loader.load();
//                        ownerStage.getScene().setRoot(signInPage);
//                    } catch (IOException e) {
//                        LOGGER.log(Level.SEVERE, "Error loading sign in page", e);
//                    }
//                }
//        );