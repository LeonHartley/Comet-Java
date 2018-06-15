package com.cometproject.api.game.catalog.types;

import java.util.List;
import java.util.Map;

public interface ICatalogPage {
    int getOfferSize();

    int getId();

    String getCaption();

    int getIcon();

    int getMinRank();

    String getTemplate();

    int getParentId();

    boolean isEnabled();

    Map<Integer, ICatalogItem> getItems();

    List<String> getImages();

    List<String> getTexts();

    String getLinkName();

    String getExtraData();

    CatalogPageType getType();

    int getOrder();

    List<ICatalogPage> getChildren();
}
