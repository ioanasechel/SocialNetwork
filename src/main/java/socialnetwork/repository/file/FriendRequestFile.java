package socialnetwork.repository.file;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class FriendRequestFile extends AbstractFileRepository<Long, FriendRequest>{

    private Repository<Long, User> repoUser;

    /**
     * constructor
     * @param fileName the file name
     * @param validator the validator
     * @param repoUser the repository for the users
     */
    public FriendRequestFile(String fileName, Validator<FriendRequest> validator,
                             Repository<Long, User> repoUser) {
        super(fileName, validator);
        this.repoUser = repoUser;
        loadData();
    }

    /**
     * function that extracts a line from the file and turns it into an entity
     * @param attributes List of Strings
     * @return the entity
     */
    @Override
    public FriendRequest extractEntity(List<String> attributes) {

        Long id1=Long.parseLong(attributes.get(1));
        User user1=repoUser.findOne(id1);
        Long id2=Long.parseLong(attributes.get(2));
        User user2=repoUser.findOne(id2);
        String status=attributes.get(3);
        LocalDateTime date = LocalDateTime.parse(attributes.get(4));
        FriendRequest friendRequest=new FriendRequest(user1, user2, status, date);
        friendRequest.setId(Long.parseLong(attributes.get(0)));

        return friendRequest;
    }

    /**
     * function that writes an entity to the file
     * @param entity the entity
     * @return a string
     */
    @Override
    protected String createEntityAsString(FriendRequest entity) {
        return entity.getId()+";"+entity.getFrom().getId()+";"
                +entity.getTo().getId()+";"+entity.getStatus()+";"+entity.getDate();
    }
}
