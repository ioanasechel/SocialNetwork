package socialnetwork.service;

import socialnetwork.domain.Message;
import socialnetwork.domain.ReplyMessage;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessageService {

    private Repository<Long, Message> repoMessage;
    private Repository<Long, User> repoUser;

    /**
     * constructor
     * @param repoMessage the repository for messages
     * @param repoUser the repository for users
     */
    public MessageService(Repository<Long, Message> repoMessage, Repository<Long, User> repoUser) {
        this.repoMessage = repoMessage;
        this.repoUser = repoUser;
    }

    /**
     * add a message
     * @param userFromId the id of the user who sent the message
     * @param usersToID the id of the user who received the message
     * @param messageString the message itself
     * @return the message (entity)
     */
    public Message sendMessage(Long userFromId, String usersToID, String messageString){
        User userFrom =repoUser.findOne(userFromId);
        String[] listTo=usersToID.split(" ");
        List<User> usersTo=new ArrayList<>();
        for(int i=0; i<listTo.length; i++){
            User user=repoUser.findOne(Long.parseLong(listTo[i]));
            usersTo.add(user);
        }
        Message message=new Message(userFrom, usersTo, messageString, LocalDateTime.now());
        message.setId(generateUserId());
        Message rez=repoMessage.save(message);
        return rez;
    }

    /**
     * add a reply for a message
     * @param id the id of the message to be replied
     * @param idUser the us of the user who replied
     * @param messageString the message itself
     * @return the message (entity)
     */
    public Message replyMessage(Long id, Long idUser, String messageString){
        Message toAnswer=repoMessage.findOne(id);
        User user=repoUser.findOne(idUser);
        List<User> to=new ArrayList<>();
        to.add(toAnswer.getFrom());
        ReplyMessage reply=new ReplyMessage(toAnswer,user, to, messageString, LocalDateTime.now());
        reply.setId(generateUserId());
        //System.out.println(reply.toString());
        Message rez=repoMessage.save(reply);
        return rez;
    }

    /**
     * add a reply for a message
     * @param id the id of the message to be replied
     * @param idUser the us of the user who replied
     * @param messageString the message itself
     * @return the message (entity)
     */
    public Message replyAll(Long id, Long idUser, String messageString){
        Message toAnswer=repoMessage.findOne(id);
        User user=repoUser.findOne(idUser);
        List<User> to=new ArrayList<>();
        to.add(toAnswer.getFrom());
        toAnswer.getTo().forEach(user1->{
            if (!user1.equals(user))
                to.add(user1);
        });
        ReplyMessage reply=new ReplyMessage(toAnswer,user, to, messageString, LocalDateTime.now());
        reply.setId(generateUserId());
        Message rez=repoMessage.save(reply);
        return rez;
    }

    /**
     * get all the messages
     * @return the list of messages
     */
    public Iterable<Message> getAllMessages(){
        return repoMessage.findAll();
    }


    /**
     * return the conversation between two users
     * @param id1 the id of the first user
     * @param id2 the id of the second user
     * @return the list with the messages between id1 and id2
     */
    public List<Message> showConversation(Long id1, Long id2){
        User user1=repoUser.findOne(id1);
        User user2=repoUser.findOne(id2);
        Iterable<Message> messageList= repoMessage.findAll();
        List<Message> rez=new ArrayList<>();

        for(Message message:messageList)
            if(message.getFrom()==user1){
                List<User> users = message.getTo();
                for(User user:users)
                    if (user==user2)
                        rez.add(message);
            }
            else if (message.getFrom()==user2){
                List<User> users = message.getTo();
                for(User user:users)
                    if (user==user1)
                        rez.add(message);
            }
            rez.sort(Comparator.comparing(Message::getData));
        return rez;
    }

    /**
     * generate an id
     * @return int-the id
     */
    private Long generateUserId(){
        for(int i = 1; i <= repoMessage.count(); i++){
            if(repoMessage.findOne((long) i)==null)
                return (long) i;
        }
        return (long) repoMessage.count() + 1;
    }
}
