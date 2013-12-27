package com.cometsrv.game.players.components;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.players.types.Player;

public class PermissionComponent {
    private Player player;

    public PermissionComponent(Player player) {
        this.player = player;
    }

    public boolean hasPermission(String key) {
        if(GameEngine.getPermissions().getPermissions().containsKey(key)) {
            if(GameEngine.getPermissions().getPermissions().get(key).getRank() <= this.getPlayer().getData().getRank()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasCommand(String key) {
        if(GameEngine.getPermissions().getCommands().containsKey(key)) {
            if(GameEngine.getPermissions().getCommands().get(key) <= this.getPlayer().getData().getRank()) {
                return true;
            }
        }

        return false;
    }

    public Player getPlayer() {
        return this.player;
    }
}
