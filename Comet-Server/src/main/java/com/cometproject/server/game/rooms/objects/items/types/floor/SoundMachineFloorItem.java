package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.items.music.SongItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.utilities.JsonFactory;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SoundMachineFloorItem extends RoomItemFloor {
    private List<SongItem> songs;

    public SoundMachineFloorItem(int id, int itemId, RoomInstance room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if(data.startsWith("{")) {
            this.songs = JsonFactory.getInstance().fromJson(data, new TypeToken<ArrayList<SongItem>>() {
            }.getType());
        } else {
            this.songs = new ArrayList<>();
        }
    }

    public void addSong(SongItem songItem) {
        this.songs.add(songItem);
    }

    public void removeSong(SongItem songItem) {
        this.songs.remove(songItem);
    }

    @Override
    public String getDataObject() {
        return JsonFactory.getInstance().toJson(this.songs);
    }

    public List<SongItem> getSongs() {
        return this.songs;
    }
}
