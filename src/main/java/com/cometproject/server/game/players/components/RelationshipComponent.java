package com.cometproject.server.game.players.components;

import com.cometproject.server.game.players.components.types.RelationshipLevel;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.storage.queries.player.relationships.RelationshipDao;

import java.util.Map;

public class RelationshipComponent {
    private Player player;
    private Map<Integer, RelationshipLevel> relationships;

    public RelationshipComponent(Player player) {
        this.player = player;

        this.relationships = RelationshipDao.getRelationshipsByPlayerId(player.getId());
    }

    public void dispose() {
        this.relationships.clear();
        this.relationships = null;
        this.player = null;
    }

    public int countByLevel(RelationshipLevel level) {
        int i = 0;

        for (RelationshipLevel l : relationships.values()) {
            if (l == level)
                i++;
        }

        return i;
    }

    public RelationshipLevel get(int playerId) {
        return this.relationships.get(playerId);
    }

    public void remove(int playerId) {
        this.getRelationships().remove(playerId);
    }

    public int count() {
        return this.relationships.size();
    }

    public synchronized Map<Integer, RelationshipLevel> getRelationships() {
        return this.relationships;
    }

    public Player getPlayer() {
        return this.player;
    }
}
