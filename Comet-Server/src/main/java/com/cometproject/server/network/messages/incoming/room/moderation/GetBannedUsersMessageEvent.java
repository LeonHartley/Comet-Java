package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomBannedListMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class GetBannedUsersMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();

        if ((room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().getRank().roomFullControl())) {
            return;
        }

        if (Comet.getTime() - client.getPlayer().lastBannedListRequest < 5) {
            return;
        }

        client.getPlayer().lastBannedListRequest = (int) Comet.getTime();

        client.send(new RoomBannedListMessageComposer(room.getId(), room.getRights().getBannedPlayers()));
    }
}
