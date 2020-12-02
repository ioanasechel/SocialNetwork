package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message{

    private Message messageString;

    /**
     * constructor
     * @param from the user who sent the friend request
     * @param to the users who received the friend request
     * @param replyString the message for reply
     * @param dataReply the date the message was sent
     */
    public ReplyMessage(Message message,User from, List<User> to,
                        String replyString, LocalDateTime dataReply) {
        super( from, to, replyString, dataReply);
        this.messageString =message;
    }
}
