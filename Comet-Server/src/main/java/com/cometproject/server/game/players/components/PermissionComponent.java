package com.cometproject.server.game.players.components;

import com.cometproject.api.game.players.data.components.PlayerPermissions;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.permissions.types.Rank;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.storage.queries.permissions.PermissionsDao;

import java.util.Map;


public class PermissionComponent implements PlayerPermissions {
    private Player player;

    private Map<String, Boolean> commandOverridePermissions;

    public PermissionComponent(Player player) {
        this.player = player;

        this.commandOverridePermissions = PermissionsDao.getCommandOverridePermissionsByPlayerId(this.player.getId());
    }

    @Override
    public Rank getRank() {
        return PermissionsManager.getInstance().getRank(this.player.getData().getRank());
    }

    @Override
    public boolean hasCommand(String key) {
        if(this.player.getData().getRank() == 255) {
            return true;
        }

        if(this.commandOverridePermissions.containsKey(key)) {
            return this.commandOverridePermissions.get(key);
        }

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
