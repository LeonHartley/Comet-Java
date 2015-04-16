package com.cometproject.server.game.groups.types.components;

import com.cometproject.server.game.groups.types.Group;

public interface GroupComponent {
    public Group getGroup();

    public void dispose();
}
