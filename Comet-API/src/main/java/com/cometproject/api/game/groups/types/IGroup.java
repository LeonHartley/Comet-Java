package com.cometproject.api.game.groups.types;

import com.cometproject.api.networking.messages.IMessageComposer;

public interface IGroup {
    GroupDataObject getCacheObject();

    IMessageComposer composeInformation(boolean flag, int playerId);

    void initializeForum();

    void dispose();

    void commit();

    int getId();

    IGroupData getData();

    MembershipComponent getMembershipComponent();

    ForumComponent getForumComponent();

    GroupDataObject getGroupDataObject();
}
