package com.cometproject.server.game.catalog;

import com.cometproject.server.game.catalog.purchase.CatalogPurchaseHandler;
import com.cometproject.server.game.catalog.types.CatalogClubOffer;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.storage.queries.catalog.CatalogDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogManager {
    /**
     * The pages within the catalog
     */
    private Map<Integer, CatalogPage> pages;

    /**
     * The handler of everything catalog-purchase related
     */
    private CatalogPurchaseHandler purchaseHandler;

    /**
     * The logger for the catalog manager
     */
    private Logger log = Logger.getLogger(CatalogManager.class.getName());

    /**
     * Initialize the catalog
     */
    public CatalogManager() {
        this.pages = new FastMap<>();

        this.purchaseHandler = new CatalogPurchaseHandler(this);

        this.loadPages();
    }

    /**
     * Load all catalog pages
     */
    public void loadPages() {
        if (this.getPages().size() >= 1) {
            this.getPages().clear();
        }

        try {
            this.pages = CatalogDao.getPages();
        } catch (Exception e) {
            log.error("Error while loading catalog pages", e);
        }

        log.info("Loaded " + this.getPages().size() + " catalog pages");
    }

    /**
     * Get pages for a specific player rank
     * @param rank Player rank
     * @return A list of pages that are accessible by the specified rank
     */
    public List<CatalogPage> getPagesForRank(int rank) {
        List<CatalogPage> pages = new ArrayList<>();

        for (CatalogPage page : this.getPages().values()) {
            if (rank >= page.getMinRank()) {
                pages.add(page);
            }
        }

        return pages;
    }

    /**
     * Get a catalog page by it's ID
     * @param id Catalog Page ID
     * @return Catalog Page object with the specified ID
     */
    public CatalogPage getPage(int id) {
        if (this.pageExists(id)) {
            return this.getPages().get(id);
        }
/
        return null;
    }

    /**
     * Does a page with a specific ID exist?
     * @param id The ID of the page we want to check that exists
     * @return Whether or not the page with the specified ID exists
     */
    public boolean pageExists(int id) {
        return this.getPages().containsKey(id);
    }

    /**
     * Get all catalog pages
     * @return All catalog pages in-memory
     */
    public Map<Integer, CatalogPage> getPages() {
        return this.pages;
    }

    /**
     * Get the catalog page handler
     * @return The catalog page handler
     */
    public CatalogPurchaseHandler getPurchaseHandler() {
        return purchaseHandler;
    }
}
