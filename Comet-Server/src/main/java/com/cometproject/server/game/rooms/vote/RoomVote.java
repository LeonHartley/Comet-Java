package com.cometproject.server.game.rooms.vote;

import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomVote {
    private final int playerId;

    private final int[] roomIds;
    private final AtomicInteger[] votes;
    private final Set<Integer> players;

    public RoomVote(final int playerId, final int roomIdA, final int roomIdB) {
        this.roomIds = new int[]{roomIdA, roomIdB};
        this.votes = new AtomicInteger[]{new AtomicInteger(0), new AtomicInteger(0)};
        this.players = new ConcurrentHashSet<>();

        this.playerId = playerId;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public boolean hasVoted(int playerId) {
        return this.players.contains(playerId);
    }

    public int[] getVotes() {
        return new int[]{
                this.votes[0].get(),
                this.votes[1].get()
        };
    }

    public void vote(int playerId, int room) {
        if (room >= this.votes.length) return;

        this.votes[room].incrementAndGet();
        this.players.add(playerId);
    }
}
