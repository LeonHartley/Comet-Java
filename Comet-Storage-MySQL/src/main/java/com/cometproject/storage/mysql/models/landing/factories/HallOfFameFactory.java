package com.cometproject.storage.mysql.models.landing.factories;

import com.cometproject.api.game.landing.types.IHallOfFame;
import com.cometproject.storage.mysql.models.landing.HallOfFame;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public class HallOfFameFactory {

    public IHallOfFame create(int id, int currency, String username, String figure) {
        return new HallOfFame(id, currency, username, figure);
    }

}
