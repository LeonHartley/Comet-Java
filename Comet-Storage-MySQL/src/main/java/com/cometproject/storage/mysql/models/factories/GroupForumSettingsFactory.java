package com.cometproject.storage.mysql.models.factories;

import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.storage.mysql.models.GroupForumSettingsData;

public class GroupForumSettingsFactory {

    public static IForumSettings createSettings(int groupId, ForumPermission readPermission, ForumPermission postPermission,
                                                ForumPermission startThreadPermission, ForumPermission moderatePermission) {
        return new GroupForumSettingsData(groupId, readPermission, postPermission, startThreadPermission, moderatePermission);
    }
}
