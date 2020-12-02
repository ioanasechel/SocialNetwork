package socialnetwork.ui;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Ui {
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;

    /**
     * constructor
     * @param userService the service for users
     * @param friendshipService the service for friendships
     * @param messageService the service for messages
     * @param friendRequestService the service for friend requests
     */
    public Ui(UserService userService, FriendshipService friendshipService,
              MessageService messageService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
    }

    /**
     * add a user
     * @throws IOException
     */
    private void addUser() {
        Scanner scn= new Scanner(System.in);

        System.out.print("Last name: ");
        String lastName = scn.nextLine();

        System.out.print("First name: ");
        String firstName = scn.nextLine();

        userService.addUser(firstName, lastName);
        System.out.println("User added!");
    }

    /**
     * remove a user
     * @throws IOException
     */

    private void deleteUser() throws IOException {

        Scanner scn= new Scanner(System.in);

        System.out.print("Id: ");
        Long id = scn.nextLong();
        userService.deleteUser(id);
        System.out.println("The user has been deleted!");
    }

    /**
     * print all users
     */
    public void printAllUsers(){
        System.out.println("Users:");
        userService.getAllUsers().forEach(System.out::println);
    }

    /**
     * add a friendship
     */
    public void addFriendship(){
        Scanner scn= new Scanner(System.in);

        System.out.print("Friendship between(id): ");
        Long id1 = scn.nextLong();
        scn.nextLine();

        System.out.print("And(id): ");
        Long id2 = scn.nextLong();
        scn.nextLine();

        friendshipService.addFriendship(id1, id2);
        System.out.println("Friendship added!");
    }

    /**
     * delete a friendship
     * @throws IOException
     */
    public void deleteFriendship() throws IOException {
        Scanner scn= new Scanner(System.in);

        System.out.print("Friendship between(id): ");
        Long id1 = scn.nextLong();
        scn.nextLine();

        System.out.print("And(id): ");
        Long id2 = scn.nextLong();
        scn.nextLine();

        friendshipService.deleteFriendship(id1, id2);
        System.out.println("The friendship has been deleted!");

    }

    /**
     * print all friendships
     */
    public void printAllFriendships() {
        System.out.println("Friendships:");
        friendshipService.getAllFriendships().forEach(System.out::println);
    }

    /**
     * print the number of the communities
     */
    public void numberOfCommunities(){
        System.out.print("There are " +
                friendshipService.numberOfCommunities()+
            " communities \n");
    }

    /**
     * print the most sociable community
     */
    public void theMostSociableCommunity(){

        System.out.print("The most sociable community consists of: \n");
        String friends= friendshipService.cel_mai_lung_drum();
        System.out.println(friends);
    }

    /**
     * show all the friends for a user
     */
    public void showFriendsOfAUser(){
        Scanner scn= new Scanner(System.in);

        System.out.print("User id: ");
        Long id = scn.nextLong();
        List<User> friends = userService.getFriends(id);
        Optional<String> rez=friends.stream()
                .map(user -> {
                    return user.getLastName()+" | "+
                            user.getFirstName()+" | "+
                            friendshipService.getFriendshipData(id, user.getId());
                })
                .reduce((x,y)->x.concat("\n"+y));
        rez.ifPresent(x-> System.out.println(x));

    }

    /**
     * show all the friendships for a user created in a specific month
     */
    public void showFriendsOfAUserbyMonth(){
        Scanner scn= new Scanner(System.in);

        System.out.print("User id: ");
        Long id = scn.nextLong();
        System.out.print("Month: ");
        Long month=scn.nextLong();
        List<User> friends = friendshipService.getFriendsOfAUserbyMonth(id, month);
        System.out.println("Users:");
        friends.forEach(user-> {
            System.out.println(user.getLastName()+" | "+
                    user.getFirstName()+" | "+
                    friendshipService.getFriendshipData(id, user.getId()));
        });
    }

    /**
     * send a message
     */
    private void addMessage() {
        Scanner scn= new Scanner(System.in);

        System.out.print("From (id): ");
        Long idFrom = scn.nextLong();
        scn.nextLine();

        System.out.print("To (id):");
        String usersToId = scn.nextLine();

        System.out.print("Message: ");
        String message = scn.nextLine();

        messageService.sendMessage(idFrom, usersToId, message);
        System.out.println("Message added!");
    }

    /**
     * print all the messages
     */
    public void printAllMessages(){
        System.out.println("Messages: ");
        messageService.getAllMessages().forEach(System.out::println);
    }

    /**
     * reply for a message
     */
    public void replyMessage(){
        Scanner scn= new Scanner(System.in);

        System.out.print("Message (id): ");
        Long idMesaj = scn.nextLong();
        scn.nextLine();

        System.out.print("From (id): ");
        Long idUser = scn.nextLong();
        scn.nextLine();

        System.out.print("Message: ");
        String message = scn.nextLine();

        messageService.replyMessage(idMesaj, idUser, message);
    }

    /**
     * reply to all users of a message
     */
    public void replyAllMessage(){
        Scanner scn= new Scanner(System.in);

        System.out.print("Message (id): ");
        Long idMessage = scn.nextLong();
        scn.nextLine();

        System.out.print("From (id): ");
        Long idUser = scn.nextLong();
        scn.nextLine();

        System.out.print("Message: ");
        String message = scn.nextLine();

        messageService.replyAll(idMessage, idUser, message);
    }

    /**
     * show the conversation between 2 users
     */
    public void showConversation(){
        Scanner scn= new Scanner(System.in);

        System.out.print("Conversation between (id): ");
        Long id1 = scn.nextLong();
        scn.nextLine();

        System.out.print("And (id): ");
        Long id2 = scn.nextLong();
        scn.nextLine();

        System.out.println("Convesation: ");
        messageService.showConversation(id1, id2).forEach(System.out::println);
    }

    /**
     * send a friend request
     */
    private void addFriendRequest() {
        Scanner scn= new Scanner(System.in);

        System.out.print("From (id): ");
        Long idFrom = scn.nextLong();
        scn.nextLine();

        System.out.print("To (id): ");
        Long idTo = scn.nextLong();
        scn.nextLine();

        friendRequestService.addFriendRequest(idFrom, idTo);
        System.out.println("The friend request was sent");
    }

    /**
     * accept/refuse a friend request
     * @throws IOException Exception
     */
    public void changeStatus() throws IOException{
        Scanner scn= new Scanner(System.in);

        System.out.print("Friend request (id): ");
        Long id = scn.nextLong();
        scn.nextLine();

        System.out.print("Do you accept? ");
        String ss = scn.nextLine();
        //if (ss.equals("yes"))
        friendRequestService.changeStatus(id, ss);
        System.out.println("The status is changed!");
    }

    /**
     * print all the friend requests
     */
    public void printFriendRequest(){
        System.out.println("Friend requests:");
        friendRequestService.getAllFriendRequest().forEach(System.out::println);
    }

    /**
     * print the menu
     */
    public void showMenu(){
        System.out.println(" ");
        System.out.println(" ~ Menu ~ ");
//        System.out.println("1. Add user ");
//        System.out.println("2. Delete user ");
//        System.out.println("3. Print all users");
//        System.out.println("4. Add friendship");
//        System.out.println("5. Delete friendship");
//        System.out.println("6. Print all friendships");
//        System.out.println("7. Print the number of communities");
//        System.out.println("8. Print the most sociable community");
        System.out.println("9. Print all friends of a user");
        System.out.println("10. Print all friends of a user from a given month");
        System.out.println("11. Add a message");
        System.out.println("12. Print all messages");
        System.out.println("13. Reply to a message");
        System.out.println("14. Reply to all messages");
        System.out.println("15. Print the conversation of two users");
        System.out.println("16. Add friend request");
        System.out.println("17. Print all friend requests");
        System.out.println("18. Change the status of a friend request");
        System.out.println("0. Exit");
        System.out.println("Command:");
    }

    /**
     * read the command
     * @return int-the command
     */
    public int readCommand(){
        Scanner comm = new Scanner(System.in);
        return comm.nextInt();
    }

    public void run() throws IOException {
        if(userService == null)
            throw new NullPointerException("UserService null!");
        if(friendshipService == null)
            throw new NullPointerException("FriendshipService null!");
        while (true){
            showMenu();
             try {
                int cmd = readCommand();
                if (cmd == 0)
                    System.exit(0);
                else if (cmd == 1)
                    addUser();
                else if (cmd == 2)
                    deleteUser();
                else if (cmd == 3)
                    printAllUsers();
                else if (cmd == 4)
                    addFriendship();
                else if (cmd == 5)
                    deleteFriendship();
                else if (cmd == 6)
                    printAllFriendships();
                else if (cmd == 7)
                    numberOfCommunities();
                else if (cmd == 8)
                    theMostSociableCommunity();
                else if (cmd == 9)
                    showFriendsOfAUser();
                else if (cmd == 10)
                    showFriendsOfAUserbyMonth();
                else if (cmd == 11)
                    addMessage();
                else if (cmd == 12)
                    printAllMessages();
                else if (cmd == 13)
                    replyMessage();
                else if (cmd == 14)
                    replyAllMessage();
                else if (cmd == 15)
                    showConversation();
                else if (cmd == 16)
                    addFriendRequest();
                else if (cmd == 17)
                    printFriendRequest();
                else if (cmd == 18)
                    changeStatus();
                else
                    System.out.println("Invalid command");
            }catch (ValidationException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
