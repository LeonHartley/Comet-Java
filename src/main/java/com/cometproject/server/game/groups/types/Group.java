package com.cometproject.server.game.groups.types;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.components.MembershipComponent;

import java.util.List;

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
     * @param id The ID of the group
     */
    public Group(int id) {
        this.id = id;

        this.membershipComponent = new MembershipComponent(this.id);
    }

    /**
     * Get the ID of the group
     * @return The ID of the group
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the data assigned to a group instance (by the ID)
     * @return The data object
     */
    public GroupData getData() {
        return CometManager.getGroups().getData(this.id);
    }

    /**
     * Get the membership component
     * @return The component which will handle everything member-related
     */
    public MembershipComponent getMembershipComponent() {
        return membershipComponent;
    }
}
