package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;

public class FriendsValidator implements Validator<Friendship>{

    /**
     * validates a friendship
     * @param entity a friendship
     * @throws ValidationException
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        Tuple<Long,Long> elem = entity.getId();
        if(elem.getRight() == elem.getLeft())
            throw new ValidationException("From id must be different from to id");
    }
}
