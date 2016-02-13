package com.cometproject.server.game.permissions;

import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.game.permissions.types.Rank;
import com.cometproject.server.storage.queries.permissions.PermissionsDao;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class PermissionsManager implements Initializable {
    private static PermissionsManager permissionsManagerInstance;

    private Map<Integer, Perk> perks;
    private Map<Integer, Rank> ranks;
    private Map<String, CommandPermission> commands;

    private static Logger log = Logger.getLogger(PermissionsManager.class.getName());

    public PermissionsManager() {

    }

    @Override
    public void initialize() {
        this.perks = new HashMap<>();
        this.commands = new HashMap<>();
        this.ranks = new HashMap<>();

        this.loadPerks();
        this.loadRankPermissions();
        this.loadCommands();


        log.info("PermissionsManager initialized");
    }

    public static PermissionsManager getInstance() {
        if (permissionsManagerInstance == null)
            permissionsManagerInstance = new PermissionsManager();

        return permissionsManagerInstance;
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
            this.ranks.put(255, new Rank(255, "Comet", true, 0, false, true, false, false, true, true, true, false, true, true, true, true, true, 10000, true, true));
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

    public Map<Integer, Perk> getPerks() {
        return perks;
    }
}
