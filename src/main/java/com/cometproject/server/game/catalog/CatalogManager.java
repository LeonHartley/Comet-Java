package com.cometproject.server.game.catalog;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.catalog.purchase.CatalogPurchaseHandler;
import com.cometproject.server.game.catalog.types.CatalogClubOffer;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class CatalogManager {
    private List<CatalogClubOffer> clubOffers;
    private Map<Integer, CatalogPage> pages;

    private CatalogPurchaseHandler purchaseHandler;

    private int itemCount = 0;

    private Logger log = Logger.getLogger(CatalogManager.class.getName());

    public CatalogManager() {
        this.clubOffers = new FastList<>();
        this.pages = new FastMap<>();

        this.purchaseHandler = new CatalogPurchaseHandler(this);

        this.loadClubOffers();
        this.loadPages();
    }

    public void loadPages() {
        if(this.getPages().size() >= 1) {
            this.getPages().clear();
        }

        try {
            ResultSet page = Comet.getServer().getStorage().getTable("SELECT * FROM catalog_pages WHERE visible = '1' ORDER BY order_num");

            while(page.next()) {
                this.getPages().put(page.getInt("id"), new CatalogPage(page, this.loadItems(page.getInt("id"))));
            }
        } catch(Exception e) {
            log.error("Error while loading catalog pages", e);
        }

        log.info("Loaded " + this.getPages().size() + " catalog pages");
        log.info("Loaded " + this.itemCount + " catalog items");
    }

    public Map<Integer, CatalogItem> loadItems(int pageId) {
        Map<Integer, CatalogItem> items = new FastMap<>();

        try {
            ResultSet item = Comet.getServer().getStorage().getTable("SELECT * FROM catalog_items WHERE page_id = " + pageId);

            while(item.next()) {
                itemCount++;

                CatalogItem i = new CatalogItem(item);
                items.put(i.getId(), i);
            }
        } catch(Exception e) {
            log.error("Error while loading items for page: " + pageId, e);
        }

        return items;
    }

    public List<CatalogPage> getPagesForRank(int rank) {
        List<CatalogPage> pages = new FastList<>();

        for(CatalogPage page : this.getPages().values()) {
            if(rank >= page.getMinRank()) {
                pages.add(page);
            }
        }

        return pages;
    }

    public CatalogPage getPage(int id) {
        if(this.pageExists(id)) {
            return this.getPages().get(id);
        }

        return null;
    }

    public boolean pageExists(int id) {
        return this.getPages().containsKey(id);
    }

    public void loadClubOffers() {
        if(this.getClubOffers().size() >= 1) {
            this.getClubOffers().clear();
        }

        try {
            ResultSet offer = Comet.getServer().getStorage().getTable("SELECT * FROM catalog_club_offers");

            while(offer.next()) {
                this.getClubOffers().add(new CatalogClubOffer(offer));
            }
        } catch(Exception e) {
            log.error("Error while loading catalog club offers", e);
        }

        log.info("Loaded " + this.getClubOffers().size() + " catalog club offers");
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
