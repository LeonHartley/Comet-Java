package com.cometproject.storage.mysql.models;

import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;

public class GroupForumSettingsData implements IForumSettings {

    private final int groupId;
    private ForumPermission readPermission;
    private ForumPermission postPermission;
    private ForumPermission startThreadPermission;
    private ForumPermission moderatePermission;

    public GroupForumSettingsData(int groupId, ForumPermission readPermission, ForumPermission postPermission, ForumPermission startThreadPermission, ForumPermission moderatePermission) {
        this.groupId = groupId;
        this.readPermission = readPermission;
        this.postPermission = postPermission;
        this.startThreadPermission = startThreadPermission;
        this.moderatePermission = moderatePermission;
    }

    @Override
    public int getGroupId() {
        return this.groupId;
    }

    @Override
    public ForumPermission getReadPermission() {
        return this.readPermission;
    }

    @Override
    public void setReadPermission(ForumPermission readPermission) {
        this.readPermission = readPermission;
    }

    @Override
    public ForumPermission getPostPermission() {
        return this.postPermission;
    }

    @Override
    public void setPostPermission(ForumPermission postPermission) {
        this.postPermission = postPermission;
    }

    @Override
    public ForumPermission getStartThreadsPermission() {
        return this.startThreadPermission;
    }

    @Override
    public void setStartThreadsPermission(ForumPermission startThreadsPermission) {
        this.startThreadPermission = startThreadsPermission;
    }

    @Override
    public ForumPermission getModeratePermission() {
        return this.moderatePermission;
    }

    @Override
    public void setModeratePermission(ForumPermission moderatePermission) {
        this.moderatePermission = moderatePermission;
    }
}
