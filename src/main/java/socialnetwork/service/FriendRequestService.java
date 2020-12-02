package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.AbsentObjectException;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.ChangeFriendRequestEventType;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.events.FriendRequestChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestService implements Observable {

    private Repository<Long, FriendRequest> repoFriendReguest;
    private Repository<Long, User> repoUser;
    private FriendshipService serviceFriends;

    /**
     * constructor
     * @param repoFriendRequest the repository for friend requests
     * @param repoUser the repository for users
     * @param serviceFriends the service for friends
     */
    public FriendRequestService(Repository<Long, FriendRequest> repoFriendRequest,
                                Repository<Long, User> repoUser,
                                FriendshipService serviceFriends) {
        this.repoFriendReguest = repoFriendRequest;
        this.repoUser = repoUser;
        this.serviceFriends = serviceFriends;
    }

    /**
     * add a friend request
     * @param idFrom the user who send the friend request
     * @param idTo the user who receive the friend request
     * @return the friend request
     */
    public FriendRequest addFriendRequest(Long idFrom, Long idTo){
        User userFrom=repoUser.findOne(idFrom);
        User userTo=repoUser.findOne(idTo);
        FriendRequest friendRequest=new FriendRequest(userFrom, userTo, "pending", LocalDateTime.now());
        friendRequest.setId(generateUserId());
        FriendRequest saved=repoFriendReguest.save(friendRequest);
        notifyObservers(new FriendRequestChangeEvent(ChangeFriendRequestEventType.SEND, saved));
        return saved;
    }

    /**
     * change the status of a friend request
     * @param id the friend request id
     * @param status the friend request id
     * @return the friend request
     * @throws IOException if status is different than 'pending'
     */
    public FriendRequest changeStatus(Long id, String status) throws IOException {
        FriendRequest fr=repoFriendReguest.findOne(id);
        if (!fr.getStatus().equals("pending"))
            throw new ValidationException("This friend request has been accepted or deleted");
        User userFrom=fr.getFrom();
        User userTo=fr.getTo();
        try{
            FriendRequest removed=repoFriendReguest.delete(id);
        }
        catch (IOException e) {
        e.printStackTrace();
        }

        if (status.equals("yes")){
            FriendRequest friendRequest=new FriendRequest(userFrom, userTo, "approved", LocalDateTime.now());
            serviceFriends.addFriendship(userFrom.getId(), userTo.getId());
            friendRequest.setId(id);
            FriendRequest saved=repoFriendReguest.save(friendRequest);
            notifyObservers(new FriendRequestChangeEvent(ChangeFriendRequestEventType.ACCEPT, saved));
            return saved;
        }
        else {
            FriendRequest friendRequest = new FriendRequest(userFrom, userTo, "rejected", LocalDateTime.now());
            friendRequest.setId(id);
            FriendRequest rejected=repoFriendReguest.save(friendRequest);
            notifyObservers(new FriendRequestChangeEvent(ChangeFriendRequestEventType.REJECT, rejected));
            return rejected;
        }

    }

    /**
     * get all the friend requests
     * @return the list of friend requests
     */
    public Iterable<FriendRequest> getAllFriendRequest(){
        return repoFriendReguest.findAll();
    }

    /**
     * generate an id for a user
     * @return the first free position in the repo
     */
    private Long generateUserId(){
        for(int i = 1; i <= repoFriendReguest.count(); i++){
            if(repoFriendReguest.findOne((long) i)==null)
                return (long) i;
        }
        return (long) repoFriendReguest.count() + 1;
    }

    private List<Observer> observers = new ArrayList<Observer>();

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

    public List<FriendRequest> getAllFriendRequestsOfAnUser(Long id) {
//        List<FriendRequest> all= (List<FriendRequest>) getAllFriendRequest();
//        List<FriendRequest> rez=new ArrayList<>();
//        for(FriendRequest fr:all){
//            if (fr.getFrom().getId()==id)
//                rez.add(fr);
//        }
//        return rez;
        User user = repoUser.findOne(id);
//        if (user == null)
//            throw new AbsentObjectException("This user does not exist!\n");

        /*List<FriendRequest> friendRequestList = new ArrayList<FriendRequest>();
        for (FriendRequest fr : friendRequestRepository.findAll()) {
            if (fr.getTo() == user && fr.getStatus() == "pending")
                friendRequestList.add(fr);
        }
        return friendRequestList;*/

        return StreamSupport.stream(repoFriendReguest.findAll().spliterator(), false)
                .filter(fr -> fr.getTo() == user)
                .filter(fr -> fr.getStatus().equals("pending"))
                .collect(Collectors.toList());

    }
}
