package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.service.MessageService;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.observer.Observer;

import java.util.List;

import static socialnetwork.controller.MessageAlert.showErrorMessage;

public class MessageController implements Observer {

    @FXML
    private Label lblUser;

    @FXML
    private TableView<Message> tableMessage;
    @FXML
    private TableColumn<Message, String> txtMessage;
    @FXML
    private TextField txtReply;

    @FXML
    private Button idButtonReply;
    @FXML
    private Button idButtonReplyAll;

    User user;
    User toCommunicate;
    MessageService messageService;
    Stage stage;

    ObservableList<Message> messageTableModel = FXCollections.observableArrayList();

    public void setService(MessageService messageService,
                           User user, User toCommunicate, Stage stage) {
        this.messageService = messageService;
        messageService.addObserver(this);
        this.user = user;
        this.toCommunicate = toCommunicate;
        this.stage = stage;

        initMessageTableModel();
        setVisibility();

        lblUser.setText(toCommunicate.getFirstName() + " " + toCommunicate.getLastName());
    }

    @FXML
    public void initialize() {
        initializeMessageTable();
    }


    private void initializeMessageTable() {
        txtMessage.setCellValueFactory(new PropertyValueFactory<>("StringMessage"));
        tableMessage.setItems(messageTableModel);
    }

    private void initMessageTableModel() {
        List<Message> conversation = messageService.showConversation(user.getId(), toCommunicate.getId());
        messageTableModel.setAll(conversation);
    }


    @Override
    public void update(Event event) {
        initMessageTableModel();
    }

    @FXML
    public void handleReply(){
        Message selectedMessage = tableMessage.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            String replyMessage= txtReply.getText();
            messageService.replyMessage(selectedMessage.getId(), user.getId(), replyMessage);
        } else {
            showErrorMessage(stage, "You must select a message!");
        }
    }

    private void setVisibility(){
        List<Message> conversation = messageService.showConversation(user.getId(), toCommunicate.getId());
       if (conversation.size()!=0)
        {
            idButtonReply.setDisable(false);
            idButtonReply.setManaged(true);
            idButtonReplyAll.setDisable(false);
            idButtonReplyAll.setManaged(true);

        }
        else{
            idButtonReply.setDisable(true);
            idButtonReply.setManaged(false);
            idButtonReplyAll.setDisable(true);
            idButtonReplyAll.setManaged(false);
        }
    }

    @FXML
    public void handleReplyAll(){
        Message selectedMessage = tableMessage.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            String replyMessage= txtReply.getText();
            messageService.replyAll(selectedMessage.getId(), user.getId(), replyMessage);
        } else {
            showErrorMessage(stage, "You must select a message!");
        }
    }

    @FXML
    public void handleSendMessage(){
        String replyMessage= txtReply.getText();
        String stringToId= toCommunicate.getId()+" ";
        messageService.sendMessage(user.getId(),stringToId, replyMessage);
    }
}
