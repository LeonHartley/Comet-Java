package com.cometproject.api.game.groups;

import com.cometproject.api.game.groups.types.components.forum.IForumSettings;

public interface IGroupForumService {

    IForumSettings getSettings(final int groupId);

}
