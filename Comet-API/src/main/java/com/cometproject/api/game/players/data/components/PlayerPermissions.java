package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.data.IPlayerComponent;
import com.cometproject.api.game.players.data.components.permissions.PlayerRank;

public interface PlayerPermissions extends IPlayerComponent {
    PlayerRank getRank();

    boolean hasCommand(String key);
}
