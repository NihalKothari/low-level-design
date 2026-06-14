package com.lld.problems.chat.demo;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.chat.model.ChatMessageEvent;
import com.lld.problems.chat.model.ChatRoom;
import com.lld.problems.chat.model.PresenceEvent;
import com.lld.problems.chat.service.ChatService;

public class ChatDemo {

    public static void main(String[] args) {
        InMemoryEventBus bus = new InMemoryEventBus();
        ChatService chat = new ChatService(bus);

        ChatRoom general = chat.createRoom("general");
        if (!chat.joinRoom(general.getRoomId(), "alice").isSuccess()
                || !chat.joinRoom(general.getRoomId(), "bob").isSuccess()) {
            throw new IllegalStateException("Failed to join room");
        }

        bus.subscribe(ChatRoom.messageTopic(general.getRoomId()), (ChatMessageEvent e) ->
                System.out.println(e.getMessage().getSenderId() + ": " + e.getMessage().getText()));
        bus.subscribe(ChatRoom.presenceTopic(general.getRoomId()), (PresenceEvent e) ->
                System.out.println("Presence: " + e.getUserId() + " " + e.getType()));

        chat.sendMessage(general.getRoomId(), "alice", "Hello everyone!").getValue().orElseThrow();
    }
}
