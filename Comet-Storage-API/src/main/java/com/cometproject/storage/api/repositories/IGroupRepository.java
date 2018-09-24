package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.IForumComponent;

import java.util.List;
import java.util.function.Consumer;

public interface IGroupRepository {
    void getDataById(final int groupId, Consumer<IGroupData> consumer);

    void saveGroupData(IGroupData groupData);

    void create(IGroupData groupData);

    void getGroupIdByRoomId(int roomId, Consumer<Integer> consumer);

    void deleteGroup(int groupId);

    void getGroupIdsByPlayerId(int playerId, Consumer<List<Integer>> consumer);

    void createForumSettings(IForumComponent forumComponent);
}
