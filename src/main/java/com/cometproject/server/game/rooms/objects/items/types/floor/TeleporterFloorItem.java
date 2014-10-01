package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.messenger.FollowFriendMessageComposer;

public class TeleporterFloorItem extends RoomItemFloor {
    private boolean inUse = false;
    private GenericEntity outgoingEntity;
    private GenericEntity incomingEntity;

    private int state = -1;
    boolean isDoor = false;


    public TeleporterFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if(this.getDefinition().getInteraction().equals("teleport_door")) {
            this.isDoor = true;
        }
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if(isWiredTrigger) return; //go away u canny use fkin teleport via wired

        Position posInFront = this.getPosition().squareInFront(this.getRotation());

        if (entity.getPosition().getX() != posInFront.getX() || entity.getPosition().getY() != posInFront.getY()) {
            entity.moveTo(posInFront.getX(), posInFront.getY());
            return;
        }

        if (this.inUse) {
            return;
        }

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

                this.outgoingEntity.moveTo(this.getPosition().getX(), this.getPosition().getY());

                this.toggleDoor(true);

                this.state = 1;
                this.setTicks(RoomItemFactory.getProcessTime(1));
                break;
            }

            case 1: {
                int pairId = CometManager.getItems().getTeleportPartner(this.getId());
                RoomItemFloor pairItem = this.getPartner(pairId);

                if (pairItem == null) {
                    int roomId = CometManager.getItems().roomIdByItemId(pairId);

                    if (CometManager.getRooms().get(roomId) == null) {
                        this.state = 8;
                        this.setTicks(RoomItemFactory.getProcessTime(0.5));
                        return;
                    }
                }

                if(!this.isDoor)
                    this.toggleDoor(false);

                this.state = 2;
                this.setTicks(RoomItemFactory.getProcessTime(0.5));
                break;
            }

            case 2: {
                if(!this.isDoor) {
                    this.toggleAnimation(true);

                    this.state = 3;
                    this.setTicks(RoomItemFactory.getProcessTime(2));
                } else {
                    this.state = 3;
                    this.setTicks(RoomItemFactory.getProcessTime(0.1));
                }
                break;
            }

            case 3: {
                int pairId = CometManager.getItems().getTeleportPartner(this.getId());

                if (pairId == 0) {
                    this.state = 8;
                    this.setTicks(RoomItemFactory.getProcessTime(0.5));
                    return;
                }

                RoomItemFloor pairItem = this.getPartner(pairId);

                if (pairItem == null) {
                    int roomId = CometManager.getItems().roomIdByItemId(pairId);

                    if (CometManager.getRooms().get(roomId) != null) {
                        if (this.outgoingEntity instanceof PlayerEntity) {
                            PlayerEntity pEntity = (PlayerEntity) this.outgoingEntity;
                            pEntity.getPlayer().setTeleportId(pairId);
                            pEntity.getPlayer().getSession().send(FollowFriendMessageComposer.compose(roomId));
                        }
                    } else {
                        this.state = 8;
                        this.setTicks(RoomItemFactory.getProcessTime(0.5));
                        return;
                    }
                }

                TeleporterFloorItem teleItem = (TeleporterFloorItem) pairItem;

                if (teleItem != null)
                    teleItem.handleIncomingEntity(this.outgoingEntity, this);
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

                if (this.incomingEntity != null) {
                    this.incomingEntity.moveTo(this.getPosition().squareInFront(this.getRotation()).getX(), this.getPosition().squareInFront(this.getRotation()).getY());
                }

                this.state = 7;
                this.setTicks(RoomItemFactory.getProcessTime(1));
                break;
            }

            case 7: {
                this.toggleDoor(false);
                this.incomingEntity.setOverriden(false);
                this.incomingEntity = null;
                this.inUse = false;
                break;
            }

            case 8: {
                this.toggleDoor(true);

                if (this.outgoingEntity != null) {
                    this.outgoingEntity.moveTo(this.getPosition().squareBehind(this.rotation).getX(), this.getPosition().squareBehind(this.rotation).getY());
                }

                this.state = 7;
                this.setTicks(RoomItemFactory.getProcessTime(1));
                break;
            }
        }
    }

    @Override
    public void onPlaced() {
        this.setExtraData("0");
    }

    public void endTeleporting() {
        this.toggleAnimation(false);

        this.state = -1;
        this.outgoingEntity = null;
        this.inUse = false;
    }

    public void handleIncomingEntity(GenericEntity entity, TeleporterFloorItem otherItem) {
        if (otherItem != null)
            otherItem.endTeleporting();

        this.toggleAnimation(true);
        entity.updateAndSetPosition(this.getPosition().copy());

        this.incomingEntity = entity;

        if(!this.isDoor) {
            this.state = 5;
            this.setTicks(RoomItemFactory.getProcessTime(2));
        } else {
            this.state = 6;
            this.setTicks(RoomItemFactory.getProcessTime(0.1));
        }
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

    protected RoomItemFloor getPartner(int pairId) {
        return this.getRoom().getItems().getFloorItem(pairId);
    }
}
