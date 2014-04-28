package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.google.gson.Gson;

import java.sql.PreparedStatement;

public class ChangeHomeRoomMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        Room room = client.getPlayer().getEntity().getRoom();

        int roomId = room.getId();
        client.getPlayer().getSettings().setHomeRoom(roomId);

        client.send(HomeRoomMessageComposer.compose(roomId));

        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE player_settings SET home_room = ? WHERE player_id = ?");

            statement.setInt(1, roomId);
            statement.setInt(2, client.getPlayer().getId());

            statement.executeUpdate();
        } catch(Exception e) {
            GameEngine.getLogger().error("Error while saving player wardrobe", e);
        }
    }
}
