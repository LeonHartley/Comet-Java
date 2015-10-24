package com.cometproject.server.network.messages.types.tasks;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.tasks.CometTask;
import org.apache.log4j.Logger;

public class MessageEventTask implements CometTask {
    private static final Logger log = Logger.getLogger(MessageEventTask.class.getName());

    private Event messageEvent;
    private Session session;
    private MessageEvent messageEventData;

    private long start;

    public MessageEventTask(Event messageEvent, Session session, MessageEvent messageEventData) {
        this.messageEvent = messageEvent;
        this.session = session;
        this.messageEventData = messageEventData;

        this.start = System.currentTimeMillis();
    }

    @Override
    public void run() {
        try {
            log.debug("Started packet process for packet: [" + this.messageEvent.getClass().getSimpleName() + "][" + messageEventData.getId() + "]");
            this.messageEvent.handle(this.session, this.messageEventData);

            long timeTakenSinceCreation = ((System.currentTimeMillis() - this.start));

            // If the packet took more than 750ms to be handled, red flag!
            if (timeTakenSinceCreation >= 750) {
                if (session.getPlayer() != null && session.getPlayer().getData() != null)
                    log.trace("[" + this.messageEvent.getClass().getSimpleName() + "][" + messageEventData.getId() + "][" + session.getPlayer().getId() + "][" + session.getPlayer().getData().getUsername() + "] Packet took " + timeTakenSinceCreation + "ms to execute");
                else
                    log.trace("[" + this.messageEvent.getClass().getSimpleName() + "][" + messageEventData.getId() + "] Packet took " + timeTakenSinceCreation + "ms to execute");
            }
            log.debug("Finished packet process for packet: [" + this.messageEvent.getClass().getSimpleName() + "][" + messageEventData.getId() + "] in " + timeTakenSinceCreation + "ms");

        } catch (Exception e) {
            if (this.session.getLogger() != null)
                session.getLogger().error("Error while handling event: " + this.messageEvent.getClass().getSimpleName(), e);
            else
                log.error("Error while handling event: " + this.messageEvent.getClass().getSimpleName(), e);
        }
    }
}
