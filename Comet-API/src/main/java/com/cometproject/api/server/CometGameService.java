package com.cometproject.api.server;

import com.cometproject.api.events.EventHandler;

public class CometGameService {
    /**
     * The main system-wide event handler
     */
    private EventHandler eventHandler;

    /**
     * Default constructor
     */
    public CometGameService(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * Get the main event handler
     * @return EventHandler instance
     */
    public EventHandler getEventHandler() {
        return this.eventHandler;
    }
}
