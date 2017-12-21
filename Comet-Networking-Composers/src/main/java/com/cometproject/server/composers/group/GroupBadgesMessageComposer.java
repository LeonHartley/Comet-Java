package com.cometproject.server.composers.group;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;


public class GroupBadgesMessageComposer extends MessageComposer {
    private final Map<Integer, String> badges;

    private final int groupId;
    private final String badge;

    public GroupBadgesMessageComposer(final Map<Integer, String> badges) {
        this.badges = badges;
        this.groupId = 0;
        this.badge = null;
    }

    public GroupBadgesMessageComposer(final int groupId, final String badge) {
        this.badges = null;
        this.groupId = groupId;
        this.badge = badge;
    }

    @Override
    public short getId() {
        return Composers.HabboGroupBadgesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (this.badges != null) {
            msg.writeInt(badges.size());

            for (Map.Entry<Integer, String> badge : badges.entrySet()) {
                this.composeGroupBadge(msg, badge.getKey(), badge.getValue());
            }
        } else {
            msg.writeInt(1);

            this.composeGroupBadge(msg, this.groupId, this.badge);
        }
    }

    private void composeGroupBadge(final IComposer msg, final int groupId, final String badge) {
        msg.writeInt(groupId);
        msg.writeString(badge);
    }
}
