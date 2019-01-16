package com.cometproject.server.network.ws.handlers;

import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.network.sessions.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

public abstract class AbstractWsHandler<T> implements WsHandler {
    protected static final AttributeKey<Session> SESSION = AttributeKey.newInstance("Session");

    private static final Logger log = Logger.getLogger(AbstractWsHandler.class);

    private final Class<? extends T> messageClass;

    AbstractWsHandler(Class<? extends T> messageType) {
        this.messageClass = messageType;
    }

    public void handle(String data, ChannelHandlerContext ctx) {
        try {
            final T obj = JsonUtil.getInstance().fromJson(data, this.messageClass);
            this.onMessage(obj, ctx);
        } catch (Exception e) {
            log.error("Failed to read message from ws", e);
        }
    }

    protected abstract void onMessage(T message, ChannelHandlerContext ctx);
}
