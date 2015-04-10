package com.cometproject.server.game.rooms.objects.entities.pathfinding.types;

import com.cometproject.server.game.rooms.objects.entities.pathfinding.Pathfinder;

public class ItemPathfinder extends Pathfinder {
    private static ItemPathfinder pathfinderInstance;

    public static ItemPathfinder getInstance() {
        if(pathfinderInstance == null) {
            pathfinderInstance = new ItemPathfinder();
        }

        return pathfinderInstance;
    }
}
