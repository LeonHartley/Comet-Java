package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class GetRoomDataMessageComposer {
    public static Composer compose(Room room, boolean staff) {
        Composer msg = new Composer(Composers.RoomSettingsDataMessageComposer);

        msg.writeInt(room.getId());
        msg.writeString(room.getData().getName());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getData().getAccess()));
        msg.writeInt(room.getData().getCategory().getId());
        msg.writeInt(room.getData().getMaxUsers());
        msg.writeInt(staff ? 500 : CometSettings.maxPlayersInRoom);
        msg.writeInt(room.getData().getTags().length);

        for (String tag : room.getData().getTags()) {
            msg.writeString(tag);
        }

        msg.writeInt(room.getData().getTradeState().getState());
        msg.writeInt(room.getData().isAllowPets() ? 1 : 0); // allow pets
        msg.writeInt(1); // allow pets eat
        msg.writeInt(room.getData().getAllowWalkthrough() ? 1 : 0);
        msg.writeInt(room.getData().getHideWalls() ? 1 : 0);
        msg.writeInt(room.getData().getWallThickness());
        msg.writeInt(room.getData().getFloorThickness());
        msg.writeInt(room.getData().getBubbleMode());
        msg.writeInt(room.getData().getBubbleType());
        msg.writeInt(room.getData().getBubbleScroll());
        msg.writeInt(room.getData().getChatDistance());
        msg.writeInt(room.getData().getAntiFloodSettings());
        msg.writeBoolean(false);//??
        msg.writeInt(room.getData().getMuteState().getState());
        msg.writeInt(room.getData().getKickState().getState());
        msg.writeInt(room.getData().getBanState().getState());

        return msg;
    }
}
