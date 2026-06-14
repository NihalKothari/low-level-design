package com.lld.problems.chat.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.common.events.EventBus;
import com.lld.problems.chat.model.ChatMessageEvent;
import com.lld.problems.chat.model.ChatRoom;
import com.lld.problems.chat.model.Message;
import com.lld.problems.chat.model.PresenceEvent;
import com.lld.problems.chat.model.PresenceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {

    private final EventBus eventBus;
    private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();
    private final Map<String, List<Message>> history = new ConcurrentHashMap<>();

    public ChatService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public ChatRoom createRoom(String name) {
        ChatRoom room = new ChatRoom(name);
        rooms.put(room.getRoomId(), room);
        history.put(room.getRoomId(), new ArrayList<>());
        return room;
    }

    public Result<Void> joinRoom(String roomId, String userId) {
        ChatRoom room = rooms.get(roomId);
        if (room == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        room.addMember(userId);
        eventBus.publish(new PresenceEvent(roomId, userId, PresenceType.JOINED));
        return Result.success(null);
    }

    public Result<Void> leaveRoom(String roomId, String userId) {
        ChatRoom room = rooms.get(roomId);
        if (room == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        room.removeMember(userId);
        eventBus.publish(new PresenceEvent(roomId, userId, PresenceType.LEFT));
        return Result.success(null);
    }

    public Result<Message> sendMessage(String roomId, String senderId, String text) {
        ChatRoom room = rooms.get(roomId);
        if (room == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!room.isMember(senderId)) {
            return Result.failure(ErrorCode.UNAUTHORIZED);
        }
        Message message = new Message(roomId, senderId, text);
        history.computeIfAbsent(roomId, k -> new ArrayList<>()).add(message);
        eventBus.publish(new ChatMessageEvent(message));
        return Result.success(message);
    }

    public List<Message> getHistory(String roomId) {
        return List.copyOf(history.getOrDefault(roomId, List.of()));
    }

    public ChatRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }
}
