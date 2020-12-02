package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long>{
    protected User from;
    protected List<User> to;
    protected String message;
    protected LocalDateTime data;

    /**
     * constructor
     * @param from the user who sent the friend request
     * @param to the users who received the friend request
     * @param message the message itself
     * @param data the date the message was sent
     */
    public Message(User from, List<User> to, String message, LocalDateTime data) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
    }


    /**
     * return the user who sent the friend request
     * @return a user
     */
    public User getFrom() {
        return from;
    }

    /**
     * return the users who received the friend request
     * @return a list of users
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * return the message itself
     * @return a string
     */
    public String getMessage() {
        return message;
    }

    /**
     * return the date the message was sent
     * @return LocalDateTime
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     *form a string with the list of users who received the message
     * @return the string
     */
    private String printListTo(){
        StringBuilder rez= new StringBuilder();
        to.forEach(user->{
            rez.append(user.getId()+" ");
        });
        return rez.toString();
    }

    /**
     * print
     * @return a string with all users with their friends
     */
    @Override
    public String toString() {
        return from.getId() +
                " sent to " + printListTo()
                + " the message '" + message
                + "' on " + data;
    }
}
