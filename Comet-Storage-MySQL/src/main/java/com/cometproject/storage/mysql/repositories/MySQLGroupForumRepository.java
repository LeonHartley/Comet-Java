package com.cometproject.storage.mysql.repositories;

import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.storage.api.repositories.IGroupForumRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.results.IResultReader;
import com.cometproject.storage.mysql.models.factories.GroupForumSettingsFactory;

import java.util.Map;
import java.util.function.Consumer;

public class MySQLGroupForumRepository extends MySQLRepository implements IGroupForumRepository {

    private final GroupForumSettingsFactory forumSettingsFactory;

    public MySQLGroupForumRepository(GroupForumSettingsFactory forumSettingsFactory, MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);

        this.forumSettingsFactory = forumSettingsFactory;
    }

    @Override
    public void getSettingsByGroupId(int groupId, Consumer<IForumSettings> forumSettingsConsumer) {
        select("SELECT * FROM group_forum_settings WHERE group_id = ?", (data) -> {
            forumSettingsConsumer.accept(this.buildSettings(groupId, data));
        }, groupId);
    }

    @Override
    public void saveSettings(IForumSettings forumSettings) {
        update("UPDATE group_forum_settings SET read_permission = ?, post_permission = ?, thread_permission = ?, " +
                "moderate_permission = ? WHERE group_id = ?",
                forumSettings.getReadPermission().toString(),
                forumSettings.getPostPermission().toString(),
                forumSettings.getStartThreadsPermission().toString(),
                forumSettings.getModeratePermission().toString());
    }

    @Override
    public void getAllMessages(Integer groupId, Consumer<Map<Integer, IForumThread>> threadConsumer) {

    }

    @Override
    public void createThread(int groupId, String title, String message, int authorId, Consumer<Integer> threadId) {

    }

    @Override
    public void createReply(int groupId, int threadId, String message, int authorId, Consumer<Integer> messageId) {

    }

    @Override
    public void saveMessageState(int messageId, int state, int modId, String modUsername) {

    }

    @Override
    public void saveMessageLock(int messageId, boolean locked, int modId, String modUsername) {

    }

    private IForumSettings buildSettings(final int groupId, IResultReader resultReader) throws Exception {
        return this.forumSettingsFactory.createSettings(groupId,
                ForumPermission.valueOf(resultReader.readString("read_permission")),
                ForumPermission.valueOf(resultReader.readString("post_permission")),
                ForumPermission.valueOf(resultReader.readString("thread_permission")),
                ForumPermission.valueOf(resultReader.readString("moderate_permission")));
    }
}
