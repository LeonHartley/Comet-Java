package com.cometproject.api.networking.messages;

public interface IMessageEventHandler {
    void handle(IMessageEvent eventData) throws Exception;
}
