package com.cometproject.server.game.catalog.types;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.utilities.JsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogPage {
    private static final Type listType = new TypeToken<List<String>>(){}.getType();

    private int id;
    private String caption;
    private int icon;
    private int minRank;
    private String template;
    private int parentId;

    private boolean enabled;

    private List<String> images;
    private List<String> texts;

    private Map<Integer, CatalogItem> items;

    public CatalogPage(ResultSet data, Map<Integer, CatalogItem> items) throws SQLException {

        this.id = data.getInt("id");
        this.caption = data.getString("caption");
        this.icon = data.getInt("icon_image");
        this.minRank = data.getInt("min_rank");
        this.template = data.getString("page_layout");
        this.parentId = data.getInt("parent_id");

        if(data.getString("page_images") == null || data.getString("page_images").isEmpty()) {
            this.images = new ArrayList<>();
        } else {
            this.images = JsonFactory.getInstance().fromJson(data.getString("page_images"), listType);
        }

        if(data.getString("page_texts") == null || data.getString("page_texts").isEmpty()) {
            this.texts = new ArrayList<>();
        } else {
            this.texts = JsonFactory.getInstance().fromJson(data.getString("page_texts"), listType);
        }

        this.enabled = data.getString("enabled").equals("1");
        this.items = items;
    }

    public int getOfferSize() {
        int size = 0;

        for(CatalogItem item : this.items.values()) {
            if(CometManager.getItems().getDefinition(item.getItems().get(0)) != null) {
                if(CometManager.getItems().getDefinition(item.getItems().get(0)).getOfferId() != -1 && CometManager.getItems().getDefinition(item.getItems().get(0)).getOfferId() != 0) {
                    size++;
                }
            }
        }

        return size;
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public int getIcon() {
        return icon;
    }

    public int getMinRank() {
        return minRank;
    }

    public String getTemplate() {
        return template;
    }

    public int getParentId() {
        return parentId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Map<Integer, CatalogItem> getItems() {
        return items;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getTexts() {
        return texts;
    }
}
