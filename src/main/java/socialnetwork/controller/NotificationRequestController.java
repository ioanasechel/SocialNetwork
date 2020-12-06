package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.User;
import socialnetwork.service.FriendRequestService;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.observer.Observer;

import java.util.List;

import static socialnetwork.controller.MessageAlert.showErrorMessage;
import static socialnetwork.controller.MessageAlert.showMessage;

public class NotificationRequestController implements Observer {

    @FXML
    private TableView<FriendRequest> tabelRequest;
    @FXML
    private TableColumn<FriendRequest, String> stringRequest;

    User user;
    FriendRequestService friendRequestService;
    Stage stage;

    ObservableList<FriendRequest> receivedTableModel = FXCollections.observableArrayList();

    public void setService(FriendRequestService friendRequestService,
                           User user,Stage stage) {
        this.friendRequestService = friendRequestService;
        friendRequestService.addObserver(this);
        this.user = user;
        this.stage = stage;

        initReceivedTableModel();

    }

    @FXML
    public void initialize() {
        initializeReceivedTable();
    }

    private void initializeReceivedTable() {
        stringRequest.setCellValueFactory(new PropertyValueFactory<>("StringReceive"));
        tabelRequest.setItems(receivedTableModel);
    }

    private void initReceivedTableModel() {
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestsOfAnUser(user.getId());
        receivedTableModel.setAll(friendRequests);
        //notifications();
    }

    @Override
    public void update(Event event) {
            initReceivedTableModel();
    }

    @FXML
    public void handleAcceptRequest() {
        FriendRequest selectedRequest = tabelRequest.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            FriendRequest acceptedRequest = friendRequestService.changeStatus(selectedRequest.getId(), true);
            if (acceptedRequest == null)
                showMessage(
                        stage, Alert.AlertType.INFORMATION, "Accept", "You have accepted the friend request from " + selectedRequest.getFrom()+"!"
                );
        }
        else {
            showErrorMessage(stage, "You must select a request!");
        }
    }

    @FXML
    public void handleRejectRequest(){
        FriendRequest selectedRequest = tabelRequest.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            FriendRequest acceptedRequest = friendRequestService.changeStatus(selectedRequest.getId(), false);
            if (acceptedRequest == null)
                showMessage(
                        stage, Alert.AlertType.INFORMATION, "Accept", "You have rejected the friend request from " + selectedRequest.getFrom()+"!"
                );
        }
        else {
            showErrorMessage(stage, "You must select a request!");
        }
    }
}
