package com.cometproject.server.network.ws.messages;

public class RoomVoteRequest {
    private final int vote;

    public RoomVoteRequest(int vote) {
        this.vote = vote;
    }

    public int getVote() {
        return vote;
    }
}
