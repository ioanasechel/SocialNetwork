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

public class SignUpController {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;

    User user;
    UserService userService;
    Stage stage;

    public void setService(UserService userService) {
        this.userService = userService;
        //this.stage = stage;
    }

    public void createUser() {
        userService.addUser(txtFirstName.getText(), txtLastName.getText(),
                txtUsername.getText(), txtPassword.getText());
        User user= userService.getOne(txtUsername.getText());
        this.user = user;
    }

    public String checkTextFields(){
        String ok="";
        if (txtFirstName.getText().length()==0)
            ok+="The first name is missing \n";
        if (txtLastName.getText().length()==0)
            ok+="The last name is missing \n";
        if (txtUsername.getText().length()==0)
            ok+="The username is missing \n";
        if (txtPassword.getText().length()==0)
            ok+="The password is missing \n";
        if (txtConfirmPassword.getText().length()==0)
            ok+="The confirmation for the password is missing \n";
        if (!txtPassword.getText().equals(txtConfirmPassword.getText()))
            ok+="Password mismatch \n";

        Iterable<User> users=userService.getAllUsers();
        List<User> all=new ArrayList<>();
        users.forEach(user->{all.add(user);});
        for(User user:all)
            if (user.getUsername().equals(txtUsername.getText())) {
                ok += "This username is already used \n";
                txtUsername.setText("");
            }
        return ok;
    }

    @FXML
    public void handleRegister() {
        String ok=checkTextFields();
        if (ok.length()==0) {
            createUser();
            showMessage(
                    null, Alert.AlertType.INFORMATION, "Congratulation", "You have successfully registered! "
            );
        }
        else {
            if(ok.equals("Password mismatch \n")){
                txtPassword.setText("");
                txtConfirmPassword.setText("");
            }
            showErrorMessage(null, ok);
        }
    }
}

