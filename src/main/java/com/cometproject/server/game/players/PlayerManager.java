package com.cometproject.server.game.players;

import com.cometproject.server.utilities.Initializable;
import javolution.util.FastMap;
import org.apache.log4j.Logger;


public class PlayerManager implements Initializable {
    private static PlayerManager playerManagerInstance;
    private static Logger log = Logger.getLogger(PlayerManager.class.getName());

    private FastMap<Integer, Integer> playerIdToSessionId;

    private FastMap<String, Integer> playerUsernameToPlayerId;

    public PlayerManager() {

    }

    @Override
    public void initialize() {
        this.playerIdToSessionId = new FastMap<>();
        this.playerUsernameToPlayerId = new FastMap<>();

        log.info("PlayerManager initialized");
    }

    public static PlayerManager getInstance() {
        if (playerManagerInstance == null)
            playerManagerInstance = new PlayerManager();

        return playerManagerInstance;
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
        this.playerUsernameToPlayerId.remove(username.toLowerCase());
    }

    public int getPlayerIdByUsername(String username) {
        if (this.playerUsernameToPlayerId.containsKey(username.toLowerCase())) {
            return this.playerUsernameToPlayerId.get(username.toLowerCase());
        }

        return -1;
    }

    public int getSessionIdByPlayerId(int playerId) {
        if (this.playerIdToSessionId.containsKey(playerId)) {
            return this.playerIdToSessionId.get(playerId);
        }

        return -1;
    }

    public boolean isOnline(int playerId) {
        return this.playerIdToSessionId.containsKey(playerId);
    }

    public boolean isOnline(String username) {
        return this.playerUsernameToPlayerId.containsKey(username.toLowerCase());
    }

    public int size() {
        return this.playerIdToSessionId.size();
    }
}
