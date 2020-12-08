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

public class UnsentController implements Observer {

    @FXML
    private TableView<FriendRequest> tableSent;
    @FXML
    private TableColumn<FriendRequest, String> sentTo;

    User user;
    FriendRequestService friendRequestService;
    Stage stage;

    ObservableList<FriendRequest> sentTableModel = FXCollections.observableArrayList();

    public void setService(FriendRequestService friendRequestService,
                           User user, Stage stage) {
        this.friendRequestService = friendRequestService;
        friendRequestService.addObserver(this);

        this.user = user;
        this.stage = stage;

        initSentTableModel();
    }

    @FXML
    public void initialize() {
        initializeSentTable();
    }


    private void initializeSentTable() {
        sentTo.setCellValueFactory(new PropertyValueFactory<>("StringSent"));
        tableSent.setItems(sentTableModel);
    }

    private void initSentTableModel() {
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestsSentOfAnUser(user.getId());
        sentTableModel.setAll(friendRequests);
    }

    @Override
    public void update(Event event) {
            initSentTableModel();

    }


    @FXML
    public void handleUnsent() {
        FriendRequest selectedRequest = tableSent.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            FriendRequest unsentFriendRequest = friendRequestService.unsentFriendRequest(selectedRequest.getId());
            if (unsentFriendRequest == null)
                showMessage(
                        stage, Alert.AlertType.INFORMATION, "Unsent", "You have deleted the friend request to " + selectedRequest.getTo()+"!"
                );
        }
        else {
            showErrorMessage(stage, "You must select a request!");
        }
    }

    @FXML
    private void handleBack() {
        stage.close();
    }
}
