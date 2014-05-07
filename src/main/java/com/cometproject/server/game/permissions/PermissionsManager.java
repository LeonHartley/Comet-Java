package com.cometproject.server.game.permissions;

import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.game.permissions.types.Permission;
import com.cometproject.server.storage.queries.permissions.PermissionsDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;

public class PermissionsManager {
    private FastMap<Integer, Perk> perks;
    private FastMap<String, Permission> permissions;
    private FastMap<String, CommandPermission> commands;

    private static Logger log = Logger.getLogger(PermissionsManager.class.getName());

    public PermissionsManager() {
        this.perks = new FastMap<>();
        this.permissions = new FastMap<>();
        this.commands = new FastMap<>();

        this.loadPerks();
        this.loadPermissions();
        this.loadCommands();
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

    public void loadPermissions() {
        try {
            if (this.getPermissions().size() != 0) {
                this.getPermissions().clear();
            }

            this.permissions = PermissionsDao.getRankPermissions();
        } catch (Exception e) {
            log.error("Error while loading rank permissions", e);
            return;
        }

        log.info("Loaded " + this.getPermissions().size() + " permissions");
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

    public Map<String, Permission> getPermissions() {
        return this.permissions;
    }

    public Map<String, CommandPermission> getCommands() {
        return this.commands;
    }

    public Map<Integer, Perk> getPerks() {
        return perks;
    }
}
