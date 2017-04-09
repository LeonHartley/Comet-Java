package com.cometproject.server.game.groups.types;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.components.forum.ForumComponent;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.game.groups.types.components.membership.MembershipComponent;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.outgoing.group.GroupInformationMessageComposer;
import com.cometproject.server.storage.cache.CacheManager;
import com.cometproject.server.storage.cache.objects.GroupDataObject;
import com.cometproject.server.storage.queries.groups.GroupForumDao;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;


public class Group {
    /**
     * The ID of the group
     */
    private int id;

    private GroupDataObject groupDataObject;

    /**
     * The component which will handle everything member-related
     */
    private MembershipComponent membershipComponent;

    /**
     * The component which will handle the group forum data
     */
    private ForumComponent forumComponent;

    /**
     * The data of the group
     */
    private final GroupData groupData;

    /**
     * Initialize the group instance
     *
     * @param id The ID of the group
     */
    public Group(int id) {
        this.id = id;

        if (CacheManager.getInstance().isEnabled() && CacheManager.getInstance().exists("groups." + id)) {
            groupDataObject = CacheManager.getInstance().get(GroupDataObject.class, "groups." + id);
        }

        if (groupDataObject != null) {
            this.groupData = groupDataObject.getGroupData();
        } else {
            this.groupData = GroupManager.getInstance().getData(id);
        }

        this.membershipComponent = new MembershipComponent(this);

        if (this.getData().hasForum()) {
            this.initializeForum();
        }
    }

    public GroupDataObject getCacheObject() {
        final List<Integer> requests = new ArrayList<>();

        for (Integer request : this.getMembershipComponent().getMembershipRequests()) {
            requests.add(request);
        }

        return new GroupDataObject(this.id, this.getData(),
                this.getMembershipComponent().getMembersAsList(),
                requests,
                this.forumComponent != null ? this.forumComponent.getForumSettings() : null,
                this.forumComponent != null ? this.forumComponent.getForumThreads() : null);
    }

    /**
     * Create a packet containing group information
     *
     * @param flag     Flag sent by the client (Unknown right now...)
     * @param playerId The ID of the player to receive this message
     * @return Packet containing the group information
     */
    public MessageComposer composeInformation(boolean flag, int playerId) {
        return new GroupInformationMessageComposer(this, RoomManager.getInstance().getRoomData(this.getData().getRoomId()), flag, playerId == this.getData().getOwnerId(), this.getMembershipComponent().getAdministrators().contains(playerId),
                this.getMembershipComponent().getMembers().containsKey(playerId) ? 1 : this.getMembershipComponent().getMembershipRequests().contains(playerId) ? 2 : 0);
    }

    public void initializeForum() {
        if (this.groupDataObject != null && this.groupDataObject.getForumThreads() == null) {
            return;
        }

        ForumSettings forumSettings = this.getGroupDataObject() != null ? this.getGroupDataObject().getForumSettings() : GroupForumDao.getSettings(this.id);

        if (forumSettings == null) {
            forumSettings = GroupForumDao.createSettings(this.id);
        }

        this.forumComponent = new ForumComponent(this, forumSettings);
    }

    private boolean disposed = false;

    public void dispose() {
        if (this.disposed) {
            return;
        }

        this.disposed = true;

        this.commit();

        if (this.membershipComponent != null) {
            this.membershipComponent.dispose();
        }

        if (this.forumComponent != null) {
            this.forumComponent.dispose();
        }

        GroupManager.getInstance().getLogger().debug("Group with id #" + this.getId() + " was disposed");
    }

    /**
     * Commits the group data to the cache (if enabled)
     */
    public void commit() {
        if (CacheManager.getInstance().isEnabled()) {
            CacheManager.getInstance().put("groups." + id, this.getCacheObject());
        }
    }

    /**
     * Get the ID of the group
     *
     * @return The ID of the group
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the data assigned to a group instance (by the ID)
     *
     * @return The data object
     */
    public GroupData getData() {
        return this.groupData;
    }

    /**
     * Get the membership component
     *
     * @return The component which will handle everything member-related
     */
    public MembershipComponent getMembershipComponent() {
        return membershipComponent;
    }

    /**
     * Get the group forum component
     *
     * @return The group forumc component
     */
    public ForumComponent getForumComponent() {
        return forumComponent;
    }

    public GroupDataObject getGroupDataObject() {
        return groupDataObject;
    }
}
