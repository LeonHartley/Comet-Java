package com.cometproject.server.game.items.music;

public class SongItem {

    private int itemId;
    private int songId;

    public SongItem(int itemId, int songId) {
        this.itemId = itemId;
        this.songId = songId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
