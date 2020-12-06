package socialnetwork.service;

import socialnetwork.domain.Message;
import socialnetwork.domain.ReplyMessage;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.*;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessageService implements Observable {

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
        notifyObservers(new MessageChangeEvent(ChangeMessageEventType.SEND, rez));

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
        Message rez=repoMessage.save(reply);
        notifyObservers(new MessageChangeEvent(ChangeMessageEventType.SEND, rez));
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
        notifyObservers(new MessageChangeEvent(ChangeMessageEventType.SEND, rez));
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

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {
        observers.stream().forEach(x->x.update(t));
    }

    public List<Message> getNotificationMessage(Long id) {
        User user = repoUser.findOne(id);
        Iterable<Message> allMessages = getAllMessages();
        List<Message> all = new ArrayList<>();
        List<Message> rez = new ArrayList<>();
        allMessages.forEach(message -> {
            all.add(message);
        });
        all.sort(Comparator.comparing(Message::getData));

        Boolean ok=false;
        for(Message message:all)
            if (message.getFrom() == user)
                ok = true;

        if(ok) {
            LocalDateTime date = null;
            for (Message message : all) { //salvez data la care el trimite ultimul mesaj
                if (message.getFrom() == user) {
                    date = message.getData();
                    //rez.add(message);
                }
            }
            System.out.println(date);
            //salvez mesajele care sunt trimise dupa data la care a trimis userul
            //user ultimul mesaj pt ca acelea sunt mesajele pe care nu le-a citi
            for (Message message : all) {
                for (User userTo : message.getTo())
                    if (userTo == user && message.getData().isAfter(date)) {
                        rez.add(message);
                    }
            }
        }else{
            for (Message message : all) {
                for (User userTo : message.getTo())
                    if (userTo == user) {
                        rez.add(message);
                    }
            }
        }

        return rez;
    }
}
