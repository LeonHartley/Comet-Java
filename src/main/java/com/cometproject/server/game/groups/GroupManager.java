package com.cometproject.server.game.groups;

import com.cometproject.server.game.groups.items.GroupItemManager;
import com.cometproject.server.game.groups.items.types.*;
import com.cometproject.server.storage.queries.groups.GroupDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupManager {
    private GroupItemManager groupItems;

    private Logger log = Logger.getLogger(GroupManager.class.getName());

    public GroupManager() {
        this.groupItems = new GroupItemManager();
    }

    public GroupItemManager getGroupItems() {
        return groupItems;
    }
}
