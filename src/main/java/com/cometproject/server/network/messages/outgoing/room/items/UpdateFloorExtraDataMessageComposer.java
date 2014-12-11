package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.utilities.attributes.Stateable;


public class UpdateFloorExtraDataMessageComposer {
    public static Composer compose(int id, RoomItemFloor floorItem, boolean useGroupItem) {
        Composer msg = new Composer(Composers.UpdateFloorItemExtraDataMessageComposer);

        if (floorItem instanceof MannequinFloorItem) {
            msg.writeString(String.valueOf(id));

            msg.writeInt(1);
            msg.writeInt(3);

            msg.writeString("FIGURE");
            msg.writeString(((MannequinFloorItem) floorItem).getFigure());
            msg.writeString("GENDER");
            msg.writeString(((MannequinFloorItem) floorItem).getGender());
            msg.writeString("OUTFIT_NAME");
            msg.writeString(((MannequinFloorItem) floorItem).getName());
        } else if (floorItem instanceof GroupFloorItem && useGroupItem) {
            GroupData groupData = GroupManager.getInstance().getData(((GroupFloorItem) floorItem).getGroupId());

            msg.writeString(id);

            msg.writeInt(0);
            if (groupData == null) {
                msg.writeInt(0);
            } else {
                msg.writeInt(2);
                msg.writeInt(5);
                msg.writeString(groupData.getTitle());
                msg.writeString(floorItem.getExtraData());
                msg.writeString(groupData.getBadge());

                String colourA = GroupManager.getInstance().getGroupItems().getSymbolColours().get(groupData.getColourA()).getColour();
                String colourB = GroupManager.getInstance().getGroupItems().getBackgroundColours().get(groupData.getColourB()).getColour();

                msg.writeString(colourA);
                msg.writeString(colourB);
            }
        } else {
            msg.writeString(id);
            msg.writeInt(0);
            msg.writeString(floorItem instanceof Stateable ? (((Stateable) floorItem).getState() ? "1" : "0") : floorItem.getExtraData());
        }

        return msg;
    }

    public static Composer compose(int id, RoomItemFloor floorItem) {
        return compose(id, floorItem, true);
    }
}
