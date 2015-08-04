package com.cometproject.server.game.groups.types.components.forum;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.GroupComponent;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.storage.queries.groups.GroupForumThreadDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForumComponent implements GroupComponent {
    public static final int MAX_MESSAGES_PER_PAGE = 20;

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

        msg.writeInt(this.forumThreads.size());//total threads
        msg.writeInt(0);//leaderboard score
        msg.writeInt(this.forumThreads.size());// TODO: keep a count of all messages (threads+replies)
        msg.writeInt(0);//unread messages

        msg.writeInt(0);//last message id
        msg.writeInt(0);//last message author id
        msg.writeString("");//last message author name
        msg.writeInt(0);//last message time
    }

    public List<ForumThread> getForumThreads(int start) {
        List<ForumThread> threads = Lists.newArrayList();

        if(start == 0) {
            for(int pinnedThread : this.pinnedThreads) {
                ForumThread forumThread = this.getForumThreads().get(pinnedThread);

                if(forumThread != null && threads.size() < MAX_MESSAGES_PER_PAGE) {
                    threads.add(forumThread);
                }
            }

            for (ForumThread forumThread : this.group.getForumComponent().getForumThreads().values()) {
                if (forumThread.isPinned() || threads.size() >= MAX_MESSAGES_PER_PAGE) continue;

                threads.add(forumThread);
            }

            return threads;
        }

        int currentThreadIndex = 0;

        for(ForumThread forumThread : this.forumThreads.values()) {
            if(currentThreadIndex >= start && threads.size() < MAX_MESSAGES_PER_PAGE) {
                threads.add(forumThread);
            }

            currentThreadIndex++;
        }

        return threads;
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
