package com.lld.problems.chat.model;

import com.lld.common.events.Event;

public class ChatMessageEvent extends Event {

    private final Message message;

    public ChatMessageEvent(Message message) {
        super(ChatRoom.messageTopic(message.getRoomId()));
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
