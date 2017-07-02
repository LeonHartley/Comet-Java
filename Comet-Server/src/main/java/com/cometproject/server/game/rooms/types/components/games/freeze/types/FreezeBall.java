package com.cometproject.server.game.rooms.types.components.games.freeze.types;

import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.FreezeTileFloorItem;

public class FreezeBall {
    public static final int START_TICKS = 2;

    private final int playerId;
    private final FreezeTileFloorItem source;
    private final int range;
    private final boolean diagonal;

    private int ticksUntilExplode;

    public FreezeBall(int playerId, FreezeTileFloorItem source, int range, boolean diagonal) {
        this.playerId = playerId;
        this.source = source;
        this.range = range;
        this.diagonal = diagonal;

        // default ticks = 4
        this.ticksUntilExplode = 2;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public FreezeTileFloorItem getSource() {
        return source;
    }

    public boolean isMega() {
        return this.range == 999;
    }

    public int getRange() {
        return range;
    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public int getTicksUntilExplode() {
        return ticksUntilExplode;
    }

    public void decrementTicksUntilExplode() {
        this.ticksUntilExplode--;
    }
}
