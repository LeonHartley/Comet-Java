package com.cometproject.server.game.catalog;

import com.cometproject.api.game.catalog.ICatalogService;
import com.cometproject.api.game.catalog.types.*;
import com.cometproject.api.game.catalog.types.purchase.ICatalogPurchaseHandler;
import com.cometproject.server.game.catalog.purchase.LegacyPurchaseHandler;
import com.cometproject.server.storage.queries.catalog.CatalogDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.log4j.Logger;

import java.util.*;


public class CatalogManager implements ICatalogService {
    private static CatalogManager catalogManagerInstance;
    /**
     * Maps the offer ID of an item to the page ID.
     */
    private final Map<Integer, ICatalogOffer> catalogOffers = new HashMap<>();
    /**
     * The new style of gift boxes
     */
    private final List<Integer> giftBoxesNew = Lists.newArrayList();
    /**
     * The old style of gift boxes
     */
    private final List<Integer> giftBoxesOld = Lists.newArrayList();
    /**
     * Featured catalog pages (they are displayed on the front page)
     */
    private final List<ICatalogFrontPageEntry> frontPageEntries = new ArrayList<>();
    /**
     * Redeemable clothing items
     */
    private final Map<String, IClothingItem> clothingItems = Maps.newConcurrentMap();
    /**
     * The pages within the catalog
     */
    private Map<Integer, ICatalogPage> pages;
    /**
     * The items within the catalog
     */
    private Map<Integer, ICatalogItem> items;
    /**
     * The catalog item IDs to page IDs map
     */
    private Map<Integer, Integer> catalogItemIdToPageId;
    /**
     * The handler of everything catalog-purchase related
     */
    private ICatalogPurchaseHandler purchaseHandler;

    /**
     * The logger for the catalog manager
     */
    private Logger log = Logger.getLogger(CatalogManager.class.getName());

    /**
     * Parent pages
     */
    private List<ICatalogPage> parentPages = Lists.newCopyOnWriteArrayList();

    /**
     * Initialize the catalog
     */
    public CatalogManager() {

    }

    public static CatalogManager getInstance() {
        if (catalogManagerInstance == null) {
            catalogManagerInstance = new CatalogManager();
        }

        return catalogManagerInstance;
    }

    @Override
    public void initialize() {
        this.pages = new ListOrderedMap<>();
        this.items = new ListOrderedMap<>();

        this.catalogItemIdToPageId = new HashMap<>();

        this.purchaseHandler = new LegacyPurchaseHandler();

        this.loadItemsAndPages();
        this.loadGiftBoxes();
        this.loadClothingItems();

        log.info("CatalogManager initialized");
    }

    /**
     * Load all catalog pages
     */
    @Override
    public void loadItemsAndPages() {
        if (this.items.size() >= 1) {
            this.items.clear();
        }

        if (this.getPages().size() >= 1) {
            this.getPages().clear();
        }

        if (this.frontPageEntries.size() >= 1) {
            this.frontPageEntries.clear();
        }

        if (getCatalogOffers().size() >= 1) {
            getCatalogOffers().clear();
        }

        if (this.catalogItemIdToPageId.size() >= 1) {
            this.catalogItemIdToPageId.clear();
        }

        try {
            CatalogDao.getItems(this.items);
            CatalogDao.getPages(this.pages);
            CatalogDao.getFeaturedPages(this.frontPageEntries);
        } catch (Exception e) {
            log.error("Error while loading catalog pages/items", e);
        }

        for (ICatalogPage page : this.pages.values()) {
            for (Integer item : page.getItems().keySet()) {
                this.catalogItemIdToPageId.put(item, page.getId());
            }
        }

        this.sortCatalogChildren();

        log.info("Loaded " + this.getPages().size() + " catalog pages and " + this.items.size() + " catalog items");
    }

    @Override
    public void loadGiftBoxes() {
        if (this.giftBoxesNew.size() >= 1) {
            this.giftBoxesNew.clear();
        }

        if (this.giftBoxesOld.size() >= 1) {
            this.giftBoxesOld.clear();
        }

        CatalogDao.loadGiftBoxes(this.giftBoxesOld, this.giftBoxesNew);
        log.info("Loaded " + (this.giftBoxesNew.size() + this.giftBoxesOld.size()) + " gift wrappings");
    }

    @Override
    public void loadClothingItems() {
        if (this.clothingItems.size() >= 1) {
            this.clothingItems.clear();
        }

        CatalogDao.getClothing(this.clothingItems);
        log.info("Loaded " + clothingItems.size() + " clothing items");
    }

    /**
     * Get pages for a specific player rank
     *
     * @param rank Player rank
     * @return A list of pages that are accessible by the specified rank
     */
    @Override
    public List<ICatalogPage> getPagesForRank(int rank) {
        List<ICatalogPage> pages = new ArrayList<>();

        for (ICatalogPage page : this.getPages().values()) {
            if (rank >= page.getMinRank()) {
                pages.add(page);
            }
        }

        return pages;
    }

    public void sortCatalogChildren() {
        this.parentPages.clear();

        for (ICatalogPage catalogPage : this.pages.values()) {
            if (catalogPage.getParentId() != -1) {
                final ICatalogPage parentPage = this.getPage(catalogPage.getParentId());

                if(parentPage == null) {
                    log.warn("Page " + catalogPage.getId() + " with invalid parent id: " + catalogPage.getParentId());
                } else {
                    parentPage.getChildren().add(catalogPage);
                }
            } else {
                this.parentPages.add(catalogPage);
            }
        }

        this.parentPages.sort(Comparator.comparing(ICatalogPage::getOrder));
    }

    @Override
    public ICatalogItem getCatalogItemByOfferId(int offerId) {
        ICatalogOffer offer = getCatalogOffers().get(offerId);

        if (offer == null)
            return null;

        ICatalogPage page = this.getPage(offer.getCatalogPageId());
        if (page == null)
            return null;

        return page.getItems().get(offer.getCatalogItemId());
    }

    @Override
    public ICatalogPage getCatalogPageByCatalogItemId(int id) {
        if (!this.catalogItemIdToPageId.containsKey(id)) {
            return null;
        }

        return this.pages.get(this.catalogItemIdToPageId.get(id));
    }

    @Override
    public ICatalogItem getCatalogItemByItemId(int itemId) {
        if (!this.items.containsKey(itemId)) {
            return null;
        }

        return this.items.get(itemId);
    }

    @Override
    public Map<Integer, ICatalogItem> getItemsForPage(int pageId) {
        Map<Integer, ICatalogItem> items = Maps.newHashMap();

        for (Map.Entry<Integer, ICatalogItem> catalogItem : this.items.entrySet()) {
            if (catalogItem.getValue().getPageId() == pageId) {
                items.put(catalogItem.getKey(), catalogItem.getValue());
            }
        }

        return items;
    }

    /**
     * Get a catalog page by its ID
     *
     * @param id Catalog Page ID
     * @return Catalog Page object with the specified ID
     */
    @Override
    public ICatalogPage getPage(int id) {
        if (this.pageExists(id)) {
            return this.getPages().get(id);
        }

        return null;
    }

    /**
     * Get a catalog item by its ID
     *
     * @param catalogItemId The ID of the catalog item
     * @return CatalogItem object with specified ID
     */
    @Override
    public ICatalogItem getCatalogItem(final int catalogItemId) {
        return this.items.get(catalogItemId);
    }

    /**
     * Does a page with a specific ID exist?
     *
     * @param id The ID of the page we want to check that exists
     * @return Whether or not the page with the specified ID exists
     */
    @Override
    public boolean pageExists(int id) {
        return this.getPages().containsKey(id);
    }

    /**
     * Get all catalog pages
     *
     * @return All catalog pages in-memory
     */
    @Override
    public Map<Integer, ICatalogPage> getPages() {
        return this.pages;
    }

    /**
     * Get the catalog page handler
     *
     * @return The catalog page handler
     */
    @Override
    public ICatalogPurchaseHandler getPurchaseHandler() {
        return purchaseHandler;
    }

    /**
     * Gift wrappings new
     *
     * @return The new style of gift wrapping boxes
     */
    @Override
    public List<Integer> getGiftBoxesNew() {
        return giftBoxesNew;
    }

    /**
     * Gift wrappings old
     *
     * @return The old style of gift wrapping boxes
     */
    @Override
    public List<Integer> getGiftBoxesOld() {
        return giftBoxesOld;
    }

    /**
     * List all front page entries
     *
     * @return List of all front page entries
     */
    @Override
    public List<ICatalogFrontPageEntry> getFrontPageEntries() {
        return this.frontPageEntries;
    }

    @Override
    public Map<String, IClothingItem> getClothingItems() {
        return this.clothingItems;
    }

    @Override
    public Map<Integer, ICatalogOffer> getCatalogOffers() {
        return catalogOffers;
    }

    @Override
    public List<ICatalogPage> getParentPages() {
        return parentPages;
    }
}
