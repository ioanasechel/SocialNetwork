package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static socialnetwork.controller.MessageAlert.showErrorMessage;
import static socialnetwork.controller.MessageAlert.showMessage;

public class MainController implements Observer {

    @FXML
    private TextField txtLoggedIn;

    @FXML
    private TableView<User> tableFriends;
    @FXML
    private TableColumn<User, String> friendFirstName;
    @FXML
    private TableColumn<User, String> friendLastName;

    @FXML
    private Button idButtonRequest;
    @FXML
    private Button idButtonMessage;

    User user;
    UserService userService;
    FriendshipService friendshipService;
    MessageService messageService;
    FriendRequestService friendRequestService;
    Stage stage;
    Stage previousStage;

    ObservableList<User> friendsTableModel = FXCollections.observableArrayList();

    public void setService(UserService userService, FriendshipService friendshipService,
                           MessageService messageService,
                           FriendRequestService friendRequestService,
                           User user, Stage previousStage, Stage stage) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        friendshipService.addObserver(this);

        this.user = user;
        this.previousStage = previousStage;
        this.stage = stage;

        initFriendshipTableModel();
        notifications();

        txtLoggedIn.setText(user.getFirstName() + " " + user.getLastName());
        txtLoggedIn.setEditable(false);
    }

    @FXML
    public void logOut() {
        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList());
        for (Window w : open)
            w.hide();
        stage.close();
        previousStage.show();
    }

    @FXML
    public void initialize() {
        initializeFriendsTable();
    }


    private void initializeFriendsTable() {
        friendFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        friendLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        tableFriends.setItems(friendsTableModel);
    }

    private void initFriendshipTableModel() {
        List<User> userFriends = userService.getFriends(user.getId());
        friendsTableModel.setAll(userFriends);
    }


    private void notifications(){
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestsOfAnUser(user.getId());
        if (friendRequests.size()!=0)
        {
            idButtonRequest.setDisable(false);
            idButtonRequest.setManaged(true);
        }
        else{
            idButtonRequest.setDisable(true);
            idButtonRequest.setManaged(false);
        }

//        Iterable<Message> allMessages=messageService.getAllMessages();
        List<Message> all=messageService.getNotificationMessage(user.getId());
        if (all.size()>0)
        {
            idButtonMessage.setDisable(false);
            idButtonMessage.setManaged(true);
        }
        else{
            idButtonMessage.setDisable(true);
            idButtonMessage.setManaged(false);
        }
    }


    @Override
    public void update(Event event) {
            initFriendshipTableModel();
    }

    @FXML
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

    @FXML
    private void loadSearchStage() throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/SearchPage.fxml"));
        AnchorPane layout = loader.load();
        newStage.setScene(new Scene(layout));

        SearchController searchController = loader.getController();
        searchController.setService(user, userService, messageService, friendRequestService, newStage);

        newStage.show();
    }

    @FXML
    private void loadNotificationRequestStage() throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/NotificationRequestPage.fxml"));
        AnchorPane layout = loader.load();
        newStage.setScene(new Scene(layout));

        NotificationRequestController requestController = loader.getController();
        requestController.setService(friendRequestService, user, newStage);

        newStage.show();
    }

    @FXML
    private void loadNotificationMessageStage() throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/NotificationMessagePage.fxml"));
        AnchorPane layout = loader.load();
        newStage.setScene(new Scene(layout));

        NotificationMessageController messageController = loader.getController();
        messageController.setService(messageService, user, newStage);

        newStage.show();
    }

    @FXML
    private void loadUnsentStage() throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/UnsentPage.fxml"));
        AnchorPane layout = loader.load();
        newStage.setScene(new Scene(layout));

        UnsentController unsentController = loader.getController();
        unsentController.setService(friendRequestService, user, newStage);

        newStage.show();
    }

    @FXML
    private void handleRefresh(){
        notifications();
    }


}
