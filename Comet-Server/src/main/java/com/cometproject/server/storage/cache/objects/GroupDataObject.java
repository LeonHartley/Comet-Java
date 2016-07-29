package com.cometproject.server.storage.cache.objects;

import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.storage.cache.CachableObject;

import java.util.List;
import java.util.Map;

public class GroupDataObject extends CachableObject {
    private final int id;
    private final GroupData groupData;
    private final List<GroupMember> groupMembers;
    private final List<Integer> groupRequests;

    private final ForumSettings forumSettings;
    private final Map<Integer, ForumThread> forumThreads;

    public GroupDataObject(int id, GroupData groupData, List<GroupMember> groupMembers, List<Integer> groupRequests, ForumSettings forumSettings, Map<Integer, ForumThread> forumThreads) {
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

    public GroupData getGroupData() {
        return groupData;
    }

    public List<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public ForumSettings getForumSettings() {
        return forumSettings;
    }

    public Map<Integer, ForumThread> getForumThreads() {
        return forumThreads;
    }

    public List<Integer> getGroupRequests() {
        return groupRequests;
    }
}
