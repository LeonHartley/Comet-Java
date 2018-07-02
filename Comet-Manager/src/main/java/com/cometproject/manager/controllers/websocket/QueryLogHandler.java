package com.cometproject.manager.controllers.websocket;

import io.netty.util.internal.ConcurrentSet;
import org.springframework.core.annotation.Order;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;

public class QueryLogHandler extends TextWebSocketHandler {

    public static Set<WebSocketSession> listeners = new ConcurrentSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        listeners.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        listeners.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

    }
}
