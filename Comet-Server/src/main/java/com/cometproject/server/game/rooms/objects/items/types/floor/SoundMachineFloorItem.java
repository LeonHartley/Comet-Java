package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.music.MusicData;
import com.cometproject.server.game.items.music.SongItem;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.music.PlayMusicMessageComposer;
import com.cometproject.server.utilities.JsonFactory;
import com.cometproject.server.utilities.attributes.Stateable;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SoundMachineFloorItem extends RoomItemFloor implements Stateable {
    public static final int MAX_CAPACITY = 20;

    private boolean isPlaying = false;
    private int currentPlayingIndex = -1;
    private long startTimestamp = 0;

    private List<SongItem> songs;

    public SoundMachineFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if(data.startsWith("[")) {
            this.songs = JsonFactory.getInstance().fromJson(data, new TypeToken<ArrayList<SongItem>>() {
            }.getType());
        } else {
            this.songs = new ArrayList<>();
        }
    }

    @Override
    public boolean onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if(entity instanceof PlayerEntity) {
            if(((PlayerEntity) entity).getPlayerId() != this.getOwner()) {
                return false;
            }
        }

        if(requestData == -1) return false;

        if(this.isPlaying) {
            this.stop();
        } else {
            this.play();
        }

        this.sendUpdate();
        return true;
    }

    @Override
    public void onTick() {
        if(this.isPlaying) {
            SongItem songItem = this.getSongs().get(this.currentPlayingIndex);

            if(songItem != null) {
                MusicData musicData = ItemManager.getInstance().getMusicData(songItem.getSongId());

                if(musicData != null) {
                    if (this.timePlaying() >= musicData.getLengthSeconds() + 1.0) {
                        this.playNextSong();
                    }
                }
            }
        }
    }

    public void addSong(SongItem songItem) {
        this.songs.add(songItem);
    }

    public SongItem removeSong(int index) {
        SongItem songItem = this.songs.get(index);
        this.songs.remove(index);

        return songItem;
    }

    public void play() {
        if(this.songs.size() == 0) return;

        this.isPlaying = true;
        this.currentPlayingIndex = -1;

        this.playNextSong();
    }

    public void stop() {
        this.isPlaying = false;
        this.currentPlayingIndex = -1;

        this.broadcastSong();
    }

    public void playNextSong() {
        if(currentPlayingIndex >= this.getSongs().size()) {
            currentPlayingIndex = -1;
        }

        this.startTimestamp = Comet.getTime();
        this.currentPlayingIndex++;
        this.broadcastSong();
    }

    private void broadcastSong() {
        if(!this.isPlaying) {
            this.getRoom().getEntities().broadcastMessage(new PlayMusicMessageComposer());
            return;
        }

        SongItem songItem = this.songs.get(this.currentPlayingIndex);

        if(songItem == null) {
            return;
        }

        int songId = songItem.getSongId();
        this.getRoom().getEntities().broadcastMessage(new PlayMusicMessageComposer(songId, this.currentPlayingIndex, this.songTimeSync()));
    }

    private int timePlaying() {
        return (int) (Comet.getTime() - this.startTimestamp);
    }

    public int songTimeSync() {
        if(!this.isPlaying || this.currentPlayingIndex < 0 || this.currentPlayingIndex >= this.getSongs().size()) {
            return 0;
        }

        SongItem songItem = this.getSongs().get(this.currentPlayingIndex);

        if(songItem != null) {
            MusicData musicData = ItemManager.getInstance().getMusicData(songItem.getSongId());

            if(musicData != null) {
                if((this.timePlaying() * 1000) >= musicData.getLengthMilliseconds())
                    return musicData.getLengthMilliseconds();
            }
        }

        return this.timePlaying() * 1000;
    }

    @Override
    public String getDataObject() {
        return JsonFactory.getInstance().toJson(this.songs);
    }

    public List<SongItem> getSongs() {
        return this.songs;
    }

    @Override
    public boolean getState() {
        return this.isPlaying;
    }
}
