package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private TextField txtId;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;

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
    }

    public void initializeUser() {
        this.user = userService.getOne(Long.parseLong(txtId.getText()));
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
        String id = txtId.getText();
        if (id.matches("[0-9]*") && userService.exist(Long.parseLong(id))) {
            initializeUser();
            loadMainStage();
            stage.close();
        }
        else {
            lblStatus.setText("Login failed");
            lblStatus.setTextFill(Color.web("#ba170b"));
        }
    }

    public void createUser() {
        userService.addUser(txtFirstName.getText(), txtLastName.getText());
        User user= userService.getUser(txtFirstName.getText(), txtLastName.getText());
        this.user = user;
    }

    @FXML
    public void logIn() throws IOException{
        if (txtFirstName.getText().length()!=0 ||
            txtLastName.getText().length()!=0) {
            createUser();
            loadMainStage();
            stage.close();
        }
        else {
            lblStatus.setText("Login failed");
            lblStatus.setTextFill(Color.web("#ba170b"));
       }
    }
}
