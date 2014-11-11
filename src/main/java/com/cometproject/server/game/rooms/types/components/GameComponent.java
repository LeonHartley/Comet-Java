package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.game.rooms.types.components.games.banzai.BanzaiGame;
import com.google.common.collect.Lists;
import javolution.util.FastMap;

import java.util.List;
import java.util.Map;

public class GameComponent {
    private Room room;
    private RoomGame instance;

    private Map<GameTeam, List<Integer>> teams;

    public GameComponent(Room room) {
        this.teams = new FastMap<GameTeam, List<Integer>>() {{
            add(GameTeam.BLUE, Lists.newArrayList());
            add(GameTeam.YELLOW, Lists.newArrayList());
            add(GameTeam.RED, Lists.newArrayList());
            add(GameTeam.GREEN, Lists.newArrayList());
        }};

        this.room = room;
    }

    public void dispose() {
        for(Map.Entry<GameTeam, List<Integer>> entry : this.teams.entrySet()) {
            entry.getValue().clear();
        }

        this.teams.clear();
    }

    public void stop() {
        if (this.instance != null) {
            this.instance.stop();
        }

        this.instance = null;
    }

    public void createNew(GameType game) {
        if (game == GameType.BANZAI) {
            this.instance = new BanzaiGame(this.room);
        } else if (game == GameType.FREEZE) {
            this.instance = null; // TODO: freeze game
        }
    }

    public boolean isTeamed(int id) {
        return this.getTeam(id) != GameTeam.NONE;
    }

    public void removeFromTeam(GameTeam team, Integer id) {
        if (this.teams.get(team).contains(id))
            this.teams.get(team).remove(id);
    }

    public GameTeam getTeam(int userId) {
        for (Map.Entry<GameTeam, List<Integer>> entry : this.getTeams().entrySet()) {
            if (entry.getValue().contains(userId)) {
                return entry.getKey();
            }
        }

        return GameTeam.NONE;
    }

    public Map<GameTeam, List<Integer>> getTeams() {
        return teams;
    }


    public RoomGame getInstance() {
        return this.instance;
    }

    public Room getRoom() {
        return this.room;
    }
}
