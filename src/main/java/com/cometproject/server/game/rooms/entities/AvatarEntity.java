package com.cometproject.server.game.rooms.entities;

import com.cometproject.server.game.rooms.avatars.effects.UserEffect;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.Pathfinder;
import com.cometproject.server.game.rooms.avatars.pathfinding.Square;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;
import java.util.Map;

public interface AvatarEntity {
    public int getVirtualId();

    public Position3D getPosition();
    public void setPosition(Position3D position);

    public Position3D getWalkingGoal();
    public void setWalkingGoal(int x, int y);

    public Position3D getPositionToSet();
    public void updateAndSetPosition(Position3D position);
    public void markPositionIsSet();

    public int getBodyRotation();
    public void setBodyRotation(int rotation);

    public int getHeadRotation();
    public void setHeadRotation(int rotation);

    public List<Square> getWalkingPath();
    public void setWalkingPath(List<Square> path);
    public List<Square> getProcessingPath();
    public void setProcessingPath(List<Square> path);
    public boolean isWalking();

    public Square getFutureSquare();
    public void setFutureSquare(Square square);

    public Pathfinder getPathfinder();
    public void moveTo(int x, int y);

    public Map<String, String> getStatuses();
    public void addStatus(String key, String value);
    public void removeStatus(String key);
    public boolean hasStatus(String key);

    public void markNeedsUpdate();
    public boolean needsUpdate();

    public void setIdle();
    public int getIdleTime();
    public boolean isIdleAndIncrement();
    public void resetIdleTime();

    public int getDanceId();
    public void setDanceId(int danceId);

    public int getSignTime();
    public void markDisplayingSign();
    public boolean isDisplayingSign();

    public boolean isInTeleporter();
    public void setIsInTeleporter(boolean isInTeleporter);

    public UserEffect getCurrentEffect();
    public void applyEffect(UserEffect effect);

    public void carryItem(int id);
    public int getHandItem();
    public boolean handItemNeedsRemove();
    public int getHandItemTimer();
    public void setHandItemTimer(int time);

    public Room getRoom();

    public String getUsername();
    public String getMotto();
    public String getFigure();
    public String getGender();

    public void compose(Composer msg);
}
