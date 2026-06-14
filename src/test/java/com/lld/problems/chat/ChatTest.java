package com.lld.problems.chat;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.chat.model.ChatMessageEvent;
import com.lld.problems.chat.model.ChatRoom;
import com.lld.problems.chat.model.PresenceEvent;
import com.lld.problems.chat.model.PresenceType;
import com.lld.problems.chat.service.ChatService;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatTest {

    private ChatService chat;
    private ChatRoom room;
    private final List<ChatMessageEvent> messages = new CopyOnWriteArrayList<>();
    private final List<PresenceEvent> presence = new CopyOnWriteArrayList<>();

    @BeforeEach
    void setUp() {
        InMemoryEventBus bus = new InMemoryEventBus();
        chat = new ChatService(bus);
        room = chat.createRoom("general");
        bus.subscribe(ChatRoom.messageTopic(room.getRoomId()), (ChatMessageEvent e) -> messages.add(e));
        bus.subscribe(ChatRoom.presenceTopic(room.getRoomId()), (PresenceEvent e) -> presence.add(e));
    }

    @Test
    void subscribersReceiveChatMessages() {
        assertTrue(chat.joinRoom(room.getRoomId(), "alice").isSuccess());
        chat.sendMessage(room.getRoomId(), "alice", "hi").getValue().orElseThrow();

        assertEquals(1, messages.size());
        assertEquals("hi", messages.get(0).getMessage().getText());
        assertEquals(1, chat.getHistory(room.getRoomId()).size());
    }

    @Test
    void subscribersReceivePresenceEvents() {
        assertTrue(chat.joinRoom(room.getRoomId(), "bob").isSuccess());
        assertTrue(chat.leaveRoom(room.getRoomId(), "bob").isSuccess());

        assertEquals(2, presence.size());
        assertEquals(PresenceType.JOINED, presence.get(0).getType());
        assertEquals(PresenceType.LEFT, presence.get(1).getType());
    }

    @Test
    void rejectMessageFromNonMember() {
        assertTrue(chat.sendMessage(room.getRoomId(), "stranger", "hi").getError().isPresent());
        assertTrue(messages.isEmpty());
    }
}
