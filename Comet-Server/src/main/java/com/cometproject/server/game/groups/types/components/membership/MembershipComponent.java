package com.cometproject.server.game.groups.types.components.membership;

import com.cometproject.api.game.groups.types.components.IMembershipComponent;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.api.game.groups.types.GroupComponent;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.storage.api.StorageContext;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.collections4.set.ListOrderedSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MembershipComponent implements IMembershipComponent, GroupComponent {
    /**
     * The instance of the group
     */
    private Group group;

    /**
     * All members of this group
     */
    private Map<Integer, IGroupMember> groupMembers;

    /**
     * Administrators of this group
     */
    private Set<Integer> groupAdministrators;

    /**
     * The IDs of players who are still not accepted to join
     */
    private Set<Integer> groupMembershipRequests;

    /**
     * Initialize the MembershipComponent
     *
     * @param group The ID of the group
     */
    public MembershipComponent(Group group) {
        this.group = group;

        this.groupMembers = new ListOrderedMap<>();
        this.groupAdministrators = new ListOrderedSet<>();
        this.groupMembershipRequests = new ListOrderedSet<>();

        this.loadMemberships();
    }

    /**
     * Load members of this group from the database
     */
    @Override
    public void loadMemberships() {
        final List<IGroupMember> members = this.group.getGroupDataObject() != null ? this.group.getGroupDataObject().getGroupMembers() : new ArrayList<>();

        if(members.isEmpty()) {
            StorageContext.getCurrentContext().getGroupMemberRepository().getAllByGroupId(this.group.getId(), members::addAll);
        }

        for (IGroupMember groupMember : members) {
            this.createMembership(groupMember);
        }

        final List<Integer> requests = this.group.getGroupDataObject() != null ?
                this.group.getGroupDataObject().getGroupRequests() : new ArrayList<>();

        if(this.group.getGroupDataObject() == null) {
            StorageContext.getCurrentContext().getGroupMemberRepository().getAllRequests(this.group.getId(), requests::addAll);
        }

        this.groupMembershipRequests.addAll(requests);
    }

    /**
     * Add a new member to the group
     *
     * @param groupMember The new member
     */
    @Override
    public void createMembership(IGroupMember groupMember) {
        boolean needsCommit = false;

        if (groupMember.getMembershipId() == 0) {
            StorageContext.getCurrentContext().getGroupMemberRepository().create(groupMember);

            needsCommit = true;
        }

        if (groupMembers.containsKey(groupMember.getPlayerId()))
            groupMembers.remove(groupMember.getPlayerId());

        if (groupMember.getAccessLevel().isAdmin())
            this.groupAdministrators.add(groupMember.getPlayerId());

        groupMembers.put(groupMember.getPlayerId(), groupMember);

        if (needsCommit) {
            this.group.commit();
        }
    }

    /**
     * Remove a player's membership to the group
     *
     * @param playerId The ID of the player to remove
     */
    @Override
    public void removeMembership(int playerId) {
        if (!groupMembers.containsKey(playerId))
            return;

        int groupMembershipId = groupMembers.get(playerId).getMembershipId();

        StorageContext.getCurrentContext().getGroupMemberRepository().delete(groupMembershipId);

        groupMembers.remove(playerId);

        if (groupAdministrators.contains(playerId))
            groupAdministrators.remove(playerId);

        this.group.commit();
    }

    /**
     * Add a new membership request to the group
     *
     * @param playerId The ID of the player who is requesting to join
     */
    @Override
    public void createRequest(int playerId) {
        if (groupMembers.containsKey(playerId))
            return;

        if (groupMembershipRequests.contains(playerId))
            return;

        groupMembershipRequests.add(playerId);

        StorageContext.getCurrentContext().getGroupMemberRepository().createRequest(this.group.getId(), playerId);

        this.group.commit();
    }

    /**
     * Clears all membership requests
     */
    @Override
    public void clearRequests() {
        if (groupMembershipRequests.size() == 0)
            return;

        groupMembershipRequests.clear();
        StorageContext.getCurrentContext().getGroupMemberRepository().clearRequests(this.group.getId());
    }

    /**
     * Removes membership request
     */
    @Override
    public void removeRequest(int playerId) {
        if (!groupMembershipRequests.contains(playerId))
            return;

        groupMembershipRequests.remove(playerId);

        StorageContext.getCurrentContext().getGroupMemberRepository().deleteRequest(this.group.getId(), playerId);
    }

    /**
     * Broadcasts a message to every online group member
     *
     * @param messageComposer The message payload to send
     * @param sender          The sender (The sender will not be sent the message)
     */
    @Override
    public void broadcastMessage(final IMessageComposer messageComposer, int sender) {
        for (IGroupMember groupMember : this.getMembersAsList()) {
            if (groupMember.getPlayerId() == sender) continue;

            final Session session = NetworkManager.getInstance().getSessions().getByPlayerId(groupMember.getPlayerId());

            if (session != null) {
                session.send(messageComposer);
            }
        }
    }

    /**
     * Clears all lists associated with this object
     */
    @Override
    public void dispose() {
        groupMembers.clear();
        groupAdministrators.clear();
        groupMembershipRequests.clear();
    }

    /**
     * Get the members of the group
     *
     * @return The members of the group
     */
    @Override
    public Map<Integer, IGroupMember> getAll() {
        return groupMembers;
    }

    /**
     * Get the members of the group in a list
     *
     * @return The members of the group in a list
     */
    @Override
    public List<IGroupMember> getMembersAsList() {
        List<IGroupMember> groupMembers = new ArrayList<>();

        groupMembers.addAll(this.getAll().values());

        return groupMembers;
    }

    /**
     * Get the administrators of the group
     *
     * @return The administrators of the group
     */
    @Override
    public Set<Integer> getAdministrators() {
        return groupAdministrators;
    }

    /**
     * Get the membership requests of the group
     *
     * @return The membership requests of the group
     */
    @Override
    public Set<Integer> getMembershipRequests() {
        return groupMembershipRequests;
    }

    /**
     * Get the group that this component is assigned to
     *
     * @return The group that this component is assigned to
     */
    @Override
    public Group getGroup() {
        return this.group;
    }
}
