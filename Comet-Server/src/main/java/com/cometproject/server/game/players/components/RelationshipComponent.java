package com.cometproject.server.game.players.components;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.players.data.components.PlayerRelationships;
import com.cometproject.api.game.players.data.components.messenger.RelationshipLevel;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.storage.queries.player.relationships.RelationshipDao;

import java.util.Map;


public class RelationshipComponent extends PlayerComponent implements PlayerRelationships {
    private Map<Integer, RelationshipLevel> relationships;

    public RelationshipComponent(IPlayer player) {
        super(player);

        this.relationships = RelationshipDao.getRelationshipsByPlayerId(player.getId());
    }

    public void dispose() {
        this.relationships.clear();
        this.relationships = null;
    }

    @Override
    public RelationshipLevel get(int playerId) {
        return this.relationships.get(playerId);
    }

    @Override
    public void remove(int playerId) {
        this.getRelationships().remove(playerId);

        this.getPlayer().flush();
    }

    @Override
    public int count() {
        return this.relationships.size();
    }

    @Override
    public Map<Integer, RelationshipLevel> getRelationships() {
        return this.relationships;
    }
}
