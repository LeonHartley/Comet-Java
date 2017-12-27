package com.cometproject.storage.mysql.repositories.landing;

import com.cometproject.api.game.landing.types.IPromoArticle;
import com.cometproject.storage.api.repositories.landing.IPromoArticleRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.results.IResultReader;
import com.cometproject.storage.mysql.models.landing.factories.PromoArticleFactory;
import com.cometproject.storage.mysql.repositories.MySQLRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public class MySQLPromoArticleRepository extends MySQLRepository implements IPromoArticleRepository {
    private final PromoArticleFactory promoArticleFactory;

    public MySQLPromoArticleRepository(PromoArticleFactory promoArticleFactory, MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.promoArticleFactory = promoArticleFactory;
    }

    @Override
    public void getPromoArticles(Consumer<List<IPromoArticle>> promoArticlesConsumer) {
        final List<IPromoArticle> promoArticles = new ArrayList<>();
        select("SELECT * FROM server_articles WHERE visible = '1'", data -> {
            promoArticles.add(readPromoArticle(data));
        });
        promoArticlesConsumer.accept(promoArticles);
    }

    private IPromoArticle readPromoArticle(IResultReader data) throws Exception {
        int id = data.readInteger("id");
        String title = data.readString("title");
        String message = data.readString("message");
        String buttonText = data.readString("button_text");
        String buttonLink = data.readString("button_link");
        String imagePath = data.readString("image_path");

        return this.promoArticleFactory.create(id, title, message, buttonText, buttonLink, imagePath);
    }
}
