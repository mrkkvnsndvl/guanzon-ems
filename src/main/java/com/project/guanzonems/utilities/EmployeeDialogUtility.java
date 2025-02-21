package com.project.guanzonems.utilities;

import com.project.guanzonems.components_controller.EmployeeDialogController;
import com.project.guanzonems.controller.EmployeeController;
import com.project.guanzonems.model.Employee;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

public class EmployeeDialogUtility {

    public static void show(AnchorPane ownerAnchorPane, EmployeeController employeeController) {
        try {
            FXMLLoader loader = new FXMLLoader(EmployeeDialogUtility.class.getResource("/com/project/guanzonems/components/EmployeeDialog.fxml"));
            Parent root = loader.load();
            EmployeeDialogController controller = loader.getController();
            controller.setOwnerAnchorPane(ownerAnchorPane);
            controller.setEmployeeController(employeeController);

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

    public static void showForUpdate(AnchorPane ownerAnchorPane, Employee selectedEmployee, EmployeeController employeeController) {
        try {
            FXMLLoader loader = new FXMLLoader(EmployeeDialogUtility.class.getResource("/com/project/guanzonems/components/EmployeeDialog.fxml"));
            Parent root = loader.load();
            EmployeeDialogController controller = loader.getController();
            controller.setOwnerAnchorPane(ownerAnchorPane);
            controller.setSelectedEmployee(selectedEmployee);
            controller.setEmployeeController(employeeController);

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
