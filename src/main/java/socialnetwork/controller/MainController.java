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
import javafx.stage.Window;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.AbsentObjectException;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.events.FriendRequestChangeEvent;
import socialnetwork.utils.events.FriendshipChangeEvent;
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
    private TableView<FriendRequest> tableFriendsRequest;
    @FXML
    private TableColumn<FriendRequest, String> requestFrom;
    @FXML
    private TableColumn<FriendRequest, String> requestStatus;
    @FXML
    private TableColumn<FriendRequest, String> requestDate;

    User user;
    UserService userService;
    FriendshipService friendshipService;
    MessageService messageService;
    FriendRequestService friendRequestService;
    Stage stage;
    Stage previousStage;

    ObservableList<User> friendsTableModel = FXCollections.observableArrayList();
    ObservableList<FriendRequest> receivedTableModel = FXCollections.observableArrayList();

    public void setService(UserService userService, FriendshipService friendshipService,
                           MessageService messageService,
                           FriendRequestService friendRequestService,
                           User user, Stage previousStage, Stage stage) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        friendshipService.addObserver(this);
        friendRequestService.addObserver(this);

        this.user = user;
        this.previousStage = previousStage;
        this.stage = stage;

        initFriendshipTableModel();
        initReceivedTableModel();

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
        initializeReceivedTable();
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

    private void initializeReceivedTable() {
        requestFrom.setCellValueFactory(new PropertyValueFactory<>("StringFrom"));
        requestStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        requestDate.setCellValueFactory(new PropertyValueFactory<>("StringDate"));
        tableFriendsRequest.setItems(receivedTableModel);
    }

    private void initReceivedTableModel() {
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestsOfAnUser(user.getId());
        receivedTableModel.setAll(friendRequests);
    }

    @Override
    public void update(Event event) {
        if (event instanceof FriendshipChangeEvent)
            initFriendshipTableModel();
        else if (event instanceof FriendRequestChangeEvent) {
            initReceivedTableModel();
        }
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
    public void handleAcceptRequest() throws IOException {
        FriendRequest selectedRequest = tableFriendsRequest.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            FriendRequest acceptedRequest = friendRequestService.changeStatus(selectedRequest.getId(), true);
            if (acceptedRequest != null)
                showMessage(
                        null, Alert.AlertType.INFORMATION, "Accept", "You have accepted the friend request from " + selectedRequest.getFrom()+"!"
                );
        }
        else {
            showErrorMessage(null, "You must select a request!");
        }
    }

    @FXML
    public void handleRejectRequest() throws IOException {
        FriendRequest selectedRequest = tableFriendsRequest.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            FriendRequest acceptedRequest = friendRequestService.changeStatus(selectedRequest.getId(), false);
            if (acceptedRequest != null)
                showMessage(
                        null, Alert.AlertType.INFORMATION, "Accept", "You have rejected the friend request from " + selectedRequest.getFrom()+"!"
                );
        }
        else {
            showErrorMessage(null, "You must select a request!");
        }
    }
}
