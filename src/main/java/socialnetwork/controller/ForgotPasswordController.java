package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static socialnetwork.controller.MessageAlert.showErrorMessage;
import static socialnetwork.controller.MessageAlert.showMessage;

public class ForgotPasswordController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;

    User user;
    UserService userService;

    Stage stage;
    Stage previousStage;

    public void setService(UserService userService, Stage previousStage, Stage stage) {
        this.userService = userService;
        this.stage=stage;
        this.previousStage=previousStage;
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

    public void clearFields(){
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    public String checkTextFields(){
        String ok="";
        if (txtUsername.getText().length()==0)
            ok+="The username is missing \n";
        if (txtPassword.getText().length()==0)
            ok+="The password is missing \n";
        if (txtConfirmPassword.getText().length()==0)
            ok+="The confirmation for the password is missing \n";
        if (!txtPassword.getText().equals(txtConfirmPassword.getText()))
            ok+="Password mismatch \n";

        return ok;
    }

    private User updateUser(User user) {
        Long id=user.getId();
        String firstName=user.getFirstName();
        String lastName=user.getLastName();
        String username=user.getUsername();
        try {
            userService.deleteUser(id);
        }catch (IOException exception){
            showErrorMessage(stage, exception.toString());
        }
        User update=userService.addUser(firstName, lastName, username, hashPassword(txtPassword.getText()));
        return update;
    }



    @FXML
    public void handleSearch() {
        String ok=checkTextFields();
        if (ok.length()==0) {
            User user=getUser();
            User update=updateUser(user);
            if(update==null){
            showMessage(
                    stage, Alert.AlertType.INFORMATION, "Password", "The password has been changed!"
            );
                clearFields();}
                //stage.close();}
            else{
                showErrorMessage(stage, "This username does not exist");
            }
        }
        else {
            showErrorMessage(stage, "The username is missing \n");
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
