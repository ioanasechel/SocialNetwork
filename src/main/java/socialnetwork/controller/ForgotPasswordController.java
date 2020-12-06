package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static socialnetwork.controller.MessageAlert.showErrorMessage;
import static socialnetwork.controller.MessageAlert.showMessage;

public class ForgotPasswordController {

    @FXML
    private TextField txtUsername;

    User user;
    UserService userService;

    Stage stage;

    public void setService(UserService userService, Stage stage) {
        this.userService = userService;
        this.stage=stage;
    }

    public User getUser() {
        Iterable<User> users=userService.getAllUsers();
        List<User> all=new ArrayList<>();
        for (User user:users)
            all.add(user);
        for(User user:all)
            if (user.getUsername().equals(txtUsername.getText())) {
                this.user = user;
                return user;
            }
        return null;
    }


    @FXML
    public void handleSearch() {
        if (txtUsername.getText().length()!=0) {
            User user=getUser();
            if(user!=null){
            showMessage(
                    stage, Alert.AlertType.INFORMATION, "Password", "Your password is "+user.getPassword()
            );}
            else{
                showErrorMessage(stage, "This username does not exist");
            }
        }
        else {
            showErrorMessage(stage, "The username is missing \n");
        }
    }
}
