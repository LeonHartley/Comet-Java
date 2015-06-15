package com.cometproject.server.game.players.components;

import com.cometproject.api.game.players.data.components.IPermissionComponent;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.players.types.Player;


public class PermissionComponent implements IPermissionComponent {
    private Player player;

    public PermissionComponent(Player player) {
        this.player = player;
    }

    @Override
    public boolean hasPermission(String key) {
        try {
            if (PermissionsManager.getInstance().getPermissions().containsKey(key)) {
                if (this.getPlayer() == null || this.getPlayer().getData() == null) return false;

                if (PermissionsManager.getInstance().getPermissions().get(key).getRank() <= this.getPlayer().getData().getRank()) {
                    return true;
                }
            }
        } catch (Exception ignored) {
            return false;
        }

        return false;
    }

    @Override
    public boolean hasCommand(String key) {
        if (PermissionsManager.getInstance().getCommands().containsKey(key)) {
            CommandPermission permission = PermissionsManager.getInstance().getCommands().get(key);

            if (permission.getMinimumRank() <= this.getPlayer().getData().getRank()) {
                if ((permission.isVipOnly() && player.getData().isVip()) || !permission.isVipOnly())
                    return true;
            }
        } else if (key.equals("debug") && Comet.isDebugging) {
            return true;
        } else if (key.equals("dev")) {
            return true;
        }

        return false;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void dispose() {

    }
}
