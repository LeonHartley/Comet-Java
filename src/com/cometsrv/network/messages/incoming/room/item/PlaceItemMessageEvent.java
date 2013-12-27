package com.cometsrv.network.messages.incoming.room.item;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.players.components.types.InventoryItem;
import com.cometsrv.game.rooms.avatars.misc.Position;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometsrv.network.messages.outgoing.room.items.SendWallItemMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

import java.sql.PreparedStatement;

public class PlaceItemMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String[] parts = msg.readString().split(" ");
        int id = Integer.parseInt(parts[0].replace("-", ""));

        if(!client.getPlayer().getData().getUsername().equals(client.getPlayer().getAvatar().getRoom().getData().getOwner())) {
            return;
        }

        try {
            if(parts[1].startsWith(":")) {
                String position = Position.validateWallPosition(parts[1] + " " + parts[2] + " " + parts[3]);

                if(position == null) {
                    return;
                }

                InventoryItem item = client.getPlayer().getInventory().getWallItem(id);
                PreparedStatement query = Comet.getServer().getStorage().prepare("UPDATE items SET room_id = ?, wall_pos = ?, extra_data = ? WHERE id = ?;");

                query.setInt(1, client.getPlayer().getAvatar().getRoomId());
                query.setString(2, position);
                query.setString(3, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData());
                query.setInt(4, item.getId());

                query.executeUpdate();

                client.getPlayer().getInventory().removeWallItem(id);
                client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(
                        SendWallItemMessageComposer.compose(
                                client.getPlayer().getAvatar().getRoom().getItems().addWallItem(id, item.getBaseId(), client.getPlayer().getId(), position, (item.getExtraData().isEmpty() || item.getExtraData() == " ") ? "0" : item.getExtraData()),
                                client.getPlayer().getAvatar().getRoom()
                        )
                );
            } else {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int rot = Integer.parseInt(parts[3]);

                InventoryItem item = client.getPlayer().getInventory().getFloorItem(id);
                float height = (float) client.getPlayer().getAvatar().getRoom().getModel().getSquareHeight()[x][y];

                for(FloorItem stackItem : client.getPlayer().getAvatar().getRoom().getItems().getItemsOnSquare(x, y)) {
                    if(item.getId() != stackItem.getId()) {
                        // TODO: re-do the stack heights in the database (for all items)
                        if(stackItem.getDefinition().canStack) {
                            height+= stackItem.getDefinition().getHeight();
                        } else {
                            return;
                        }
                    }
                }

                PreparedStatement query = Comet.getServer().getStorage().prepare("UPDATE items SET room_id = ?, x = ?, y = ?, z = ?, rot = ?, extra_data = ? WHERE id = ?;");

                query.setInt(1, client.getPlayer().getAvatar().getRoomId());
                query.setInt(2, x);
                query.setInt(3, y);
                query.setFloat(4, height);
                query.setInt(5, rot);
                query.setString(6, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData());
                query.setInt(7, id);

                query.executeUpdate();

                client.getPlayer().getInventory().removeFloorItem(id);
                client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(
                        SendFloorItemMessageComposer.compose(
                                client.getPlayer().getAvatar().getRoom().getItems().addFloorItem(id, item.getBaseId(), client.getPlayer().getId(), x, y, rot, height, (item.getExtraData().isEmpty() || item.getExtraData() == " ") ? "0" : item.getExtraData()),
                                client.getPlayer().getAvatar().getRoom()
                        )
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
