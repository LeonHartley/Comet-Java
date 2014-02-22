package com.cometproject.server.game.rooms.types.components.games.banzai;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import javolution.util.FastMap;

import java.util.Map;

public class BanzaiGame extends RoomGame {
    private Map<GameTeam, int[][]> capturedTiles;
    private Map<GameTeam, Integer> scores;

    public BanzaiGame(Room room) {
        super(room, GameType.BANZAI);

        capturedTiles = new FastMap<>();
        scores = new FastMap<>();

        int sizeX = room.getModel().getSizeX();
        int sizeY = room.getModel().getSizeY();

        for(GameTeam team : GameTeam.values()) {
            if(team == GameTeam.NONE) continue;

            int[][] tiles = new int[sizeX][sizeY];

            for(int x = 0; x < sizeX; x++) {
                for(int y = 0; y < sizeY; y++) {
                    tiles[x][y] = 0;
                }
            }

            capturedTiles.put(team, tiles);

            if(!scores.containsKey(team))
                scores.put(team, 0);
        }
    }

    @Override
    public void tick() {
        // TODO: banzai game tick (?)

        this.getLog().info("Game tick");
    }

    public void captureTile(int x, int y, GameTeam team) {
        for(int[][] tile : capturedTiles.values()) {
            if(tile[x][y] != 0) {
                // reset this tile!

                if(tile[x][y] >= 2) {
                    // tile is locked, why are we trying to capture it??
                    return;
                }

                tile[x][y] = 0;
            }
        }

        // increase the level of tile
        capturedTiles.get(team)[x][y]++;

        if(capturedTiles.get(team)[x][y] > 2) {
            // maybe increase score idk
            int score = scores.get(team);
            score++;

            scores.remove(team);
            scores.put(team, score);
        }

        // TODO: broadcast score etc.
    }

    public int getScore(GameTeam team) {
        if(!scores.containsKey(team)) {
            return 0; // Not teamed (team = GameTeam.NONE probably)
        }

        if(scores.get(team) != null) {
            return scores.get(team);
        }

        return 0;
    }
}
