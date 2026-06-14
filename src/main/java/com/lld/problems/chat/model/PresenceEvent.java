package com.lld.problems.chat.model;

import com.lld.common.events.Event;

public class PresenceEvent extends Event {

    private final String roomId;
    private final String userId;
    private final PresenceType type;

    public PresenceEvent(String roomId, String userId, PresenceType type) {
        super(ChatRoom.presenceTopic(roomId));
        this.roomId = roomId;
        this.userId = userId;
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getUserId() {
        return userId;
    }

    public PresenceType getType() {
        return type;
    }
}
