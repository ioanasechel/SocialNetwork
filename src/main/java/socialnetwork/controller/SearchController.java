package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.AbsentObjectException;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;
import socialnetwork.utils.observer.Observer;

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

    ObservableList<User> usersTableModel = FXCollections.observableArrayList();

    public void setService(User user, UserService userService, MessageService messageService, FriendRequestService friendRequestService, Stage stage) {
        this.user = user;
        this.userService = userService;
        this.friendRequestService = friendRequestService;
        this.messageService = messageService;
//        this.dialogStage = stage;
//        this.event = event;
 //       friendRequestService.addObserver((Observer) this);
//        setVisibility();

        this.user = user;

        initUserTableModel();
    }

    @FXML
    public void initialize() {
        initializeUsersTable();
        txtSearch.textProperty().addListener((x)->handleserchField());
    }

    private void initializeUsersTable() {
        addFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        addLastName.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        tableAddFriends.setItems(usersTableModel);
    }

    private void initUserTableModel() {
        Iterable<User> users = userService.getAllUsers();
        List<User> all=new ArrayList<>();
        users.forEach(user->{all.add(user);});
        usersTableModel.setAll(all);
    }

    public void handleserchField () {
        Iterable<User> users = userService.getAllUsers();
        List<User> all=new ArrayList<>();
        users.forEach(user->{all.add(user);});
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
            FriendRequest fr = friendRequestService.addFriendRequest(user.getId(), selectedUser.getId());
            if (fr == null)
                MessageAlert.showMessage(
                        null, Alert.AlertType.INFORMATION, "Add friend", "You have sent a friend request to " + selectedUser.getFirstName() + " " + selectedUser.getLastName() + "!"
                );
        }
        else
            MessageAlert.showErrorMessage(null, "You must select a friend!");
    }

    /*
    public void handleUnfriend() throws IOException {
        User selectedFriend = tableFriends.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            User found = userService.getOne(selectedFriend.getId());
            Friendship deletedFriendship = friendshipService.deleteFriendship(user.getId(), found.getId());
            if (deletedFriendship != null)
                showMessage(
                        null, Alert.AlertType.INFORMATION, "Unfriend", "You have unfriended " + selectedFriend.getFirstName() + " " + selectedFriend.getLastName() +"!"
                );
        }
        else {
            showErrorMessage(null, "You must select a friend!");
        }
    }
     */
}
