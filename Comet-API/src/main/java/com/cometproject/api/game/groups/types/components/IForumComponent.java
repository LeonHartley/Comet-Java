package com.cometproject.api.game.groups.types.components;

import com.cometproject.api.game.groups.types.GroupComponent;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.networking.messages.IComposer;

import java.util.List;
import java.util.Map;

public interface IForumComponent extends GroupComponent {
    void composeData(IComposer msg, IGroupData groupData);

    List<IForumThread> getForumThreads(int start);

    IForumSettings getForumSettings();

    Map<Integer, IForumThread> getForumThreads();

    List<Integer> getPinnedThreads();
}