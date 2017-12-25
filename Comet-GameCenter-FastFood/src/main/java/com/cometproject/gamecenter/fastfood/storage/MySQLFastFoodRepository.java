package com.cometproject.gamecenter.fastfood.storage;

import com.cometproject.gamecenter.fastfood.FastFoodGame;
import com.cometproject.gamecenter.fastfood.net.FastFoodGameSession;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.repositories.MySQLRepository;

public class MySQLFastFoodRepository extends MySQLRepository {
    public MySQLFastFoodRepository(MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public void loadPlayerData(final FastFoodGameSession gameSession) {
        select("SELECT * FROM fastfood_user_data WHERE player_id = ?", (data) -> {
            final int parachutes = data.readInteger("parachutes");
            final int shields = data.readInteger("shields");
            final int missiles = data.readInteger("missiles");

            gameSession.setParachutes(parachutes);
            gameSession.setMissiles(missiles);
            gameSession.setShields(shields);

        }, gameSession.getPlayerId());

        if(gameSession.getParachutes() == -1) {
            insert("INSERT into fastfood_user_data (player_id, shields, parachutes, missiles, games_played) VALUES(?, 10, 10, 10, 0);", (k) -> {}, gameSession.getPlayerId());
            gameSession.setParachutes(10);
            gameSession.setMissiles(10);
            gameSession.setShields(10);
        }
    }

    public void savePlayerData(final FastFoodGameSession gameSession) {
        update("UPDATE fastfood_user_data SET parachutes = ?, objects = ?, shields = ?, games_played = ? WHERE player_id = ?",
                gameSession.getParachutes(), gameSession.getMissiles(), gameSession.getShields(), 1,
                gameSession.getPlayerId());
    }
}
