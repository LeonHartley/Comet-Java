package com.cometproject.api.game.catalog;

import com.cometproject.api.game.catalog.types.*;
import com.cometproject.api.game.catalog.types.purchase.ICatalogPurchaseHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICatalogService {
    Map<Integer, ICatalogOffer> getCatalogOffers();

    void initialize();

    void loadItemsAndPages();

    void loadGiftBoxes();

    void loadClothingItems();

    List<ICatalogPage> getPagesForRank(int rank);

    ICatalogItem getCatalogItemByOfferId(int offerId);

    ICatalogPage getCatalogPageByCatalogItemId(int id);

    ICatalogItem getCatalogItemByItemId(int itemId);

    Map<Integer, ICatalogItem> getItemsForPage(int pageId);

    ICatalogPage getPage(int id);

    ICatalogItem getCatalogItem(int catalogItemId);

    boolean pageExists(int id);

    Map<Integer, ICatalogPage> getPages();

    ICatalogPurchaseHandler getPurchaseHandler();

    List<Integer> getGiftBoxesNew();

    List<Integer> getGiftBoxesOld();

    List<ICatalogFrontPageEntry> getFrontPageEntries();

    Map<String, IClothingItem> getClothingItems();

    List<ICatalogPage> getParentPages();
}
