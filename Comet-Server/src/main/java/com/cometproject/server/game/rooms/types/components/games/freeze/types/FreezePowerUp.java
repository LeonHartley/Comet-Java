package com.cometproject.server.game.rooms.types.components.games.freeze.types;

import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.utilities.RandomUtil;

public enum FreezePowerUp {
    None,
    ExtraRange,
    ExtraBall,
    DiagonalExplosion,
    MegaExplosion,
    Life,
    Shield;

    public static FreezePowerUp getRandom() {
        FreezePowerUp[] powerUps = FreezePowerUp.values();

        return powerUps[RandomUtil.getRandomInt(0, powerUps.length - 1)];
    }
}
