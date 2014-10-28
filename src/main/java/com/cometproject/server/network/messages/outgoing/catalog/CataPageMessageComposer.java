package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class CataPageMessageComposer {
    public static Composer compose(CatalogPage page) {
        Composer msg = new Composer(Composers.CataPageMessageComposer);
        msg.writeInt(page.getId());

        msg.writeString("NORMAL");

        msg.writeString(page.getTemplate());

        msg.writeInt(page.getImages().size());

        for(String image : page.getImages()) {
            msg.writeString(image);
        }

        msg.writeInt(page.getTexts().size());

        for(String text : page.getTexts()) {
            msg.writeString(text);
        }

        if (!page.getTemplate().equals("frontpage") && !page.getTemplate().equals("club_buy")) {
            msg.writeInt(page.getItems().size());

            for (CatalogItem item : page.getItems().values()) {
                msg.writeInt(item.getId());
                msg.writeString(item.getDisplayName());
                msg.writeBoolean(false);
                msg.writeInt(item.getCostCredits());

                if (item.getCostOther() > 0) {
                    msg.writeInt(item.getCostOther());
                    msg.writeInt(105);
                } else if(item.getCostActivityPoints() > 0) {
                    msg.writeInt(item.getCostActivityPoints());
                    msg.writeInt(0);
                } else {
                    msg.writeInt(0);
                    msg.writeInt(0);
                }

                msg.writeBoolean(false); // Can gift

                if (!item.hasBadge()) {
                    msg.writeInt(item.getItems().size());
                } else {
                    msg.writeInt(item.getItems().size() + 1);
                    msg.writeString("b");
                    msg.writeString(item.getBadgeId());
                }

                for (int i : item.getItems()) {
                    ItemDefinition def = CometManager.getItems().getDefinition(i);
                    msg.writeString(def.getType());
                    msg.writeInt(def.getSpriteId());

                    if (item.getDisplayName().contains("wallpaper_single") || item.getDisplayName().contains("floor_single") || item.getDisplayName().contains("landscape_single")) {
                        msg.writeString(item.getDisplayName().split("_")[2]);
                    } else {
                        msg.writeString(item.getPresetData());
                    }

                    msg.writeInt(item.getAmount());

                    if (item.getLimitedTotal() == 0)
                        msg.writeInt(0);
                }

                msg.writeBoolean(item.getLimitedTotal() != 0);

                if (item.getLimitedTotal() > 0) {
                    msg.writeInt(item.getLimitedTotal());
                    msg.writeInt(item.getLimitedTotal() - item.getLimitedSells());
                    msg.writeInt(0);
                }

                msg.writeBoolean(!(item.getLimitedTotal() > 0) && item.allowOffer());
            }
        } else {
            msg.writeInt(0);
        }

        msg.writeInt(0);

        msg.writeBoolean(false);
        msg.writeBoolean(false);

        return msg;
    }
}
