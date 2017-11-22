package com.cometproject.server.game.groups.types.components.forum.settings;

import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.server.storage.queries.groups.GroupForumDao;

public class ForumSettings implements IForumSettings {
    private int groupId;

    private ForumPermission readPermission;
    private ForumPermission postPermission;
    private ForumPermission startThreadsPermission;
    private ForumPermission moderatePermission;

    public ForumSettings(int groupId, ForumPermission readPermission, ForumPermission postPermission,
                         ForumPermission startThreadsPermission, ForumPermission moderatePermission) {
        this.groupId = groupId;
        this.readPermission = readPermission;
        this.postPermission = postPermission;
        this.startThreadsPermission = startThreadsPermission;
        this.moderatePermission = moderatePermission;
    }

    @Override
    public void save() {
        GroupForumDao.saveSettings(this);
    }

    @Override
    public int getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public ForumPermission getReadPermission() {
        return readPermission;
    }

    @Override
    public void setReadPermission(ForumPermission readPermission) {
        this.readPermission = readPermission;
    }

    @Override
    public ForumPermission getPostPermission() {
        return postPermission;
    }

    @Override
    public void setPostPermission(ForumPermission postPermission) {
        this.postPermission = postPermission;
    }

    @Override
    public ForumPermission getStartThreadsPermission() {
        return startThreadsPermission;
    }

    @Override
    public void setStartThreadsPermission(ForumPermission startThreadsPermission) {
        this.startThreadsPermission = startThreadsPermission;
    }

    @Override
    public ForumPermission getModeratePermission() {
        return moderatePermission;
    }

    @Override
    public void setModeratePermission(ForumPermission moderatePermission) {
        this.moderatePermission = moderatePermission;
    }
}
