package socialnetwork.utils.events;

import socialnetwork.domain.FriendRequest;

public class FriendRequestChangeEvent implements Event {
    private ChangeFriendRequestEventType type;
    private FriendRequest data;

    public FriendRequestChangeEvent(ChangeFriendRequestEventType type, FriendRequest data) {
        this.type = type;
        this.data = data;
    }

    public ChangeFriendRequestEventType getType() {
        return type;
    }

}
