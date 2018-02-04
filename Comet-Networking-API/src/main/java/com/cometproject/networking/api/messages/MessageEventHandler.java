package com.cometproject.networking.api.messages;

import com.cometproject.api.networking.messages.IMessageEvent;
import com.cometproject.api.networking.messages.IMessageEventHandler;
import org.apache.log4j.Logger;

import java.util.function.Consumer;

public abstract class MessageEventHandler<T extends MessageParser> implements IMessageEventHandler {

    private static Logger log = Logger.getLogger(MessageEventHandler.class);

    private final short messageId;
    private final Consumer<T> parserConsumer;

    private T parserTypeField;
    private Class<T> parserType;

    public MessageEventHandler(short messageId, Consumer<T> parserConsumer) {
        this.messageId = messageId;
        this.parserConsumer = parserConsumer;

        try {
            // Naughty as fuck but it works.. lol
            this.parserTypeField = null;
            this.parserType = ((Class<T>)
                    this.getClass().getDeclaredField("parserTypeField").getType());
        } catch (Exception e) {
            log.error("Failed to get parser type for event: " + this.getClass().getName(), e);
        }
    }

    public void handle(IMessageEvent eventData) throws Exception {
        final T parser = this.parserType.newInstance();

        parser.parse(eventData);

        this.parserConsumer.accept(parser);
    }

    public short getMessageId() {
        return messageId;
    }
}
