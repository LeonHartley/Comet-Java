package com.cometproject.server.game.catalog;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.catalog.purchase.CatalogPurchaseHandler;
import com.cometproject.server.game.catalog.types.CatalogClubOffer;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.storage.queries.catalog.CatalogDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogManager {
    private List<CatalogClubOffer> clubOffers;
    private Map<Integer, CatalogPage> pages;

    private CatalogPurchaseHandler purchaseHandler;

    private Logger log = Logger.getLogger(CatalogManager.class.getName());

    public CatalogManager() {
        this.clubOffers = new ArrayList<>();
        this.pages = new FastMap<>();

        this.purchaseHandler = new CatalogPurchaseHandler(this);

        this.loadPages();
    }

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

    public List<CatalogPage> getPagesForRank(int rank) {
        List<CatalogPage> pages = new ArrayList<>();

        for (CatalogPage page : this.getPages().values()) {
            if (rank >= page.getMinRank()) {
                pages.add(page);
            }
        }

        return pages;
    }

    public CatalogPage getPage(int id) {
        if (this.pageExists(id)) {
            return this.getPages().get(id);
        }

        return null;
    }

    public boolean pageExists(int id) {
        return this.getPages().containsKey(id);
    }

    public List<CatalogClubOffer> getClubOffers() {
        return this.clubOffers;
    }

    public Map<Integer, CatalogPage> getPages() {
        return this.pages;
    }

    public CatalogPurchaseHandler getPurchaseHandler() {
        return purchaseHandler;
    }
}
