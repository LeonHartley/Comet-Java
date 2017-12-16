package com.cometproject.api.game.groups;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;

import javax.validation.GroupDefinitionException;

public interface IGroupService {

    IGroupData getData(int groupId);

    IGroup getGroup(int groupId);

    void saveGroupData(IGroupData groupData);

    void addForum(IGroup group);

    IGroup createGroup(IGroupData groupData, int ownerId);

    void addGroupMember(IGroup group, IGroupMember groupMember);

    void removeGroupMember(IGroup group, IGroupMember groupMember);

    void createRequest(IGroup group, int playerId);

    void removeRequest(IGroup group, int playerId);

    void clearRequests(IGroup group);

    void saveForumSettings(IForumSettings forumSettings);

    IGroupItemService getItemService();

    void removeGroup(int id);
}
