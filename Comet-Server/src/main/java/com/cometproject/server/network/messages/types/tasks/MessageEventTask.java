package com.cometproject.server.network.messages.types.tasks;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.tasks.CometTask;
import org.apache.log4j.Logger;

public class MessageEventTask implements CometTask {
    private static final Logger log = Logger.getLogger(MessageEventTask.class.getName());

    private Event messageEvent;
    private Session session;
    private MessageEvent messageEventData;

    public MessageEventTask(Event messageEvent, Session session, MessageEvent messageEventData) {
        this.messageEvent = messageEvent;
        this.session = session;
        this.messageEventData = messageEventData;
    }

    @Override
    public void run() {
        try {
            final long start = System.currentTimeMillis();

            log.debug("Started packet process for packet: [" + this.messageEvent.getClass().getSimpleName() + "][" + messageEventData.getId() + "]");

            this.messageEvent.handle(this.session, this.messageEventData);

            log.debug("Finished packet process for packet: [" + this.messageEvent.getClass().getSimpleName() + "][" + messageEventData.getId() + "] in " + ((System.currentTimeMillis() - start)) + "ms");

        } catch (Exception e) {
            if (this.session.getLogger() != null)
                session.getLogger().error("Error while handling event: " + this.messageEvent.getClass().getSimpleName(), e);
            else
                log.error("Error while handling event: " + this.messageEvent.getClass().getSimpleName(), e);
        }
    }
}
