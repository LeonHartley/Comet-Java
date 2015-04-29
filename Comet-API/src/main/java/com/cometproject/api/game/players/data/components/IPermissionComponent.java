package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.data.IPlayerComponent;

public interface IPermissionComponent extends IPlayerComponent {
    boolean hasPermission(String key);

    boolean hasCommand(String key);
}
