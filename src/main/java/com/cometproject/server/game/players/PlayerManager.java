package com.cometproject.server.game.players;

import javolution.util.FastMap;

public class PlayerManager {
    private FastMap<Integer, Integer> playerIdToSessionId;
    private FastMap<String, Integer> playerUsernameToPlayerId;

    public PlayerManager() {
        this.playerIdToSessionId = new FastMap<>();
        this.playerUsernameToPlayerId = new FastMap<>();
    }

    public void put(int playerId, int sessionId, String username) {
        if (this.playerIdToSessionId.containsKey(playerId)) {
            this.playerIdToSessionId.remove(playerId);
        }

        if (this.playerUsernameToPlayerId.containsKey(username.toLowerCase())) {
            this.playerUsernameToPlayerId.remove(username.toLowerCase());
        }

        this.playerIdToSessionId.put(playerId, sessionId);
        this.playerUsernameToPlayerId.put(username.toLowerCase(), playerId);
    }

    public void remove(int playerId, String username) {
        this.playerIdToSessionId.remove(playerId);
        this.playerUsernameToPlayerId.remove(username);
    }

    public int getPlayerIdByUsername(String username) {
        if(this.playerUsernameToPlayerId.containsKey(username.toLowerCase())) {
            return this.playerUsernameToPlayerId.get(username.toLowerCase());
        }

        return -1;
    }

    public int getSessionIdByPlayerId(int playerId) {
        if(this.playerIdToSessionId.containsKey(playerId)) {
            return this.playerIdToSessionId.get(playerId);
        }

        return -1;
    }

    public boolean isOnline(int playerId) {
        return this.playerIdToSessionId.containsKey(playerId);
    }

    public boolean isOnline(String username) {
        return this.playerUsernameToPlayerId.containsKey(username);
    }

    public int size() {
        return this.playerIdToSessionId.size();
    }
}
