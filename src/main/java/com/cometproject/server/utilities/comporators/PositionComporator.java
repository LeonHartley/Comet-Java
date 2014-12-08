package com.cometproject.server.utilities.comporators;

import com.cometproject.server.game.rooms.objects.RoomObject;

import java.util.Comparator;

public class PositionComporator implements Comparator<RoomObject> {
    private RoomObject roomObject;

    public PositionComporator(RoomObject roomObject) {
        this.roomObject = roomObject;
    }

    @Override
    public int compare(RoomObject o1, RoomObject o2) {
        final double distanceOne = o1.getPosition().distanceTo(this.roomObject.getPosition());
        final double distanceTwo = o2.getPosition().distanceTo(this.roomObject.getPosition());

        if(distanceOne > distanceTwo)
            return 1;
        else if(distanceOne < distanceTwo)
            return -1;

        return 0;
    }
}
