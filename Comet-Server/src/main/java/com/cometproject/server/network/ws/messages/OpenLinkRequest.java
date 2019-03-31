package com.cometproject.server.network.ws.messages;

public class OpenLinkRequest {
    private final String link;

    public OpenLinkRequest(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
