package com.cometproject.server.game.players.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.players.types.Player;

public class PermissionComponent {
    private Player player;

    public PermissionComponent(Player player) {
        this.player = player;
    }

    public boolean hasPermission(String key) {
        if (CometManager.getPermissions().getPermissions().containsKey(key)) {
            if (CometManager.getPermissions().getPermissions().get(key).getRank() <= this.getPlayer().getData().getRank()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasCommand(String key) {
        if (CometManager.getPermissions().getCommands().containsKey(key)) {
            CommandPermission permission = CometManager.getPermissions().getCommands().get(key);

            if (permission.getMinimumRank() <= this.getPlayer().getData().getRank()) {
                if ((permission.isVipOnly() && player.getData().isVip()) || !permission.isVipOnly())
                    return true;
            }
        } else if(key.equals("debug") && Comet.isDebugging) {
            return true;
        }

        return false;
    }

    public Player getPlayer() {
        return this.player;
    }
}
