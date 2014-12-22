package com.cometproject.server.game.rooms.objects.entities;

import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.utilities.attributes.Attributable;

import java.util.List;
import java.util.Map;


public interface AvatarEntity extends Attributable {
    public int getId();

    public Position getWalkingGoal();

    public void setWalkingGoal(int x, int y);

    public Position getPositionToSet();

    public void updateAndSetPosition(Position position);

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

    public void moveTo(int x, int y);

    public Map<RoomEntityStatus, String> getStatuses();

    public void addStatus(RoomEntityStatus key, String value);

    public void removeStatus(RoomEntityStatus key);

    public boolean hasStatus(RoomEntityStatus key);

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

    public boolean isOverriden();

    public void setOverriden(boolean overriden);

    public PlayerEffect getCurrentEffect();

    public void applyEffect(PlayerEffect effect);

    public void carryItem(int id);

    public int getHandItem();

    public boolean handItemNeedsRemove();

    public int getHandItemTimer();

    public void setHandItemTimer(int time);

    public String getUsername();

    public String getMotto();

    public String getFigure();

    public String getGender();

    public void compose(Composer msg);

    public void warp(Position position);

    public void kick();
}
