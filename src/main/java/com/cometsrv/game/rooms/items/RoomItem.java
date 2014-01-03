package com.cometsrv.game.rooms.items;

import com.cometsrv.game.items.interactions.InteractionQueueItem;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.utilities.DistanceCalculator;
import com.cometsrv.network.messages.types.Composer;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public abstract class RoomItem implements GenericRoomItem, InteractableRoomItem {
    private static final int MAX_INTERACTION_QUEUE = 1;

    protected int id;
    protected int itemId;
    protected int ownerId;

    protected int x;
    protected int y;

    protected int rotation;

    protected boolean state;

    private Queue<InteractionQueueItem> interactionQueue = new LinkedList<>();
    protected InteractionQueueItem curInteractionItem;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getItemId() {
        return this.itemId;
    }

    @Override
    public int getOwner() {
        return this.ownerId;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getRotation() {
        return this.rotation;
    }

    @Override
    public boolean getState() {
        return this.state;
    }

    @Override
    public boolean hasInteraction() {
        return (this.interactionQueue.size() > 0 || this.curInteractionItem != null);
    }

    @Override
    public InteractionQueueItem getNextInteraction() {
        if (this.curInteractionItem != null) {
            if (this.curInteractionItem.getUpdateCycles() > 0 && this.curInteractionItem.needsCycling()) {
                return this.curInteractionItem;
            } else {
                this.curInteractionItem = null;
            }
        }

        try {
            this.curInteractionItem = this.interactionQueue.remove();
        } catch (NoSuchElementException e) {
            this.curInteractionItem = null;
        }

        return this.curInteractionItem;
    }

    @Override
    public void queueInteraction(InteractionQueueItem interaction) {
        // check the queue size
        if (this.interactionQueue.size() > MAX_INTERACTION_QUEUE) {
            return;
        }

        this.interactionQueue.add(interaction);
    }

    public int distance(Avatar avatar) {
        int avatarX = avatar.getPosition().getX();
        int avatarY = avatar.getPosition().getY();

        return DistanceCalculator.calculate(avatarX, avatarY, this.getX(), this.getY());
    }

    public boolean touching(Avatar avatar) {
        int avatarX = avatar.getPosition().getX();
        int avatarY = avatar.getPosition().getY();

        return DistanceCalculator.tilesTouching(avatarX, avatarY, this.getX(), this.getY());
    }

    public abstract void serialize(Composer msg);
}
