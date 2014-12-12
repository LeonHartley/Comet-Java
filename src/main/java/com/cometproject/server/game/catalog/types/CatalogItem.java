package com.cometproject.server.game.catalog.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.types.Composer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CatalogItem {
    /**
     * The ID of the catalog item
     */
    private int id;

    /**
     * The ID of the item definition
     */
    private String itemId;

    /**
     * The name of item which will be displayed in the catalog
     */
    private String displayName;

    /**
     * The coin cost of the item
     */
    private int costCredits;

    /**
     * The duckets cost of the item
     */
    private int costActivityPoints;

    /**
     * The seasonal currency cost of the items (usually diamonds)
     */
    private int costOther;

    /**
     * The amount of items you get if you purchase this
     */
    private int amount;

    /**
     * Is this item only available to VIP members?
     */
    private boolean vip;

    /**
     * The items (if this is a bundle)
     */
    private List<Integer> items;

    /**
     * If this item is limited edition, how many items are available
     */
    private int limitedTotal;

    /**
     * If this item is limited edition, how many items have been sold
     */
    private int limitedSells;

    /**
     * Allow this item to be sold
     */
    private boolean allowOffer;

    /**
     * Badge ID that's bundled with this item (if any)
     */
    private String badgeId;

    /**
     * Item extra-data presets (once purchased, this preset will be applied to the item)
     */
    private String presetData;

    /**
     * Initialize the catalog item with data from the database
     *
     * @param data Data from the database
     * @throws SQLException
     */
    public CatalogItem(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.itemId = data.getString("item_ids");
        this.displayName = data.getString("catalog_name");
        this.costCredits = data.getInt("cost_credits");
        this.costActivityPoints = data.getInt("cost_pixels");
        this.costOther = data.getInt("cost_snow");
        this.amount = data.getInt("amount");
        this.vip = data.getString("vip").equals("1");
        this.limitedTotal = data.getInt("limited_stack");
        this.limitedSells = data.getInt("limited_sells");
        this.allowOffer = data.getString("offer_active").equals("1");
        this.presetData = data.getString("extradata");
        this.badgeId = data.getString("badge_id");

        this.items = new ArrayList<>();

        if (itemId.contains(",")) {
            String[] split = itemId.split(",");

            for (String str : split) {
                if (!str.equals("")) {
                    this.items.add(Integer.valueOf(str));
                }
            }
        } else {
            this.items.add(Integer.valueOf(this.itemId));
        }

        int offerId = ItemManager.getInstance().getDefinition(this.getItems().get(0)).getOfferId();

        if (!CatalogManager.getCatalogOffers().containsKey(offerId)) {
            CatalogManager.getCatalogOffers().put(offerId, new CatalogOffer(offerId, data.getInt("page_id"), this.getId()));
        }
    }

    public void compose(Composer msg) {
        final ItemDefinition firstItem = ItemManager.getInstance().getDefinition(this.getItems().get(0));

        msg.writeInt(this.getId());
        msg.writeString(this.getDisplayName());
        msg.writeBoolean(false);
        msg.writeInt(this.getCostCredits());

        if (this.getCostOther() > 0) {
            msg.writeInt(this.getCostOther());
            msg.writeInt(105);
        } else if (this.getCostActivityPoints() > 0) {
            msg.writeInt(this.getCostActivityPoints());
            msg.writeInt(0);
        } else {
            msg.writeInt(0);
            msg.writeInt(0);
        }

        msg.writeBoolean(firstItem.canGift());

        if (!this.hasBadge()) {
            msg.writeInt(this.getItems().size());
        } else {
            msg.writeInt(this.getItems().size() + 1);
            msg.writeString("b");
            msg.writeString(this.getBadgeId());
        }

        for (int i : this.getItems()) {
            ItemDefinition def = ItemManager.getInstance().getDefinition(i);
            msg.writeString(def.getType());
            msg.writeInt(def.getSpriteId());

            if (this.getDisplayName().contains("wallpaper_single") || this.getDisplayName().contains("floor_single") || this.getDisplayName().contains("landscape_single")) {
                msg.writeString(this.getDisplayName().split("_")[2]);
            } else {
                msg.writeString(this.getPresetData());
            }

            msg.writeInt(this.getAmount());

            if (this.getLimitedTotal() == 0)
                msg.writeInt(0);
        }

        msg.writeBoolean(this.getLimitedTotal() != 0);

        if (this.getLimitedTotal() > 0) {
            msg.writeInt(this.getLimitedTotal());
            msg.writeInt(this.getLimitedTotal() - this.getLimitedSells());
            msg.writeInt(0);
        }

        msg.writeBoolean(!(this.getLimitedTotal() > 0) && this.allowOffer());
    }

    public int getId() {
        return this.id;
    }

    public String getItemId() {
        return itemId;
    }

    public List<Integer> getItems() {
        return this.items;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCostCredits() {
        return costCredits;
    }

    public int getCostActivityPoints() {
        return costActivityPoints;
    }

    public int getCostOther() {
        return costOther;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isVip() {
        return vip;
    }

    public int getLimitedTotal() {
        return this.limitedTotal;
    }

    public int getLimitedSells() {
        return this.limitedSells;
    }

    public boolean allowOffer() {
        return this.allowOffer;
    }

    public void increaseLimitedSells(int amount) {
        this.limitedSells += amount;
    }

    public boolean hasBadge() {
        return !(this.badgeId.isEmpty());
    }

    public String getBadgeId() {
        return this.badgeId;
    }

    public String getPresetData() {
        return presetData;
    }
}
