package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.controller.LogInController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.FriendRequestValidator;
import socialnetwork.domain.validators.FriendsValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendRequestFile;
import socialnetwork.repository.file.FriendsFile;
import socialnetwork.repository.file.MessageFile;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;

public class MainApp extends Application {

    Repository<Long, User> userFileRepository;
    Repository<Tuple<Long, Long>, Friendship> friendshipFileRepository;
    Repository<Long, Message> messageFileRepository;
    Repository<Long, FriendRequest> friendRequestFileRepository;
    UserService userService;
    FriendshipService friendshipService;
    MessageService messageService;
    FriendRequestService friendRequestService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fileNameUsers="data/users.csv";
        String fileNameFriendships="data/friendship.csv";
        String fileNameMessages="data/messages.csv";
        String fileNameFriendRequests="data/friendRequest.csv";

        userFileRepository = new UserFile(fileNameUsers, new UserValidator());
        friendshipFileRepository = new FriendsFile(fileNameFriendships, new FriendsValidator());
        messageFileRepository = new MessageFile(fileNameMessages, new MessageValidator(), userFileRepository);
        friendRequestFileRepository = new FriendRequestFile(fileNameFriendRequests, new FriendRequestValidator(), userFileRepository);

        userService = new UserService(userFileRepository, friendshipFileRepository);
        friendshipService = new FriendshipService(userFileRepository,friendshipFileRepository);
        messageService = new MessageService(messageFileRepository, userFileRepository);
        friendRequestService = new FriendRequestService(friendRequestFileRepository, userFileRepository, friendshipService);

        initView(primaryStage);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws Exception {
        //login
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LogInPage.fxml"));
        AnchorPane layout = loader.load();
        primaryStage.setScene(new Scene(layout));

        LogInController loginController = loader.getController();
        loginController.setService(userService, friendshipService, messageService, friendRequestService, primaryStage);
    }
}
