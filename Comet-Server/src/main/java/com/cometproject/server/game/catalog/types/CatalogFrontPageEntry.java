package com.cometproject.server.game.catalog.types;

public class CatalogFrontPageEntry {
    private final int id;
    private final String caption;
    private final String image;
    private final String pageLink;
    private final int pageId;

    public CatalogFrontPageEntry(int id, String caption, String image, String pageLink, int pageId) {
        this.id = id;
        this.caption = caption;
        this.image = image;
        this.pageLink = pageLink;
        this.pageId = pageId;
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getImage() {
        return image;
    }

    public String getPageLink() {
        return pageLink;
    }

    public int getPageId() {
        return pageId;
    }
}
