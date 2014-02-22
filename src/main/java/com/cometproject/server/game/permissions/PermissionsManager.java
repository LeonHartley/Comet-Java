package com.cometproject.server.game.permissions;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.game.permissions.types.Permission;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class PermissionsManager {
    private FastMap<Integer, Perk> perks;
    private FastMap<String, Permission> permissions;
    private FastMap<String, Integer> commands;

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
            if(this.getPerks().size() != 0) {
                this.getPerks().clear();
            }

            ResultSet result = Comet.getServer().getStorage().getTable("SELECT * FROM permission_perks");

            while(result.next()) {
                this.getPerks().put(result.getInt("id"), new Perk(result));
            }
        } catch(Exception e) {
            log.error("Error while loading perk permissions", e);
            return;
        }

        log.info("Loaded " + this.getPerks().size() + " perks");
    }

    public void loadPermissions() {
        try {
            if(this.getPermissions().size() != 0) {
                this.getPermissions().clear();
            }

            ResultSet result = Comet.getServer().getStorage().getTable("SELECT * FROM permission_ranks");

            while(result.next()) {
                this.getPermissions().put(result.getString("fuse"), new Permission(result));
            }
        } catch(Exception e) {
            log.error("Error while loading rank permissions", e);
            return;
        }

       log.info("Loaded " + this.getPermissions().size() + " permissions");
    }

    public void loadCommands() {
        try {
            if(this.getCommands().size() != 0) {
                this.getCommands().clear();
            }

            ResultSet result = Comet.getServer().getStorage().getTable("SELECT * FROM permission_commands");

            while(result.next()) {
                this.getCommands().put(result.getString("command_id"), result.getInt("minimum_rank"));
            }
        } catch(Exception e) {
            log.error("Error while reloading command permissions", e);
            return;
        }

       log.info("Loaded " + this.getCommands().size() + " command permissions");
    }

    public Map<String, Permission> getPermissions() {
        return this.permissions;
    }

    public Map<String, Integer> getCommands() {
        return this.commands;
    }

    public Map<Integer, Perk> getPerks() {
        return perks;
    }
}
