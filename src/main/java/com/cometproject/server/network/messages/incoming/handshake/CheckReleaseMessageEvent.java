package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class CheckReleaseMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        final String release = msg.readString();

        if (Session.CLIENT_VERSION == 0) {
            Session.CLIENT_VERSION = this.getReleaseNumber(release);
        } else if (this.getReleaseNumber(release) > Session.CLIENT_VERSION) {
            Session.CLIENT_VERSION = this.getReleaseNumber(release);
        }

        client.getLogger().debug("Client running on release: " + Session.CLIENT_VERSION);
    }

    private int getReleaseNumber(String releaseString) {
        String[] parsedRelease = releaseString.split("-");

        try {
            return Integer.parseInt(parsedRelease[1].substring(0, 8));
        } catch (Exception ignored) {
            return 19700101;
        }
    }
}
