package com.cometproject.api.game.landing;

import com.cometproject.api.game.landing.types.IHallOfFame;
import com.cometproject.api.game.landing.types.IPromoArticle;

import java.util.List;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public interface ILandingService {
    void loadArticles();

    void loadHallOfFame();

    List<IPromoArticle> getArticles();

    List<IHallOfFame> getHallOfFames();
}