package com.cometproject.server.game.catalog.types;

import com.cometproject.api.game.catalog.types.CatalogPageType;
import com.cometproject.api.game.catalog.types.ICatalogBundledItem;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.api.game.catalog.types.bundles.RoomBundleItem;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class CatalogPage implements ICatalogPage {
    private static final Type listType = new TypeToken<List<String>>() {
    }.getType();

    private int id;
    private CatalogPageType type;
    private String caption;
    private int icon;
    private int minRank;
    private String template;
    private int parentId;
    private String linkName;
    private int order;

    private boolean enabled;

    private List<String> images;
    private List<String> texts;

    private Map<Integer, ICatalogItem> items;
    private String extraData;

    private List<ICatalogPage> children = Lists.newArrayList();
    private boolean sorted = false;

    public CatalogPage(ResultSet data, Map<Integer, ICatalogItem> items) throws SQLException {

        this.id = data.getInt("id");
        this.caption = data.getString("caption");
        this.icon = data.getInt("icon_image");
        this.minRank = data.getInt("min_rank");
        this.template = data.getString("page_layout");
        this.parentId = data.getInt("parent_id");
        this.linkName = data.getString("link");
        this.type = CatalogPageType.valueOf(data.getString("type"));
        this.extraData = data.getString("extra_data");
        this.order = data.getInt("order_num");

        if (data.getString("page_images") == null || data.getString("page_images").isEmpty()) {
            this.images = new ArrayList<>();
        } else {
            this.images = JsonUtil.getInstance().fromJson(data.getString("page_images"), listType);
        }

        if (data.getString("page_texts") == null || data.getString("page_texts").isEmpty()) {
            this.texts = new ArrayList<>();
        } else {
            this.texts = JsonUtil.getInstance().fromJson(data.getString("page_texts"), listType);
        }

        this.enabled = data.getString("enabled").equals("1");

        if (this.type == CatalogPageType.BUNDLE) {
            String bundleAlias = this.extraData;

            RoomBundle roomBundle = RoomBundleManager.getInstance().getBundle(bundleAlias);

            if (roomBundle != null) {
                List<ICatalogBundledItem> bundledItems = new ArrayList<>();
                Map<Integer, List<RoomBundleItem>> bundleItems = new HashMap<>();

                for (RoomBundleItem bundleItem : roomBundle.getRoomBundleData()) {
                    if (bundleItems.containsKey(bundleItem.getItemId())) {
                        bundleItems.get(bundleItem.getItemId()).add(bundleItem);
                    } else {
                        bundleItems.put(bundleItem.getItemId(), Lists.newArrayList(bundleItem));
                    }
                }

                for (Map.Entry<Integer, List<RoomBundleItem>> bundledItem : bundleItems.entrySet()) {
                    bundledItems.add(new CatalogBundledItem("0", bundledItem.getValue().size(), bundledItem.getKey()));
                }

                final ICatalogItem catalogItem = new CatalogItem(roomBundle.getId(), "-1", bundledItems, "single_bundle",
                        roomBundle.getCostCredits(), roomBundle.getCostActivityPoints(), roomBundle.getCostVip(), roomBundle.getCostSeasonal(),1, false, 0, 0, false, "", "", this.id);

                this.items = new HashMap<>();
                this.items.put(catalogItem.getId(), catalogItem);
            } else {
                this.items = new HashMap<>();
            }
        } else {
            this.items = items;
        }
    }

    public List<ICatalogPage> getChildren() {
        if (!sorted) {
            this.children.sort(Comparator.comparing(ICatalogPage::getCaption));
            sorted = true;
        }

        return this.children;
    }

    @Override
    public int getOfferSize() {
        int size = 0;

        for (ICatalogItem item : this.items.values()) {
            if (item.getItemId().equals("-1")) continue;

            if (ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId()) != null) {
                if (ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId()).getOfferId() != -1 && ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId()).getOfferId() != 0) {
                    size++;
                }
            }
        }

        return size;
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
    public int getIcon() {
        return icon;
    }

    @Override
    public int getMinRank() {
        return minRank;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public int getParentId() {
        return parentId;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Map<Integer, ICatalogItem> getItems() {
        return items;
    }

    @Override
    public List<String> getImages() {
        return images;
    }

    @Override
    public List<String> getTexts() {
        return texts;
    }

    @Override
    public String getLinkName() {
        return linkName;
    }

    @Override
    public String getExtraData() {
        return extraData;
    }

    @Override
    public CatalogPageType getType() {
        return type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
