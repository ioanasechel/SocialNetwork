package socialnetwork.repository.file;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MessageFile extends AbstractFileRepository<Long, Message>{

    private Repository<Long, User> repoUser;

    /**
     * constructor
     * @param fileName the file name
     * @param validator the validator
     * @param repoUser the repository for users
     */
    public MessageFile(String fileName, Validator<Message> validator,
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
    public Message extractEntity(List<String> attributes) {

        Long userFromId=Long.parseLong(attributes.get(1));
        User userFrom=repoUser.findOne(userFromId);
        String stringTo=attributes.get(2);
        String[] listTo=stringTo.split(" ");
        List<User> usersTo=new ArrayList<>();
        for(int i=0; i<listTo.length; i++){
            User user=repoUser.findOne(Long.parseLong(listTo[i]));
            usersTo.add(user);
        }
        String message=attributes.get(3);
        LocalDateTime data=LocalDateTime.parse(attributes.get(4));
        Message elem=new Message(userFrom, usersTo, message, data);
        elem.setId(Long.parseLong(attributes.get(0)));
        return elem;
    }

    /**
     * function that writes an entity to the file
     * @param entity the entity
     * @return a string
     */
    @Override
    public String createEntityAsString(Message entity) {
        List<User> listTo=entity.getTo();
        AtomicReference<String> rez= new AtomicReference<>("");
        listTo.forEach(user -> {
            rez.updateAndGet(v -> v + user.getId()+" ");
        });
        return entity.getId()+";"+entity.getFrom().getId()+";"+rez+
                ";"+entity.getMessage()+";"+entity.getData();
    }
}
