package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import org.apache.commons.lang.StringUtils;

public class ManageGroupMessageComposer {
    public static Composer compose(Group group) {
        Composer msg = new Composer(Composers.GroupDataEditMessageComposer);

        msg.writeInt(1); // Array for something related to rooms (int:roomId, String:roomName, Boolean:Unk)

        msg.writeInt(1);
        msg.writeString("Yes");
        msg.writeBoolean(false);

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

        int amountOfData = 5 - badgeData.length;
        int dataAppended = 0;

        for (int i = 0; i < badgeData.length; i++) {
            String text = badgeData[i];

            int num1 = (text.length() >= 6) ? Integer.parseInt(StringUtils.left(text, 3)) : Integer.parseInt(StringUtils.left(text, 2));
            int num2 = (text.length() >= 6) ? Integer.parseInt(StringUtils.left(StringUtils.right(text, 3), 2)) : Integer.parseInt(StringUtils.right(StringUtils.left(text, 4), 2));

            msg.writeInt(num1);
            msg.writeInt(num2);

            if(text.length() < 5) {
                msg.writeInt(0);
            } else if(text.length() >= 6) {
                msg.writeInt(Integer.parseInt(StringUtils.right(text, 1)));
            } else {
                msg.writeInt(Integer.parseInt(StringUtils.right(text, 1)));
            }
        }

        while(dataAppended != amountOfData) {
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            dataAppended++;
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
