package com.cometproject.server.game.permissions;

import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.game.permissions.types.Permission;
import com.cometproject.server.storage.queries.permissions.PermissionsDao;
import com.cometproject.server.utilities.Initializable;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class PermissionsManager implements Initializable {
    private static PermissionsManager permissionsManagerInstance;

    private Map<Integer, Perk> perks;
    private Map<String, Permission> permissions;
    private Map<String, CommandPermission> commands;

    private static Logger log = Logger.getLogger(PermissionsManager.class.getName());

    public PermissionsManager() {

    }

    @Override
    public void initialize() {
        this.perks = new HashMap<>();
        this.permissions = new HashMap<>();
        this.commands = new HashMap<>();

        this.loadPerks();
        this.loadPermissions();
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
