package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.User;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SearchController {

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<User> tableAddFriends;
    @FXML
    private TableColumn<User, String> addFirstName;
    @FXML
    private TableColumn<User, String> addLastName;

    User user;
    UserService userService;
    FriendRequestService friendRequestService;
    MessageService messageService;
    Stage stage;

    ObservableList<User> usersTableModel = FXCollections.observableArrayList();

    public void setService(User user, UserService userService,
                           MessageService messageService,
                           FriendRequestService friendRequestService, Stage stage) {
        this.user = user;
        this.userService = userService;
        this.friendRequestService = friendRequestService;
        this.messageService = messageService;

        this.user = user;
        this.stage=stage;

        initUserTableModel();
    }

    @FXML
    public void initialize() {
        initializeUsersTable();
        txtSearch.textProperty().addListener((x)->handleserchField());
    }

    private void initializeUsersTable() {
        addFirstName.setCellValueFactory(new PropertyValueFactory<>("StringUser"));
        //addLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        tableAddFriends.setItems(usersTableModel);
    }

    private void initUserTableModel() {
        Iterable<User> users = userService.getAllUsers();
        List<User> all=new ArrayList<>();
        users.forEach(user1->{
            if (user1.getId()!=user.getId())
                all.add(user1);});
        usersTableModel.setAll(all);
    }

    public void handleserchField () {
        Iterable<User> users = userService.getAllUsers();
        List<User> all=new ArrayList<>();
        users.forEach(user1->{
            if (user1.getId()!=user.getId())
                all.add(user1);});
        usersTableModel.setAll( StreamSupport.stream(all.spliterator(), false)
                .filter(x->{
                    String nume_user = txtSearch.getText();
                    return x.getFirstName().startsWith(nume_user);
                })
                .collect(Collectors.toList())
        );

    }

    @FXML
    private void handleAddFriend() {
        User selectedUser = tableAddFriends.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            FriendRequest fr = friendRequestService.sendFriendRequest(user.getId(), selectedUser.getId());
            if (fr == null)
                MessageAlert.showMessage(
                        null, Alert.AlertType.INFORMATION, "Add friend", "You have sent a friend request to " + selectedUser.getFirstName() + " " + selectedUser.getLastName() + "!"
                );
        }
        else
            MessageAlert.showErrorMessage(null, "You must select a friend!");
    }

    @FXML
    private void handleSendMessage() throws IOException {
        User selectedUser = tableAddFriends.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            loadMessageStage(selectedUser);
        }
        else
            MessageAlert.showErrorMessage(null, "You must select a friend!");
    }

    @FXML
    private void handleBack() {
        stage.close();
    }

    @FXML
    private void loadMessageStage(User toCommunicate) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/MessagePage.fxml"));
        AnchorPane layout = loader.load();
        newStage.setScene(new Scene(layout));

        MessageController messageController = loader.getController();
        messageController.setService(messageService, user, toCommunicate, newStage);
        newStage.setTitle("MeetLy");
        newStage.show();
    }


}
