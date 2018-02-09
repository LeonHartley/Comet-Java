package com.cometproject.server.composers.catalog;

import com.cometproject.api.game.catalog.ICatalogService;
import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.game.furniture.IFurnitureService;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;

public class CatalogIndexMessageComposer extends MessageComposer {
    private final IFurnitureService furnitureService;
    private final ICatalogService catalogService;

    private final int playerRank;

    public CatalogIndexMessageComposer(final ICatalogService catalogService, final IFurnitureService furnitureService, final int playerRank) {
        this.catalogService = catalogService;
        this.furnitureService = furnitureService;
        this.playerRank = playerRank;
    }

    @Override
    public short getId() {
        return Composers.CatalogIndexMessageComposer;
    }

    @Override
    public void compose(final IComposer msg) {
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(-1);
        msg.writeString("root");
        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(this.countAccessiblePages(this.catalogService.getParentPages()));

        for (final ICatalogPage page : this.catalogService.getParentPages()) {
            if(page.getMinRank() > this.playerRank) {
                continue;
            }

            composePage(page, msg);
        }

        msg.writeBoolean(false);
        msg.writeString("NORMAL");
    }

    private void composePage(ICatalogPage page, IComposer msg) {
        msg.writeBoolean(true);
        msg.writeInt(page.getIcon());
        msg.writeInt(page.isEnabled() ? page.getId() : -1);
        msg.writeString(page.getLinkName().equals("undefined") ? page.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : page.getLinkName());
        msg.writeString(page.getCaption());
        msg.writeInt(0);
        msg.writeInt(this.countAccessiblePages(page.getChildren()));

        for (final ICatalogPage child : page.getChildren()) {
            if (child.getMinRank() > this.playerRank) {
                continue;
            }

            msg.writeBoolean(true);
            msg.writeInt(child.getIcon());
            msg.writeInt(child.isEnabled() ? child.getId() : -1);
            msg.writeString(child.getLinkName().equals("undefined") ? child.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : child.getLinkName());
            msg.writeString(child.getCaption());
            msg.writeInt(child.getOfferSize());

            for (ICatalogItem item : child.getItems().values()) {
                if (item.getItemId().equals("-1")) continue;

                FurnitureDefinition itemDefinition = this.furnitureService.getDefinition(item.getItems().get(0).getItemId());

                if (itemDefinition != null) {
                    int offerId = itemDefinition.getOfferId();

                    if (offerId != -1) {
                        msg.writeInt(offerId);
                    }
                }
            }

            msg.writeInt(this.countAccessiblePages(child.getChildren()));

            for (final ICatalogPage childTwo : child.getChildren()) {
                if (child.getMinRank() > this.playerRank) {
                    continue;
                }

                composePage(childTwo, msg);
            }
        }
    }

    private int countAccessiblePages(final List<ICatalogPage> pages) {
        int count = 0;

        for (final ICatalogPage catalogPage : pages) {
            if (catalogPage.getMinRank() <= this.playerRank) {
                count++;
            }
        }

        return count;
    }
}
