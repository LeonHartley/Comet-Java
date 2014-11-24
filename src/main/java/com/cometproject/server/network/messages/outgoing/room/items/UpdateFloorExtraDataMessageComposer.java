package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UpdateFloorExtraDataMessageComposer {
    public static Composer compose(int id, RoomItemFloor floorItem) {
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

        } else {
            msg.writeString(id);
            msg.writeInt(0);
            msg.writeString(floorItem.getExtraData());
        }

        return msg;
    }
}
