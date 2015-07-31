package com.cometproject.server.game.groups.types.components.forum;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.GroupComponent;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.storage.queries.groups.GroupForumThreadDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForumComponent implements GroupComponent {
    private Group group;

    private ForumSettings forumSettings;

    private List<Integer> pinnedThreads;
    private Map<Integer, ForumThread> forumThreads;

    public ForumComponent(Group group, ForumSettings forumSettings) {
        this.group = group;
        this.forumSettings = forumSettings;
        this.forumThreads = GroupForumThreadDao.getAllMessagesForGroup(this.group.getId());
        this.pinnedThreads = new ArrayList<>();

        for (ForumThread forumThread : forumThreads.values()) {
            if (forumThread.isPinned()) {
                this.pinnedThreads.add(forumThread.getId());
            }
        }
    }

    public void composeData(IComposer msg) {
        msg.writeInt(group.getId());
        msg.writeString(group.getData().getTitle());
        msg.writeString(group.getData().getDescription());
        msg.writeString(group.getData().getBadge());

        msg.writeInt(0);//total threads
        msg.writeInt(0);//leaderboard score
        msg.writeInt(0);//total messages
        msg.writeInt(0);//unread messages

        msg.writeInt(0);//last message id
        msg.writeInt(0);//last message author id
        msg.writeString("");//last message author name
        msg.writeInt(0);//last message time
    }

    @Override
    public void dispose() {
        for (ForumThread forumThread : this.forumThreads.values()) {
            forumThread.dispose();
        }

        this.forumThreads.clear();
        this.pinnedThreads.clear();
    }

    @Override
    public Group getGroup() {
        return this.group;
    }

    public ForumSettings getForumSettings() {
        return this.forumSettings;
    }

    public Map<Integer, ForumThread> getForumThreads() {
        return forumThreads;
    }

    public List<Integer> getPinnedThreads() {
        return pinnedThreads;
    }
}
