package com.cometproject.server.storage.cache.objects;

import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.server.storage.cache.CachableObject;

import java.util.List;
import java.util.Map;

public class GroupDataObject extends CachableObject {
    private final int id;
    private final IGroupData groupData;
    private final List<IGroupMember> groupMembers;
    private final List<Integer> groupRequests;

    private final IForumSettings forumSettings;
    private final Map<Integer, IForumThread> forumThreads;

    public GroupDataObject(int id, IGroupData groupData, List<IGroupMember> groupMembers, List<Integer> groupRequests, IForumSettings forumSettings, Map<Integer, IForumThread> forumThreads) {
        this.id = id;
        this.groupData = groupData;
        this.groupMembers = groupMembers;
        this.groupRequests = groupRequests;
        this.forumSettings = forumSettings;
        this.forumThreads = forumThreads;
    }

    public int getId() {
        return id;
    }

    public IGroupData getGroupData() {
        return groupData;
    }

    public List<IGroupMember> getGroupMembers() {
        return groupMembers;
    }

    public IForumSettings getForumSettings() {
        return forumSettings;
    }

    public Map<Integer, IForumThread> getForumThreads() {
        return forumThreads;
    }

    public List<Integer> getGroupRequests() {
        return groupRequests;
    }
}
