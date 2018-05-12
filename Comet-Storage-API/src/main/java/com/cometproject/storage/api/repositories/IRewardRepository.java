package com.cometproject.storage.api.repositories;

import java.util.function.Consumer;

public interface IRewardRepository {
    void playerReceivedReward(int playerId, String badgeCode, Consumer<Boolean> consumer);

    void giveReward(int playerId, String badgeCode, int points);
}
