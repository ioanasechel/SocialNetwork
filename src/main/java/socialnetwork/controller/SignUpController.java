package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public void setService(UserService userService, Stage stage) {
        this.userService = userService;
        this.stage = stage;
    }

    public void createUser() {
        userService.addUser(txtFirstName.getText(), txtLastName.getText(),
                txtUsername.getText(), hashPassword(txtPassword.getText()));
        User user= userService.getOne(txtUsername.getText());
        this.user = user;
    }

    public void clearFields(){
        txtFirstName.setText("");
        txtLastName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
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
        for(User user:users)
            all.add(user);
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
                    stage, Alert.AlertType.INFORMATION, "Congratulation", "You have successfully registered! "
            );
            clearFields();
        }
        else {
            if(ok.equals("Password mismatch \n")){
                txtPassword.setText("");
                txtConfirmPassword.setText("");
            }
            showErrorMessage(stage, ok);
        }
    }

    public String hashPassword(String password){
        //String passwordToHash=password;
        String generatedPassword=null;
        try{
            MessageDigest md=MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes=md.digest();
            StringBuilder sb=new StringBuilder();
            for (int i=0; i<bytes.length; i++){
                sb.append(Integer.toString((bytes[i]&0xff)+0x100,32).substring(1));
            }
            generatedPassword=sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

