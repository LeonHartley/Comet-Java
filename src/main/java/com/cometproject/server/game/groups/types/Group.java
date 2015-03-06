package com.cometproject.server.game.groups.types;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.components.MembershipComponent;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.outgoing.group.GroupInformationMessageComposer;


public class Group {
    /**
     * The ID of the group
     */
    private int id;

    /**
     * The component which will handle everything member-related
     */
    private MembershipComponent membershipComponent;

    /**
     * Initialize the group instance
     *
     * @param id The ID of the group
     */
    public Group(int id) {
        this.id = id;

        this.membershipComponent = new MembershipComponent(this.id);
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
        return GroupManager.getInstance().getData(this.id);
    }

    /**
     * Get the membership component
     *
     * @return The component which will handle everything member-related
     */
    public MembershipComponent getMembershipComponent() {
        return membershipComponent;
    }
}
