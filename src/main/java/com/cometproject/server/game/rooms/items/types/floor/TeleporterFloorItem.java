package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.outgoing.messenger.FollowFriendMessageComposer;

public class TeleporterFloorItem extends RoomItemFloor {
    private boolean inUse = false;
    private GenericEntity outgoingEntity;
    private GenericEntity incomingEntity;

    private int state = -1;

    public TeleporterFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        //if (!this.getDefinition().canWalk) {
        //    return;
        //}

        Position3D posInFront = this.squareInfront();

        if ((entity.getPosition().getX() != posInFront.getX() && entity.getPosition().getY() != posInFront.getY())
                && !(entity.getPosition().getX() == this.getX() && entity.getPosition().getY() == this.getY())) {
            entity.moveTo(posInFront.getX(), posInFront.getY());
            return;
        }

        if (this.inUse) { return; }
        this.inUse = true;
        this.outgoingEntity = entity;

        this.state = 0;
        this.setTicks(RoomItemFactory.getProcessTime(1));
    }

    @Override
    public void onTickComplete() {
        switch (this.state) {
            case 0: {
                this.outgoingEntity.setOverriden(true);

                if (!this.getDefinition().canWalk) {
                    this.outgoingEntity.moveTo(this.getX(), this.getY());
                }
                this.toggleDoor(true);

                this.state = 1;
                this.setTicks(RoomItemFactory.getProcessTime(1));
                break;
            }

            case 1: {
                this.toggleDoor(false);

                this.state = 2;
                this.setTicks(RoomItemFactory.getProcessTime(0.5));
                break;
            }

            case 2: {
                this.toggleAnimation(true);

                this.state = 3;
                this.setTicks(RoomItemFactory.getProcessTime(2));
                break;
            }

            case 3: {
                int pairId = CometManager.getItems().getTeleportPartner(this.getId());
                RoomItemFloor pairItem = this.getRoom().getItems().getFloorItem(pairId);

                if (pairId == 0 || !(pairItem instanceof TeleporterFloorItem)) {
                    this.state = 5;
                    this.setTicks(RoomItemFactory.getProcessTime(2));
                    return;
                }

                if (pairItem == null) {
                    int roomId = CometManager.getItems().roomIdByItemId(pairId);

                    if (CometManager.getRooms().get(roomId) != null) {
                        if (this.outgoingEntity instanceof PlayerEntity) {
                            PlayerEntity pEntity = (PlayerEntity)this.outgoingEntity;
                            pEntity.getPlayer().setTeleportId(pairId);
                            pEntity.getPlayer().getSession().send(FollowFriendMessageComposer.compose(roomId));
                        }
                    }

                    this.state = 5;
                    this.setTicks(RoomItemFactory.getProcessTime(2));
                    return;
                }

                TeleporterFloorItem teleItem = (TeleporterFloorItem)pairItem;
                teleItem.handleIncomingEntity(this.outgoingEntity, this, 4);

                break;
            }

            case 5: {
                this.toggleAnimation(false);
                this.state = 6;
                this.setTicks(RoomItemFactory.getProcessTime(0.5));
                break;
            }

            case 6: {
                this.toggleDoor(true);

                if (this.incomingEntity == null) { return; }
                this.incomingEntity.moveTo(this.squareInfront().getX(), this.squareInfront().getY());

                this.state = 7;
                this.setTicks(RoomItemFactory.getProcessTime(1));
                break;
            }

            case 7: {
                this.toggleDoor(false);
                this.incomingEntity.setOverriden(false);
                this.incomingEntity = null;
                break;
            }
        }
    }

    public void endTeleporting() {
        this.toggleAnimation(false);

        this.state = -1;
        this.outgoingEntity = null;
        this.inUse = false;
    }

    public void handleIncomingEntity(GenericEntity entity, TeleporterFloorItem otherItem, int state) {
        otherItem.endTeleporting();

        this.toggleAnimation(true);
        entity.updateAndSetPosition(new Position3D(this.getX(), this.getY()));

        this.incomingEntity = entity;

        this.state = 5;
        this.setTicks(RoomItemFactory.getProcessTime(2));
    }

    protected void toggleDoor(boolean state) {
        if (state)
            this.setExtraData("1");
        else
            this.setExtraData("0");

        this.sendUpdate();
    }

    protected void toggleAnimation(boolean state) {
        if (state)
            this.setExtraData("2");
        else
            this.setExtraData("0");

        this.sendUpdate();
    }
}
