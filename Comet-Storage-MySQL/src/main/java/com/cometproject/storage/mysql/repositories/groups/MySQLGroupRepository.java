package com.cometproject.storage.mysql.repositories.groups;

import com.cometproject.api.game.groups.types.GroupType;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.storage.api.repositories.groups.IGroupRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.results.IResultReader;
import com.cometproject.storage.mysql.models.factories.GroupDataFactory;
import com.cometproject.storage.mysql.repositories.MySQLRepository;
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
        select("SELECT g.id, g.name, g.description, g.badge, g.owner_id, g.room_id, g.created, g.`type`, g.colour1, " +
                "g.colour2, g.members_deco, g.has_forum, p.username as owner_name FROM groups g " +
                "RIGHT JOIN players AS p ON p.id = g.owner_id where g.id = ?", (data -> consumer.accept(this.readGroup(data))), groupId);
    }

    @Override
    public void saveGroupData(IGroupData groupData) {
        update("UPDATE groups SET name = ?, description = ?, badge = ?, owner_id = ?, room_id = ?, type = ?, " +
                        "colour1 = ?, colour2 = ?, members_deco = ?, has_forum = ? WHERE id = ?",
                groupData.getTitle(), groupData.getDescription(), groupData.getBadge(), groupData.getOwnerId(),
                groupData.getRoomId(), groupData.getType().toString().toLowerCase(),
                groupData.getColourA(), groupData.getColourB(), groupData.canMembersDecorate() ? "1" : "0",
                groupData.hasForum() ? "1" : "0", groupData.getId());
    }

    @Override
    public void create(IGroupData groupData) {
        insert("INSERT into groups (`name`, `description`, `badge`, `owner_id`, `room_id`, `created`, `type`, `colour1`, `colour2`, `members_deco`, `has_forum`) " +
                "VALUES(? ,?, ?, ?, ?, ?, ?, ?, ?, ?, '0');", (key) -> {
            final int groupId = key.readInteger(1);

            groupData.setId(groupId);
        }, groupData.getTitle(), groupData.getDescription(), groupData.getBadge(), groupData.getOwnerId(), groupData.getRoomId(),
                groupData.getCreatedTimestamp(), groupData.getType().toString().toLowerCase(), groupData.getColourA(),
                groupData.getColourB(), groupData.canMembersDecorate() ? "1" : "0");
    }

    @Override
    public void getGroupIdByRoomId(int roomId, Consumer<Integer> consumer) {
        select("SELECT g.id FROM groups g where g.room_id = ?", (data -> consumer.accept(data.readInteger(1))));
    }

    @Override
    public void deleteGroup(int groupId) {
        transaction((transaction -> {
            update("DELETE FROM groups WHERE id = ?;", transaction, groupId);
            update("DELETE FROM group_memberships WHERE group_id = ?;", transaction, groupId);
            update("DELETE FROM group_requests WHERE group_id = ?;", transaction, groupId);
            update("UPDATE players SET favourite_group = 0 WHERE favourite_group = ?;", transaction, groupId);

            transaction.commit();
        }));
    }

    @Override
    public void getGroupIdsByPlayerId(final int playerId, Consumer<List<Integer>> consumer) {
        final List<Integer> groups = Lists.newArrayList();

        select("SELECT `group_id` FROM group_memberships WHERE player_id = ? ORDER BY id DESC", (data -> {

            groups.add((data.readInteger(1)));
        }), playerId);

        consumer.accept(groups);
    }

    private IGroupData readGroup(IResultReader data) throws Exception {
        final int groupId = data.readInteger("id");
        final String name = data.readString("name");
        final String description = data.readString("description");
        final String badge = data.readString("badge");
        final int ownerId = data.readInteger("owner_id");
        final int roomId = data.readInteger("room_id");
        final int created = data.readInteger("created");
        final GroupType groupType = GroupType.valueOf(data.readString("type").toUpperCase());
        final int colour1 = data.readInteger("colour1");
        final int colour2 = data.readInteger("colour2");
        final boolean membersDeco = data.readBoolean("members_deco");
        final boolean hasForum = data.readBoolean("has_forum");
        final String username = data.readString("owner_name");

        return this.groupDataFactory.create(groupId, name, description, badge, ownerId, username, roomId, created, groupType,
                colour1, colour2, membersDeco, hasForum);
    }
}
