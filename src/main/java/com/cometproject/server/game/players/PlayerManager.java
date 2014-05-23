package com.cometproject.server.game.players;

import javolution.util.FastMap;

public class PlayerManager {
    private FastMap<Integer, Integer> playerIdToSessionId;
    private FastMap<String, Integer> playerUsernameToPlayerId;

    public PlayerManager() {
        this.playerIdToSessionId = new FastMap<>().atomic();
        this.playerUsernameToPlayerId = new FastMap<>().atomic();
    }

    public void put(int playerId, int sessionId, String username) {
        if (this.playerIdToSessionId.containsKey(playerId)) {
            this.playerIdToSessionId.remove(playerId);
        }

        if (this.playerUsernameToPlayerId.containsKey(username)) {
            this.playerIdToSessionId.remove(username);
        }

        this.playerIdToSessionId.put(playerId, sessionId);
        this.playerUsernameToPlayerId.put(username, playerId);
    }

    public void remove(int playerId, String username) {
        this.playerIdToSessionId.remove(playerId);
        this.playerUsernameToPlayerId.remove(username);
    }

    public int getPlayerIdByUsername(String username) {
        if(this.playerUsernameToPlayerId.containsKey(username)) {
            return this.playerUsernameToPlayerId.get(username);
        }

        return 0;
    }

    public boolean isOnline(int playerId) {
        return this.playerIdToSessionId.containsKey(playerId);
    }

    public boolean isOnline(String username) {
        return this.playerUsernameToPlayerId.containsKey(username);
    }
}
