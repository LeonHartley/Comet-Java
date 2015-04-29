package com.cometproject.tools.packets.revisions;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.tools.packets.instances.MessageComposer;
import com.cometproject.tools.packets.instances.MessageEvent;

import java.util.Map;


public class HabboRevision {
    private String releaseString;

    private Map<String, MessageComposer> composers;
    private Map<String, MessageEvent> events;

    public HabboRevision(String releaseString, Map<String, MessageComposer> composers, Map<String, MessageEvent> events) {
        this.releaseString = releaseString;

        this.composers = composers;
        this.events = events;
    }

    public String getReleaseString() {
        return this.releaseString;
    }

    public Map<String, MessageComposer> getComposers() {
        return composers;
    }

    public Map<String, MessageEvent> getEvents() {
        return events;
    }
}
