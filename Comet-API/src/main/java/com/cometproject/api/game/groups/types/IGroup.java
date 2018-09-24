package com.cometproject.api.game.groups.types;

import com.cometproject.api.game.groups.types.components.IForumComponent;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;

public interface IGroup {
    int getId();

    IGroupData getData();

    IMembershipComponent getMembers();

    IForumComponent getForum();

    void setForum(IForumComponent forumComponent);

    void dispose();
}
