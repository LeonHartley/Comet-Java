package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;

import java.util.Map;
import java.util.function.Consumer;

public interface IGroupForumRepository {
    void getSettingsByGroupId(final int groupId, Consumer<IForumSettings> forumSettingsConsumer);

    void saveSettings(final IForumSettings forumSettings);

    void getAllMessages(Integer groupId, Consumer<Map<Integer, IForumThread>> threadConsumer);

    void createThread(int groupId, String title, String message, int authorId, Consumer<Integer> threadId);

    void createReply(int groupId, int threadId, String message, int authorId, Consumer<Integer> messageId);

    void saveMessageState(int messageId, int state, int modId, String modUsername);

    void saveMessageLock(int messageId, boolean locked, int modId, String modUsername);
}
