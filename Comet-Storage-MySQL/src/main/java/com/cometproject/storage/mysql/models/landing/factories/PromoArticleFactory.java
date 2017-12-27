package com.cometproject.storage.mysql.models.landing.factories;

import com.cometproject.api.game.landing.types.IPromoArticle;
import com.cometproject.storage.mysql.models.landing.PromoArticle;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public class PromoArticleFactory {

    public IPromoArticle create(int id, String title, String message, String buttonText, String buttonLink, String imagePath) {
        return new PromoArticle(id, title, message, buttonText, buttonLink, imagePath);
    }

}
