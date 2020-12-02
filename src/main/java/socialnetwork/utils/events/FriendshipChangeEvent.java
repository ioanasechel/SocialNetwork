package socialnetwork.utils.events;

import socialnetwork.domain.Friendship;

public class FriendshipChangeEvent implements Event {
    private ChangeFriendshipEventType type;
    private Friendship data, oldData;

    public FriendshipChangeEvent(ChangeFriendshipEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }

    public FriendshipChangeEvent(ChangeFriendshipEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeFriendshipEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}
