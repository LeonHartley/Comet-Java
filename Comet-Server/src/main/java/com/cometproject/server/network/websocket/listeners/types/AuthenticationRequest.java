package com.cometproject.server.network.websocket.listeners.types;

public class AuthenticationRequest {
    private String ticket;

    public AuthenticationRequest(String ticket) {
        this.ticket = ticket;
    }

    public AuthenticationRequest() {
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
