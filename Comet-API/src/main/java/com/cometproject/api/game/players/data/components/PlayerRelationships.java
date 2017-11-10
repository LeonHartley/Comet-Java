package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.data.components.messenger.RelationshipLevel;

import java.util.Map;

public interface PlayerRelationships {
    static int countByLevel(RelationshipLevel level, Map<Integer, RelationshipLevel> relationships) {
        int levelCount = 0;

        for (RelationshipLevel relationship : relationships.values()) {
            if (relationship == level) levelCount++;
        }

        return levelCount;
    }

    RelationshipLevel get(int playerId);

    void remove(int playerId);

    int count();

    Map<Integer, RelationshipLevel> getRelationships();
}
