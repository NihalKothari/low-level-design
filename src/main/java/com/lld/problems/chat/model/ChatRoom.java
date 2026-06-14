package com.lld.problems.chat.model;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatRoom {

    private final String roomId;
    private final String name;
    private final Set<String> members = new CopyOnWriteArraySet<>();

    public ChatRoom(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public ChatRoom(String roomId, String name) {
        this.roomId = Objects.requireNonNull(roomId, "roomId");
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public boolean addMember(String userId) {
        return members.add(userId);
    }

    public boolean removeMember(String userId) {
        return members.remove(userId);
    }

    public Set<String> getMembers() {
        return Set.copyOf(members);
    }

    public boolean isMember(String userId) {
        return members.contains(userId);
    }

    public static String messageTopic(String roomId) {
        return "chat.room." + roomId;
    }

    public static String presenceTopic(String roomId) {
        return "chat.presence." + roomId;
    }
}
