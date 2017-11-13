package com.cometproject.server.game.groups.types.components.forum;

import com.cometproject.api.game.groups.types.components.IForumComponent;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.api.game.groups.types.GroupComponent;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.server.storage.queries.groups.GroupForumThreadDao;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForumComponent implements IForumComponent, GroupComponent {
    public static final int MAX_MESSAGES_PER_PAGE = 20;

    private Group group;

    private IForumSettings forumSettings;

    private List<Integer> pinnedThreads;
    private Map<Integer, IForumThread> forumThreads;

    public ForumComponent(Group group, IForumSettings forumSettings) {
        this.group = group;
        this.forumSettings = forumSettings;
        this.forumThreads = group.getGroupDataObject() != null ? group.getGroupDataObject().getForumThreads() : GroupForumThreadDao.getAllMessagesForGroup(this.group.getId());
        this.pinnedThreads = new ArrayList<>();

        for (IForumThread forumThread : forumThreads.values()) {
            if (forumThread.isPinned()) {
                this.pinnedThreads.add(forumThread.getId());
            }
        }
    }

    @Override
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

    @Override
    public List<IForumThread> getForumThreads(int start) {
        List<IForumThread> threads = Lists.newArrayList();

        if(start == 0) {
            for(int pinnedThread : this.pinnedThreads) {
                IForumThread forumThread = this.getForumThreads().get(pinnedThread);

                if(forumThread != null && threads.size() < MAX_MESSAGES_PER_PAGE) {
                    threads.add(forumThread);
                }
            }

            for (IForumThread forumThread : this.group.getForumComponent().getForumThreads().values()) {
                if (forumThread.isPinned() || threads.size() >= MAX_MESSAGES_PER_PAGE) continue;

                threads.add(forumThread);
            }

            return threads;
        }

        int currentThreadIndex = 0;

        for(IForumThread forumThread : this.forumThreads.values()) {
            if(currentThreadIndex >= start && threads.size() < MAX_MESSAGES_PER_PAGE) {
                threads.add(forumThread);
            }

            currentThreadIndex++;
        }

        return threads;
    }

    @Override
    public void dispose() {
        for (IForumThread forumThread : this.forumThreads.values()) {
            forumThread.dispose();
        }

        this.forumThreads.clear();
        this.pinnedThreads.clear();
    }

    @Override
    public Group getGroup() {
        return this.group;
    }

    @Override
    public IForumSettings getForumSettings() {
        return this.forumSettings;
    }

    @Override
    public Map<Integer, IForumThread> getForumThreads() {
        return forumThreads;
    }

    @Override
    public List<Integer> getPinnedThreads() {
        return pinnedThreads;
    }
}
