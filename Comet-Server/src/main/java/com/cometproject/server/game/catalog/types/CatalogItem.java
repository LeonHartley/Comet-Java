package com.cometproject.server.game.catalog.types;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;

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
    private List<CatalogBundledItem> items;

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
    public CatalogItem(ResultSet data) throws Exception {
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
            String[] split = itemId.replace("\n", "").split(",");

            for (String str : split) {
                if (!str.equals("")) {
                    String[] parts = str.split(":");
                    if (parts.length != 3) continue;

                    try {
                        final int itemId = Integer.parseInt(parts[0]);
                        final int amount = Integer.parseInt(parts[1]);
                        final String presetData = parts[2];

                        this.items.add(new CatalogBundledItem(presetData, amount, itemId));
                    } catch (Exception ignored) {
                        Comet.getServer().getLogger().warn("Invalid item data for catalog item: " + this.id);
                    }
                }
            }
        } else {
            if(!this.itemId.equals("-1")) {
                this.items.add(new CatalogBundledItem(this.presetData, this.amount, Integer.valueOf(this.itemId)));
            }
        }

        if (this.getItems().size() == 0) return;

        for (CatalogBundledItem catalogBundledItem : this.items) {
            final ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(catalogBundledItem.getItemId());

            if (itemDefinition == null) {
                throw new Exception("Invalid item data!");
            }
        }

        if (ItemManager.getInstance().getDefinition(this.getItems().get(0).getItemId()) == null) return;
        int offerId = ItemManager.getInstance().getDefinition(this.getItems().get(0).getItemId()).getOfferId();

        if (!CatalogManager.getCatalogOffers().containsKey(offerId)) {
            CatalogManager.getCatalogOffers().put(offerId, new CatalogOffer(offerId, data.getInt("page_id"), this.getId()));
        }
    }

    public void compose(IComposer msg) {
        final ItemDefinition firstItem = ItemManager.getInstance().getDefinition(this.getItems().get(0).getItemId());

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

        msg.writeBoolean(firstItem != null && firstItem.canGift());

        if (!this.hasBadge()) {
            msg.writeInt(this.getItems().size());
        } else {
            msg.writeInt(this.isBadgeOnly() ? 1 : this.getItems().size() + 1);
            msg.writeString("b");
            msg.writeString(this.getBadgeId());
        }

        if (!this.isBadgeOnly()) {
            for (CatalogBundledItem bundledItem : this.getItems()) {
                ItemDefinition def = ItemManager.getInstance().getDefinition(bundledItem.getItemId());

                msg.writeString(def.getType());
                msg.writeInt(def.getSpriteId());

                if (this.getDisplayName().contains("wallpaper_single") || this.getDisplayName().contains("floor_single") || this.getDisplayName().contains("landscape_single")) {
                    msg.writeString(this.getDisplayName().split("_")[2]);
                } else {
                    msg.writeString(bundledItem.getPresetData());
                }

                msg.writeInt(bundledItem.getAmount());

                msg.writeBoolean(this.getLimitedTotal() != 0);

                if (this.getLimitedTotal() > 0) {
                    msg.writeInt(this.getLimitedTotal());
                    msg.writeInt(this.getLimitedTotal() - this.getLimitedSells());
                }
            }
        }

        msg.writeInt(0); // club level
        msg.writeBoolean(!(this.getLimitedTotal() > 0) && this.allowOffer());
    }

    public class CatalogBundledItem {
        private final int itemId;
        private final int amount;
        private final String presetData;

        public CatalogBundledItem(String presetData, int amount, int itemId) {
            this.presetData = presetData;
            this.amount = amount;
            this.itemId = itemId;
        }

        public int getItemId() {
            return itemId;
        }

        public int getAmount() {
            return amount;
        }

        public String getPresetData() {
            return presetData;
        }
    }

    public int getId() {
        return this.id;
    }

    public String getItemId() {
        return itemId;
    }

    public List<CatalogBundledItem> getItems() {
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

    public boolean isBadgeOnly() {
        return this.getItems().get(0).getItemId() == -1 && this.hasBadge();
    }

    public String getBadgeId() {
        return this.badgeId;
    }

    public String getPresetData() {
        return presetData;
    }
}
