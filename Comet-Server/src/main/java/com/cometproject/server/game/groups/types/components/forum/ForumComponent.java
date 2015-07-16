package com.cometproject.server.game.groups.types.components.forum;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.GroupComponent;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;

public class ForumComponent implements GroupComponent {
    private Group group;

    private ForumSettings forumSettings;
//    private Map<Integer, ForumThread> forumThreads;

    public ForumComponent(Group group, ForumSettings forumSettings) {
        this.group = group;
        this.forumSettings = forumSettings;
//        this.forumThreads = ForumThreadDao.getThreads(this.group.getId());
    }

    @Override
    public void dispose() {


    }

    @Override
    public Group getGroup() {
        return this.group;
    }

    public ForumSettings getForumSettings() {
        return this.forumSettings;
    }
}
