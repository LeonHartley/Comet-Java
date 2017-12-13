package com.cometproject.game.groups.types.components;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.IForumComponent;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.networking.messages.IComposer;

import java.util.List;
import java.util.Map;

public class ForumComponent implements IForumComponent {

    private final IGroup group;

    public ForumComponent(IGroup group) {
        this.group = group;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void composeData(IComposer msg) {

    }

    @Override
    public List<IForumThread> getForumThreads(int start) {
        return null;
    }

    @Override
    public IForumSettings getForumSettings() {
        return null;
    }

    @Override
    public Map<Integer, IForumThread> getForumThreads() {
        return null;
    }

    @Override
    public List<Integer> getPinnedThreads() {
        return null;
    }
}
