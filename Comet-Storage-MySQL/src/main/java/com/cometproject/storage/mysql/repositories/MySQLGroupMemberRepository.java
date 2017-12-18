package com.cometproject.storage.mysql.repositories;

import com.cometproject.api.game.groups.types.components.membership.GroupAccessLevel;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.storage.api.repositories.IGroupMemberRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.results.IResultReader;
import com.cometproject.storage.mysql.models.factories.GroupMemberFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MySQLGroupMemberRepository extends MySQLRepository implements IGroupMemberRepository {

    private final GroupMemberFactory groupMemberFactory;

    public MySQLGroupMemberRepository(GroupMemberFactory groupMemberFactory, MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);

        this.groupMemberFactory = groupMemberFactory;
    }

    @Override
    public void getAllByGroupId(int groupId, Consumer<List<IGroupMember>> groupMembers) {
        final List<IGroupMember> members = new ArrayList<>();

        select("SELECT * FROM group_memberships WHERE group_id = ?;", (data) -> {
            members.add(readMember(groupId, data));
        }, groupId);

        groupMembers.accept(members);
    }

    @Override
    public void saveMember(IGroupMember groupMember) {
        update("UPDATE group_memberships SET access_level = ? WHERE id = ?;",
                groupMember.getAccessLevel().toString().toLowerCase(), groupMember.getMembershipId());
    }

    @Override
    public void create(int groupId, int playerId, GroupAccessLevel accessLevel, Consumer<IGroupMember> member) {
        final int dateJoined = (int) (System.currentTimeMillis() / 1000);

        insert("INSERT into group_memberships (`group_id`, `player_id`, `access_level`, `date_joined`) VALUES(?, ?, ?, ?);",
                (data) -> member.accept(this.groupMemberFactory.create(data.readInteger(1), playerId, groupId, accessLevel, dateJoined)),
                groupId, playerId, accessLevel.toString(), dateJoined);
    }

    @Override
    public void delete(int groupMembershipId) {
        update("DELETE FROM group_memberships WHERE id = ?", groupMembershipId);
    }

    @Override
    public void createRequest(int groupId, int playerId) {
        insert("INSERT into group_requests(`group_id`, `player_id`) VALUES(?, ?);", (data) -> {
        }, groupId, playerId);
    }

    @Override
    public void deleteRequest(int groupId, int playerId) {
        update("DELETE FROM group_requests WHERE group_id = ? AND player_id = ?;", groupId, playerId);
    }

    @Override
    public void clearRequests(int groupId) {
        update("DELETE FROM group_requests WHERE group_id = ?", groupId);
    }

    @Override
    public void getAllRequests(int groupId, Consumer<List<Integer>> requestsConsumer) {
        final List<Integer> requests = new ArrayList<>();

        select("SELECT * FROM group_requests WHERE group_id = ?;", (data) -> {
            requests.add(data.readInteger("player_id"));
        }, groupId);

        requestsConsumer.accept(requests);
    }

    private IGroupMember readMember(int groupId, IResultReader data) throws Exception {
        final int membershipId = data.readInteger("id");
        final int playerId = data.readInteger("player_id");
        final int dateJoined = data.readInteger("date_joined");
        final String accessLevel = data.readString("access_level");

        return this.groupMemberFactory.create(membershipId, playerId, groupId, GroupAccessLevel.valueOf(accessLevel.toUpperCase()), dateJoined);
    }
}
