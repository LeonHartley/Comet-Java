package com.cometproject.server.game.groups.types;

import com.cometproject.server.game.CometManager;

import java.util.List;

public class Group {
    private int id;
    private List<GroupMember> groupMembers;

    public Group(int id, List<GroupMember> members) {
        this.id = id;
        this.groupMembers = members;
    }

    public int getId() {
        return this.id;
    }

    public GroupData getData() {
        return CometManager.getGroups().getData(this.id);
    }
}
