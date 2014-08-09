package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.ctc.wstx.util.StringUtil;
import org.apache.commons.lang.StringUtils;

public class ManageGroupMessageComposer {
    public static Composer compose(Group group) {
        Composer msg = new Composer(Composers.ManageGroupMessageComposer);

        msg.writeInt(0); // Array for something...
        msg.writeBoolean(true);
        msg.writeInt(group.getId());
        msg.writeString(group.getData().getTitle());
        msg.writeString(group.getData().getDescription());
        msg.writeInt(1);
        msg.writeInt(group.getData().getColourA());
        msg.writeInt(group.getData().getColourB());
        msg.writeInt(group.getData().getType().getTypeId());
        msg.writeInt(group.getData().canAdminsDecorate() ? 0 : 1);
        msg.writeBoolean(false);
        msg.writeString("");

        String[] badgeData = StringUtils.split(StringUtils.replace(group.getData().getBadge(), "b", ""), "s");

        msg.writeInt(5);

        int required = 5 - badgeData.length;

        for(String badgePart : badgeData) {
            msg.writeInt(getInt(StringUtils.substring(badgePart, 0, 2)));
            msg.writeInt(getInt(StringUtils.substring(badgePart, 2, 2)));
            msg.writeInt(getInt(StringUtils.substring(badgePart, 4, 1)));
        }

        for(int i = 0; i != required; i++) {
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
        }

        msg.writeString(group.getData().getBadge());
        msg.writeInt(group.getMembershipComponent().getMembers().size());

        return msg;
    }

    private static int getInt(String badgePart) {
        if(badgePart.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(badgePart);
    }
}
