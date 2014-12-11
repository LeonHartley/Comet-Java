package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class ChangeHomeRoomMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        Room room = client.getPlayer().getEntity().getRoom();

        int roomId = room.getId();
        client.getPlayer().getSettings().setHomeRoom(roomId);

        client.send(HomeRoomMessageComposer.compose(roomId));

        PlayerDao.updateHomeRoom(roomId, client.getPlayer().getId());
    }
}
