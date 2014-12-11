package com.cometproject.server.network.messages.outgoing.landing;

import com.cometproject.server.game.landing.types.PromoArticle;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;


public class PromoArticlesMessageComposer {
    public static Composer compose(Map<Integer, PromoArticle> articles) {
        Composer msg = new Composer(Composers.LandingPromosMessageComposer);

        msg.writeInt(articles.size());

        for (PromoArticle article : articles.values()) {
            msg.writeInt(article.getId());
            msg.writeString(article.getTitle());
            msg.writeString(article.getMessage());
            msg.writeString(article.getButtonText());
            msg.writeInt(0); // Button Type
            msg.writeString(article.getButtonLink());
            msg.writeString(article.getImagePath());
        }

        return msg;
    }
}
