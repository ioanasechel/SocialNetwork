package socialnetwork.repository.file;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class FriendsFile extends AbstractFileRepository<Tuple<Long,Long>, Friendship> {

    public FriendsFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
        loadData();
    }

    /**
     * function that extracts a line from the file and turns it into an entity
     * @param attributes List of Strings
     * @return the entity
     */
    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship elem = new Friendship(LocalDateTime.parse(attributes.get(2)));
        Tuple<Long,Long> id = new Tuple<>(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1)));
        elem.setId(id);
        return elem;

    }

    /**
     * function that writes an entity to the file
     * @param entity the entity
     * @return a string
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"+entity.getDate();
    }


}
