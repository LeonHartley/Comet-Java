package com.cometproject.server.game.navigator.types.publics;

public class PublicRoom {
    private final int roomId;
    private final String caption;
    private final String description;
    private final String imageUrl;
    private final String category;

    public PublicRoom(int roomId, String caption, String description, String imageUrl, String category) {
        this.roomId = roomId;
        this.caption = caption;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }
}
