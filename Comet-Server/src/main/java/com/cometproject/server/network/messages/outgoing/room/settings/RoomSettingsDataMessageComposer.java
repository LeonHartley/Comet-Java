package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class RoomSettingsDataMessageComposer extends MessageComposer {
    private final Room room;
    private final boolean staff;

    public RoomSettingsDataMessageComposer(Room room, boolean staff) {
        this.room = room;
        this.staff = staff;
    }

    @Override
    public short getId() {
        return Composers.RoomSettingsDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(room.getData().getId());
        msg.writeString(room.getData().getName());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getData().getAccess()));
        msg.writeInt(room.getData().getCategoryId());
        msg.writeInt(room.getData().getMaxUsers());
        msg.writeInt(staff ? 500 : CometSettings.roomMaxPlayers);
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
        msg.writeBoolean(true);//??
        msg.writeInt(room.getData().getMuteState().getState());
        msg.writeInt(room.getData().getKickState().getState());
        msg.writeInt(room.getData().getBanState().getState());
    }
}
