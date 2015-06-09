package com.cometproject.server.game.players;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.game.players.login.PlayerLoginRequest;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.utilities.Initializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.util.NamedThreadFactory;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PlayerManager implements Initializable {
    private static PlayerManager playerManagerInstance;
    private static Logger log = Logger.getLogger(PlayerManager.class.getName());

    private Map<Integer, Integer> playerIdToSessionId;
    private Map<String, Integer> playerUsernameToPlayerId;

    private Cache playerAvatarCache;
    private ExecutorService playerLoginService;

    public PlayerManager() {

    }

    @Override
    public void initialize() {
        this.playerIdToSessionId = new ConcurrentHashMap<>();
        this.playerUsernameToPlayerId = new ConcurrentHashMap<>();
        this.playerLoginService = Executors.newFixedThreadPool(6);// TODO: configure this.

        // Configure player cache
        if ((boolean) Comet.getServer().getConfig().getOrDefault("comet.cache.players.enabled", true)) {
            log.info("Initializing Player cache");

            final int oneDay = 24 * 60 * 60;
            this.playerAvatarCache = new Cache("playerAvatarCache", 5000, false, false, oneDay, oneDay);

            CacheManager.getInstance().addCache(this.playerAvatarCache);
        } else {
            log.info("Player data cache is disabled.");
        }

        log.info("Resetting player online status");
        PlayerDao.resetOnlineStatus();

        log.info("PlayerManager initialized");
    }

    public static PlayerManager getInstance() {
        if (playerManagerInstance == null)
            playerManagerInstance = new PlayerManager();

        return playerManagerInstance;
    }

    public void submitLoginRequest(Session client, String ticket) {
        this.playerLoginService.submit(new PlayerLoginRequest(client, ticket));
    }

    public PlayerAvatar getAvatarByPlayerId(int playerId, byte mode) {
        if(this.isOnline(playerId)) {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if(session != null && session.getPlayer() != null && session.getPlayer().getData() != null) {
                return session.getPlayer().getData();
            }
        }

        if(this.playerAvatarCache != null) {
            Element cachedElement = this.playerAvatarCache.get(playerId);

            if (cachedElement != null && cachedElement.getObjectValue() != null) {
                final PlayerAvatar playerAvatar = ((PlayerAvatar) cachedElement.getObjectValue());

                if(playerAvatar.getMotto() == null && mode == PlayerAvatar.USERNAME_FIGURE_MOTTO) {
                    playerAvatar.setMotto(PlayerDao.getMottoByPlayerId(playerId));
                }

                return (PlayerAvatar) cachedElement.getObjectValue();
            }
        }

        PlayerAvatar playerAvatar = PlayerDao.getAvatarById(playerId, mode);

        if(playerAvatar != null && this.playerAvatarCache != null) {
            this.playerAvatarCache.put(new Element(playerId, playerAvatar));
        }

        return playerAvatar;
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

    public void remove(int playerId, String username, int sessionId) {
        if (this.getSessionIdByPlayerId(playerId) != sessionId) {
            return;
        }

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
