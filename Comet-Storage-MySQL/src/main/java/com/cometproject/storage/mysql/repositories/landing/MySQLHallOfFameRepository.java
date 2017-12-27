package com.cometproject.storage.mysql.repositories.landing;

import com.cometproject.api.game.landing.types.IHallOfFame;
import com.cometproject.storage.api.repositories.landing.IHallOfFameRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.results.IResultReader;
import com.cometproject.storage.mysql.models.landing.factories.HallOfFameFactory;
import com.cometproject.storage.mysql.repositories.MySQLRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public class MySQLHallOfFameRepository extends MySQLRepository implements IHallOfFameRepository {

    private final HallOfFameFactory hallOfFameFactory;

    public MySQLHallOfFameRepository(HallOfFameFactory hallOfFameFactory, MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.hallOfFameFactory = hallOfFameFactory;
    }

    @Override
    public void getHallOfFamers(Consumer<List<IHallOfFame>> hallOfFamesConsumer, String currency, int limit) {
        final List<IHallOfFame> promoArticles = new ArrayList<>();
        select("SELECT id, username, figure, vip_points FROM players ORDER BY " + currency + " DESC LIMIT " + limit, data -> {
            promoArticles.add(readHallOfFame(data));
        });
        hallOfFamesConsumer.accept(promoArticles);
    }

    private IHallOfFame readHallOfFame(IResultReader data) throws Exception {
        int id = data.readInteger("id");
        int points = data.readInteger("vip_points");
        String username = data.readString("username");
        String figure = data.readString("figure");

        return this.hallOfFameFactory.create(id, points, username, figure);
    }
}
