package com.project.guanzonems.utilities;

import com.project.guanzonems.components_controller.DepartmentDialogController;
import com.project.guanzonems.controller.DepartmentController;
import com.project.guanzonems.model.Department;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

public class DepartmentDialogUtility {

    public static void show(AnchorPane ownerAnchorPane, DepartmentController departmentController) {
        try {
            FXMLLoader loader = new FXMLLoader(DepartmentDialogUtility.class.getResource("/com/project/guanzonems/components/DepartmentDialog.fxml"));
            Parent root = loader.load();
            DepartmentDialogController controller = loader.getController();
            controller.setOwnerAnchorPane(ownerAnchorPane);
            controller.setDepartmentController(departmentController);
            Stage stage = new Stage();
            stage.initOwner(ownerAnchorPane.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showForUpdate(AnchorPane ownerAnchorPane, Department selectedDepartment, DepartmentController departmentController) {
        try {
            FXMLLoader loader = new FXMLLoader(DepartmentDialogUtility.class.getResource("/com/project/guanzonems/components/DepartmentDialog.fxml"));
            Parent root = loader.load();
            DepartmentDialogController controller = loader.getController();
            controller.setOwnerAnchorPane(ownerAnchorPane);
            controller.setSelectedDepartment(selectedDepartment);
            controller.setDepartmentController(departmentController);
            Stage stage = new Stage();
            stage.initOwner(ownerAnchorPane.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
