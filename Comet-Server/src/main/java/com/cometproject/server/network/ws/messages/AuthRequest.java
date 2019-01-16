package com.cometproject.server.network.ws.messages;

public class AuthRequest {
    private final String ssoTicket;

    public AuthRequest(String ssoTicket) {
        this.ssoTicket = ssoTicket;
    }

    public String getSsoTicket() {
        return ssoTicket;
    }
}
