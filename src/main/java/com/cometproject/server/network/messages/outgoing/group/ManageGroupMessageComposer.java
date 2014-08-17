package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ManageGroupMessageComposer {
    public static Composer compose(Group group) {
        Composer msg = new Composer(Composers.ManageGroupMessageComposer);

        msg.writeInt(0); // Array for something related to rooms (int:roomId, String:roomName, Boolean:Unk)
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
        msg.writeString(""); // url

        msg.writeInt(5);

        String[] badgeData = group.getData().getBadge().replace("b", "").replace("X", "").split("s");

        for (int i = 0; i != 5; i++) {
            if (badgeData.length <= i) {
                msg.writeInt(0);
                msg.writeInt(0);
                msg.writeInt(0);
                continue;
            }

            msg.writeInt(getInt(badgeData[i].substring(0, 2)));
            msg.writeInt(getInt(badgeData[i].substring(2, 4)));
            msg.writeInt(getInt((badgeData[i]).substring(4)));
        }

        msg.writeString(group.getData().getBadge());
        msg.writeInt(group.getMembershipComponent().getMembers().size());

        return msg;
    }

    private static int getInt(String badgePart) {
        if (badgePart.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(badgePart);
    }
}
