package com.cometproject.api.game.groups;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;

public interface IGroupService {

    IGroupData getData(int groupId);

    IGroup getGroup(int groupId);

    IGroupItemService getItemService();

}
