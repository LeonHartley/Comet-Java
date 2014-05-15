package com.cometproject.server.game.groups.types;

import java.util.List;

public class Group {
    private int id;
    private GroupData data;
    private List<GroupMember> members;

    public Group(GroupData data, List<GroupMember> members) {
        this.id = data.getId();
        this.data = data;
        this.members = members;
    }

    public int getId() {
        return this.id;
    }

    public GroupData getData() {
        return this.data;
    }
}
