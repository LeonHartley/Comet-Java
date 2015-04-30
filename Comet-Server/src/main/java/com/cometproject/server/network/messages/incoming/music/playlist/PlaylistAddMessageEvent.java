package com.cometproject.server.network.messages.incoming.music.playlist;

import com.cometproject.server.game.items.music.SongItem;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.music.playlist.PlaylistMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.RemoveObjectFromInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.SongInventoryMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

public class PlaylistAddMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int inventoryItemId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        SoundMachineFloorItem soundMachineFloorItem = room.getItems().getSoundMachine();

        if (soundMachineFloorItem == null) {
            return;
        }

        InventoryItem inventoryItem = client.getPlayer().getInventory().getItem(inventoryItemId);

        if (inventoryItem == null) {
            return;
        }

        soundMachineFloorItem.addSong(new SongItem(inventoryItem.createSnapshot(), inventoryItem.getDefinition().getSongId()));
        soundMachineFloorItem.saveData();

        RoomItemDao.removeItemFromRoom(inventoryItemId, 0);
        client.getPlayer().getInventory().removeFloorItem(inventoryItem.getId());

        client.send(new SongInventoryMessageComposer(client.getPlayer().getInventory().getSongs()));
        client.send(new RemoveObjectFromInventoryMessageComposer(inventoryItemId));
        client.send(new PlaylistMessageComposer(soundMachineFloorItem.getSongs()));
    }
}
