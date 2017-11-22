package com.cometproject.api.game.groups.types;

import com.cometproject.api.game.groups.types.components.IForumComponent;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;
import com.cometproject.api.networking.messages.IMessageComposer;

public interface IGroup {
    IMessageComposer composeInformation(boolean flag, int playerId);

    void initializeForum();

    void dispose();

    void commit();

    int getId();

    IGroupData getData();

    IMembershipComponent getMembershipComponent();

    IForumComponent getForumComponent();
}
