package com.cometproject.game.groups.types;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.IForumComponent;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;

public class Group implements IGroup {
    private final IGroupData groupData;
    private final IMembershipComponent membershipComponent;

    private IForumComponent forumComponent;

    public Group(IGroupData groupData, IMembershipComponent membershipComponent, IForumComponent forumComponent) {
        this.groupData = groupData;

        this.membershipComponent = membershipComponent;
        this.forumComponent = forumComponent;
    }

    @Override
    public void dispose() {
        this.membershipComponent.dispose();

        if (this.forumComponent != null) {
            this.forumComponent.dispose();
        }
    }

    @Override
    public int getId() {
        return this.getData().getId();
    }

    @Override
    public IGroupData getData() {
        return this.groupData;
    }

    @Override
    public IMembershipComponent getMembers() {
        return this.membershipComponent;
    }

    @Override
    public IForumComponent getForum() {
        return this.forumComponent;
    }

    @Override
    public void setForum(IForumComponent forumComponent) {
        this.forumComponent = forumComponent;
    }
}
