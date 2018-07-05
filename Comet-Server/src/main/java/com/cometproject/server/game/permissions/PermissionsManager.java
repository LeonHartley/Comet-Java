package com.cometproject.server.game.permissions;

import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.permissions.types.OverrideCommandPermission;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.game.permissions.types.Rank;
import com.cometproject.server.storage.queries.permissions.PermissionsDao;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PermissionsManager implements Initialisable {
    private static PermissionsManager permissionsManagerInstance;
    private static Logger log = Logger.getLogger(PermissionsManager.class.getName());
    private Map<Integer, Perk> perks;
    private Map<Integer, Rank> ranks;
    private Map<String, CommandPermission> commands;
    private Map<String, OverrideCommandPermission> overridecommands;
    private Map<Integer, Integer> effects;
    private Map<Integer, Integer> chatBubbles;

    public PermissionsManager() {

    }

    public static PermissionsManager getInstance() {
        if (permissionsManagerInstance == null)
            permissionsManagerInstance = new PermissionsManager();

        return permissionsManagerInstance;
    }

    @Override
    public void initialize() {
        this.perks = new ConcurrentHashMap<>();
        this.commands = new ConcurrentHashMap<>();
        this.overridecommands = new ConcurrentHashMap<>();
        this.ranks = new ConcurrentHashMap<>();
        this.effects = new ConcurrentHashMap<>();
        this.chatBubbles = new ConcurrentHashMap<>();

        this.loadPerks();
        this.loadRankPermissions();
        this.loadCommands();
        this.loadOverrideCommands();
        this.loadEffects();
        this.loadChatBubbles();

        log.info("PermissionsManager initialized");
    }

    public void loadPerks() {
        try {
            if (this.getPerks().size() != 0) {
                this.getPerks().clear();
            }

            this.perks = PermissionsDao.getPerks();

        } catch (Exception e) {
            log.error("Error while loading perk permissions", e);
            return;
        }

        log.info("Loaded " + this.getPerks().size() + " perks");
    }

    public void loadRankPermissions() {
        try {
            if (this.getRankPermissions().size() != 0) {
                this.getRankPermissions().clear();
            }

            this.ranks = PermissionsDao.getRankPermissions();
            this.ranks.put(255, new Rank(255, "Comet", true, 0, false, true, false, false, true, true, true, false, true, true, true, true, true, true, 10000, true, true, false));
        } catch (Exception e) {
            log.error("Error while loading rank permissions", e);
            return;
        }

        log.info("Loaded " + this.getRankPermissions().size() + " ranks");
    }

    public void loadCommands() {
        try {
            if (this.getCommands().size() != 0) {
                this.getCommands().clear();
            }

            this.commands = PermissionsDao.getCommandPermissions();

        } catch (Exception e) {
            log.error("Error while reloading command permissions", e);
            return;
        }

        log.info("Loaded " + this.getCommands().size() + " command permissions");
    }

    public void loadOverrideCommands() {
        try {
            if (this.getOverrideCommands().size() != 0) {
                this.getOverrideCommands().clear();
            }

            this.overridecommands = PermissionsDao.getOverrideCommandPermissions();

        } catch (Exception e) {
            log.error("Error while reloading override command permissions", e);
            return;
        }

        log.info("Loaded " + this.getOverrideCommands().size() + " override command permissions");
    }

    public void loadEffects() {
        try {
            if (this.getEffects().size() != 0) {
                this.getEffects().clear();
            }

            this.effects = PermissionsDao.getEffects();

        } catch (Exception e) {
            log.error("Error while reloading effect permissions", e);
            return;
        }

        log.info("Loaded " + this.getEffects().size() + " effect permissions");
    }

    public void loadChatBubbles() {
        if (this.chatBubbles.size() != 0) {
            this.chatBubbles.clear();
        }

        this.chatBubbles = PermissionsDao.getChatBubbles();

        log.info("Loaded " + this.getEffects().size() + " chat bubbles");
    }


    public Rank getRank(final int playerRankId) {
        final Rank rank = this.ranks.get(playerRankId);

        if (rank == null) {
            log.warn("Failed to find rank by rank ID: " + playerRankId + ", are you sure it exists?");
            return this.ranks.get(1);
        }

        return rank;
    }

    public Map<Integer, Rank> getRankPermissions() {
        return this.ranks;
    }

    public Map<String, CommandPermission> getCommands() {
        return this.commands;
    }

    public Map<String, OverrideCommandPermission> getOverrideCommands() {
        return this.overridecommands;
    }

    public Map<Integer, Perk> getPerks() {
        return perks;
    }

    public Map<Integer, Integer> getEffects() {
        return effects;
    }

    public Map<Integer, Integer> getChatBubbles() {
        return this.chatBubbles;
    }
}