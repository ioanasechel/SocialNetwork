package socialnetwork.domain;

import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class FriendRequest extends Entity<Long>{

    private User from;
    private User to;
    private String status;
    private LocalDateTime date;

    /**
     * constructor
     * @param from the user who sent the friend request
     * @param to the user who received the friend request
     * @param status the status of a friend request
     */
    public FriendRequest(User from, User to, String status, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    /**
     * return the user who sent the friend request
     * @return a user
     */
    public User getFrom() {
        return from;
    }

    public String getStringFrom() {
        return from.getFirstName() + " " + from.getLastName();
    }

    public String getStringTo() {
        return to.getFirstName() + " " + to.getLastName();
    }

    public String getStringDate() {
        return date.format(Constants.DATE_TIME_FORMATTER);
    }

    public String getStringReceive(){
        return from.getFirstName() + " " + from.getLastName()+
                " sent you a friend request on " +
                date.format(Constants.DATE_TIME_FORMATTER);
    }

    /**
     * return the user who received the friend request
     * @return a user
     */
    public User getTo() {
        return to;
    }

    /**
     * return the status of a friend request
     * @return a string
     */
    public String getStatus() {
        return status;
    }


    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Friend request from " + from.getId() +
                " to " + to.getId() +
                " is " + status;
    }
}
