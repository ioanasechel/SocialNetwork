package socialnetwork.utils.events;

import socialnetwork.domain.FriendRequest;

public class FriendRequestChangeEvent implements Event {
    private ChangeFriendRequestEventType type;
    private FriendRequest data, oldData;

    public FriendRequestChangeEvent(ChangeFriendRequestEventType type, FriendRequest data) {
        this.type = type;
        this.data = data;
    }

    public FriendRequestChangeEvent(ChangeFriendRequestEventType type, FriendRequest data, FriendRequest oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeFriendRequestEventType getType() {
        return type;
    }

    public FriendRequest getData() {
        return data;
    }

    public FriendRequest getOldData() {
        return oldData;
    }
}
