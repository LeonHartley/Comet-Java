package com.cometproject.storage.mysql.repositories;

import com.cometproject.api.game.groups.types.GroupType;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.storage.api.repositories.IGroupRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.models.factories.GroupDataFactory;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

public class MySQLGroupRepository extends MySQLRepository implements IGroupRepository {

    private final GroupDataFactory groupDataFactory;

    public MySQLGroupRepository(GroupDataFactory groupDataFactory, MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);

        this.groupDataFactory = groupDataFactory;
    }

    @Override
    public void getDataById(int groupId, Consumer<IGroupData> consumer) {
        select("SELECT g.name, g.description, g.badge, g.owner_id, g.room_id, g.created, g.`type`, g.colour1, " +
                "g.colour2, g.members_deco, g.has_forum, p.username as owner_name FROM groups g " +
                "RIGHT JOIN players AS p ON p.id = g.owner_id where g.id = ?", (data -> {
            final String name = data.readString("name");
            final String description = data.readString("description");
            final String badge = data.readString("badge");
            final int ownerId = data.readInteger("owner_id");
            final int roomId = data.readInteger("room_id");
            final int created = data.readInteger("created");
            final GroupType groupType = GroupType.valueOf(data.readInteger("type"));
            final int colour1 = data.readInteger("colour1");
            final int colour2 = data.readInteger("colour2");
            final boolean membersDeco = data.readBoolean("members_deco");
            final boolean hasForum = data.readBoolean("has_forum");
            final String username = data.readString("owner_name");

            consumer.accept(this.groupDataFactory.create(groupId, name, description, badge, ownerId, username, roomId, created, groupType,
                    colour1, colour2, membersDeco, hasForum));
        }), groupId);
    }

    @Override
    public void saveGroupData(IGroupData groupData) {
        update("UPDATE groups SET name = ?, description = ?, badge = ?, owner_id = ?, room_id = ?, type = ?, " +
                        "colour1 = ?, colour2 = ?, members_deco = ?, has_forum = ? WHERE id = ?",
                groupData.getTitle(), groupData.getDescription(), groupData.getBadge(), groupData.getOwnerId(),
                groupData.getRoomId(), groupData.getType().toString().toLowerCase(),
                groupData.getColourA(), groupData.getColourB(), groupData.canMembersDecorate() ? "1" : "0",
                groupData.hasForum());
    }

    @Override
    public void create(IGroupData groupData, Consumer<IGroupData> consumer) {

    }

    @Override
    public int getIdByRoomId(int roomId, Consumer<Integer> consumer) {
        return 0;
    }

    @Override
    public void deleteGroup(int groupId) {

    }

    @Override
    public void getGroupsByPlayerId(final int playerId, Consumer<List<IGroupData>> consumer) {
        final List<IGroupData> groups = Lists.newArrayList();

        select("SELECT g.id, g.name, g.description, g.badge, g.owner_id, g.room_id, g.created, g.`type`, g.colour1, " +
                "g.colour2, g.members_deco, g.has_forum, p.username as owner_name FROM groups g " +
                "RIGHT JOIN players AS p ON p.id = g.owner_id where g.id = ?", (data -> {
            final int groupId = data.readInteger("id");
            final String name = data.readString("name");
            final String description = data.readString("description");
            final String badge = data.readString("badge");
            final int ownerId = data.readInteger("owner_id");
            final int roomId = data.readInteger("room_id");
            final int created = data.readInteger("created");
            final GroupType groupType = GroupType.valueOf(data.readInteger("type"));
            final int colour1 = data.readInteger("colour1");
            final int colour2 = data.readInteger("colour2");
            final boolean membersDeco = data.readBoolean("members_deco");
            final boolean hasForum = data.readBoolean("has_forum");
            final String username = data.readString("owner_name");

            groups.add(this.groupDataFactory.create(groupId, name, description, badge, ownerId, username, roomId, created, groupType,
                    colour1, colour2, membersDeco, hasForum));
        }), playerId);

        consumer.accept(groups);
    }
}
