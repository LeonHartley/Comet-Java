package com.cometproject.server.game.groups.types.components;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.storage.queries.groups.GroupMemberDao;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MembershipComponent {
    /**
     * The ID of the group
     */
    private int groupId;

    /**
     * All members of this group
     */
    private Map<Integer, GroupMember> groupMembers;

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
     * @param groupId The ID of the group
     */
    public MembershipComponent(int groupId) {
        this.groupId = groupId;

        this.groupMembers = new FastMap<>();
        this.groupAdministrators = new FastSet<>();
        this.groupMembershipRequests = new FastSet<>();

        this.loadMemberships();
    }

    /**
     * Load members of this group from the database
     */
    private void loadMemberships() {
        for (GroupMember groupMember : GroupMemberDao.getAllByGroupId(this.groupId)) {
            this.createMembership(groupMember);
        }

        for (Integer playerId : GroupMemberDao.getAllRequestsByGroupId(groupId)) {
            this.groupMembershipRequests.add(playerId);
        }
    }

    /**
     * Add a new member to the group
     *
     * @param groupMember The new member
     */
    public void createMembership(GroupMember groupMember) {
        if (groupMember.getMembershipId() == 0)
            groupMember.setMembershipId(GroupMemberDao.create(groupMember));

        if (groupMembers.containsKey(groupMember.getPlayerId()))
            groupMembers.remove(groupMember.getPlayerId());

        if (groupMember.getAccessLevel().isAdmin())
            this.groupAdministrators.add(groupMember.getPlayerId());

        groupMembers.put(groupMember.getPlayerId(), groupMember);
    }

    /**
     * Remove a player's membership to the group
     *
     * @param playerId The ID of the player to remove
     */
    public void removeMembership(int playerId) {
        if (!groupMembers.containsKey(playerId))
            return;

        int groupMembershipId = groupMembers.get(playerId).getMembershipId();

        GroupMemberDao.delete(groupMembershipId);

        groupMembers.remove(playerId);

        if (groupAdministrators.contains(playerId))
            groupAdministrators.remove(playerId);
    }

    /**
     * Add a new membership request to the group
     *
     * @param playerId The ID of the player who is requesting to join
     */
    public void createRequest(int playerId) {
        if (groupMembers.containsKey(playerId))
            return;

        if (groupMembershipRequests.contains(playerId))
            return;

        groupMembershipRequests.add(playerId);
        GroupMemberDao.createRequest(groupId, playerId);
    }

    /**
     * Clears all membership requests
     */
    public void clearRequests() {
        if (groupMembershipRequests.size() == 0)
            return;

        groupMembershipRequests.clear();
        GroupMemberDao.clearRequests(groupId);
    }

    /**
     * Removes membership request
     */
    public void removeRequest(int playerId) {
        if (!groupMembershipRequests.contains(playerId))
            return;

        groupMembershipRequests.remove(playerId);

        GroupMemberDao.deleteRequest(groupId, playerId);
    }

    /**
     * Clears all lists associated with this object
     */
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
    public Map<Integer, GroupMember> getMembers() {
        return groupMembers;
    }

    /**
     * Get the members of the group in a list
     *
     * @return The members of the group in a list
     */
    public List<GroupMember> getMembersAsList() {
        List<GroupMember> groupMembers = new ArrayList<>();

        for (GroupMember groupMember : this.getMembers().values()) {
            groupMembers.add(groupMember);
        }

        return groupMembers;
    }

    /**
     * Get the administrators of the group
     *
     * @return The administrators of the group
     */
    public Set<Integer> getAdministrators() {
        return groupAdministrators;
    }

    /**
     * Get the membership requests of the group
     *
     * @return The membership requests of the group
     */
    public Set<Integer> getMembershipRequests() {
        return groupMembershipRequests;
    }

    /**
     * Get the group that this component is assigned to
     *
     * @return The group that this component is assigned to
     */
    private Group getGroup() {
        return GroupManager.getInstance().get(this.groupId);
    }
}
