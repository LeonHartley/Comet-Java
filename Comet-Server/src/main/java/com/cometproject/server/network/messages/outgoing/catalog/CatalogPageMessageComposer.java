package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.catalog.types.CatalogPageType;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.server.game.rooms.bundles.types.RoomBundleItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CatalogPageMessageComposer extends MessageComposer {

    private final CatalogPage catalogPage;

    public CatalogPageMessageComposer(final CatalogPage catalogPage) {
        this.catalogPage = catalogPage;
    }

    @Override
    public short getId() {
        return Composers.CatalogPageMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.catalogPage.getId());

        msg.writeString("NORMAL"); // builders club or not

        msg.writeString(this.catalogPage.getTemplate());

        msg.writeInt(this.catalogPage.getImages().size());

        for (String image : this.catalogPage.getImages()) {
            msg.writeString(image);
        }

        msg.writeInt(this.catalogPage.getTexts().size());

        for (String text : this.catalogPage.getTexts()) {
            msg.writeString(text);
        }

        if (!this.catalogPage.getTemplate().equals("frontpage") && !this.catalogPage.getTemplate().equals("club_buy")) {
//            if (this.catalogPage.getType() == CatalogPageType.BUNDLE) {
//                if (!StringUtils.isNumeric(this.catalogPage.getExtraData())) {
//                    msg.writeInt(0);
//                } else {
//                    int bundleId = Integer.parseInt(this.catalogPage.getExtraData());
//
//                    RoomBundle roomBundle = RoomBundleManager.getInstance().getBundle(bundleId);
//
//                    if (roomBundle == null) {
//                        msg.writeInt(0);
//                    } else {
//                        Map<Integer, List<RoomBundleItem>> items = new HashMap<>();
//
//                        for (RoomBundleItem bundleItem : roomBundle.getRoomBundleData()) {
//                            if (items.containsKey(bundleItem.getItemId())) {
//                                items.get(bundleItem.getItemId()).add(bundleItem);
//                            } else {
//                                items.put(bundleItem.getItemId(), Lists.newArrayList(bundleItem));
//                            }
//                        }
//
//
//
//                        for(Map.Entry<Integer, List<RoomBundleItem>> item : items.entrySet()) {
//
//                        }
//
//                        List<CatalogItem> catalogItems = new ArrayList<>();
//
//                        for (Integer itemId : items.keySet()) {
//                            CatalogItem catalogItem = CatalogManager.getInstance().getCatalogItemByItemId(itemId);
//
//                            System.out.println(itemId);
//
//                            if (catalogItem != null) {
//                                catalogItems.add(catalogItem);
//                            }
//                        }
//
//                        msg.writeInt(catalogItems.size());
//
//                        for (CatalogItem catalogItem : catalogItems) {
//                            catalogItem.compose(msg);
//                        }
//                    }
//                }
//            } else {
                msg.writeInt(this.catalogPage.getItems().size());

                for (CatalogItem item : this.catalogPage.getItems().values()) {
                    item.compose(msg);
                }
//            }
        } else {
            msg.writeInt(0);
        }

        msg.writeInt(0);
        msg.writeBoolean(false); // allow seasonal currency as credits
    }
}
