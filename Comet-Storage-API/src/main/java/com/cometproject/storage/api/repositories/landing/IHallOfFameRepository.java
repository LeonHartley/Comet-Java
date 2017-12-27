package com.cometproject.storage.api.repositories.landing;

import com.cometproject.api.game.landing.types.IHallOfFame;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public interface IHallOfFameRepository {
    void getHallOfFamers(Consumer<List<IHallOfFame>> promoArticlesConsumer, String currency, int limit);
}
