package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IGroupForumRepository {
    void getSettingsByGroupId(final int groupId, Consumer<IForumSettings> forumSettingsConsumer);

    void saveSettings(final IForumSettings forumSettings);

    void getAllMessages(Integer groupId, BiConsumer<Map<Integer, IForumThread>, List<Integer>> threadConsumer);

    void createThread(int groupId, String title, String message, int authorId, Consumer<Integer> threadId);

    void createReply(int groupId, int threadId, String message, int authorId, Consumer<Integer> messageId);

    void saveMessageState(int messageId, int state, int modId, String modUsername);

    void saveMessageLock(int messageId, boolean locked, int modId, String modUsername);

    void saveMessagePinState(int messageId, boolean pinned);
}
