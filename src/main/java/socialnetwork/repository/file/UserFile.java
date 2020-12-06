package socialnetwork.repository.file;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.util.List;

public class UserFile extends AbstractFileRepository<Long, User>{

    public UserFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
        loadData();
    }

    /**
     * function that extracts a line from the file and turns it into an entity
     * @param attributes List of Strings
     * @return the entity
     */
    @Override
    public User extractEntity(List<String> attributes) {
        //TODO: implement method
        User user = new User(attributes.get(1),attributes.get(2),
                attributes.get(3),attributes.get(4));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    /**
     * function that writes an entity to the file
     * @param entity the entity
     * @return a string
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName()
                +";"+entity.getUsername()+";"+entity.getPassword();
    }

}
