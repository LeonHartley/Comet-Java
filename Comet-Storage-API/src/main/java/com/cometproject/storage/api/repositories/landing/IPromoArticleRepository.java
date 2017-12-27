package com.cometproject.storage.api.repositories.landing;

import com.cometproject.api.game.landing.types.IPromoArticle;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public interface IPromoArticleRepository {
    void getPromoArticles(Consumer<List<IPromoArticle>> promoArticlesConsumer);
}
