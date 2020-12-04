package socialnetwork.utils.events;

import socialnetwork.domain.Message;

public class MessageChangeEvent implements Event {
    private ChangeMessageEventType type;
    private Message data;

    public MessageChangeEvent(ChangeMessageEventType type, Message data) {
        this.type = type;
        this.data = data;
    }

    public ChangeMessageEventType getType() {
        return type;
    }

}