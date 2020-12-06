package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
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

    public void setService(UserService userService) {
        this.userService = userService;
    }

    public User getUser() {
        Iterable<User> users=userService.getAllUsers();
        List<User> all=new ArrayList<>();
        users.forEach(user->{all.add(user);});
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
                    null, Alert.AlertType.INFORMATION, "Password", "Your password is "+user.getPassword()
            );}
            else{
                showErrorMessage(null, "This username does not exist");
            }
        }
        else {
            showErrorMessage(null, "The username is missing \n");
        }
    }
}
