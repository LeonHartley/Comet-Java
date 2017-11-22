package com.cometproject.api.game.furniture.types;

public interface IMusicData {
    int getSongId();

    String getName();

    String getTitle();

    String getArtist();

    String getData();

    int getLengthSeconds();

    int getLengthMilliseconds();
}
