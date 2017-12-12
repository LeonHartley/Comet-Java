package com.cometproject.api.game.groups;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;

public interface IGroupService {

    IGroupData getData(int groupId);

    IGroup getGroup(int groupId);

    void addGroupMember(IGroup group, IGroupMember groupMember);

    void removeGroupMember(IGroup group, IGroupMember groupMember);

    void createRequest(IGroup group, int playerId);

    void removeRequest(IGroup group, int playerId);

    void clearRequests(IGroup group);

    IGroupItemService getItemService();
}
