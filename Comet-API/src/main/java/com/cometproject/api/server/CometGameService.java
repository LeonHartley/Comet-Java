package com.cometproject.api.server;

import com.cometproject.api.events.Event;
import com.cometproject.api.events.EventHandler;

public class CometGameService {
    /**
     * The main system-wide event handler
     */
    private EventHandler eventHandler;

    /**
     * Default constructor
     */
    public CometGameService() {
        this.eventHandler = new EventHandler();
    }

    /**
     * Get the main event handler
     * @return EventHandler instance
     */
    public EventHandler getEventHandler() {
        return this.eventHandler;
    }
}
