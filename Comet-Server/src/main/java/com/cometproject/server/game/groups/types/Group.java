package com.cometproject.server.game.groups.types;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.IForumComponent;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.server.composers.group.GroupInformationMessageComposer;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.components.forum.ForumComponent;
import com.cometproject.server.game.groups.types.components.membership.MembershipComponent;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.storage.cache.CacheManager;
import com.cometproject.server.storage.cache.objects.GroupDataObject;
import com.cometproject.server.storage.queries.groups.GroupForumDao;

import java.util.ArrayList;
import java.util.List;


public class Group implements IGroup {
    /**
     * The ID of the group
     */
    private int id;

    private GroupDataObject groupDataObject;

    /**
     * The component which will handle everything member-related
     */
    private IMembershipComponent membershipComponent;

    /**
     * The component which will handle the group forum data
     */
    private IForumComponent forumComponent;

    /**
     * The data of the group
     */
    private final IGroupData groupData;

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

        requests.addAll(this.getMembers().getMembershipRequests());

        return new GroupDataObject(this.id, this.getData(),
                this.getMembers().getMembersAsList(),
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
    @Override
    public MessageComposer composeInformation(boolean flag, int playerId) {
        return new GroupInformationMessageComposer(this, RoomManager.getInstance().getRoomData(this.getData().getRoomId()), flag, playerId == this.getData().getOwnerId(), this.getMembers().getAdministrators().contains(playerId),
                this.getMembers().getAll().containsKey(playerId) ? 1 : this.getMembers().getMembershipRequests().contains(playerId) ? 2 : 0);
    }

    @Override
    public void initializeForum() {
        if (this.groupDataObject != null && this.groupDataObject.getForumThreads() == null) {
            return;
        }

        IForumSettings forumSettings = this.getGroupDataObject() != null ? this.getGroupDataObject().getForumSettings() : GroupForumDao.getSettings(this.id);

        if (forumSettings == null) {
            forumSettings = GroupForumDao.createSettings(this.id);
        }

        this.forumComponent = new ForumComponent(this, forumSettings);
    }

    private boolean disposed = false;

    @Override
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
    @Override
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
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Get the data assigned to a group instance (by the ID)
     *
     * @return The data object
     */
    @Override
    public IGroupData getData() {
        return this.groupData;
    }

    /**
     * Get the membership component
     *
     * @return The component which will handle everything member-related
     */
    @Override
    public IMembershipComponent getMembers() {
        return membershipComponent;
    }

    /**
     * Get the group forum component
     *
     * @return The group forumc component
     */
    @Override
    public IForumComponent getForum() {
        return forumComponent;
    }

    public GroupDataObject getGroupDataObject() {
        return groupDataObject;
    }
}
