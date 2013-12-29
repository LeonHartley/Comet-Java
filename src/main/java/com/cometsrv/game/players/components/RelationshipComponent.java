package com.cometsrv.game.players.components;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.players.components.types.RelationshipLevel;
import com.cometsrv.game.players.types.Player;
import javolution.util.FastMap;

import java.sql.ResultSet;
import java.util.Map;

public class RelationshipComponent {
    private Player player;
    private Map<Integer, RelationshipLevel> relationships;

    public RelationshipComponent(Player player) {
        this.player = player;
        this.relationships = new FastMap<>();

        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM player_relationships WHERE player_id = " + this.getPlayer().getId());

            while(data.next()) {
                this.relationships.put(data.getInt("partner"), RelationshipLevel.getLevel(data.getString("level")));
            }
        } catch(Exception e) {
            GameEngine.getLogger().error("Error while loading player relationships", e);
        }
    }

    public void dispose() {
        this.relationships.clear();
        this.relationships = null;
        this.player = null;
    }

    public int countByLevel(RelationshipLevel level) {
        int i = 0;

        for(RelationshipLevel l : relationships.values()) {
            if(l == level)
                i++;
        }

        return i;
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
