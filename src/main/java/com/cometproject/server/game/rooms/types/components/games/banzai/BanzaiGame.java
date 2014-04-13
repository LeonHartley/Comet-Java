package com.cometproject.server.game.rooms.types.components.games.banzai;

import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import javolution.util.FastMap;

import java.util.Map;

public class BanzaiGame extends RoomGame {
    private Map<GameTeam, int[][]> capturedTiles;
    private Map<GameTeam, Integer> scores;

    //private Map<Integer, BanzaiTile> tiles;

    private RoomItem timerItem;

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

        timerItem = room.getItems().getByInteraction("bb_timer").get(0);
    }

    @Override
    public void tick() {
        // Update item timer's value ?
        timerItem.setExtraData((gameLength - timer) + "");
        timerItem.sendUpdate();

        this.getLog().info("Game tick");
    }

    @Override
    public void gameEnds() {
        for(FloorItem item : this.room.getItems().getByInteraction("bb_patch")) {
            // TODO: this
        }
    }

    @Override
    public void gameStarts() {
        for(FloorItem item : this.room.getItems().getByInteraction("bb_patch")) {
            item.setExtraData("1");
            item.sendUpdate();
        }
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

        int score = scores.get(team);

        if(capturedTiles.get(team)[x][y] > 2) {
            score++;

            scores.remove(team);
            scores.put(team, score);
        }

        for(FloorItem item : this.room.getItems().getItemsOnSquare(x, y)) {
            if(item.getDefinition().getInteraction().equals("bb_patch")) {
                int patchValue = Integer.parseInt(item.getExtraData());

                if(patchValue < 3) {
                    patchValue = 1;
                }

                item.setExtraData((patchValue + (team.getTeamId() * 3) - 1) + "");
                item.sendUpdate();
            }
        }
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
