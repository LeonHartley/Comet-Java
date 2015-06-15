package com.cometproject.server.network.websocket.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatSessionStore {
    private static ChatSessionStore instance;

    private Map<UUID, ChatSession> chatSessions;

    public ChatSessionStore() {
        this.chatSessions = new HashMap<>();
    }

    public ChatSession store(UUID uuid, ChatSession chatSession) {
        this.chatSessions.put(uuid, chatSession);

        return chatSession;
    }

    public void remove(UUID sessionId) {
        this.chatSessions.remove(sessionId);
    }

    public ChatSession getChatSession(UUID uuid) {
        return this.chatSessions.get(uuid);
    }

    public Map<UUID, ChatSession> getChatSessions() {
        return this.chatSessions;
    }

    public static ChatSessionStore getInstance() {
        if (instance == null) {
            instance = new ChatSessionStore();
        }

        return instance;
    }
}
