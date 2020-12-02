package socialnetwork.domain.validators;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Message;

public class FriendRequestValidator implements Validator<FriendRequest>{

    /**
     * validates a friend request
     * @param entity the friend request
     * @throws ValidationException
     */
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        if (entity.getFrom()==entity.getTo()) {
            throw new ValidationException("From id must be different from to id");
        }
    }
}
