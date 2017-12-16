package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.groups.types.components.membership.GroupAccessLevel;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;

import java.util.List;
import java.util.function.Consumer;

public interface IGroupMemberRepository {
    void getAllByGroupId(int groupId, Consumer<List<IGroupMember>> groupMembers);

    void saveMember(IGroupMember groupMember);

    void create(int groupId, int playerId, GroupAccessLevel accessLevel, Consumer<IGroupMember> member);

    void delete(int groupMembershipId);

    void createRequest(int groupId, int playerId);

    void deleteRequest(int groupId, int playerId);

    void clearRequests(int groupId);

    void getAllRequests(int groupId, Consumer<List<Integer>> requestsConsumer);
}
