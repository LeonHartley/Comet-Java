package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogFrontPageEntry;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class CatalogPageMessageComposer extends MessageComposer {

    private final String catalogType;
    private final CatalogPage catalogPage;

    public CatalogPageMessageComposer(final String catalogType, final CatalogPage catalogPage) {
        this.catalogType = catalogType;
        this.catalogPage = catalogPage;
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

        if (!this.catalogPage.getTemplate().equals("frontpage") && !this.catalogPage.getTemplate().equals("club_buy")) {
            msg.writeInt(this.catalogPage.getItems().size());

            for (CatalogItem item : this.catalogPage.getItems().values()) {
                item.compose(msg);
            }
        } else {
            msg.writeInt(0);
        }

        msg.writeInt(0);
        msg.writeBoolean(false); // allow seasonal currency as credits

        if(this.catalogPage.getTemplate().equals("frontpage4")) {
            msg.writeInt(CatalogManager.getInstance().getFrontPageEntries().size());

            for(CatalogFrontPageEntry entry : CatalogManager.getInstance().getFrontPageEntries()) {
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
