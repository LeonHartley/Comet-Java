package com.cometsrv.game.rooms.entities;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.PathfinderNew;
import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.types.Composer;

import java.util.List;
import java.util.Map;

public interface AvatarEntity {
    public int getVirtualId();

    public Position3D getPosition();
    public void setPosition(Position3D position);

    public Position3D getWalkingGoal();
    public void setWalkingGoal(int x, int y);

    public int getBodyRotation();
    public void setBodyRotation(int rotation);

    public int getHeadRotation();
    public void setHeadRotation(int rotation);

    public List<Square> getWalkingPath();
    public void setWalkingPath(List<Square> path);
    public boolean isWalking();

    public PathfinderNew getPathfinder();

    public Map<String, String> getStatuses();
    public void addStatus(String key, String value);

    public int getIdleTime();
    public boolean isIdleAndIncrement();
    public void resetIdleTime();

    public int getSignTime();
    public boolean isDisplayingSign();

    public Room getRoom();

    public String getUsername();
    public String getMotto();
    public String getFigure();
    public String getGender();

    public void compose(Composer msg);
}
