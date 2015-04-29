package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.data.IPlayerComponent;

public interface IPermissionComponent extends IPlayerComponent {
    public boolean hasPermission(String key);

    public boolean hasCommand(String key);
}
