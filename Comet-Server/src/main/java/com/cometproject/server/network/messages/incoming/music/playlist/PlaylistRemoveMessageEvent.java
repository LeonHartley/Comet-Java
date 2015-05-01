package com.cometproject.server.network.messages.incoming.music.playlist;

import com.cometproject.server.game.items.music.SongItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.music.playlist.PlaylistMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.SongInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

public class PlaylistRemoveMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int songIndex = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        if(client.getPlayer().getId() != room.getData().getOwnerId()) return;

        SoundMachineFloorItem soundMachineFloorItem = room.getItems().getSoundMachine();

        if (soundMachineFloorItem == null) {
            return;
        }

        if(songIndex < 0 || songIndex >= soundMachineFloorItem.getSongs().size()) {
            return;
        }

        SongItem songItem = soundMachineFloorItem.removeSong(songIndex);
        soundMachineFloorItem.saveData();

        RoomItemDao.removeItemFromRoom(songItem.getItemSnapshot().getId(), client.getPlayer().getId());
        client.getPlayer().getInventory().add(songItem.getItemSnapshot().getId(), songItem.getItemSnapshot().getBaseItemId(), songItem.getItemSnapshot().getExtraData());

        client.send(new UpdateInventoryMessageComposer());

        client.send(new SongInventoryMessageComposer(client.getPlayer().getInventory().getSongs()));
        client.send(new PlaylistMessageComposer(soundMachineFloorItem.getSongs()));
    }
}
