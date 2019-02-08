package com.cometproject.server.network.ws.messages;

public class YouTubeVideoMessage extends WsMessage {
    private final String videoId;
    private final String description;
    private final String username;
    private final String figure;

    public YouTubeVideoMessage(String videoId, String description, String username, String figure) {
        super(WsMessageType.YOUTUBE_VIDEO);
        
        this.videoId = videoId;
        this.description = description;
        this.username = username;
        this.figure = figure;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public String getFigure() {
        return figure;
    }
}
