package com.cometproject.server.game.groups;

import com.cometproject.server.game.groups.items.GroupItemManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupData;
import org.apache.log4j.Logger;
import org.apache.solr.search.LRUCache;

public class GroupManager {
    private GroupItemManager groupItems;

    private LRUCache<Integer, GroupData> groupData;
    private LRUCache<Integer, Group> groupInstances;

    private Logger log = Logger.getLogger(GroupManager.class.getName());

    public GroupManager() {
        this.groupItems = new GroupItemManager();
    }

    public GroupData getData(int id) {
        if(groupData.get(id) != null)
            return groupData.get(id);

        // Grab data from the database
        return null;
    }

    public GroupItemManager getGroupItems() {
        return groupItems;
    }
}
