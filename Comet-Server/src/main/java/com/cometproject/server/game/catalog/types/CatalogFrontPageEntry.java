package com.cometproject.server.game.catalog.types;

import com.cometproject.api.game.catalog.types.ICatalogFrontPageEntry;

public class CatalogFrontPageEntry implements ICatalogFrontPageEntry {
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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getPageLink() {
        return pageLink;
    }

    @Override
    public int getPageId() {
        return pageId;
    }
}
