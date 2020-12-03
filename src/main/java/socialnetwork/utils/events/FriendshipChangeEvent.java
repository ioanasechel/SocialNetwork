package socialnetwork.utils.events;

import socialnetwork.domain.Friendship;

public class FriendshipChangeEvent implements Event {
    private ChangeFriendshipEventType type;
    private Friendship data;

    public FriendshipChangeEvent(ChangeFriendshipEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }

    public ChangeFriendshipEventType getType() {
        return type;
    }

}
