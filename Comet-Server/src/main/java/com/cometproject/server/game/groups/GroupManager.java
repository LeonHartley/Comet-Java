package com.cometproject.server.game.groups;

import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.server.game.groups.cache.GroupCacheEventListener;
import com.cometproject.server.game.groups.items.GroupItemManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.mysql.data.Data;
import com.cometproject.storage.mysql.models.GroupData;
import net.sf.ehcache.*;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class GroupManager implements Initialisable {
    /**
     * The global GroupManager instance
     */
    private static GroupManager groupManagerInstance;

    /**
     * The amount of group instances allowed in the cache, when this
     * is reached, the group cache will remove the oldest entries
     */
    private static final int INSTANCE_LRU_MAX_ENTRIES = 5000;

    /**
     * When the max entries is reached, the cache will remove old entries
     * until the count reaches this number
     */
    private static final int INSTANCE_LRU_LOWER_WATERMARK = 3000;

    /**
     * The amount of group data instances allowed in the cache, when this
     * is reached, the group data cache will remove the oldest entries
     */
    private static final int DATA_LRU_MAX_ENTRIES = 10000;

    /**
     * When the max entries is reached, the cache will remove old entries
     * until the count reaches this number
     */
    private static final int DATA_LRU_LOWER_WATERMARK = 6000;

    /**
     * The manager of the group items (for badges and colours)
     */
    private GroupItemManager groupItems;

    /**
     * Stores room ID by group ID, so we can retrieve groups faster
     */
    private Map<Integer, Integer> roomIdToGroupId;

    /**
     * Used for logging
     */
    private Logger log = Logger.getLogger(GroupManager.class.getName());

    private Cache groupDataCache;
    private Cache groupInstanceCache;

    public GroupManager() {

    }

    /**
     * Initialize the group manager
     */
    @Override
    public void initialize() {
        this.groupItems = new GroupItemManager();

        final int oneDay = 24 * 60 * 60;

        this.groupDataCache = new Cache("groupDataCache", 1000, false, false, oneDay, oneDay);
        this.groupInstanceCache = new Cache("groupInstanceCache", 1000, false, false, oneDay, oneDay);

        this.groupInstanceCache.getCacheEventNotificationService().registerListener(new GroupCacheEventListener());

        // TODO: Move the cache manager away from player manager.
        PlayerManager.getInstance().getCacheManager().addCache(this.groupDataCache);
        PlayerManager.getInstance().getCacheManager().addCache(this.groupInstanceCache);

        this.roomIdToGroupId = new ConcurrentHashMap<>();
        log.info("GroupManager initialized");
    }

    public static GroupManager getInstance() {
        if (groupManagerInstance == null) {
            groupManagerInstance = new GroupManager();
        }

        return groupManagerInstance;
    }

    /**
     * Get group data from the cache or from the database (which would then be
     * cached for later use)
     *
     * @param id The ID of the group
     * @return Group data instance
     */
    public IGroupData getData(int id) {
        if (this.groupDataCache.get(id) != null)
            return ((GroupData) this.groupDataCache.get(id).getObjectValue());

        final Data<IGroupData> groupData = new Data<>();
        StorageContext.getCurrentContext().getGroupRepository().getDataById(id, groupData::set);

        if (groupData.has()) {
            final Element element = new Element(id, groupData.get());

            this.groupDataCache.put(element);
        }

        return groupData.get();
    }

    /**
     * Get the group instance
     *
     * @param id The ID of the group
     * @return Group instance
     */
    public Group get(int id) {
        if(id == 0) {
            // speed speed
            return null;
        }

        if(this.groupInstanceCache.get(id) != null) {
            Group group = ((Group) this.groupInstanceCache.get(id).getObjectValue());

            if(group.getData() != null) {
                return group;
            } else {
                this.groupInstanceCache.remove(id);
            }
        }

        Group groupInstance = null;

        if (this.getData(id) == null) {
            return null;
        }

        groupInstance = this.load(id);

        this.groupInstanceCache.put(new Element(id, groupInstance));

        log.trace("Group with id #" + id + " was loaded");

        return groupInstance;
    }

    /**
     * Creates a group instance based on the data provided
     *
     * @param groupData Group data of the group we want to create
     * @return Group instance
     */
    public Group createGroup(IGroupData groupData) {
        StorageContext.getCurrentContext().getGroupRepository().create(groupData);

        this.groupDataCache.put(new Element(groupData.getId(), groupData));

        Group groupInstance = new Group(groupData.getId());
        this.groupInstanceCache.put(new Element(groupData.getId(), groupInstance));

        return groupInstance;
    }

    /**
     * Get a group by a room ID
     *
     * @param roomId The ID of the room we want to use to fetch a group
     * @return The group instance
     */
    public Group getGroupByRoomId(int roomId) {
        // TODO: Optimize this.
        if (this.roomIdToGroupId.containsKey(roomId))
            return this.get(roomIdToGroupId.get(roomId));

        Data<Integer> groupId = new Data<>();
        StorageContext.getCurrentContext().getGroupRepository().getGroupIdByRoomId(roomId, groupId::set);

        if (!groupId.has())
            this.roomIdToGroupId.put(roomId, 0);

        return this.get(groupId.get());
    }

    /**
     * Removes a group from the system
     *
     * @param id The ID of the group to remove
     */
    public void removeGroup(int id) {
        Group group = this.get(id);

        if (group == null)
            return;

        if (this.roomIdToGroupId.containsKey(group.getData().getRoomId())) {
            this.roomIdToGroupId.remove(group.getData().getRoomId());
        }

        this.groupInstanceCache.remove(id);
        this.groupDataCache.remove(id);

        group.getMembershipComponent().dispose();
        StorageContext.getCurrentContext().getGroupRepository().deleteGroup(group.getId());
    }

    /**
     * Load the group by id from database
     *
     * @param id The ID of the group
     * @return Group instance
     */
    private Group load(int id) {
        return new Group(id);
    }

    /**
     * Group items manager
     *
     * @return Group items manager
     */
    public GroupItemManager getGroupItems() {
        return groupItems;
    }

    /**
     * Gets the logger
     *
     * @return Returns the group-related logger
     */
    public Logger getLogger() {
        return log;
    }
}
