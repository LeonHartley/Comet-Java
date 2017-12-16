package com.cometproject.api.game.groups.types.components.forum;

import com.cometproject.api.game.groups.types.components.forum.ForumPermission;

public interface IForumSettings {
    int getGroupId();

    ForumPermission getReadPermission();

    void setReadPermission(ForumPermission readPermission);

    ForumPermission getPostPermission();

    void setPostPermission(ForumPermission postPermission);

    ForumPermission getStartThreadsPermission();

    void setStartThreadsPermission(ForumPermission startThreadsPermission);

    ForumPermission getModeratePermission();

    void setModeratePermission(ForumPermission moderatePermission);
}
