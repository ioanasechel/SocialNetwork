package socialnetwork;

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
import socialnetwork.ui.Ui;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //String fileName=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        //String fileName2=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.prietenie");

//        String UserFile="data/users.csv";
//        String FriendshipFile="data/friendship.csv";
//        String MessageFile="data/messages.csv";
//        String FriendRequestFile="data/friendRequest.csv";
//
//        Repository<Long, User> userFileRepository =
//                new UserFile(UserFile, new UserValidator());
//        Repository<Tuple<Long,Long>, Friendship> friendsFileRepository =
//                new FriendsFile(FriendshipFile,new FriendsValidator());
//        Repository<Long, Message> messageFileRepository =
//                new MessageFile(MessageFile, new MessageValidator(), userFileRepository);
//        Repository<Long, FriendRequest> FriendRequestFileRepository =
//                new FriendRequestFile(FriendRequestFile, new FriendRequestValidator(), userFileRepository);
//
//        UserService userService = new UserService(userFileRepository,friendsFileRepository);
//        FriendshipService friendshipService = new FriendshipService(userFileRepository,friendsFileRepository);
//        MessageService messageService=new MessageService(messageFileRepository, userFileRepository);
//        FriendRequestService friendRequestService=
//                new FriendRequestService(FriendRequestFileRepository, userFileRepository, friendshipService);
//
//        Ui ui=new Ui(userService, friendshipService, messageService, friendRequestService);
//        ui.run();
        MainApp.main(args);
    }
}


