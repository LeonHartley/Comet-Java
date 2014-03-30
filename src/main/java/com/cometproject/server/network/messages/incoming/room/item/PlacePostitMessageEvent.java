package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.SendWallItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.sql.PreparedStatement;

public class PlacePostitMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        /*int itemId = msg.readInt();
        String[] positionData = msg.readString().split(":");
        String position = Position3D.validateWallPosition();

        if (position == null) {
            return;
        }

        InventoryItem item = client.getPlayer().getInventory().getWallItem(itemId);
        PreparedStatement query = Comet.getServer().getStorage().prepare("UPDATE items SET room_id = ?, wall_pos = ?, extra_data = ? WHERE id = ?;");

        query.setInt(1, client.getPlayer().getEntity().getRoom().getId());
        query.setString(2, position);
        query.setString(3, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData());
        query.setInt(4, item.getId());

        query.executeUpdate();

        client.getPlayer().getInventory().removeWallItem(itemId);

        Room r = client.getPlayer().getEntity().getRoom();

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(
                SendWallItemMessageComposer.compose(
                        r.getItems().addWallItem(itemId, item.getBaseId(), client.getPlayer().getId(), r.getId(), position, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData()), r
                )
        );*/
    }
}
