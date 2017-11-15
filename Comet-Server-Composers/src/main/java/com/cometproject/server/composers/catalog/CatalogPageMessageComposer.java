package com.cometproject.server.composers.catalog;

import com.cometproject.api.game.catalog.types.CatalogPageType;
import com.cometproject.api.game.catalog.types.ICatalogFrontPageEntry;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.catalog.ICatalogService;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.google.common.collect.Sets;

import java.util.Set;


public class CatalogPageMessageComposer extends MessageComposer {

    private final String catalogType;
    private final ICatalogPage catalogPage;
    private final IPlayer player;
    private final ICatalogService catalogService;

    public CatalogPageMessageComposer(final String catalogType, final ICatalogPage catalogPage, final IPlayer player,
                                      ICatalogService catalogService) {
        this.catalogType = catalogType;
        this.catalogPage = catalogPage;
        this.player = player;
        this.catalogService = catalogService;
    }

    @Override
    public short getId() {
        return Composers.CatalogPageMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.catalogPage.getId());
        msg.writeString(this.catalogType); // builders club or not
        msg.writeString(this.catalogPage.getTemplate());

        msg.writeInt(this.catalogPage.getImages().size());

        for (String image : this.catalogPage.getImages()) {
            msg.writeString(image);
        }

        msg.writeInt(this.catalogPage.getTexts().size());

        for (String text : this.catalogPage.getTexts()) {
            msg.writeString(text);
        }

        if (this.catalogPage.getType() == CatalogPageType.RECENT_PURCHASES) {
            final Set<ICatalogItem> recentPurchases = Sets.newHashSet();

            for (Integer catalogItemId : player.getRecentPurchases()) {
                final ICatalogItem catalogItem = this.catalogService.getCatalogItem(catalogItemId);

                if (catalogItem != null) {
                    recentPurchases.add(catalogItem);
                }
            }

            msg.writeInt(recentPurchases.size());

            for (ICatalogItem item : recentPurchases) {
                item.compose(msg);
            }

            recentPurchases.clear();
        } else if (!this.catalogPage.getTemplate().equals("frontpage") && !this.catalogPage.getTemplate().equals("club_buy")) {
            msg.writeInt(this.catalogPage.getItems().size());

            for (ICatalogItem item : this.catalogPage.getItems().values()) {
                item.compose(msg);
            }
        } else {
            msg.writeInt(0);
        }

        msg.writeInt(0);
        msg.writeBoolean(false); // allow seasonal currency as credits

        if (this.catalogPage.getTemplate().equals("frontpage4")) {
            msg.writeInt(this.catalogService.getFrontPageEntries().size());

            for (ICatalogFrontPageEntry entry : this.catalogService.getFrontPageEntries()) {
                msg.writeInt(entry.getId());
                msg.writeString(entry.getCaption());
                msg.writeString(entry.getImage());
                msg.writeInt(0);
                msg.writeString(entry.getPageLink());
                msg.writeString(entry.getPageId());
            }
        }
    }
}
