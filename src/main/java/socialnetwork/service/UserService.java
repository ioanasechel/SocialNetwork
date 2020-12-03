package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private Repository<Long, User> repoUser;
    private Repository<Tuple<Long,Long>, Friendship> repofriends;

    /**
     * constructor
     * @param repoUser the repository of the users
     * @param repofriends the repository of the friends
     */
    public UserService(Repository<Long, User> repoUser, Repository<Tuple<Long,Long>, Friendship> repofriends) {
        this.repoUser = repoUser;
        this.repofriends = repofriends;
        repofriends.findAll().forEach(this::load_friends);
    }

    /**
     * load all the friendships from the file
     * @param e a friendship
     */
    public void load_friends(Friendship e) {
        Tuple<Long, Long> id = e.getId();
        Long id1 = id.getLeft();
        Long id2 = id.getRight();
        User user1 = repoUser.findOne(id1);
        User user2 = repoUser.findOne(id2);
        user1.addFriend(user2);
        user2.addFriend(user1);
    }


    /**
     * add a user
     * @param firstName the user first name
     * @param lastName the user second name
     * @return user User
     */
    public User addUser(String firstName, String lastName) {
        User user=new User(firstName, lastName);
        user.setId(generateUserId());
        User rez = repoUser.save(user);
        return rez;
    }

    /**
     * delete a user
     * @param id the id of the user to be erased
     * @return user User
     * @throws IOException Exception
     */
    public User deleteUser(Long id) throws IOException {
        User user1=repoUser.findOne(id);
        List<User> friends = user1.getFriends();
        friends.forEach(user -> {
            Tuple<Long, Long> friendshipId = new Tuple<>(id, user.getId());
            try {
                Friendship prie2 = repofriends.delete(friendshipId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<User> elems =new ArrayList<>();
            for(User ele:user.getFriends())
            {
                if(ele.getId()!=id){
                    elems.add(ele);
                }

            }
            user.setFriends(elems);
        });
        return repoUser.delete(id);
    }

    /**
     * get all users
     * @return list of users
     */
    public Iterable<User> getAllUsers(){
        return repoUser.findAll();
    }

    /**
     * return the list of friends for a user
     * @param id the user id
     * @return the list of friends
     */
    public List<User> getFriends(Long id){
        User user =repoUser.findOne(id);
        return user.getFriends();
    }

    /**
     * generate a new id
     * @return the new id
     */
    private Long generateUserId(){
        for(int i = 1; i <= repoUser.count(); i++){
            if(repoUser.findOne((long) i)==null)
                return (long) i;
        }
        return (long) repoUser.count() + 1;
    }

    public Boolean exist(Long id) {
        return repoUser.findOne(id) != null;
    }

    public User getOne(Long id) {
        return repoUser.findOne(id);

    }

    public User getUser(String firstName, String lastName) {
        Iterable<User> users=getAllUsers();
        List<User> all=new ArrayList<>();
        users.forEach(user1 -> {all.add(user1);});
        List<User> rez=all.stream().
                filter(user->user.getFirstName().equals(firstName) && user.getLastName().equals(lastName))
                .collect(Collectors.toList());
        User user=rez.get(rez.size()-1);
        return user;
    }
}

