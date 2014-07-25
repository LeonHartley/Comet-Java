package com.cometproject.server.game.groups;

import com.cometproject.server.game.groups.items.GroupItemManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.storage.queries.groups.GroupDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.apache.solr.search.LRUCache;
import org.apache.solr.util.ConcurrentLRUCache;

import java.util.ArrayList;
import java.util.Map;

public class GroupManager {
    /**
     * The amount of group instances allowed in the cache, when this
     * is reached, the group cache will remove the oldest entries
     */
    private static final int INSTANCE_LRU_MAX_ENTRIES = 500;

    /**
     * When the max entries is reached, the cache will remove old entries
     * until the count reaches this number
     */
    private static final int INSTANCE_LRU_LOWER_WATERMARK = 100;

    /**
     * The amount of group data instances allowed in the cache, when this
     * is reached, the group data cache will remove the oldest entries
     */
    private static final int DATA_LRU_MAX_ENTRIES = 1500;

    /**
     * When the max entries is reached, the cache will remove old entries
     * until the count reaches this number
     */
    private static final int DATA_LRU_LOWER_WATERMARK = 800;

    /**
     * The manager of the group items (for badges and colours)
     */
    private GroupItemManager groupItems;

    /**
     * The cache which stores all group data. This cache follows the
     * LRU design
     */
    private ConcurrentLRUCache<Integer, GroupData> groupData;

    /**
     * The cache which stores all group instances. This cache
     * follows the LRU design
     */
    private ConcurrentLRUCache<Integer, Group> groupInstances;

    /**
     * Stores room ID by group ID, so we can retrieve groups faster
     */
    private Map<Integer, Integer> roomIdToGroupId;

    /**
     * Used for logging
     */
    private Logger log = Logger.getLogger(GroupManager.class.getName());

    /**
     * Initialize the group manager
     */
    public GroupManager() {
        this.groupItems = new GroupItemManager();

        this.groupData = new ConcurrentLRUCache<>(DATA_LRU_MAX_ENTRIES, DATA_LRU_LOWER_WATERMARK);
        this.groupInstances = new ConcurrentLRUCache<>(INSTANCE_LRU_MAX_ENTRIES, INSTANCE_LRU_LOWER_WATERMARK);

        this.roomIdToGroupId = new FastMap<>();
    }

    /**
     * Get group data from the cache or from the database (which would then be
     * cached for later use)
     * @param id The ID of the group
     * @return Group data instance
     */
    public GroupData getData(int id) {
        if(this.groupData.get(id) != null)
            return this.groupData.get(id);

        GroupData groupData = GroupDao.getDataById(id);

        if(groupData != null)
            this.groupData.put(id, groupData);

        return groupData;
    }

    /**
     * Get the group instance
     * @param id The ID of the group
     * @return Group instance
     */
    public Group get(int id) {
        Group groupInstance = this.groupInstances.get(id);

        if(groupInstance != null)
            return groupInstance;

        if(this.getData(id) == null) {
            // Group doesn't exist!
            return null;
        }

        groupInstance = this.load(id);

        if(groupInstance != null)
            this.groupInstances.put(id, groupInstance);

        return groupInstance;
    }

    /**
     * Creates a group instance based on the data provided
     * @param groupData Group data of the group we want to create
     * @return Group instance
     */
    public Group createGroup(GroupData groupData) {
        int groupId = GroupDao.create(groupData);

        groupData.setId(groupId);
        this.groupData.put(groupId, groupData);

        Group groupInstance = new Group(groupId);
        this.groupInstances.put(groupId, groupInstance);

        return groupInstance;
    }

    /**
     * Get a group by a room ID
     * @param roomId The ID of the room we want to use to fetch a group
     * @return The group instance
     */
    public Group getGroupByRoomId(int roomId) {
        if(this.roomIdToGroupId.containsKey(roomId))
            return this.get(roomIdToGroupId.get(roomId));

        int groupId = GroupDao.getIdByRoomId(roomId);

        if(groupId != 0)
            this.roomIdToGroupId.put(roomId, groupId);

        return this.get(groupId);
    }

    /**
     * Load the group by id from database
     * @param id The ID of the group
     * @return Group instance
     */
    private Group load(int id) {
        return new Group(id);
    }

    /**
     * Group items manager
     * @return Group items manager
     */
    public GroupItemManager getGroupItems() {
        return groupItems;
    }
}
