package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.service.MessageService;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationMessageController implements Observer {

    @FXML
    private TableView<Message> tableMessage;
    @FXML
    private TableColumn<FriendRequest, String> stringMessage;

    User user;
    MessageService messageService;
    Stage stage;

    ObservableList<Message> messageTableModel = FXCollections.observableArrayList();

    public void setService(MessageService messageService,
                           User user,Stage stage) {
        this.messageService = messageService;
        messageService.addObserver(this);
        this.user = user;
        this.stage = stage;

        initMessageTableModel();

    }

    @FXML
    public void initialize() {
        initializeMessageTable();
    }

    private void initializeMessageTable() {
        stringMessage.setCellValueFactory(new PropertyValueFactory<>("StringMessage"));
        tableMessage.setItems(messageTableModel);
    }

    private void initMessageTableModel() {
        List<Message> messages = messageService.getNotificationMessage(user.getId());
        messageTableModel.setAll(messages);
        //notifications();
    }

    @Override
    public void update(Event event) {
        initMessageTableModel();
    }

    @FXML
    private void handleSendMessage() throws IOException {
        Message selectedMessage = tableMessage.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            loadMessageStage(selectedMessage.getFrom());
        }
        else
            MessageAlert.showErrorMessage(stage, "You must select a friend!");
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

        newStage.show();
    }
}
