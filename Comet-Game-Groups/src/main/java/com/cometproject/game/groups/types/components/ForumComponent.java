package com.cometproject.game.groups.types.components;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.IForumComponent;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.networking.messages.IComposer;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class ForumComponent implements IForumComponent {

    public static final int MAX_MESSAGES_PER_PAGE = 20;

    private final IForumSettings forumSettings;
    private final Map<Integer, IForumThread> forumThreads;
    private final List<Integer> pinnedThreads;

    public ForumComponent(IForumSettings forumSettings, List<Integer> pinnedThreads,
                          Map<Integer, IForumThread> forumThreads) {
        this.forumSettings = forumSettings;
        this.pinnedThreads = pinnedThreads;
        this.forumThreads = forumThreads;
    }

    @Override
    public void dispose() {
        for(IForumThread forumThread : this.forumThreads.values()) {
            forumThread.dispose();
        }

        this.forumThreads.clear();

        this.pinnedThreads.clear();
    }

    @Override
    public void composeData(IComposer msg, IGroupData groupData) {
        msg.writeInt(groupData.getId());
        msg.writeString(groupData.getTitle());
        msg.writeString(groupData.getDescription());
        msg.writeString(groupData.getBadge());

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

            for (IForumThread forumThread : this.getForumThreads().values()) {
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
    public IForumSettings getForumSettings() {
        return this.forumSettings;
    }

    @Override
    public Map<Integer, IForumThread> getForumThreads() {
        return this.forumThreads;
    }

    @Override
    public List<Integer> getPinnedThreads() {
        return this.pinnedThreads;
    }
}
