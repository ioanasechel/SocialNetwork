package socialnetwork.controller;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MessageAlert {
    static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        owner.getIcons().add(new Image("images/app_icon.png"));
        message.showAndWait();

    }

    static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        owner.getIcons().add(new Image("images/app_icon.png"));
        message.setTitle("Error");
        message.setContentText(text);
        message.showAndWait();
    }
}
