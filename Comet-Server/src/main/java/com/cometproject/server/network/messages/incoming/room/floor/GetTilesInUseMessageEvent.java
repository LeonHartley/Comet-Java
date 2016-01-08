package com.cometproject.server.network.messages.incoming.room.floor;

import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.floor.FloorPlanDoorMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.floor.TilesInUseMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GetTilesInUseMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() != null) {
            client.send(new TilesInUseMessageComposer(client.getPlayer().getEntity().getRoom().getMapping().tilesWithFurniture()));

            if (client.getPlayer().getEntity() != null) {
                RoomModel model = client.getPlayer().getEntity().getRoom().getModel();

                if (model == null) return;

                client.send(new FloorPlanDoorMessageComposer(model.getDoorX(), model.getDoorY(), model.getDoorRotation()));
            }

        }
    }
}
