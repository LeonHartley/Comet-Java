package com.cometproject.api.game.players.data.types;

public interface IPlaylistItem {
    public String getVideoId();

    public void setVideoId(String videoId);

    public String getTitle();

    public void setTitle(String title);

    public String getDescription();

    public void setDescription(String description);

    public int getDuration();

    public void setDuration(int duration);
}
