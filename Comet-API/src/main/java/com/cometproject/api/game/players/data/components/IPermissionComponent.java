package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.data.IPlayerComponent;
import com.cometproject.api.game.players.data.components.permissions.IRank;

public interface IPermissionComponent extends IPlayerComponent {
    IRank getRank();

    boolean hasCommand(String key);
}
