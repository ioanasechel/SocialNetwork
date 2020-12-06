package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private List<User> friends;

    /**
     * constructor
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     */
    public User(String firstName, String lastName, String username,String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        friends = new ArrayList<User>();
    }

    /**
     * return the first name of an user
     * @return firstName String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * return the last name of a user
     * @return lastName String
     */
    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

//    public String setPassword(String password) {
//        String pass= DigestUtils.md5Hex(password);
//        return pass;
//    }

    public String getStringUser(){
        return "\t" + firstName + " " + lastName;
    }

    /**
     * @return the friends list
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     * set the friends list for a user
     * @param list List of Users
     */
    public void setFriends(List<User> list) {
        this.friends=list;
    }

    /**
     * add a user in friends list
     * @param user the user
     */
    public void addFriend(User user) {
        friends.add(user);
    }

    /**
     * return list of friends
     */
    private String printFriends(){
        StringBuilder rez= new StringBuilder();
        friends.forEach(user->{
            rez.append(user.getFirstName()+" "+user.getLastName()+", ");
        });
        return rez.toString();
    }

    /**
     * print
     * @return a string with all users with their friends
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + ", " +
                "friends=" + printFriends();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}