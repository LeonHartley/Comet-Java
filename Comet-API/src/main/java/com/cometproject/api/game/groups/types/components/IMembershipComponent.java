package com.cometproject.api.game.groups.types.components;

import com.cometproject.api.game.groups.types.GroupComponent;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.networking.messages.IMessageComposer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IMembershipComponent extends GroupComponent {
    void broadcastMessage(IMessageComposer messageComposer, int sender);

    boolean hasMembership(final int playerId);

    Map<Integer, IGroupMember> getAll();

    List<IGroupMember> getMembersAsList();

    Set<Integer> getAdministrators();

    Set<Integer> getMembershipRequests();
}
