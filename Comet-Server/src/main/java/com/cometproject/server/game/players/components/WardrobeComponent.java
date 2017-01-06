package com.cometproject.server.game.players.components;

import com.cometproject.server.game.players.components.types.wardrobe.WardrobeClothing;
import com.cometproject.server.game.players.types.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WardrobeComponent {
    private final Map<Integer, WardrobeClothing> purchasedClothing;

    private final Player player;

    public WardrobeComponent(final Player player) {
        this.player = player;

        this.purchasedClothing = new ConcurrentHashMap<>();

    }
}
