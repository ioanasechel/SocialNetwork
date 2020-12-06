package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;

import java.io.IOException;

public class LogInController {
    @FXML
    private Label lblStatus;
    @FXML
    private ImageView idImage;

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    User user;
    UserService userService;
    FriendshipService friendshipService;
    MessageService messageService;
    FriendRequestService friendRequestService;
    Stage stage;

    public void setService(UserService userService,FriendshipService friendshipService,
            MessageService messageService, FriendRequestService friendRequestService,
                           Stage stage) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.stage = stage;

        Image image=new Image("/images/first.png");
        idImage.setImage(image);
    }

    public void initializeUser() {
        this.user = userService.getOne(txtUsername.getText());
    }

    public void loadMainStage() throws IOException {

        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/MainPage.fxml"));
        AnchorPane layout = loader.load();
        newStage.setScene(new Scene(layout));

        MainController mainController = loader.getController();
        mainController.setService(userService, friendshipService, messageService, friendRequestService, user, stage, newStage);

        newStage.show();
    }

    @FXML
    public void signIn() throws IOException {
        if (txtUsername.getText().length()!=0 &&
                txtPassword.getText().length()!=0
                && userService.getUser(txtUsername.getText(), txtPassword.getText())!=null) {
            initializeUser();
            loadMainStage();
            stage.close();
        }
        else {
            lblStatus.setText("Sign in failed");
            lblStatus.setTextFill(Color.web("#ba170b"));
        }
    }

    @FXML
    public void handleSignUp() throws IOException{
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/SignUpPage.fxml"));
        AnchorPane layout = loader.load();
        newStage.setScene(new Scene(layout));

        SignUpController signUpController = loader.getController();
        signUpController.setService(userService);

        newStage.show();
    }

    @FXML
    public void handleForgotPassword() throws IOException{
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ForgotPasswordPage.fxml"));
        AnchorPane layout = loader.load();
        newStage.setScene(new Scene(layout));

        ForgotPasswordController forgotPasswordController = loader.getController();
        forgotPasswordController.setService(userService);

        newStage.show();
    }
}
