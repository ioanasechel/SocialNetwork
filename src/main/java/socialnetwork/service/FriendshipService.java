package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.ChangeFriendshipEventType;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FriendshipService implements Observable {
    private Repository<Long, User> repoUser;
    private Repository<Tuple<Long,Long>, Friendship> repofriends;

    /**
     * constructor
     * @param repoUser the repository for users
     * @param repofriends the repository for friends
     */
    public FriendshipService(Repository<Long, User> repoUser, Repository<Tuple<Long,Long>, Friendship> repofriends) {
        this.repoUser = repoUser;
        this.repofriends = repofriends;
    }

    /**
     * add a friendship between the user with id1 and the user with id2
     * @param id1 id of the first user
     * @param id2 id of the second user
     * @return their friendship
     */
    public Friendship addFriendship(Long id1, Long id2) {
        User user1 = repoUser.findOne(id1);
        User user2 = repoUser.findOne(id2);
        Friendship friendship = new Friendship(LocalDateTime.now());
        Tuple<Long,Long> friendshipId = new Tuple<>(id1,id2);
        friendship.setId(friendshipId);
        Friendship elem = repofriends.save(friendship);
        user1.addFriend(user2);
        user2.addFriend(user1);
        notifyObservers(new FriendshipChangeEvent(ChangeFriendshipEventType.ADD, elem));
        return elem;
    }

    /**
     * remove friendship between the user with id1 and the user with id2
     * @param id1 id of the first user
     * @param id2 id of the second user
     * @return their friendship
     * @throws IOException Exception
     */
    public Friendship deleteFriendship(Long id1, Long id2) throws IOException {
        Tuple<Long,Long> friendshipId = new Tuple<>(id1,id2);
        removeFriend(id1,id2);
        removeFriend(id2,id1);
        Friendship elem = repofriends.delete(friendshipId);
        if (elem != null) {
            notifyObservers(new FriendshipChangeEvent(ChangeFriendshipEventType.REMOVE, elem));
        }
        return elem;
    }

    /**
     * remove a friend
     * @param id1 id of the first user
     * @param id2 id of the second user
     * @return
     */
    public User removeFriend(Long id1, Long id2) {
        User user = repoUser.findOne(id1);
        List<User> elems =new ArrayList<>();
        for(User ele:user.getFriends())
        {
            if(ele.getId()!=id2){
                elems.add(ele);
            }
        }
        user.setFriends(elems);
        return user;
    }

    /**
     * get all friendships
     * @return the list of friendships
     */
    public Iterable<Friendship> getAllFriendships(){
        return repofriends.findAll();
    }

    public void dfs(User x, Map<Long,Long> vizitat, Map<Long,Long>  distanta, Long time) {
        time++;
        distanta.replace(x.getId(),time);
        vizitat.replace(x.getId(), (long) 1);
        for(User ele:x.getFriends()) {
            if(vizitat.get(ele.getId()) == 0) {
                User user = repoUser.findOne(ele.getId());
                dfs(user,vizitat,distanta,time);
            }
        }
    }

    public Integer numberOfCommunities(){
        Map<Long,Long>  distanta = new HashMap<>();
        Map<Long,Long>  vizitat = new HashMap<>();
        for(User ele: repoUser.findAll())
        {
            distanta.put(ele.getId(), (long) 0);
            vizitat.put(ele.getId(), (long) 0);
        }
        Integer conex = 0;
        for(User ele: repoUser.findAll()) {
            if (vizitat.get(ele.getId()) == 0)
            {
                dfs(ele,vizitat,distanta, (long) 0);
                conex++;
            }
        }
        return conex;
    }

    public String cel_mai_lung_drum(){
        Map<Long,Long>  distanta = new HashMap<>();
        Map<Long,Long>  vizitat = new HashMap<>();
        String friends = "";
        for(User ele: repoUser.findAll())
        {
            distanta.put(ele.getId(), (long) 0);
            vizitat.put(ele.getId(), (long) 0);
        }
        Integer max=0;
        User user1 = null;
        for(User ele: repoUser.findAll()) {
            dfs(ele,vizitat,distanta, (long) 0);
            for(User user: repoUser.findAll()) {
                Long id = user.getId();
                if(distanta.get(id)>max) {
                    max= Math.toIntExact(distanta.get(id));
                    user1 = repoUser.findOne(id);
                }
                vizitat.replace(id, (long) 0);
                distanta.replace(id, (long) 0);
            }
        }
        dfs(user1,vizitat,distanta, (long) 0);
        for(User user: repoUser.findAll()){
            Long id = user.getId();
            if(distanta.get(id)>0) {
                friends+=repoUser.findOne(id).getId()+" "
                        +repoUser.findOne(id).getFirstName()+" "
                        +repoUser.findOne(id).getLastName()+"\n";
            }
        }
        return friends;
    }

    /**
     * return the friendship data
     * @param id1 id of the first user
     * @param id2 id of the second user
     * @return the data of their friendship
     */
    public LocalDateTime getFriendshipData(Long id1, Long id2){
        Tuple<Long,Long> friendshipId = new Tuple<>(id1,id2);
        Friendship friendship = repofriends.findOne(friendshipId);
        return friendship.getDate();
    }

    /**
     * returns all friends of a user created in a given month
     * @param id the user id
     * @param month the month
     * @return the list of users
     */
    public List<User> getFriendsOfAUserbyMonth(Long id, Long month){
        User user=repoUser.findOne(id);
        List<User> friends=user.getFriends();
        List<User> rez= friends.stream().
                filter(user1 -> getFriendshipData(id, user1.getId()).getMonthValue() == month)
                .collect(Collectors.toList());
        return rez;
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
}
