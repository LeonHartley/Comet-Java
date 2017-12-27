package com.cometproject.server.network.messages.outgoing.landing;

import com.cometproject.api.game.landing.types.IPromoArticle;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;


public class PromoArticlesMessageComposer extends MessageComposer {
    private final List<IPromoArticle> articles;

    public PromoArticlesMessageComposer(final List<IPromoArticle> articles) {
        this.articles = articles;
    }

    @Override
    public short getId() {
        return Composers.PromoArticlesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(articles.size());

        for (IPromoArticle article : articles) {
            msg.writeInt(article.getId());
            msg.writeString(article.getTitle());
            msg.writeString(article.getMessage());
            msg.writeString(article.getButtonText());
            msg.writeInt(0); // Button Type
            msg.writeString(article.getButtonLink());
            msg.writeString(article.getImagePath());
        }
    }
}
