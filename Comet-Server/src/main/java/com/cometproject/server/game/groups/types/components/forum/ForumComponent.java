package com.cometproject.server.game.groups.types.components.forum;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.GroupComponent;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.google.common.collect.Maps;

import java.util.Map;

public class ForumComponent implements GroupComponent {
    private Group group;

    private ForumSettings forumSettings;
    private Map<Integer, ForumThread> forumThreads;

    public ForumComponent(Group group, ForumSettings forumSettings) {
        this.group = group;
        this.forumSettings = forumSettings;
        this.forumThreads = Maps.newHashMap();
    }

    @Override
    public void dispose() {
        for(ForumThread thread : this.forumThreads.values()) {
            thread.dispose();
        }

        this.forumThreads.clear();
    }

    @Override
    public Group getGroup() {
        return this.group;
    }

    public ForumSettings getForumSettings() {
        return this.forumSettings;
    }

    public Map<Integer, ForumThread> getForumThreads() {
        return this.forumThreads;
    }
}
